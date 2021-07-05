package com.info.baymax.common.client;

import com.alibaba.fastjson.JSONObject;
import com.info.baymax.common.entity.HdfsFile;
import com.info.baymax.common.entity.HdfsFilePermission;
import com.info.baymax.common.http.HadoopHttpClient;
import jodd.net.URLCoder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.ContentType;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.CountDownLatch;

/**
 * create by pengchuan.chen on 2020/6/17
 */
@Slf4j
public class HttpFSClient extends HadoopHttpClient {

    private static final int MAX_DOWNLOAD_THREAD = 5;

    /**
     * 检查NameNode节点状态参数。
     */
    private static final String NAMENODE_STATUS_URL = "jmx?get=Hadoop:service=NameNode,name=FSNamesystem::tag.HAState";

    private static final String SERVICE_PATH = "/webhdfs/v1";

    public static final String DELETE_OP = "op=DELETE&recursive=true";

    public static final String LISTSTATUS_OP = "op=LISTSTATUS";

    public static final String MKDIR_OP = "op=MKDIRS";

    public static final String CREATE_OP = "op=CREATE&buffersize=1000&overwrite=true";

    public static final String OPEN_OP = "op=OPEN&offset=0";

    public static final String GETFILESTATUS_OP = "op=GETFILESTATUS";

    public HttpFSClient(String http_fs_url) {
        super();
        initHttpUrl(http_fs_url);
    }

    public HttpFSClient(String http_fs_url, KerberosParameter kerberosParameter) {
        super(kerberosParameter);
        initHttpUrl(http_fs_url);
    }

    @Override
    protected void initHttpUrl(String http_fs_url) {
        if (StringUtils.isEmpty(http_fs_url)) {
            throw new IllegalArgumentException("http_fs_url can't be null or empty.");
        }
        String[] nameNodeArr = http_fs_url.split(",");
        if (nameNodeArr.length > 1) {
            for (int i = 0; i < nameNodeArr.length; i++) {
                String nameNode = nameNodeArr[i].trim();
                String httpFsUrl = (nameNode.startsWith("http://")) ? nameNode : "http://" + nameNode;
                String nameNodeStatusUrl = new StringBuilder()
                        .append(httpFsUrl)
                        .append("/")
                        .append(NAMENODE_STATUS_URL)
                        .toString();
                try {
                    String responseJson = this.getAsJson(URLCoder.encodeHttpUrl(nameNodeStatusUrl));
                    String state = (String) JSONObject.parseObject(responseJson).getJSONArray("beans").getJSONObject(0).get("tag.HAState");
                    if ("active".equals(state)) {
                        this.http_fs_url = httpFsUrl;
                        break;
                    } else {
                        continue;
                    }
                } catch (Exception e) {
                    log.error("check NameNode:{} Status error,{}", nameNode, e.getMessage());
                }
            }
        } else {
            this.http_fs_url = http_fs_url.startsWith("http://") ? http_fs_url : "http://" + http_fs_url;
        }

        if (this.http_fs_url == null) {
            throw new RuntimeException(http_fs_url + " State is not: active.");
        }
    }

    public String createURL(String path, String params) {
        StringBuilder sb = new StringBuilder();
        if (!kerberosEnable) {
            if (params == null || params.length() == 0) {
                params = "user.name=" + hadoop_user_name;
            } else if (!params.contains("user.name")) {
                params += "&user.name=" + hadoop_user_name;
            }
        }
        if (path.startsWith("hdfs://")) {
            path = path.substring(path.indexOf("/", 7));
        }
        sb.append(SERVICE_PATH).append(path).append("?").append(params);
        return createURL(sb.toString());
    }

    /**
     * 上传文件。
     * 上传文件会分两步；第一步put请求会重定向到一个DataNode，第二步,根据重定向的DataNode进行文件上传.
     */
    public int upload(String hdfsPath, InputStream inputStream) throws IOException {

        //todo 第一步put请求会重定向到一个DataNode.
        HttpResponse putResponse = this.put(createURL(hdfsPath, CREATE_OP), null);
        String location = putResponse.getFirstHeader("Location").getValue();
        log.debug("upload step1 status code:{},Location:{}.", putResponse.getStatusLine().getStatusCode(), location);

        //todo 第二步,根据重定向的DataNode进行文件上传.
        HttpEntity entityParam = EntityBuilder.create()
                .setStream(inputStream)
                .setContentType(ContentType.APPLICATION_OCTET_STREAM)
                .build();
        HttpResponse uploadResponse = this.put(location, entityParam);
        log.info("upload step2 status code:{},message:{}", uploadResponse.getStatusLine().getStatusCode(), new String(IOUtils.toByteArray(uploadResponse.getEntity().getContent()), "UTF-8"));

        return uploadResponse.getStatusLine().getStatusCode();
    }

    /**
     * 以json字符串格式返回给定path的文件/目录信息
     */
    public String getAsJson(String path, String params) throws IOException {
        String url = createURL(path, params);
        return getAsJson(url);
    }

