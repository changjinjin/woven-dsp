package com.info.baymax.dsp.common.mybatis.mybatis.test;

import com.info.baymax.common.page.IPage;
import com.info.baymax.common.service.criteria.example.ExampleQuery;
import com.info.baymax.common.service.criteria.example.FieldGroup;
import com.info.baymax.dsp.common.mybatis.mybatis.entity.TUser;
import com.info.baymax.dsp.common.mybatis.mybatis.service.TUserService;
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
    public void delete() {
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

}
