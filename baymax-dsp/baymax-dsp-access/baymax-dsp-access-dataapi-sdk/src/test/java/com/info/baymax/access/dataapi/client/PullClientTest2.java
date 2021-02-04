package com.info.baymax.access.dataapi.client;

import com.info.baymax.common.core.page.IPage;
import com.info.baymax.common.core.result.MapEntity;
import com.info.baymax.common.queryapi.query.sql.SqlQuery;
import com.info.baymax.dsp.access.dataapi.client.DeaultPullClient;
import com.info.baymax.dsp.access.dataapi.client.PullClient;
import com.info.baymax.dsp.access.dataapi.client.PullClientException;
import org.junit.Test;

public class PullClientTest2 {

    // 服务地址
    private final String baseUrl = "http://192.168.1.82:8008/";

    // 从用户的注册的应用信息里面获取
    private final String accessKey = "2729da8c-1ff7-42ac-9f3a-2bc0cd652a05";
    // 注册应用时RSA生成的公钥和私钥，私钥用于加密每次secertkey请求的秘钥，公钥用于此处解密，解密后的字符串用于数据的AES解密
    private final String publicKey = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAIT214CYGJGuntnZ2N380r+U+uhUES/9gjx3r+ub8ZA9VksNG/uQnbQ91LerEhrr1pZGCYAl7Ribpg8iU92kkLMCAwEAAQ==";

    // 数据服务ID
    private final Long dataServiceId = 745976750915190784L;

    // 绑定IP
    private final String hosts = "192.168.2.145";

    // 是否启用报文加密，如果启用需要先请求秘钥，使用秘钥对加密报文解密
    private final boolean encrypted = false;

    private final PullClient client = new DeaultPullClient(baseUrl);

    @Test
    public void pullBySql() {
        try {
            SqlQuery query = SqlQuery.builder()//
                .parameter("price", 100)//
                .page(1, 20)// 分页
                ;
            IPage<MapEntity> pullBySql = client.pullBySql(accessKey, publicKey, dataServiceId, encrypted, hosts, query);
            System.out.println(pullBySql);
        } catch (PullClientException e) {
            e.printStackTrace();
        }
    }

}
