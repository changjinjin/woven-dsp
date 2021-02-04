package com.info.baymax.dsp.access.dataapi.client;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.info.baymax.common.core.page.IPage;
import com.info.baymax.common.core.result.MapEntity;
import com.info.baymax.common.core.result.Response;
import com.info.baymax.common.queryapi.query.aggregate.AggQuery;
import com.info.baymax.common.queryapi.query.record.RecordQuery;
import com.info.baymax.common.queryapi.query.sql.SqlQuery;
import com.info.baymax.dsp.access.dataapi.api.AggRequest;
import com.info.baymax.dsp.access.dataapi.api.DataRequest;
import com.info.baymax.dsp.access.dataapi.api.RecordRequest;
import com.info.baymax.dsp.access.dataapi.api.SqlRequest;
import com.info.baymax.dsp.access.dataapi.utils.AESUtil;
import com.info.baymax.dsp.access.dataapi.utils.HttpUtils;
import com.info.baymax.dsp.access.dataapi.utils.RSAGenerater;

import java.util.HashMap;
import java.util.Map;

public class DeaultPullClient implements PullClient {
    private final String baseUrl;
    private final Gson gson;
    private final Map<String, Object> params;
    private final Map<String, String> headers;

    public DeaultPullClient(String baseUrl) {
        this.baseUrl = baseUrl;
        gson = new Gson();
        params = new HashMap<String, Object>();
        headers = new HashMap<String, String>();
    }

    @Override
    public String secertkey(String accessKey, String publicKey) throws PullClientException {
        Response<String> response = null;
        try {
            params.clear();
            params.put("accessKey", accessKey);
            String httpGet = HttpUtils.httpGet(getUrl("secertkey"), params, headers);
            if (httpGet != null && !httpGet.isEmpty()) {
                response = gson.fromJson(httpGet, new TypeToken<Response<String>>() {
                }.getType());
                if (response.isOk()) {
                    return RSAGenerater.decryptByPublicKey(response.getContent(), publicKey);
                }
            }
        } catch (Exception e) {
            throw new PullClientException(e);
        }
        return null;
    }

    @Override
    public IPage<MapEntity> pullRecords(String accessKey, String publicKey, Long dataServiceId, boolean encrypted,
                                        String hosts, RecordQuery query) throws PullClientException {
        return pullData("pullRecords", accessKey, publicKey, encrypted, hosts,
            new RecordRequest(accessKey, dataServiceId, System.currentTimeMillis(), encrypted, query));
    }

    @Override
    public IPage<MapEntity> pullAggs(String accessKey, String publicKey, Long dataServiceId, boolean encrypted,
                                     String hosts, AggQuery query) throws PullClientException {
        return pullData("pullAggs", accessKey, publicKey, encrypted, hosts,
            new AggRequest(accessKey, dataServiceId, System.currentTimeMillis(), encrypted, query));
    }

    @Override
    public IPage<MapEntity> pullBySql(String accessKey, String publicKey, Long dataServiceId, boolean encrypted,
                                      String hosts, SqlQuery query) throws PullClientException {
        return pullData("pullBySql", accessKey, publicKey, encrypted, hosts,
            new SqlRequest(accessKey, dataServiceId, System.currentTimeMillis(), encrypted, query));
    }

    private IPage<MapEntity> pullData(String queryName, String accessKey, String publicKey, boolean encrypted,
                                      String hosts, DataRequest<?> request) throws PullClientException {
        String secertkey = null;
        try {
            headers.clear();
            headers.put("hosts", hosts);

            if (encrypted) {
                secertkey = secertkey(accessKey, publicKey);
            }

            String httpPostJson = HttpUtils.httpPostJson(getUrl(queryName), gson.toJson(request), headers);
            Response<Object> response = gson.fromJson(httpPostJson, new TypeToken<Response<Object>>() {
            }.getType());

            if (response.isOk()) {
                Object object = response.getContent();
                if (object != null) {
                    String content = null;
                    if (!(object instanceof String)) {
                        content = gson.toJson(object);
                    } else {
                        content = (String) object;
                    }
                    if (content != null && !content.isEmpty()) {
                        if (encrypted) {
                            content = AESUtil.decrypt(content, secertkey);
                        }
                        return gson.fromJson(content, new TypeToken<IPage<MapEntity>>() {
                        }.getType());
                    }
                }
            } else {
                throw new RuntimeException(response.getMessage());
            }
        } catch (Exception e) {
            throw new PullClientException(e);
        }
        return null;
    }

    private String getUrl(String queryName) {
        return baseUrl + "api/dsp/dataapi/data/" + queryName;
    }
}
