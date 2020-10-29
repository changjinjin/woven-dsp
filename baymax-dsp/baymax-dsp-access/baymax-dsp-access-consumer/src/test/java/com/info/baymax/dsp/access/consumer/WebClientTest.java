/*
 *
 * Copyright 2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 *
 */
package com.info.baymax.dsp.access.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.info.baymax.dsp.data.sys.entity.security.User;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;

@AutoConfigureRestDocs(outputDir = "build/asciidoc/snippets")
@AutoConfigureWebTestClient
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {DspConsumerStarter.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class WebClientTest {

    @Autowired
    private WebTestClient webTestClient;

    @Ignore
    @Test
    public void addANewPetToTheStore() throws Exception {
        // @formatter:off
        webTestClient
            .post().uri("/createUser/")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .body(Mono.just(createUser()), User.class)
            .exchange()
            .expectStatus().isOk()
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .consumeWith(WebTestClientRestDocumentation.document("addPetUsingPOST", preprocessResponse(prettyPrint())))
            .jsonPath("$.name").isNotEmpty()
            .jsonPath("$.name").isEqualTo("zhangsan");
        // @formatter:on
    }

    private String createUser() throws JsonProcessingException {
        User user = new User();
        user.setId("1");
        user.setAdmin(1);
        user.setEmail("760374564@qq.com");
        user.setDescription("我叫张三");
        user.setName("张三");
        user.setLoginId("zhangsan");
        return new ObjectMapper().writeValueAsString(user);
    }
}
