package com.info.baymax.common.persistence.mybatis.criteria;

import com.info.baymax.common.persistence.mybatis.mapper.example.Example;
import com.info.baymax.common.persistence.mybatis.mybatis.entity.TUser;
import com.info.baymax.common.persistence.service.criteria.example.ExampleHelper;
import com.info.baymax.common.persistence.service.criteria.example.ExampleQuery;
import com.info.baymax.common.queryapi.query.field.FieldGroup;
import com.info.baymax.common.queryapi.query.field.FieldItem;
import com.info.baymax.common.utils.JsonUtils;
import org.junit.jupiter.api.Test;

import java.util.List;

public class FieldGroupTest {

    @Test
    public void test() {
        FieldGroup g = FieldGroup.builder()//
            .andEqualTo("Brower", "Baidu")//
            .andEqualTo("Brower Version", "Amigo")//
            .group(//
                FieldGroup.builder()//
                    .andEqualTo("Brower", "Sougou")//
                    .andEqualTo("Country", "Alianbia")//
                    .group(//
                        FieldGroup.builder()//
                            .andEqualTo("id", 1)//
                            .andEqualTo("name", "zhangsan")//
                    )//
                    .group(//
                        FieldGroup.builder()//
                            .andEqualTo("Brower Version", "Amigo")//
                            .andEqualTo("Device", "100 Plus 100C")//
                    )//
            )//
            .andEqualTo("Platform", "Amigo")//
            .group(//
                FieldGroup.builder()//
                    .andEqualTo("Brower", "Chrome")//
                    .andEqualTo("Country", "China")//
            )//
            ;
        System.out.println(JsonUtils.toJson(g));
    }

    @Test
    public void test1() {
        FieldGroup group = FieldGroup.builder()//
            .andEqualTo("name", "zhangsan") //
            .group(FieldGroup.builder()//
                .andEqualTo("name", "zhangsan")//
                .group(FieldGroup.builder()//
                    .andEqualTo("name", "zhangsan")//
                    .andEqualTo("age", 11))//
                .andEqualTo("age", 11)//
                .group(FieldGroup.builder()//
                    .andEqualTo("name", "zhangsan")//
                    .andEqualTo("age", 11)))//
            .andEqualTo("age", 11)//
            ;

        System.out.println(JsonUtils.toJson(group));
        List<FieldItem> ordItems = group.ordItems();
        for (FieldItem fieldItem : ordItems) {
            System.out.println(fieldItem.getIndex());
            System.out.println(fieldItem);
        }
    }

    @Test
    public void test2() {
        FieldGroup group = FieldGroup.builder()//
            .andEqualTo("name", "zhangsan") //
            .group(FieldGroup.builder()//
                .andEqualTo("name", "zhangsan")//
                .group(FieldGroup.builder()//
                    .andEqualTo("name", "zhangsan")//
                    .andEqualTo("age", 11))//
                .andEqualTo("age", 11)//
                .group(FieldGroup.builder()//
                    .andEqualTo("name", "zhangsan")//
                    .andEqualTo("age", 11)))//
            .andEqualTo("age", 11)//
            ;
        Example example = ExampleHelper.createExample(ExampleQuery.builder().fieldGroup(group), TUser.class);
        System.out.println(JsonUtils.toJson(example));
    }

}
