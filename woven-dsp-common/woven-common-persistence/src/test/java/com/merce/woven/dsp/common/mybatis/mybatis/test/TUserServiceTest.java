package com.merce.woven.dsp.common.mybatis.mybatis.test;

import com.info.baymax.common.service.criteria.example.ExampleQuery;
import com.info.baymax.common.service.criteria.example.FieldGroup;
import com.merce.woven.dsp.common.mybatis.mybatis.entity.TUser;
import com.merce.woven.dsp.common.mybatis.mybatis.service.TUserService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

// @TestConfiguration
public class TUserServiceTest extends AbstractMapperTest {

    @Autowired
    private TUserService tUserService;

    // @Test
    public void insert() {
        TUser t = new TUser();
        t.setUsername("admin");
        t.setPassword("123456");
        t.setRealName("管理员");
        t.setBirth(new Date());
        t.setGender((byte) 1);
        tUserService.insert(t);
        tUserService.insertSelective(t);
    }

    // @Test
    public void update() {
        TUser t = new TUser();
        t.setId(1L);
        t.setUsername("admin");
        t.setPassword("1234567");
        t.setRealName("管理员");
        t.setBirth(new Date());
        t.setGender((byte) 1);

        tUserService.updateByPrimaryKey(t);
        tUserService.updateByPrimaryKeySelective(t);
    }

    // @Test
    public void delte() {
        tUserService.deleteByPrimaryKey(1L);

        TUser t = new TUser();
        t.setId(1L);
        tUserService.delete(t);
    }

    // @Test
    public void select() {
        TUser t = tUserService.selectByPrimaryKey(1L);
        System.out.println(t);

        TUser t1 = new TUser();
        t1.setId(1L);
        List<TUser> list = tUserService.select(t1);
        System.out.println(list);
    }

    // @Test
    public void selectList() {
        ExampleQuery query = ExampleQuery//
            .builder(TUser.class)// 构建一个TUser的查询对象
            .paged()// 分页
            .pageNum(1)// 页码
            .pageSize(10)// 页长
            .excludeProperties("poassword")// 排除的查询字段
            .selectProperties("id", "name", "age")// 查询的字段
            .fieldGroup()// 构建匹配条件
            .andEqualTo("gender", 1)// gender = 1
            .andGreaterThanOrEqualTo("age", 12)// age >= 12
            .andLeftLike("name", "zhangsan")// name like 'zhangsan%'
            .andBetween("birth", "2010-01-01", "2020-01-01")// birth between "2010-01-01" and "2020-01-01"
            .andIsNotNull("intro")// intro is not null
            .orGroup(// 添加一个条件 or ( id = 1 and name = 'zhangsan')
                FieldGroup.builder()//
                    .andEqualTo("id", 1)//
                    .andEqualTo("name", "zhangsan")//
            )//
            .orGroup(// 添加一个条件 or ( id < 10 and name = 'wangwu' or age between 10 and 13)
                FieldGroup.builder()//
                    .andLessThan("id", 10)// id = 10
                    .andEqualTo("name", "wangwu")// name = 'wangwu'
                    .orBetween("age", 10, 13) // age between 10 and 13
            )//
            .end()// 返回query
            .orderBy("createTime")// 正序排序
            .orderByDesc("id")// 逆序排序
            .forUpdate(true);
        List<TUser> list = tUserService.selectList(query);

    }

}
