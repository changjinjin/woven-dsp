package com.info.baymax.dsp.data.consumer.beans.source;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString(doNotUseGetters = true, exclude = {"password"})
public class FtpDataSource extends DataSource {

    private String host;

    private int port;

    private String username;

    private String password;

    private String fieldsSeparator;

    private String dir;

    private String filename;

    private String schemaId;

    private String schemaName;

    private boolean recursive;

    private boolean secure;

    private boolean skipHeader;

    public FtpDataSource() {
        super("FTP");
    }
    /*
     * @Override public void format(Task task, List<String> cmdList) { cmdList.add("-Rhost=" + getHost());
     * cmdList.add("-Rport=" + getPort()); cmdList.add("-Rusername=" + getUsername()); cmdList.add("-Rpassword=" +
     * getPassword()); cmdList.add("-Rdir=" + getDir()); cmdList.add("-Rfilename=" +
     * DynamicReplacer.replace(getFilename())); if (recursive) { cmdList.add("-Rrecursive=true"); } if (secure) {
     * cmdList.add("-Rsecure=true"); } if (skipHeader) { cmdList.add("-Rskip.header=true"); } if (task instanceof
     * SyncDataTask) { cmdList.add("-Rfields.separator=" + getFieldsSeparator()); } if (task instanceof SyncFileTask) {
     * SyncFileTask syncFileTask = (SyncFileTask) task; if
     * ("WOVEN-SERVER".equalsIgnoreCase(syncFileTask.getCollecterId())) { cmdList.add("-Rread.to=hdfs"); } else {
     * cmdList.add("-Rread.to=socket"); } LocalDataStore localDataStore = (LocalDataStore) syncFileTask.getDataStore();
     * cmdList.add("-Rhdfs.path=" + localDataStore.getPath()); cmdList.add("-Rhdfs.overwrite=" +
     * localDataStore.isOverwrite()); } }
     */

    @Override
    public String getObject() {
        return dir;
    }
}
