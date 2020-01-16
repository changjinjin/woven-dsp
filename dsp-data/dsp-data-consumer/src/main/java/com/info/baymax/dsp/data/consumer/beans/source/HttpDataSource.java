package com.info.baymax.dsp.data.consumer.beans.source;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HttpDataSource extends DataSource {

    private String url;

    private String method;

    private String parameters;

    private String rootPath;

    private String schemaId;

    private String schemaName;

    public HttpDataSource() {
        super("HTTP");
    }

    /**
     * "-Rurl=" + getUrl() + " -Rmethod=" + getMethod() + " -Rparameters=" + getParameters() + "
     * -Rroot.path=" + getRootPath() + " -Rfields=" + StringUtils.join(task.getSourceFields(), ",");
     *
     * @param task
     * @param cmdList
     */
    /*
     * @Override public void format(Task task, List<String> cmdList) { SyncDataTask syncDataTask = (SyncDataTask) task;
     * cmdList.add("-Rurl=" + getUrl()); cmdList.add("-Rmethod=" + getMethod()); cmdList.add("-Rparameters=" +
     * getParameters()); cmdList.add("-Rroot.path=" + getRootPath()); cmdList.add("-Rfields=" +
     * StringUtils.join(syncDataTask.getSourceFields(), ",")); }
     */
    @Override
    public String getObject() {
        return url;
    }
}
