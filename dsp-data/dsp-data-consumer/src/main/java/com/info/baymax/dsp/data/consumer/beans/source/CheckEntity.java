package com.info.baymax.dsp.data.consumer.beans.source;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.info.baymax.common.message.result.ErrType;
import com.info.baymax.common.message.result.Response;
import com.info.baymax.dsp.data.consumer.entity.CustDataSource;

public class CheckEntity {

    public static Response<?> checkDataSource(CustDataSource dataSource) {
        if (dataSource == null) {
            return Response.error(ErrType.ENTITY_EMPTY, "dataSource is null");
        } else {
            if (StringUtils.isEmpty(dataSource.getName()) || "$unknow".equals(dataSource.getName())) {
                return Response.error(ErrType.ENTITY_EMPTY, "dataSource[Attributes[name]] is null");
            }
            if (!DataSourceType.contains(dataSource.getType())) {
                return Response.error(ErrType.BAD_REQUEST, "dataSource[type] is INVALID");
            }
            if (DataSourceType.DB.getValue().equals(dataSource.getType())) {
                Map<String, Object> storageMap = dataSource.getAttributes();
                if (storageMap == null) {
                    return Response.error(ErrType.BAD_REQUEST, "dataSource[Attributes] is null");
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
                        return Response.error(ErrType.BAD_REQUEST, "dataSource[Attributes[dbType]] is null");
                    }
                    if (!DBType.contains(dbType)) {
                        return Response.error(ErrType.BAD_REQUEST, "dataSource[Attributes[dbType]] is INVALID");
                    }
                    if (StringUtils.isEmpty(url)) {
                        return Response.error(ErrType.BAD_REQUEST, "dataSource[Attributes[url]] is null");
                    }
                    if (StringUtils.isEmpty(database) && DBType.HIVE != DBType.valueOfType(dbType)) {
                        return Response.error(ErrType.BAD_REQUEST, "dataSource[Attributes[database]] is null");
                    }
                    if (StringUtils.isEmpty(password)) {
                        return Response.error(ErrType.BAD_REQUEST, "dataSource[Attributes[password]] is null");
                    }
                    if (StringUtils.isEmpty(driver)) {
                        return Response.error(ErrType.BAD_REQUEST, "dataSource[Attributes[driver]] is null");
                    }
                    if (port == null || (port != null && StringUtils.isEmpty(port.toString()))) {
                        return Response.error(ErrType.BAD_REQUEST, "dataSource[Attributes[port]] is null");
                    }
                    if (StringUtils.isEmpty(user)) {
                        return Response.error(ErrType.BAD_REQUEST, "dataSource[Attributes[user]] is null");
                    }
                    if (StringUtils.isEmpty(host)) {
                        return Response.error(ErrType.BAD_REQUEST, "dataSource[Attributes[host]] is null");
                    }
                }
            }
            if (DataSourceType.HTTP.getValue().equals(dataSource.getType())) {
            	Map<String, Object> storageMap = dataSource.getAttributes();
                if (storageMap == null) {
                    return Response.error(ErrType.BAD_REQUEST, "dataSource[Attributes] is null");
                } else {
                    String method = (String) storageMap.get("method");
                    String url = (String) storageMap.get("url");
                    if (!RequestType.contains(method)) {
                        return Response.error(ErrType.BAD_REQUEST, "dataSource[Attributes[method]] is INVALID");
                    }
                    if (StringUtils.isEmpty(url)) {
                        return Response.error(ErrType.BAD_REQUEST, "dataSource[Attributes[url]] is null");
                    }
                }

            }
            if (DataSourceType.FTP.getValue().equals(dataSource.getType())) {
            	Map<String, Object> storageMap = dataSource.getAttributes();
                if (storageMap == null) {
                    return Response.error(ErrType.BAD_REQUEST, "dataSource[Attributes] is null");
                } else {
                    String password = (String) storageMap.get("password");
                    Object port = storageMap.get("port");
                    String host = (String) storageMap.get("host");
                    String dir = (String) storageMap.get("dir");
                    String username = (String) storageMap.get("username");
                    if (StringUtils.isEmpty(password)) {
                        return Response.error(ErrType.BAD_REQUEST, "dataSource[Attributes[password]] is null");
                    }
                    if (port == null || (port != null && StringUtils.isEmpty(port.toString()))) {
                        return Response.error(ErrType.BAD_REQUEST, "dataSource[Attributes[port]] is null");
                    }
                    if (StringUtils.isEmpty(host)) {
                        return Response.error(ErrType.BAD_REQUEST, "dataSource[Attributes[host]] is null");
                    }
                    if (StringUtils.isEmpty(dir)) {
                        return Response.error(ErrType.BAD_REQUEST, "dataSource[Attributes[dir]] is null");
                    }
                    if (StringUtils.isEmpty(username)) {
                        return Response.error(ErrType.BAD_REQUEST, "dataSource[Attributes[username]] is null");
                    }
                }
            }
            if (DataSourceType.SOCKET.getValue().equals(dataSource.getType())) {
            	Map<String, Object> storageMap = dataSource.getAttributes();
                if (storageMap == null) {
                    return Response.error(ErrType.BAD_REQUEST, "dataSource[Attributes] is null");
                } else {
                    String protocol = (String) storageMap.get("protocol");
                    String ipAddress = (String) storageMap.get("ipAddress");
                    Object port = storageMap.get("port");
                    if (!ProtocolType.contains(protocol)) {
                        return Response.error(ErrType.BAD_REQUEST, "dataSource[Attributes[protocol]] is INVALID");
                    }
                    if (StringUtils.isEmpty(ipAddress)) {
                        return Response.error(ErrType.BAD_REQUEST, "dataSource[Attributes[ipAddress]] is null");
                    }
                    if (port == null || (port != null && StringUtils.isEmpty(port.toString()))) {
                        return Response.error(ErrType.BAD_REQUEST, "dataSource[Attributes[port]] is null");
                    }
                }
            }
            if (DataSourceType.MONGODB.getValue().equals(dataSource.getType())) {
            	Map<String, Object> storageMap = dataSource.getAttributes();
                if (storageMap == null) {
                    return Response.error(ErrType.BAD_REQUEST, "dataSource[Attributes] is null");
                } else {
                    String database = (String) storageMap.get("database");
                    String address = (String) storageMap.get("address");
                    Object port = storageMap.get("port");
                    if (StringUtils.isEmpty(database)) {
                        return Response.error(ErrType.BAD_REQUEST, "dataSource[Attributes[database]] is null");
                    }
                    if (StringUtils.isEmpty(address)) {
                        return Response.error(ErrType.BAD_REQUEST, "dataSource[Attributes[address]] is null");
                    }
                    if (port == null || (port != null && StringUtils.isEmpty(port.toString()))) {
                        return Response.error(ErrType.BAD_REQUEST, "dataSource[Attributes[port]] is null");
                    }
                }

            }
            if (DataSourceType.ES.getValue().equals(dataSource.getType())) {
            	Map<String, Object> storageMap = dataSource.getAttributes();
                if (storageMap == null) {
                    return Response.error(ErrType.BAD_REQUEST, "dataSource[Attributes] is null");
                } else {
                    String indexType = (String) storageMap.get("indexType");
                    String clusterName = (String) storageMap.get("clusterName");
                    String index = (String) storageMap.get("index");
                    String ipAddresses = (String) storageMap.get("ipAddresses");
                    String version = (String) storageMap.get("version");
                    if (StringUtils.isEmpty(indexType)) {
                        return Response.error(ErrType.BAD_REQUEST, "dataSource[Attributes[indexType]] is null");
                    }
                    if (StringUtils.isEmpty(clusterName)) {
                        return Response.error(ErrType.BAD_REQUEST, "dataSource[Attributes[clusterName]] is null");
                    }
                    if (StringUtils.isEmpty(index)) {
                        return Response.error(ErrType.BAD_REQUEST, "dataSource[Attributes[index]] is null");
                    }
                    if (StringUtils.isEmpty(ipAddresses)) {
                        return Response.error(ErrType.BAD_REQUEST, "dataSource[Attributes[ipAddresses]] is null");
                    }
                    if (!EsVersion.contains(version)) {
                        return Response.error(ErrType.BAD_REQUEST, "dataSource[Attributes[ipAddresses]] is INVALID");
                    }
                }
            }
        }
        return Response.ok();
    }
}
