package com.info.baymax.dsp.data.consumer.beans.source;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString(doNotUseGetters = true, exclude = {"password"})
public class MongoDBDataSource extends DataSource {

    private String address;

    private int port;

    private String username;

    private String password;

    private String database;

    private String collection;

    private String query;

    private String schemaId;

    private String schemaName;

    public MongoDBDataSource() {
        super("MONGODB");
    }

    /*
     * @Override public void format(Task task, List<String> cmdList) { SyncDataTask syncDataTask = (SyncDataTask) task;
     * cmdList.add("-Raddress=" + address); cmdList.add("-Rport=" + port); cmdList.add("-Rusername=" + username);
     * cmdList.add("-Rpassword=" + password); cmdList.add("-Rdatabase=" + database); cmdList.add("-Rcollection=" +
     * collection); cmdList.add("-Rquery=" + query); cmdList.add("-Rcolumns=" +
     * StringUtils.join(syncDataTask.getSourceFields(), ",")); String cursorCol = syncDataTask.getCursorCol(); String
     * cursorType = syncDataTask.getCursorType(); String cursorVal = syncDataTask.getCursorVal(); if
     * (StringUtils.isNotBlank(cursorCol)) { cmdList.add("-Rcursor.column=" + cursorCol); } if
     * (StringUtils.isNotBlank(cursorType)) { cmdList.add("-Rcursor.type=" + cursorType); } if
     * (StringUtils.isNotBlank(cursorVal)) { cmdList.add("-Rcursor.value=" + cursorVal); } }
     */

    @Override
    public String getObject() {
        return this.database + "/" + this.collection;
    }
}
