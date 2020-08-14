package com.info.baymax.dsp.access.dataapi.client;

import com.alibaba.fastjson.JSON;
import com.info.baymax.access.dataapi.api.AggRequest;
import com.info.baymax.access.dataapi.api.RecordRequest;
import com.info.baymax.common.queryapi.query.aggregate.AggQuery;
import com.info.baymax.common.queryapi.query.aggregate.AggType;
import com.info.baymax.common.queryapi.query.field.FieldGroup;
import com.info.baymax.common.queryapi.query.record.RecordQuery;
import org.junit.Before;
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

@AutoConfigureWebTestClient(timeout = "3600000")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DataPullClientTest {
    @Autowired
    private WebTestClient webTestClient;
    private String accessKey = "07739986-6232-4e75-9e97-fe3321919d94";

    @Before
    public void init() {
        webTestClient//
            .get().uri(uriBuilder -> uriBuilder.path("/data/secertkey").queryParam("accessKey", accessKey).build())
            .accept(MediaType.APPLICATION_JSON)//
            .exchange()//
            .expectStatus().isOk()//
            .expectBody()//
            .consumeWith(t -> {
                System.out.println(JSON.toJSONString(t));
            });
    }

    @Test
    public void pullJdbc() throws Exception {
        // long dataServiceId = 725059842368077824L;//snowball
        long dataServiceId = 730732306678939648L; // mysql
        RecordQuery query = RecordQuery.builder()//
            .page(1, 3)//
            // .allProperties("id", "code", "date", "project", "income", "manager")//
            // .selectProperties("id", "code", "project", "income", "manager")//
            .excludeProperties("manager")//
            .fieldGroup(//
                FieldGroup.builder()//
                    .andGreaterThan("id", 2)//
                    .andGroup(FieldGroup.builder().andNotEqualTo("code", "x").orNotEqualTo("project", "px"))//
                // .andIn("code", new String[] { "'1'", "'2'", "'3'" })//
            )//
            .orderBy("id")//
            .orderBy("code");
        RecordRequest request = new RecordRequest(accessKey, dataServiceId, System.currentTimeMillis(), false, query);
        webTestClient//
            .post().uri("/data/pullRecords")//
            .header("hosts", "183.6.116.33")//
            .body(Mono.just(request), RecordRequest.class)//
            .accept(MediaType.APPLICATION_JSON)//
            .exchange()//
            .expectStatus().isOk()//
            .expectBody()//
            .consumeWith(t -> {
                String string = new String(t.getResponseBodyContent());
                System.out.println(string);
            });
    }

    @Test
    public void pullJdbcAgg() throws Exception {
        // long dataServiceId = 725059842368077824L;//snowball
        long dataServiceId = 730732306678939648L; // mysql
        AggQuery query = AggQuery.builder()//
            .page(1, 3)//
            // .allProperties("id", "code", "date", "project", "income", "manager")//
            // .selectProperties("id", "code", "project", "income", "manager")//
            .fieldGroup(//
                FieldGroup.builder()//
                    .andGreaterThan("id", 2)//
                    .andGroup(FieldGroup.builder().andNotEqualTo("code", "x").orNotEqualTo("project", "px"))//
                // .andIn("code", new String[] { "'1'", "'2'", "'3'" })//
            )//
            .aggField("income", AggType.COUNT)//
            .aggField("income", AggType.AVG)//
            .aggField("income", AggType.SUM)//
            .aggField("income", AggType.MAX)//
            .aggField("income", AggType.MIN)//
            .groupField("project")//
            .groupField("code")//
            .havingFieldGroup(
                FieldGroup.builder().andGreaterThan("sum_income", 1000).andLessThan("min_income", 10000))//
            .orderBy("sum_income");
        AggRequest request = new AggRequest(accessKey, dataServiceId, System.currentTimeMillis(), false, query);
        webTestClient//
            .post().uri("/data/pullAggs")//
            .header("hosts", "183.6.116.33")//
            .body(Mono.just(request), AggRequest.class)//
            .accept(MediaType.APPLICATION_JSON)//
            .exchange()//
            .expectStatus().isOk()//
            .expectBody()//
            .consumeWith(t -> {
                String string = new String(t.getResponseBodyContent());
                System.out.println(string);
            });
    }

    @Test
    public void pullEsJdbc() throws Exception {
        RecordQuery query = RecordQuery.builder()//
            .page(1, 3)//
            .allProperties("skuId", "name", "category", "price", "brand", "stock")//
            .selectProperties("skuId", "name", "category", "price", "brand", "stock")//
            .fieldGroup(FieldGroup.builder().andGreaterThan("price", 200D)//
                // .andIn("category", new String[] { "101" })//
            )//
            .orderBy("skuId")//
            .orderBy("name");

        long dataServiceId = 727202723199451136L;
        RecordRequest request = new RecordRequest(accessKey, dataServiceId, System.currentTimeMillis(), false, query);
        System.out.println("request:" + JSON.toJSONString(request));
        webTestClient//
            .post().uri("/data/pullRecords")//
            .header("hosts", "183.6.116.33")//
            .body(Mono.just(request), RecordRequest.class)//
            .accept(MediaType.APPLICATION_JSON)//
            .exchange()//
            .expectStatus().isOk()//
            .expectBody()//
            .consumeWith(t -> {
                String string = new String(t.getResponseBodyContent());
                System.out.println(string);
            });
    }

    @Test
    public void pullEsJdbcAgg() throws Exception {
        AggQuery query = AggQuery.builder()//
            .page(1, 3)//
            // .allProperties("skuId", "name", "category", "price", "brand", "stock")//
            // .selectProperties("skuId", "name", "category", "price", "brand", "stock")//
            .fieldGroup(FieldGroup.builder().andGreaterThan("price", 200D)//
                // .andIn("category", new String[] { "101" })//
            )//
            .orderBy("skuId")//
            .orderBy("name")//
            .aggField("price", AggType.COUNT)//
            .aggField("price", AggType.AVG)//
            .aggField("price", AggType.MAX)//
            .aggField("price", AggType.MIN)//
            .aggField("price", AggType.SUM)//
            .groupFields("skuId", "name", "category")//
            .havingFieldGroup(FieldGroup.builder()/* .andBetween("avg_price", 0, 10000) */)//
            .orderBy("max_price");

        long dataServiceId = 727202723199451136L;
        AggRequest request = new AggRequest(accessKey, dataServiceId, System.currentTimeMillis(), false, query);
        System.out.println("request:" + JSON.toJSONString(request));
        webTestClient//
            .post().uri("/data/pullAggs")//
            .header("hosts", "183.6.116.33")//
            .body(Mono.just(request), RecordRequest.class)//
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
