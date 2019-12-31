package com.info.baymax.dsp.job.exec.util;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class HdfsUtil {

    private static HdfsUtil hdfsUtil = new HdfsUtil();

    public static HdfsUtil getInstance() {
        return hdfsUtil;
    }

    private FileSystem fs;

    private HdfsUtil() {
        init();
    }

    private void init() {
        try {
            Configuration conf = new Configuration();
            String USER_NAME = System.getenv("HADOOP_USER_NAME");
            if (StringUtils.isBlank(USER_NAME)) {
                USER_NAME = "merce";
            }
            System.setProperty("HADOOP_USER_NAME", USER_NAME);
            String CONF_PATH = System.getenv("HADOOP_CONF_DIR");
            if (StringUtils.isNotBlank(CONF_PATH)) {
                conf.addResource(new Path(CONF_PATH + File.separator + "core-site.xml"));
                conf.addResource(new Path(CONF_PATH + File.separator + "core-site.xml"));
            }
            conf.setBoolean("fs.hdfs.impl.disable.cache", true);
            this.fs = FileSystem.get(conf);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public boolean clean(String filePath) throws Throwable {
        try {
            Path hdfsPath = new Path(filePath);
            if (fs.exists(hdfsPath)) {
                boolean isDeleted = fs.delete(hdfsPath, true);
                if (isDeleted) {
                    return fs.mkdirs(hdfsPath);
                }
            }
        } catch (Throwable e) {
            init();
            throw e;
        }
        return false;
    }

    public boolean exist(String path) {
        try {
            return fs.exists(new Path(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public FileStatus status(String path) {
        if (!exist(path)) return null;
        try {
            FileStatus status = fs.getFileStatus(new Path(path));
            return status;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<String> read(String fileName) {
        try {
            List<String> output = new ArrayList<String>();
            FSDataInputStream dis = fs.open(new Path(fileName));
            BufferedReader reader = new BufferedReader(new InputStreamReader(dis));
            String line = null;
            while ((line = reader.readLine()) != null) {
                output.add(line);
            }
            reader.close();
            dis.close();
            return output;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<String> read(String fileName, String charset) {
        try {
            List<String> output = new ArrayList<String>();
            FSDataInputStream dis = fs.open(new Path(fileName));
            BufferedReader reader = new BufferedReader(new InputStreamReader(dis, charset));
            String line = null;
            while ((line = reader.readLine()) != null) {
                output.add(line);
            }
            reader.close();
            dis.close();
            return output;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}