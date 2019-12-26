package com.merce.woven.dsp.common.mybatis.mybatis.test;

import com.info.baymax.common.mybatis.page.IPage;
import com.info.baymax.common.service.criteria.example.ExampleQuery;
import com.info.baymax.common.service.criteria.example.FieldGroup;
import com.merce.woven.dsp.common.mybatis.mybatis.entity.TUser;
import com.merce.woven.dsp.common.mybatis.mybatis.service.TUserService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

// @TestConfiguration
public class ExampleQueryDemoTest extends AbstractMapperTest {

    @Autowired
    private TUserService tUserService;

    // @Test
    public void selectDemo() {
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

        // 查询数据条数
        int count = tUserService.selectCount(query);

        // 查询数据集合
        List<TUser> list = tUserService.selectList(query);

        // 分页查询
        IPage<TUser> page = tUserService.selectPage(query);

        // 删除数据
        int delete = tUserService.delete(query);
    }

    // @Test
    public void updateDemo() {

        // 1、 需要修改的属性包装进record中，只需设置需要更新的字段值即可
        TUser record = new TUser();
        record.setEmail("123@163.com");
        record.setMobile("13460286086");

        // 2、 修改的匹配条件放到query对象里
        ExampleQuery query = ExampleQuery.builder(TUser.class)//
            .fieldGroup()//
            .andEqualTo("gender", 0)// and gender = 0
            .andLike("username", "%lisi")// and username like '%lisi'
            .andNotIn("id", new Long[]{1L, 2L, 34L})// id not in (1 ,2 34)
            .andBetween("birth", "1990-01-01", "2019-1231")// and birth between '1990-01-01' and '2019-1231'
            .end();

        // 3、执行更新
        // 全量字段更新
        int updateByExample = tUserService.updateByExample(record, query);

        // 部分字段更新
        int updateByExampleSelective = tUserService.updateByExampleSelective(record, query);
    }

}
