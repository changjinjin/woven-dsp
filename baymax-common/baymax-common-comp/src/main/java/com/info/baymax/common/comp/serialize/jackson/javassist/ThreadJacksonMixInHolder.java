package com.info.baymax.common.comp.serialize.jackson.javassist;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 在当前线程内保存ObjectMapper供Jackson2HttpMessageConverter使用
 *
 * @author: jingwei.yang
 * @date: 2019年4月23日 下午3:00:15
 */
public class ThreadJacksonMixInHolder {
    private static ThreadLocal<ThreadJacksonMixInHolder> holderThreadLocal = new ThreadLocal<ThreadJacksonMixInHolder>();
    private Set<Map.Entry<Class<?>, Class<?>>> mixIns;
    private ObjectMapper objectMapper;

    /**
     * 根据当前MixIn集合生成objectMapper，该方法在返回mapper对象之后调用clear方法，如果再次调用builderMapper()肯定会保存
     *
     * @return
     */
    public static ObjectMapper builderObjectMapper() {
        ThreadJacksonMixInHolder holder = holderThreadLocal.get();
        if (holder.objectMapper == null && isContainsMixIn()) {
            holder.objectMapper = new ObjectMapper();
            for (Map.Entry<Class<?>, Class<?>> mixIn : holder.mixIns) {
                holder.objectMapper.addMixIn(mixIn.getKey(), mixIn.getValue());
            }
        }
        clear();// 如果不调用clear可能导致线程内的数据是脏的！
        return holder.objectMapper;
    }

    /**
     * 清除当前线程内的数据
     */
    public static void clear() {
        holderThreadLocal.set(null);
        // holderThreadLocal.remove();
    }

    /**
     * 设置MixIn集合到线程内，如果线程内已经存在数据，则会先清除
     *
     * @param resetMixIns
     */
    public static void setMixIns(Set<Map.Entry<Class<?>, Class<?>>> resetMixIns) {
        ThreadJacksonMixInHolder holder = holderThreadLocal.get();
        if (holder == null) {
            holder = new ThreadJacksonMixInHolder();
            holderThreadLocal.set(holder);
        }
        holder.mixIns = resetMixIns;
    }

    /**
     * 不同于setMixIns，addMixIns为增加MixIn集合到线程内，即不会清除已经保存的数据 <br>
     * 2014年4月4日 下午12:08:15
     *
     * @param toAddMixIns
     */
    public static void addMixIns(Set<Map.Entry<Class<?>, Class<?>>> toAddMixIns) {
        ThreadJacksonMixInHolder holder = holderThreadLocal.get();
        if (holder == null) {
            holder = new ThreadJacksonMixInHolder();
            holderThreadLocal.set(holder);
        }
        if (holder.mixIns == null) {
            holder.mixIns = new HashSet<Map.Entry<Class<?>, Class<?>>>();
        }
        holder.mixIns.addAll(toAddMixIns);
    }

    /**
     * 获取线程内的MixIn集合
     * <p>
     * <b>注意：为了防止线程执行完毕之后仍然存在有数据，请务必适时调用clear()方法</b>
     *
     * @return
     */
    public static Set<Map.Entry<Class<?>, Class<?>>> getMixIns() {
        ThreadJacksonMixInHolder holder = holderThreadLocal.get();
        return holder.mixIns;
    }

    /**
     * 判断当前线程是否存在MixIn集合
     *
     * @return
     */
    public static boolean isContainsMixIn() {
        if (holderThreadLocal.get() == null) {
            return false;
        }
        if (holderThreadLocal.get().mixIns != null && holderThreadLocal.get().mixIns.size() > 0) {
            return true;
        }
        return false;
    }

}