package com.info.baymax.common.elasticsearch.test;

import com.info.baymax.common.elasticsearch.EsMybatisStarter;
import com.info.baymax.common.elasticsearch.entity.TStudent;
import com.info.baymax.common.elasticsearch.service.TStudentService;
import com.info.baymax.common.utils.JsonUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {EsMybatisStarter.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TStudentServiceTests {

	@Autowired
	private TStudentService tStudentService;

	@Test
	public void selectByPrimaryKey() {
		try {
			TStudent t = tStudentService.selectByPrimaryKey(1L);
			System.out.println(JsonUtils.toJson(t));
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	@Test
	public void list() {
		try {
			List<TStudent> list = tStudentService.selectAll();
			System.out.println(JsonUtils.toJson(list));
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

}
