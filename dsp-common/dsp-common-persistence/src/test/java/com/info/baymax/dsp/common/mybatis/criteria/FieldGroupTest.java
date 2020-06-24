package com.info.baymax.dsp.common.mybatis.criteria;

import java.util.List;

import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.info.baymax.common.mybatis.mapper.example.Example;
import com.info.baymax.common.mybatis.mapper.example.Example.CriteriaItem;
import com.info.baymax.common.service.criteria.example.ExampleHelper;
import com.info.baymax.common.service.criteria.example.ExampleQuery;
import com.info.baymax.common.service.criteria.field.FieldGroup;
import com.info.baymax.dsp.common.mybatis.mybatis.entity.TUser;

public class FieldGroupTest {

    @Test
    public void test() {
        FieldGroup<?> g = FieldGroup.<ExampleQuery>builder()//
            .andEqualTo("Brower", "Baidu")//
            .andEqualTo("Brower Version", "Amigo")//
            .group(//
                FieldGroup.<ExampleQuery>builder()//
                    .andEqualTo("Brower", "Sougou")//
                    .andEqualTo("Country", "Alianbia")//
                    .group(//
                        FieldGroup.<ExampleQuery>builder()//
                            .andEqualTo("id", 1)//
                            .andEqualTo("name", "zhangsan")//
                    )//
                    .group(//
                        FieldGroup.<ExampleQuery>builder()//
                            .andEqualTo("Brower Version", "Amigo")//
                            .andEqualTo("Device", "100 Plus 100C")//
                    )//
            )//
            .andEqualTo("Platform", "Amigo")//
            .group(//
                FieldGroup.<ExampleQuery>builder()//
                    .andEqualTo("Brower", "Chrome")//
                    .andEqualTo("Country", "China")//
            )//
            ;
        System.out.println(JSON.toJSONString(g));
    }

    @Test
    public void test1() {
        FieldGroup<?> group = FieldGroup.<ExampleQuery>builder()//
            .andEqualTo("name", "zhangsan") //
            .group(FieldGroup.<ExampleQuery>builder()//
                .andEqualTo("name", "zhangsan")//
                .group(FieldGroup.<ExampleQuery>builder()//
                    .andEqualTo("name", "zhangsan")//
                    .andEqualTo("age", 11))//
                .andEqualTo("age", 11)//
                .group(FieldGroup.<ExampleQuery>builder()//
                    .andEqualTo("name", "zhangsan")//
                    .andEqualTo("age", 11)))//
            .andEqualTo("age", 11)//
            ;

        System.out.println(JSON.toJSONString(group));
        List<CriteriaItem> ordItems = group.ordItems();
        for (CriteriaItem fieldItem : ordItems) {
            System.out.println(fieldItem.getIndex());
            System.out.println(fieldItem);
        }
    }

    @Test
    public void test2() {
        FieldGroup<ExampleQuery> group = FieldGroup.<ExampleQuery>builder()//
            .andEqualTo("name", "zhangsan") //
            .group(FieldGroup.<ExampleQuery>builder()//
                .andEqualTo("name", "zhangsan")//
                .group(FieldGroup.<ExampleQuery>builder()//
                    .andEqualTo("name", "zhangsan")//
                    .andEqualTo("age", 11))//
                .andEqualTo("age", 11)//
                .group(FieldGroup.<ExampleQuery>builder()//
                    .andEqualTo("name", "zhangsan")//
                    .andEqualTo("age", 11)))//
            .andEqualTo("age", 11)//
            ;
        Example example = ExampleHelper.createExample(ExampleQuery.builder().fieldGroup(group), TUser.class);
        System.out.println(JSON.toJSONString(example));
    }

}