    /**
     * 判断路径是否存在
     */
    public boolean exist(String path) {
        String url = createURL(path, GETFILESTATUS_OP);
        HttpGet request = new HttpGet(url);
        try {
            HttpResponse response = this.execute(request);
            String json = new String(IOUtils.toByteArray(response.getEntity().getContent()), "UTF-8");
            if (response.getStatusLine().getStatusCode() < 300) {
                return true;
            } else if (response.getStatusLine().getStatusCode() == 404 && json.contains("File does not exist:")) {
                return false;
            } else {
                throw new RuntimeException("status:" + response.getStatusLine() + ",error mesage:" + json);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            request.releaseConnection();
        }
    }

    /**
     * 创建目录.
     *
     * @param path 需要创建的hdfs路径.
     * @return http请求状态码.
     */
    public int mkdirs(String path) throws IOException {
        int statuCode = 0;
        String url = createURL(path, HttpFSClient.MKDIR_OP);
        HttpResponse response = this.put(url, null);
        if (response.getStatusLine().getStatusCode() >= 300) {
            throw new RuntimeException(response.getEntity().toString());
        }
        statuCode = response.getStatusLine().getStatusCode();

        return statuCode;
    }

    /**
     * 以流的方式打开指定路径的单个文件.
     *
     * @param path
     */
    public InputStream openHdfsFile(String path) {
        String url = this.createURL(path, HttpFSClient.OPEN_OP);
        try {
            HttpResponse response = this.get(url);
            log.info("httpMethod:GET,response status:{}", response.getStatusLine());
            return response.getEntity().getContent();
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public HdfsFile getHdfsFileByPath(String path) {
        try {
            String json = getAsJson(path, HttpFSClient.GETFILESTATUS_OP);
            JsonBuilder jsonBuilder = JsonBuilder.getInstance();
            Map<String, Map<String, Object>> map = jsonBuilder.fromJson(json, Map.class);
            Map<String, Object> fileStatus = map.get("FileStatus");
            String type = (String) fileStatus.get("type");
            String pathSuffix = (String) fileStatus.get("pathSuffix");
            if (StringUtils.isEmpty(pathSuffix)) {
                pathSuffix = path.substring(path.lastIndexOf("/") + 1);
            }
            boolean isdir = "DIRECTORY".equalsIgnoreCase(type) ? true : false;
            HdfsFile hdfsFile = new HdfsFile(path,
                    pathSuffix,
                    (String) fileStatus.get("owner"),
                    (String) fileStatus.get("permission"),
                    MapUtils.getLongValue(fileStatus, "length", 0L),
                    (long) fileStatus.get("modificationTime"),
                    isdir);

            return hdfsFile;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 获取用户对当前路径的操作权限。
     */
    public HdfsFilePermission permission(String path) {
        HdfsFile hdfsFile = getHdfsFileByPath(path);
        if (hdfsFile != null) {
            String permission = hdfsFile.getPermission();
            String owner = hdfsFile.getOwner();
            log.info("path:\"{}\",owner:{},permission:{}", path, owner, permission);
            int p = 0;
            if (owner.equals(this.getHadoop_user_name())) {
                p = Integer.valueOf(permission.substring(0, 1));
            } else {
                p = Integer.valueOf(permission.substring(1, 2));
            }
            int xn = 1;
            int wn = 2;
            int rn = 4;
            boolean ri = false;
            boolean wi = false;
            boolean xi = false;

            int k = p - rn;
            if (k >= 0) {
                ri = true;
                p = k;
            }

            k = p - wn;
            if (k >= 0) {
                wi = true;
                p = k;
            }

            k = p - xn;
            if (k >= 0) {
                xi = true;
            }
            return new HdfsFilePermission(owner, ri, wi, xi);
        } else {
            return new HdfsFilePermission();
        }
    }


    /**
     * 下载文件及其目录到本地目录。
     *
     * @param localPath 本地目录
     * @param localPath hdfs文件路径
     */
    public boolean copyToLocal(String localPath, String... hdfsPath) throws IOException {
        boolean status = false;
        List<HdfsFile> hdfsFileList = new ArrayList<HdfsFile>();
        for (String p : hdfsPath) {
            if (StringUtils.isNotEmpty(p)) {
                HdfsFile hdfsFile = getHdfsFileByPath(p);
                hdfsFileList.add(hdfsFile);
            }
        }
        if (!hdfsFileList.isEmpty()) {
            status = copyToLocal(localPath, hdfsFileList);
        } else {
            log.error("hdfsFileList is empty; giving hdfsPath:{}", hdfsPath);
        }

        return status;
    }

    /**
     * 下载文件及其目录到本地目录。
     *
     * @param localPath
     * @param files
     */
    public boolean copyToLocal(String localPath, List<HdfsFile> files) throws IOException {

        //TODO 获取paths的父目录路径,前端传过来的paths数组里的所有path只可能是属于同一级,所以这里取path[0]的就可以了。
        String path_0 = files.get(0).getPath();
        String currentDir = new File(path_0).getParent();

        //TODO 平铺展开paths及其子目录下的所有文件(不包括空目录)。
        List<HdfsFile> needDownLoadHdfsFiles = new ArrayList<>();
        for (HdfsFile hf : files) {
            expandHdfsFile(hf, needDownLoadHdfsFiles);
        }

        int fileNum = needDownLoadHdfsFiles.size();
        if (fileNum > 0) {
            //TODO 根据需要下载的文件数量决定需要开启的子线程数。
            int threadCount = fileNum >= MAX_DOWNLOAD_THREAD ? MAX_DOWNLOAD_THREAD : fileNum;
            CountDownLatch countDownLatch = new CountDownLatch(threadCount);

            //TODO 定义并初始化数组,用于为每一个线程分配需要下载的hdfs文件。
            Set<HdfsFile>[] threadHdfsSet = new Set[threadCount];
            for (int i = 0; i < threadCount; i++) {
                threadHdfsSet[i] = new HashSet<>();
            }

            //TODO 为每一个现线程配需要下载的hdfs文件。
            for (int i = 0; i < fileNum; i++) {
                threadHdfsSet[i % threadCount].add(needDownLoadHdfsFiles.get(i));
            }

            //TODO 开启子线程进行下载到本地缓存目录。
            for (int i = 0; i < threadCount; i++) {
                new CopyToLocalThread(countDownLatch, this, localPath, currentDir, threadHdfsSet[i].iterator())
                        .start();
            }

            //TODO 等待所有下载子线程结束。
            try {
                countDownLatch.await();
                return true;
            } catch (InterruptedException e) {
                log.error("copy hdfs to localPath error,Exception type:{}", e.getClass().getName());
                throw new RuntimeException(e.getMessage());
            }
        } else {
            return false;
        }
    }

    /**
     * 列出当前路径下的所有文件/目录，包含子目录下的
     */
    public List<HdfsFile> listFilesWhithSubDir(String path) throws IOException {
        List<HdfsFile> ret = new ArrayList<>();
        HdfsFile hdfsFile = getHdfsFileByPath(path);
        expandHdfsFile(hdfsFile, ret);
        return ret;
    }

    /**
     * 列出当前路径下的所有文件/目录，不包含子目录下的
     */
    public List<HdfsFile> listFiles(String path) throws IOException {
        List<HdfsFile> ret = new ArrayList<>();
        List<HdfsFile> dirs = new ArrayList<>();
        List<HdfsFile> files = new ArrayList<>();

        String json = getAsJson(path, HttpFSClient.LISTSTATUS_OP);
        JsonBuilder jsonBuilder = JsonBuilder.getInstance();
        Map<String, Map<String, List>> map = jsonBuilder.fromJson(json, Map.class);
        List<Map<String, Object>> fileStatus = map.get("FileStatuses").get("FileStatus");

        for (Map<String, Object> m : fileStatus) {
            String pathSuffix = (String) m.get("pathSuffix");
            String type = (String) m.get("type");
            boolean isdir = "DIRECTORY".equalsIgnoreCase(type) ? true : false;
            String filePath = path.endsWith("/") ? path + pathSuffix : path + "/" + (String) m.get("pathSuffix");
            HdfsFile hdfsFile = new HdfsFile(filePath,
                    pathSuffix,
                    (String) m.get("owner"),
                    (String) m.get("permission"),
                    MapUtils.getLongValue(m, "length", 0L),
                    (long) m.get("modificationTime"),
                    isdir);
            if (hdfsFile.isDir()) {
                dirs.add(hdfsFile);
            } else {
                files.add(hdfsFile);
            }
        }
        Collections.sort(dirs, Comparator.comparing(HdfsFile::getName));
        Collections.sort(files, Comparator.comparing(HdfsFile::getName));
        ret.addAll(dirs);
        ret.addAll(files);
        return ret;

    }

    /**
     * 展开目录结构，找到指定hdfsFile(path)下的所有文件(不包括空目录)
     *
     * @param hdfsFile
     * @param hdfsFileSet hdfsFile对应path目录下及其子目录下的所有文件。
     */
    private void expandHdfsFile(HdfsFile hdfsFile, List<HdfsFile> hdfsFileSet) throws IOException {
        if (hdfsFile.isDir()) {
            List<HdfsFile> list = listFiles(hdfsFile.getPath());
            for (HdfsFile hf : list) {
                if (hf.isDir()) {
                    expandHdfsFile(hf, hdfsFileSet);
                } else {
                    hdfsFileSet.add(hf);
                }
            }
        } else {
            hdfsFileSet.add(hdfsFile);
        }
    }

    /**
     * 级联删除目录或文件.
     *
     * @param paths
     * @return
     */
    public void delete(String... paths) throws IOException {
        //TODO 检查对这些path是否具有可删除权限。
        for (String path : paths) {
            HdfsFilePermission permission = this.permission(path);
            if (!permission.isWrite()) {
                throw new IllegalArgumentException("user " + hadoop_user_name + " does not have delete permession on path:" + path);
            }
        }
        for (String path : paths) {
            HttpResponse response = delete(this.createURL(path, HttpFSClient.DELETE_OP));
            if (response.getStatusLine().getStatusCode() >= 300) {
                throw new RuntimeException(new String(IOUtils.toByteArray(response.getEntity().getContent()), "UTF-8"));
            }
        }
    }
}
