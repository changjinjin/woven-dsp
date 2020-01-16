package com.info.baymax.dsp.data.consumer.beans.source;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LocalFSDataSource extends DataSource {

    private String path;

    private String encoder;

    private String schemaId;

    private String schemaName;

    public LocalFSDataSource() {
        super("LOCALFS");
    }

    /*
     * @Override public void format(Task task, List<String> cmdList) { cmdList.add("-Rpath=" + getPath());
     * cmdList.add("-Rencoder=" + getEncoder()); }
     */

    @Override
    public String getObject() {
        return path;
    }
}
