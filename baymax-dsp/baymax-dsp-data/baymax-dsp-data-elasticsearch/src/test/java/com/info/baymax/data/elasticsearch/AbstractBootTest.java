package com.info.baymax.data.elasticsearch;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {ElasticsearchTestStarter.class}, //
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, //
    properties = {"spring.main.web-application-type=none"})
@TestPropertySource(locations = {"classpath:/application-test.properties"})
@TestConfiguration
public abstract class AbstractBootTest {

    @Autowired
    protected ApplicationContext applicationContext;

    @BeforeEach
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

    @AfterEach
    public void destory() {
    }
}
