package com.info.baymax.dsp.access.dataapi.client;

import com.alibaba.fastjson.JSON;
import com.info.baymax.dsp.access.dataapi.data.condition.RequestQuery;
import com.info.baymax.dsp.access.dataapi.web.request.PullRequest;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

@AutoConfigureWebTestClient
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DataPullClientTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    public void pull() throws Exception {
        String accessKey = "07739986-6232-4e75-9e97-fe3321919d94";
        long dataServiceId = 725059842368077824L;
        webTestClient//
            .get().uri(uriBuilder -> uriBuilder.path("/data/secertkey").queryParam("accessKey", accessKey).build())
            .accept(MediaType.APPLICATION_JSON)//
            .exchange()//
            .expectStatus().isOk()//
            .expectBody()//
            .consumeWith(t -> {
                System.out.println(JSON.toJSONString(t));
            });

        RequestQuery query = RequestQuery.builder()//
            .page(1, 3)//
            .allProperties("id", "code", "date", "project", "income", "manager")//
            .selectProperties("id", "code", "project", "income", "manager")//
            .fieldGroup()//
            .andGreaterThan("id", 2)//
            .andIn("code", new Integer[]{1, 2, 3})//
            .end()//
            .orderBy("id")//
            .orderBy("code");
        PullRequest request = new PullRequest(dataServiceId, query, accessKey, System.currentTimeMillis(), false);
        webTestClient//
            .post().uri("/data/pull")//
            .header("hosts", "183.6.116.33")//
            .body(Mono.just(request), PullRequest.class)//
            .accept(MediaType.APPLICATION_JSON)//
            .exchange()//
            .expectStatus().isOk()//
            .expectBody()//
            .consumeWith(t -> {
                String string = new String(t.getResponseBodyContent());
                System.out.println(string);
            });
    }

}