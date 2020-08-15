package com.info.baymax.dsp.access.dataapi.data.elasticsearch;

import com.info.baymax.dsp.access.dataapi.data.StorageConf;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

@Setter
@Getter
public class ElasticSearchStorageConf extends StorageConf {
    private static final long serialVersionUID = -187465371872121001L;

    private String clusterName;// "elasticsearch"
    private String encryptColumns;// ""
    private String encryptKey;// ""
    private String index;// "sink_es"
    private String indexType;// "sink_es"
    private String ipAddresses;// "info5;//9203"
    private String pathMode;// "exact"

    private String httpAuthUser;
    private String httpAuthPassword;
    private String username;
    private String password;
    private boolean multiThreaded = true;
    private long connectionTimeout = 300L;
    private long readTimeout = 300L;
    private String proxyHost;
    private String proxyPort;

    public String getUsername() {
        return StringUtils.defaultIfEmpty(username, httpAuthUser);
    }

    public String getPassword() {
        return StringUtils.defaultIfEmpty(password, httpAuthPassword);
    }

}
