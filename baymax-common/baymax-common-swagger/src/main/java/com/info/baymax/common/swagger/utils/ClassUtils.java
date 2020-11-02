package com.info.baymax.common.swagger.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class ClassUtils {

    /**
     * 获取指定类的所有包含所有类中的属性列表
     *
     * @param entityClass 指定类性=型
     * @param fieldList   属性列表
     * @param level       父类层级
     * @return 所有的属性列表
     */
    public static List<Field> getFields(Class<?> entityClass, List<Field> fieldList, Integer level) {
        if (fieldList == null) {
            fieldList = new ArrayList<Field>();
        }
        if (level == null) {
            level = 0;
        }
        if (entityClass.equals(Object.class)) {
            return fieldList;
        }
        Field[] fields = entityClass.getDeclaredFields();
        int index = 0;
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            // 排除静态字段，解决bug#2
            if (!Modifier.isStatic(field.getModifiers()) && !Modifier.isTransient(field.getModifiers())) {
                if (level.intValue() != 0) {
                    // 将父类的字段放在前面
                    fieldList.add(index, field);
                    index++;
                } else {
                    fieldList.add(field);
                }
            }
        }
        Class<?> superClass = entityClass.getSuperclass();
        if (superClass != null && !superClass.equals(Object.class)
            && (!Map.class.isAssignableFrom(superClass) && !Collection.class.isAssignableFrom(superClass))) {
            return getFields(entityClass.getSuperclass(), fieldList, ++level);
        }
        return fieldList;
    }
}
