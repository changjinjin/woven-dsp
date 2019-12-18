package com.merce.woven.dsp.common.mybatis.mybatis.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = { Starter.class }, //
//		webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, //
//		properties = { //
//				"ribbon.eureka.enabled=false"  //
//		})
//@TestPropertySource(locations = { "classpath:/woven.properties", "classpath:/woven-pipeline.properties" })
//@TestConfiguration
public abstract class AbstractMapperTest {

    @Autowired
    protected ApplicationContext applicationContext;

    // @Before
    public void init() {
    }

    // @Test
    public void insert() {
    }

    // @Test
    public void select() {
    }

    // @Test
    public void update() {
    }

    // @Test
    public void delete() {
    }

    // @After
    public void destory() {
    }
}
