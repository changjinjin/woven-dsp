package com.merce.woven.dsp.common.mybatis.example;

import java.util.Arrays;
import java.util.Date;

import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.info.baymax.common.mybatis.mapper.example.Example;
import com.merce.woven.dsp.common.mybatis.mybatis.entity.TUser;

import tk.mybatis.mapper.util.Sqls;

public class ExampleTest {

    @Test
    public void test1() {
        Example example = Example.builder(TUser.class)//
            .andWhere(Sqls.custom().andBetween("id", 1, 13).orIn("status", Arrays.asList(1, 2, 3)))//
            .andWhere(Sqls.custom().andEqualTo("username", "lisi").orLessThan("birth", new Date()))//
            .build();
        System.out.println(JSON.toJSONString(example));
    }
}
