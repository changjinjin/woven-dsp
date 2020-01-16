package com.info.baymax.dsp.data.consumer.beans.source;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by joey on 2017/5/26.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", include = JsonTypeInfo.As.PROPERTY)
@JsonSubTypes({
        @JsonSubTypes.Type(value = JdbcDataSource.class, name = "JDBC"),
        @JsonSubTypes.Type(value = HttpDataSource.class, name = "HTTP"),
        @JsonSubTypes.Type(value = FtpDataSource.class, name = "FTP"),
        @JsonSubTypes.Type(value = SocketDataSource.class, name = "SOCKET"),
        @JsonSubTypes.Type(value = MongoDBDataSource.class, name = "MONGODB"),
        @JsonSubTypes.Type(value = EsDataSource.class, name = "ES"),
        @JsonSubTypes.Type(value = LocalFSDataSource.class, name = "LOCALFS"),
})
@Setter
@Getter
public abstract class DataSource implements Serializable {

    private String id;

    private String name;

    private String type = "JDBC";

    private Map properties = new HashMap();

    public DataSource(String type) {
        this.type = type;
    }

//    public abstract void format(Task task, List<String> cmdList);

    public abstract String getObject();

    public String getReaderName() {
        return type.toLowerCase();
    }
}
