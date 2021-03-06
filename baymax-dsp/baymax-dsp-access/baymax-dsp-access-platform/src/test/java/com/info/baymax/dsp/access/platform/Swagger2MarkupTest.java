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
package com.info.baymax.dsp.access.platform;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@AutoConfigureRestDocs(outputDir = "build/asciidoc/snippets")
@AutoConfigureWebTestClient
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class Swagger2MarkupTest {

	@Autowired
	private WebTestClient webTestClient;

	@Test
	public void createSpringfoxSwaggerJson() throws Exception {
		log.info("create Springfox Swagger Json begin .....");
		// String designFirstSwaggerLocation = Swagger2MarkupTest.class.getResource("/swagger.yaml").getPath();
		String outputDir = System.getProperty("io.springfox.staticdocs.outputDir");

		webTestClient//
				.get().uri("/v2/api-docs")//
				.accept(MediaType.APPLICATION_JSON)//
				.exchange()//
				.expectStatus().isOk()//
				.expectBody().consumeWith(t -> {
					try {
						Files.createDirectories(Paths.get(outputDir));
						try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(outputDir, "swagger.json"),
								StandardCharsets.UTF_8)) {
							writer.write(new String(t.getResponseBodyContent(), "UTF-8"));
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				});
		log.info("create Springfox Swagger Json end .....");
	}
}
