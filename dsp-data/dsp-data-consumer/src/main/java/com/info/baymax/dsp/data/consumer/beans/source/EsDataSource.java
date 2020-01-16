package com.info.baymax.dsp.data.consumer.beans.source;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString(doNotUseGetters = true)
public class EsDataSource extends DataSource {

    public EsDataSource() {
        super("ES");
    }

    private String clusterName;

    private String ipAddresses;

    private String index;

    private String indexType;

    private String version;// 1.x, 2.x, 5.x, 6.x

    private String filter;

    private String schemaId;

    private String schemaName;

    /*
     * @Override public void format(Task task, List<String> cmdList) { SyncDataTask syncDataTask = (SyncDataTask) task;
     * cmdList.add("-Rip.addresses=" + ipAddresses); cmdList.add("-Rcluster.name=" + clusterName);
     * cmdList.add("-Rindex=" + index); cmdList.add("-Rindex.type=" + indexType); if (StringUtils.isNotBlank(filter)) {
     * cmdList.add("-Rfilter=" + filter); } cmdList.add("-Rcolumns=" + StringUtils.join(syncDataTask.getSourceFields(),
     * ",")); String cursorCol = syncDataTask.getCursorCol(); String cursorType = syncDataTask.getCursorType(); String
     * cursorVal = syncDataTask.getCursorVal(); if (StringUtils.isNotBlank(cursorCol)) { cmdList.add("-Rcursor.column="
     * + cursorCol); } if (StringUtils.isNotBlank(cursorType)) { cmdList.add("-Rcursor.type=" + cursorType); } if
     * (StringUtils.isNotBlank(cursorVal)) { cmdList.add("-Rcursor.value=" + cursorVal); } }
     */
    @Override
    public String getObject() {
        return index + "/" + indexType;
    }

    @Override
    public String getReaderName() {
        String fullVersion = "";
        if (version.startsWith("5.")) {
            fullVersion = "5.6";
        }
        if (version.startsWith("1.")) {
            fullVersion = "1.7";
        }
        if (version.startsWith("6.")) {
            fullVersion = "6.4";
        }
        return super.getReaderName() + fullVersion;
    }
}
