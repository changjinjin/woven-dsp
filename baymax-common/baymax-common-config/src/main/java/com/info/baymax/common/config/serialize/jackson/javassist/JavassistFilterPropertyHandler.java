package com.info.baymax.common.config.serialize.jackson.javassist;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.info.baymax.common.core.annotation.JsonBody;
import com.info.baymax.common.core.annotation.JsonBodys;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.ClassFile;
import javassist.bytecode.ConstPool;
import javassist.bytecode.annotation.*;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ResponseBody;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.charset.Charset;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

/**
 * 使用代理来创建jackson的MixInAnnotation注解接口，
 * 如果使用本实现方法，一定要配置在web.xml中配置过滤器WebContextFilter，否则无法输出json到客户端 <br>
 *
 * @author: jingwei.yang
 * @date: 2019年4月23日 下午2:58:11
 */
public class JavassistFilterPropertyHandler implements FilterPropertyHandler {

    private final ObjectMapper objectMapper;

    /**
     * 注解的方法对应生成的代理类映射表
     */
    private static Map<Method, Map<Class<?>, Class<?>>> proxyMethodMap = new HashMap<Method, Map<Class<?>, Class<?>>>();

    /**
     * String数组的hashCode与生成的对应的代理类的映射表
     */
    private static Map<Integer, Class<?>> proxyMixInAnnotationMap = new HashMap<Integer, Class<?>>();

    private static String[] globalIgnoreProperties = new String[]{"hibernateLazyInitializer", "handler"};

    /**
     * 如果是标注的SpringMVC中的Controller方法，则应判断是否注解了@ResponseBody
     */
    private boolean isResponseBodyAnnotation;

    /**
     * 创建代理接口的唯一值索引
     */
    private static int proxyIndex;

    public JavassistFilterPropertyHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public JavassistFilterPropertyHandler(ObjectMapper objectMapper, String[] globalIgnoreProperties) {
        this(objectMapper);
        JavassistFilterPropertyHandler.globalIgnoreProperties = globalIgnoreProperties;
    }

    /**
     * @param isResponseBodyAnnotation 如果是标注的SpringMVC中的Controller方法，则应判断是否注解了@ResponseBody
     */
    public JavassistFilterPropertyHandler(ObjectMapper objectMapper, boolean isResponseBodyAnnotation) {
        this(objectMapper);
        this.isResponseBodyAnnotation = isResponseBodyAnnotation;
    }

    private Collection<String> checkAndPutToCollection(Collection<String> collection, String[] names) {
        if (collection == null) {
            collection = new HashSet<String>();
        }
        Collections.addAll(collection, names);
        return collection;
    }

    private Collection<String> putGlobalIgnoreProperties(Collection<String> collection) {
        if (globalIgnoreProperties != null) {
            if (collection == null) {
                collection = new HashSet<String>();
            }
            for (int i = 0; i < globalIgnoreProperties.length; i++) {
                String name = globalIgnoreProperties[i];
                collection.add(name);
            }
        }
        return collection;
    }

    /**
     * 处理JsonBody注解
     *
     * @param jsonBody        注解对象
     * @param pojoAndNamesMap 实体类和字段集合
     */
    private void processJsonBodyAnnotation(JsonBodys jsonBody, Map<Class<?>, Collection<String>> pojoAndNamesMap) {
        JsonBody[] filters = jsonBody.value();
        if (filters != null && filters.length > 0) {
            for (JsonBody filter : filters) {
                processJsonFilterAnnotationIncludeFields(filter, pojoAndNamesMap);
                processJsonFilterAnnotationExcludeFields(filter, pojoAndNamesMap);
            }
        }
    }

    /**
     * 解析注解中忽略的字段信息，将配置的字段存入忽略的字段集合中并放入映射
     *
     * @param filter          注解
     * @param pojoAndNamesMap 实体类和字段名称集合映射
     */
    private void processJsonFilterAnnotationExcludeFields(JsonBody filter,
                                                          Map<Class<?>, Collection<String>> pojoAndNamesMap) {
        String[] includeFields = filter.excludes();
        Class<?> pojoClass = filter.type();
        // 根据注解创建代理接口
        Collection<String> nameCollection = pojoAndNamesMap.get(pojoClass);
        nameCollection = checkAndPutToCollection(nameCollection, includeFields);
        pojoAndNamesMap.put(pojoClass, nameCollection);
    }

