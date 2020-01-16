package com.info.baymax.dsp.data.consumer.beans.source;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SocketDataSource extends DataSource {

    private String bind;

    private int port;

    private String protocol;

    private int operateType; //0: 原样, 1: 抽取, 2: 分割, 3: 过滤

    private String regex;

    private String nullString;

    private String defaultsMap;

    private String schemaId;

    private String schemaName;

    private String charset;

    public SocketDataSource() {
        super("SOCKET");
    }

    /*
     * @Override public void format(Task task, List<String> cmdList) { SyncDataTask syncDataTask = (SyncDataTask) task;
     * cmdList.add("-Rreceiver.bind=" + this.bind); cmdList.add("-Rreceiver.port=" + this.port); if
     * (StringUtils.isNotBlank(this.charset)) { cmdList.add("-Rreceiver.charset=" + this.charset); }
     * cmdList.add("-Roperate.type=" + this.operateType); cmdList.add("-Roperate.regex=" + this.regex); List<String>
     * sourceFields = syncDataTask.getSourceFields(); cmdList.add("-Rextract.columns=" + StringUtils.join(sourceFields,
     * ",")); if (this.nullString != null) cmdList.add("-Rnull.string=" + this.nullString); if (this.defaultsMap !=
     * null) cmdList.add("-Rdefaults.map=" + this.defaultsMap); }
     */

    @Override
    public String getObject() {
        return String.valueOf(this.port);
    }
}
