package com.merce.woven.common.mybatis.utils;

import java.util.Map;

import tk.mybatis.mapper.MapperException;
import tk.mybatis.mapper.entity.EntityColumn;
import tk.mybatis.mapper.mapperhelper.EntityHelper;
import tk.mybatis.mapper.util.StringUtil;

public class TkEntityHelper {

    public static String getColumn(Class<?> entityClass, String property) {
        Map<String, EntityColumn> propertyMap = EntityHelper.getEntityTable(entityClass).getPropertyMap();
        if (StringUtil.isEmpty(property) || StringUtil.isEmpty(property.trim())) {
            throw new MapperException("接收的property为空！");
        }
        property = property.trim();
        if (!propertyMap.containsKey(property)) {
            throw new MapperException("当前实体类不包含名为" + property + "的属性!");
        }
        return propertyMap.get(property).getColumn();
    }

}
