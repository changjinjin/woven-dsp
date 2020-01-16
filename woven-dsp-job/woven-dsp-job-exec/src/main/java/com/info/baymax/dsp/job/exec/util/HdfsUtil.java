package com.info.baymax.dsp.job.exec.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.PathFilter;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

@Slf4j
public class HdfsUtil {

    private FileSystem fs;

    public HdfsUtil() {
        init();
    }


    public HdfsUtil(byte[] confFile){
        initConf(confFile);
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
                conf.addResource(new Path(CONF_PATH + File.separator + "hdfs-site.xml"));
            }
            this.fs = FileSystem.get(conf);
        } catch (Throwable e) {
            log.error("init hdfs FileSystem exception :", e);
        }
    }

    private void initConf(byte[] confBuff){
        try {
            Configuration conf = buildConfiguration(confBuff);

            this.fs = FileSystem.get(conf);
        } catch (Throwable e) {
            log.error("init hdfs FileSystem exception :", e);
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

    public String[] files(String folder) {
        return files(folder, null);
    }

    public String[] files(String folder, PathFilter filter) {
        try {
            Path f = new Path(folder);
            FileStatus[] files = null;
            if (filter == null) {
                files = fs.listStatus(f);
            } else {
                files = fs.listStatus(f, filter);
            }
            String[] result = new String[files.length];
            for (int i = 0; i < result.length; i++) {
                result[i] = files[i].getPath().getName();
            }
//			hdfs.close();
            return result;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Configuration buildConfiguration(byte[] buff) {
        if (buff == null || buff.length == 0){
            throw new RuntimeException("hadoop conf file can't be null or empty.");
        }
        String LOCAL_TEMP_PATH = "/tmp/dsp_hadoop_config.zip";
        File tmpZipFile = new File(LOCAL_TEMP_PATH);

        Configuration configuration = new Configuration(false);
        try {
            //TODO 将byte[]写入本地临时zip文件.
            FileUtils.writeByteArrayToFile(tmpZipFile, buff, false);
            //TODO 获取文件输入流
            ByteArrayInputStream input = new ByteArrayInputStream(buff);
            //TODO 获取ZIP输入流
            ZipInputStream zipInputStream = new ZipInputStream(new BufferedInputStream(input), StandardCharsets.UTF_8);

            ZipFile zipFile = new ZipFile(tmpZipFile);
            ZipEntry ze = null;
            while ((ze = zipInputStream.getNextEntry()) != null) {
                if (ze.getName().endsWith(".xml")) {
                    configuration.addResource(zipFile.getInputStream(ze), ze.getName());
                    log.info("add resource:{}", ze.getName());
                }
            }

            //TODO close input stream.
            zipInputStream.closeEntry();
            input.close();
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        } finally {
            tmpZipFile.deleteOnExit();
        }

        return configuration;
    }
}