    /**
     * 解析注解中包含的字段信息，就是说如果配置的字段存在于忽略的字段集合中的话，在这里则要将他们从集合中删除
     *
     * @param filter          注解
     * @param pojoAndNamesMap 实体类和字段名称集合映射
     */
    private void processJsonFilterAnnotationIncludeFields(JsonBody filter,
                                                          Map<Class<?>, Collection<String>> pojoAndNamesMap) {
        String[] includeFields = filter.includes();
        Class<?> pojoClass = filter.type();

        Collection<String> ignoreProperties = getUnstaticClassFieldNameCollection(pojoClass);

        Collection<String> allowNameCollection = new ArrayList<String>();
        Collections.addAll(allowNameCollection, includeFields);

        Collection<String> nameCollection = pojoAndNamesMap.get(pojoClass);
        if (nameCollection != null) {
            nameCollection.removeAll(allowNameCollection);
        } else {
            ignoreProperties.removeAll(allowNameCollection);
            nameCollection = ignoreProperties;
        }
        pojoAndNamesMap.put(pojoClass, nameCollection);
    }

    // 获取类型的非静态属性名
    public Collection<String> getUnstaticClassFieldNameCollection(Class<?> clazz) {
        if (clazz == null) {
            throw new NullPointerException("传入的clazz为空对象！");
        }
        return Arrays.stream(clazz.getDeclaredFields())//
            .filter(t -> (!Modifier.isStatic(t.getModifiers())))//
            .map(t -> t.getName())//
            .collect(Collectors.toList());
    }

    /**
     * 根据方法获取过滤映射表
     *
     * @param method 注解了 @JsonFilter的方法（所在的类）
     * @return
     */
    public Map<Class<?>, Class<?>> getProxyMixInAnnotation(Method method) {
        if (isResponseBodyAnnotation && !method.isAnnotationPresent(ResponseBody.class)) {
            return null;
        }
        Map<Class<?>, Class<?>> map = proxyMethodMap.get(method);// 从缓存中查找是否存在

        if (map != null && map.entrySet().size() > 0) {// 如果已经读取该方法的注解信息，则从缓存中读取
            return map;
        } else {
            map = new HashMap<Class<?>, Class<?>>();
        }

        Class<?> clazzOfMethodIn = method.getDeclaringClass();// 方法所在的class

        Map<Class<?>, Collection<String>> pojoAndNamesMap = new HashMap<Class<?>, Collection<String>>();

        JsonBodys classJsonBody = clazzOfMethodIn.getAnnotation(JsonBodys.class);
        JsonBody classJsonFilter = clazzOfMethodIn.getAnnotation(JsonBody.class);

        JsonBodys jsonBody = method.getAnnotation(JsonBodys.class);
        JsonBody jsonFilter = method.getAnnotation(JsonBody.class);

        if (jsonFilter != null) {// 方法上的AllowProperty注解
            processJsonFilterAnnotationIncludeFields(jsonFilter, pojoAndNamesMap);
        }
        if (classJsonFilter != null) {
            processJsonFilterAnnotationIncludeFields(classJsonFilter, pojoAndNamesMap);
        }

        if (classJsonBody != null) {// 类上的IgnoreProperties注解
            processJsonBodyAnnotation(classJsonBody, pojoAndNamesMap);
        }
        if (classJsonFilter != null) {// 类上的IgnoreProperty注解
            processJsonFilterAnnotationExcludeFields(classJsonFilter, pojoAndNamesMap);
        }

        if (jsonBody != null) {// 方法上的JsonBody注解
            processJsonBodyAnnotation(jsonBody, pojoAndNamesMap);
        }
        if (jsonFilter != null) {// 方法上的JsonFilter注解
            processJsonFilterAnnotationExcludeFields(jsonFilter, pojoAndNamesMap);
        }

        Set<Entry<Class<?>, Collection<String>>> entries = pojoAndNamesMap.entrySet();
        for (Iterator<Entry<Class<?>, Collection<String>>> iterator = entries.iterator(); iterator.hasNext(); ) {
            Entry<Class<?>, Collection<String>> entry = (Entry<Class<?>, Collection<String>>) iterator.next();
            Collection<String> nameCollection = entry.getValue();
            nameCollection = putGlobalIgnoreProperties(nameCollection);// 将全局过滤字段放入集合内
            String[] names = nameCollection.toArray(new String[]{});

            Class<?> clazz = createMixInAnnotation(names);
            map.put(entry.getKey(), clazz);
        }

        proxyMethodMap.put(method, map);
        return map;
    }

