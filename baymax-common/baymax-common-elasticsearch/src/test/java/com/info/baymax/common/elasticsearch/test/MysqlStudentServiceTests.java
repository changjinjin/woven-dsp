package com.info.baymax.common.elasticsearch.test;

import com.info.baymax.common.core.page.IPage;
import com.info.baymax.common.core.page.IPageable;
import com.info.baymax.common.elasticsearch.MultipleDatasourceMybatisStarter;
import com.info.baymax.common.elasticsearch.entity.mysql.MysqlStudent;
import com.info.baymax.common.elasticsearch.service.mysql.MysqlStudentService;
import com.info.baymax.common.persistence.service.criteria.example.ExampleQuery;
import com.info.baymax.common.queryapi.query.field.FieldGroup;
import com.info.baymax.common.utils.JsonUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Date;
import java.util.List;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {
	MultipleDatasourceMybatisStarter.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MysqlStudentServiceTests {

	@Autowired
	private MysqlStudentService mysqlStudentService;

	@Test
	public void addData() {
		try {
			MysqlStudent t = new MysqlStudent();
			t.setId(1L);
			t.setName("zhangsan");
			t.setAge(12);
			t.setBirth(new Date());
			t.setGender("F");
			t.setGrade("03");
			t.setClazz("03-01");
			t.setIntro("张三是个好同学！");
			t.setUpdateTime(new Date());
			mysqlStudentService.insertSelective(t);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	@Test
	public void selectByPrimaryKey() {
		try {
			MysqlStudent t = mysqlStudentService.selectByPrimaryKey(1L);
			System.out.println(JsonUtils.toJson(t));
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	@Test
	public void count() {
		try {
			int count = mysqlStudentService.selectCount();
			System.out.println(count);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	@Test
	public void selectAll() {
		try {
			List<MysqlStudent> list = mysqlStudentService.selectAll();
			System.out.println(JsonUtils.toJson(list));
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	@Test
	public void page() {
		try {
			IPage<MysqlStudent> page = mysqlStudentService.selectPage(ExampleQuery.builder().page(1, 10).orderBy("id"));
			System.out.println(JsonUtils.toJson(page));

			page = mysqlStudentService.selectPage(ExampleQuery.builder()//
				.fieldGroup(FieldGroup.builder()//
					.andLike("name", "zhang%") //
					.andEqualTo("gender", "F") //
				)//
				.page(1, 10).orderBy("updateTime", false));
			System.out.println(JsonUtils.toJson(page));

			MysqlStudent t = new MysqlStudent();
			t.setAge(12);
			page = mysqlStudentService.selectPage(t, IPageable.page(1, 10));
			System.out.println(JsonUtils.toJson(page));
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

}
