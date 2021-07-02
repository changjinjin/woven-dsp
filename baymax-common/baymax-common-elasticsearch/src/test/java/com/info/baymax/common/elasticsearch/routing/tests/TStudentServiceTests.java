package com.info.baymax.common.elasticsearch.routing.tests;

import com.info.baymax.common.core.page.IPage;
import com.info.baymax.common.core.page.IPageable;
import com.info.baymax.common.datasource.routing.lookup.LookupKeyContext;
import com.info.baymax.common.elasticsearch.routing.RoutingDatasourceTestStarter;
import com.info.baymax.common.elasticsearch.routing.entity.TStudent;
import com.info.baymax.common.elasticsearch.routing.service.TStudentService;
import com.info.baymax.common.persistence.service.criteria.example.ExampleQuery;
import com.info.baymax.common.queryapi.query.field.FieldGroup;
import com.info.baymax.common.utils.JsonUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {
	RoutingDatasourceTestStarter.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TStudentServiceTests {

	@Autowired
	private TStudentService tStudentService;

	@Rollback(false)
	@Test
	public void addData() {
		try {
			Random random = new Random();
			List<TStudent> list = new ArrayList<TStudent>();
			for (int i = 0; i < 100; i++) {
				TStudent t = new TStudent();
				// t.setId((long) i);
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
			String lookupKey = LookupKeyContext.get();
			System.out.println("lookupKey:" + lookupKey);

			int selectCount = tStudentService.selectCount();
			System.out.println("select count:" + selectCount);
			lookupKey = LookupKeyContext.get();
			System.out.println("lookupKey:" + lookupKey);

			int insertCount = tStudentService.insertListSelective(list);
			System.out.println("insert count:" + insertCount);
			lookupKey = LookupKeyContext.get();
			System.out.println("lookupKey:" + lookupKey);

			selectCount = tStudentService.selectCount();
			System.out.println("select count:" + selectCount);
			lookupKey = LookupKeyContext.get();
			System.out.println("lookupKey:" + lookupKey);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	@Test
	public void selectByPrimaryKey() {
		try {
			TStudent t = tStudentService.selectByPrimaryKey("1");
			System.out.println(JsonUtils.toJson(t));
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	@Test
	public void count() {
		try {
			int count = tStudentService.selectCount();
			System.out.println(count);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	@Test
	public void selectAll() {
		try {
			List<TStudent> list = tStudentService.selectAll();
			System.out.println(JsonUtils.toJson(list));
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	@Test
	public void page() {
		try {
			IPage<TStudent> page = tStudentService.selectPage(ExampleQuery.builder().page(1, 10).orderBy("id"));
			System.out.println(JsonUtils.toJson(page));

			page = tStudentService.selectPage(ExampleQuery.builder()//
				.fieldGroup(FieldGroup.builder()//
					.andLike("name", "zhang%") //
					.andEqualTo("enabled", 1) //
				)//
				.page(1, 10).orderBy("updateTime", false));
			System.out.println(JsonUtils.toJson(page));

			TStudent t = new TStudent();
			t.setAge(12);
			page = tStudentService.selectPage(t, IPageable.page(1, 10));
			System.out.println(JsonUtils.toJson(page));
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

}
