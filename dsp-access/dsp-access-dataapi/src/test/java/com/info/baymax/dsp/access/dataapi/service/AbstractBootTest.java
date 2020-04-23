package com.info.baymax.dsp.access.dataapi.service;

import com.info.baymax.dsp.access.dataapi.DspDataapiStarter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { DspDataapiStarter.class }, //
		webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, //
		properties = {/* "spring.main.web-application-type=none"*/ })
//@TestPropertySource(locations = { "classpath:/application-test.properties" })
@TestConfiguration
public abstract class AbstractBootTest {

	@Autowired
	protected ApplicationContext applicationContext;

	@Before
	public void init() {
	}

	@Test
	public void insert() {
	}

	@Test
	public void select() {
	}

	@Test
	public void update() {
	}

	@Test
	public void delete() {
	}

	@After
	public void destory() {
	}
}
