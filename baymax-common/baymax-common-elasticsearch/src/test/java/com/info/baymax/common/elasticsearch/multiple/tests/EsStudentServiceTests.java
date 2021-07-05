package com.info.baymax.common.elasticsearch.multiple.tests;

import com.info.baymax.common.core.page.IPage;
import com.info.baymax.common.core.page.IPageable;
import com.info.baymax.common.elasticsearch.multiple.MultipleDatasourceTestStarter;
import com.info.baymax.common.elasticsearch.multiple.entity.elasticsearch.EsStudent;
import com.info.baymax.common.elasticsearch.multiple.service.elasticsearch.EsStudentService;
import com.info.baymax.common.persistence.service.criteria.example.ExampleQuery;
import com.info.baymax.common.queryapi.query.field.FieldGroup;
import com.info.baymax.common.utils.JsonUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {
	MultipleDatasourceTestStarter.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EsStudentServiceTests {

	@Autowired
	private EsStudentService esStudentService;
	@Autowired
	private ElasticsearchRestTemplate restTemplate;

	@Test
	public void addData() {
		try {
			Random random = new Random();
			List<EsStudent> list = new ArrayList<EsStudent>();
			for (int i = 0; i < 1; i++) {
				EsStudent t = new EsStudent();
				t.setId((long) i);
				t.setName("zhangsan_" + i);
				t.setAge(12 + random.nextInt(10));
				t.setBirth(new Date());
				t.setGender(random.nextBoolean() ? "F" : "M");
				t.setGrade("03");
				t.setClazz("03-01");
				t.setIntro("张三" + i + "是个好同学！");
				t.setUpdateTime(new Date());
				list.add(t);
			}
			restTemplate.save(list);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	@Test
	public void selectByPrimaryKey() {
		try {
			EsStudent t = esStudentService.selectByPrimaryKey("1");
			System.out.println(JsonUtils.toJson(t));
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	@Test
	public void count() {
		try {
			int count = esStudentService.selectCount();
			System.out.println(count);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	@Test
	public void selectAll() {
		try {
			List<EsStudent> list = esStudentService.selectAll();
			System.out.println(JsonUtils.toJson(list));
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	@Test
	public void page() {
		try {
			IPage<EsStudent> page = esStudentService.selectPage(ExampleQuery.builder().page(1, 10).orderBy("id"));
			System.out.println(JsonUtils.toJson(page));

			page = esStudentService.selectPage(ExampleQuery.builder()//
				.fieldGroup(FieldGroup.builder()//
					.andLike("name", "zhang%") //
					.andEqualTo("enabled", 1) //
				)//
				.page(1, 10).orderBy("updateTime", false));
			System.out.println(JsonUtils.toJson(page));

			EsStudent t = new EsStudent();
			t.setAge(12);
			page = esStudentService.selectPage(t, IPageable.page(1, 10));
			System.out.println(JsonUtils.toJson(page));
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

}