    /**
     * 创建jackson的代理注解接口类
     *
     * @param names 要生成的字段
     * @return 代理接口类
     */
    private Class<?> createMixInAnnotation(String[] names) {
        Class<?> clazz = null;
        clazz = proxyMixInAnnotationMap.get(Arrays.deepHashCode(names));
        if (clazz != null) {
            return clazz;
        }

        ClassPool pool = ClassPool.getDefault();
        // 创建代理接口
        CtClass cc = pool.makeInterface("ProxyMixInAnnotation" + System.currentTimeMillis() + proxyIndex++);

        ClassFile ccFile = cc.getClassFile();
        ConstPool constpool = ccFile.getConstPool();

        // create the annotation
        AnnotationsAttribute attr = new AnnotationsAttribute(constpool, AnnotationsAttribute.visibleTag);
        // 创建JsonIgnoreProperties注解
        Annotation jsonIgnorePropertiesAnnotation = new Annotation(JsonIgnoreProperties.class.getName(), constpool);

        BooleanMemberValue ignoreUnknownMemberValue = new BooleanMemberValue(false, constpool);

        ArrayMemberValue arrayMemberValue = new ArrayMemberValue(constpool);// value的数组成员

        Collection<MemberValue> memberValues = new HashSet<MemberValue>();
        for (int i = 0; i < names.length; i++) {
            String name = names[i];
            StringMemberValue memberValue = new StringMemberValue(constpool);// 将name值设入注解内
            memberValue.setValue(name);
            memberValues.add(memberValue);
        }
        arrayMemberValue.setValue(memberValues.toArray(new MemberValue[]{}));

        jsonIgnorePropertiesAnnotation.addMemberValue("value", arrayMemberValue);
        jsonIgnorePropertiesAnnotation.addMemberValue("ignoreUnknown", ignoreUnknownMemberValue);

        attr.addAnnotation(jsonIgnorePropertiesAnnotation);
        ccFile.addAttribute(attr);

        // generate the class
        try {
            clazz = cc.toClass();
            proxyMixInAnnotationMap.put(Arrays.deepHashCode(names), clazz);
        } catch (CannotCompileException e) {
            e.printStackTrace();
        }
        return clazz;
    }

    @Override
    public Object filterProperties(Method method, Object object) {
        Map<Class<?>, Class<?>> map = getProxyMixInAnnotation(method);
        if (map == null || map.entrySet().size() == 0) {// 如果该方法上没有注解，则返回原始对象
            return object;
        }
        ThreadJacksonMixInHolder.addMixIns(getEntries(map));
        return object;
    }

    public Set<Entry<Class<?>, Class<?>>> getEntries(Map<Class<?>, Class<?>> map) {
        Set<Entry<Class<?>, Class<?>>> entries = map.entrySet();
        return entries;
    }

    private ObjectMapper createObjectMapper(Map<Class<?>, Class<?>> map) {
        // ObjectMapper mapper = new ObjectMapper();
        Set<Entry<Class<?>, Class<?>>> entries = map.entrySet();
        for (Iterator<Entry<Class<?>, Class<?>>> iterator = entries.iterator(); iterator.hasNext(); ) {
            Entry<Class<?>, Class<?>> entry = iterator.next();
            objectMapper.addMixIn(entry.getKey(), entry.getValue());
        }
        return objectMapper;
    }

    public ObjectMapper createObjectMapper(Method method) {
        return createObjectMapper(getProxyMixInAnnotation(method));
    }

    protected JsonEncoding getJsonEncoding(MediaType contentType) {
        if (contentType != null && contentType.getCharset() != null) {
            Charset charset = contentType.getCharset();
            for (JsonEncoding encoding : JsonEncoding.values()) {
                if (charset.name().equals(encoding.getJavaName())) {
                    return encoding;
                }
            }
        }
        return JsonEncoding.UTF8;
    }

}