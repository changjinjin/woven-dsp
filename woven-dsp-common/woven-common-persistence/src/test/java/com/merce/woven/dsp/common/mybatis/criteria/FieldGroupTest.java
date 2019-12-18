package com.merce.woven.dsp.common.mybatis.criteria;

import java.util.List;

import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.info.baymax.common.mybatis.mapper.example.Example;
import com.info.baymax.common.mybatis.mapper.example.Example.CriteriaItem;
import com.info.baymax.common.service.criteria.example.ExampleHelper;
import com.info.baymax.common.service.criteria.example.ExampleQuery;
import com.info.baymax.common.service.criteria.example.FieldGroup;
import com.merce.woven.dsp.common.mybatis.mybatis.entity.TUser;

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
        System.out.println(JSON.toJSONString(g));
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

        System.out.println(JSON.toJSONString(group));
        List<CriteriaItem> ordItems = group.ordItems();
        for (CriteriaItem fieldItem : ordItems) {
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
        System.out.println(JSON.toJSONString(example));
    }

}
