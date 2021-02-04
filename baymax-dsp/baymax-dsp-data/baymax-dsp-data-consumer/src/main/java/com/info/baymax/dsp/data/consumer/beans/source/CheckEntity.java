package com.info.baymax.dsp.data.consumer.beans.source;

import com.info.baymax.common.core.result.ErrType;
import com.info.baymax.common.core.result.Response;
import com.info.baymax.common.utils.JsonUtils;
import com.info.baymax.dsp.data.consumer.entity.CustDataSource;
import org.apache.commons.lang3.StringUtils;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class CheckEntity {

    public static Response<?> checkDataSource(CustDataSource dataSource) {
        List<String> errorList = new LinkedList<>();
        if (dataSource == null) {
            return Response.error(ErrType.ENTITY_EMPTY, "dataSource is null").build();
        } else {
            if (StringUtils.isEmpty(dataSource.getName()) || "$unknow".equals(dataSource.getName())) {
                return Response.error(ErrType.ENTITY_EMPTY, "dataSource[Attributes[name]] is null").build();
            }
            if (!DataSourceType.contains(dataSource.getType())) {
                return Response.error(ErrType.BAD_REQUEST, "dataSource[type] is INVALID").build();
            }
            if (DataSourceType.DB.getValue().equals(dataSource.getType())) {
                Map<String, Object> storageMap = dataSource.getAttributes();
                if (storageMap == null) {
                    errorList.add("dataSource[Attributes] is null");
                } else {
                    String database = (String) storageMap.get("database");
                    String password = (String) storageMap.get("password");
                    String driver = (String) storageMap.get("driver");
                    Object port = storageMap.get("port");
                    String host = (String) storageMap.get("host");
                    String dbType = (String) storageMap.get("DBType");
                    String user = (String) storageMap.get("user");
                    String url = (String) storageMap.get("url");
                    if (StringUtils.isEmpty(dbType)) {
                        errorList.add("dataSource[Attributes[dbType]] is null");
                    }
                    if (!DBType.contains(dbType)) {
                        errorList.add("dataSource[Attributes[dbType]] is INVALID");
                    }
                    if (StringUtils.isEmpty(url)) {
                        errorList.add("dataSource[Attributes[url]] is null");
                    }
                    if (StringUtils.isEmpty(database) && DBType.HIVE != DBType.valueOfType(dbType)) {
                        errorList.add("dataSource[Attributes[database]] is null");
                    }
                    if (StringUtils.isEmpty(password)) {
                        errorList.add("dataSource[Attributes[password]] is null");
                    }
                    if (StringUtils.isEmpty(driver)) {
                        errorList.add("dataSource[Attributes[driver]] is null");
                    }
                    if (port == null || (port != null && StringUtils.isEmpty(port.toString()))) {
                        errorList.add("dataSource[Attributes[port]] is null");
                    }
                    if (StringUtils.isEmpty(user)) {
                        errorList.add("dataSource[Attributes[user]] is null");
                    }
                    if (StringUtils.isEmpty(host)) {
                        errorList.add("dataSource[Attributes[host]] is null");
                    }
                }
            }
            else if (DataSourceType.HTTP.getValue().equals(dataSource.getType())) {
            	Map<String, Object> storageMap = dataSource.getAttributes();
                if (storageMap == null) {
                    errorList.add("dataSource[Attributes] is null");
                } else {
                    String method = (String) storageMap.get("method");
                    String url = (String) storageMap.get("url");
                    if (!RequestType.contains(method)) {
                        errorList.add("dataSource[Attributes[method]] is INVALID");
                    }
                    if (StringUtils.isEmpty(url)) {
                        errorList.add("dataSource[Attributes[url]] is null");
                    }
                }

            }
            else if (DataSourceType.FTP.getValue().equals(dataSource.getType())) {
            	Map<String, Object> storageMap = dataSource.getAttributes();
                if (storageMap == null) {
                    errorList.add("dataSource[Attributes] is null");
                } else {
                    String password = (String) storageMap.get("password");
                    Object port = storageMap.get("port");
                    String host = (String) storageMap.get("host");
                    String dir = (String) storageMap.get("dir");
                    String username = (String) storageMap.get("username");
                    if (StringUtils.isEmpty(password)) {
                        errorList.add("dataSource[Attributes[password]] is null");
                    }
                    if (port == null || (port != null && StringUtils.isEmpty(port.toString()))) {
                        errorList.add("dataSource[Attributes[port]] is null");
                    }
                    if (StringUtils.isEmpty(host)) {
                        errorList.add("dataSource[Attributes[host]] is null");
                    }
                    if (StringUtils.isEmpty(dir)) {
                        errorList.add("dataSource[Attributes[dir]] is null");
                    }
                    if (StringUtils.isEmpty(username)) {
                        errorList.add("dataSource[Attributes[username]] is null");
                    }
                }
            }
            else if (DataSourceType.SOCKET.getValue().equals(dataSource.getType())) {
            	Map<String, Object> storageMap = dataSource.getAttributes();
                if (storageMap == null) {
                    errorList.add("dataSource[Attributes] is null");
                } else {
                    String protocol = (String) storageMap.get("protocol");
                    String ipAddress = (String) storageMap.get("ipAddress");
                    Object port = storageMap.get("port");
                    if (!ProtocolType.contains(protocol)) {
                        errorList.add("dataSource[Attributes[protocol]] is INVALID");
                    }
                    if (StringUtils.isEmpty(ipAddress)) {
                        errorList.add("dataSource[Attributes[ipAddress]] is null");
                    }
                    if (port == null || (port != null && StringUtils.isEmpty(port.toString()))) {
                        errorList.add("dataSource[Attributes[port]] is null");
                    }
                }
            }
            else if (DataSourceType.MONGODB.getValue().equals(dataSource.getType())) {
            	Map<String, Object> storageMap = dataSource.getAttributes();
                if (storageMap == null) {
                    errorList.add("dataSource[Attributes] is null");
                } else {
                    String database = (String) storageMap.get("database");
                    String address = (String) storageMap.get("address");
                    Object port = storageMap.get("port");
                    if (StringUtils.isEmpty(database)) {
                        errorList.add("dataSource[Attributes[database]] is null");
                    }
                    if (StringUtils.isEmpty(address)) {
                        errorList.add("dataSource[Attributes[address]] is null");
                    }
                    if (port == null || (port != null && StringUtils.isEmpty(port.toString()))) {
                        errorList.add("dataSource[Attributes[port]] is null");
                    }
                }

            }
            else if (DataSourceType.ES.getValue().equals(dataSource.getType())) {
            	Map<String, Object> storageMap = dataSource.getAttributes();
                if (storageMap == null) {
                    errorList.add("dataSource[Attributes] is null");
                } else {
                    String indexType = (String) storageMap.get("indexType");
                    String clusterName = (String) storageMap.get("clusterName");
                    String index = (String) storageMap.get("index");
                    String ipAddresses = (String) storageMap.get("ipAddresses");
                    String version = (String) storageMap.get("version");
                    if (StringUtils.isEmpty(indexType)) {
                        errorList.add("dataSource[Attributes[indexType]] is null");
                    }
                    if (StringUtils.isEmpty(clusterName)) {
                        errorList.add("dataSource[Attributes[clusterName]] is null");
                    }
                    if (StringUtils.isEmpty(index)) {
                        errorList.add("dataSource[Attributes[index]] is null");
                    }
                    if (StringUtils.isEmpty(ipAddresses)) {
                        errorList.add("dataSource[Attributes[ipAddresses]] is null");
                    }
                    if (!EsVersion.contains(version)) {
                        errorList.add("dataSource[Attributes[ipAddresses]] is INVALID");
                    }
                }
            }
            else if (DataSourceType.KAFKA.getValue().equals(dataSource.getType())) {
                Map<String, Object> storageMap = dataSource.getAttributes();
                if (storageMap == null) {
                    errorList.add("dataSource[Attributes] is null");
                } else {
                    String zookeeper = (String)storageMap.get("zookeeper");
                    String brokers = (String)storageMap.get("brokers");
                    String topic = (String)storageMap.get("topic");
                    String groupId = (String)storageMap.get("groupId");
                    String format = (String)storageMap.get("format");
                    String quoteChar = (String)storageMap.get("quoteChar");
                    String escapeChar = (String)storageMap.get("escapeChar");
                    String separator = (String)storageMap.get("separator");
                    String header = (String)storageMap.get("header");
                    if (StringUtils.isEmpty(zookeeper)) {
                        errorList.add("dataSource[Attributes[zookeeper]] is null");
                    }
                    if (StringUtils.isEmpty(brokers)) {
                        errorList.add("dataSource[Attributes[brokers]] is null");
                    }
                    if (StringUtils.isEmpty(topic)) {
                        errorList.add("dataSource[Attributes[topic]] is null");
                    }
                    if (StringUtils.isEmpty(groupId)) {
                        errorList.add("dataSource[Attributes[groupId]] is null");
                    }
                    if (StringUtils.isEmpty(format)) {
                        errorList.add("dataSource[Attributes[format]] is null");
                    }else if (!KafkaFormat.contains(format)) {
                        errorList.add("dataSource[Attributes[format]] is INVALID");
                    }else if (KafkaFormat.CSV == KafkaFormat.valueOf(format.toUpperCase())) {
                        if (StringUtils.isEmpty(quoteChar)) {
                            errorList.add("dataSource[Attributes[quoteChar]] is null");
                        }
                        if (StringUtils.isEmpty(escapeChar)) {
                            errorList.add("dataSource[Attributes[escapeChar]] is null");
                        }
                        if (StringUtils.isEmpty(separator)) {
                            errorList.add("dataSource[Attributes[separator]] is null");
                        }
                        if (!StringUtils.isEmpty(header)) {
                            if (!TrueOrFalse.contains(header)) {
                                errorList.add("dataSource[Attributes[header]] is INVALID");
                            }
                        }
                    }
                }
            }
            else if (DataSourceType.HDFS.getValue().equals(dataSource.getType())) {
                Map<String, Object> storageMap = dataSource.getAttributes();
                String path = (String)storageMap.get("path");
                String quoteChar = (String)storageMap.get("quoteChar");
                String escapeChar = (String)storageMap.get("escapeChar");
                String separator = (String)storageMap.get("separator");
                String header = (String)storageMap.get("header");
                String format = (String)storageMap.get("format");
                String recursive = (String)storageMap.get("recursive");
                if (StringUtils.isEmpty(path)) {
                    errorList.add("dataSource[Attributes[path]] is null");
                }
                if (StringUtils.isEmpty(format)) {
                    errorList.add("dataSource[Attributes[format]] is null");
                }else if (!FileFormat.contains(format)) {
                    errorList.add("dataSource[Attributes[format]] is INVALID");
                }else if (FileFormat.CSV == FileFormat.valueOf(format.toUpperCase())) {
                    if (StringUtils.isEmpty(quoteChar)) {
                        errorList.add("dataSource[Attributes[quoteChar]] is null");
                    }
                    if (StringUtils.isEmpty(escapeChar)) {
                        errorList.add("dataSource[Attributes[escapeChar]] is null");
                    }
                    if (StringUtils.isEmpty(separator)) {
                        errorList.add("dataSource[Attributes[separator]] is null");
                    }
                    if (!StringUtils.isEmpty(header)) {
                        if (!TrueOrFalse.contains(header)) {
                            errorList.add("dataSource[Attributes[header]] is INVALID");
                        }
                    }
                    if (!StringUtils.isEmpty(recursive)) {
                        if (!TrueOrFalse.contains(recursive)) {
                            errorList.add("dataSource[Attributes[recursive]] is INVALID");
                        }
                    }
                }
            }

            if(errorList.size() > 0){
                return Response.error(ErrType.BAD_REQUEST, JsonUtils.toJson(errorList)).build();
            }
        }
        return Response.ok().build();
    }
}
