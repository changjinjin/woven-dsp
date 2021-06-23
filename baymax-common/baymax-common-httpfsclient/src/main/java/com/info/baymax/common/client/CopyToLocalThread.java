package com.info.baymax.common.client;

import com.info.baymax.common.entity.HdfsFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Iterator;
import java.util.concurrent.CountDownLatch;

/**
 * create by pengchuan.chen on 2021/6/22
 */
class CopyToLocalThread extends Thread {

    private static final Logger logger = LoggerFactory.getLogger(CopyToLocalThread.class);

    private CountDownLatch countDownLatch;

    private HttpFSClient httpFSClient;

    //本地临时缓存目录。
    private String localTempPath;

    //需要被下载的hdfs文件/目录的父目录,即当前所在位置(路径)。
    private String currentPath;

    //需要被下载的文件详细描述信息。
    private Iterator<HdfsFile> hdfsFiles;

    public CopyToLocalThread(CountDownLatch countDownLatch, HttpFSClient httpFSClient, String localTempPath, String currentPath, Iterator<HdfsFile> hdfsFiles) {
        this.countDownLatch = countDownLatch;
        this.httpFSClient = httpFSClient;
        this.localTempPath = localTempPath;
        this.currentPath = currentPath;
        this.hdfsFiles = hdfsFiles;
    }

    @Override
    public void run() {
        int fileNumber = 0;
        long fileSize = 0L;
        while (hdfsFiles.hasNext()) {
            HdfsFile hdfsFile = hdfsFiles.next();
            fileNumber += 1;
            fileSize += hdfsFile.getLen();
            String path = hdfsFile.getPath();
            String relativePath = path.substring(currentPath.length() + 1);
            File tempFileDir = new File(localTempPath, relativePath).getParentFile();
            if (!tempFileDir.exists()) {
                tempFileDir.mkdirs();
            }
            InputStream inputStream = httpFSClient.openHdfsFile(path);
            inputStreamToFile(hdfsFile.getName(), tempFileDir.getPath(), inputStream);
        }
        countDownLatch.countDown();
        logger.info(Thread.currentThread().getName() + " has finished fileNum:{} ,fileSize:{}, running thread:{}.", fileNumber, fileSize, countDownLatch.getCount());
    }


    /**
     * 保存文件到磁盘
     *
     * @param fileName 文件名
     * @param filePath 文件保存路径
     * @param inStream 文件流
     * @return 保存是否成功
     */
    private static boolean inputStreamToFile(String fileName, String filePath, InputStream inStream) {
        boolean result = false;
        if ((filePath == null) || (filePath.trim().length() == 0)) {
            return result;
        }
        OutputStream outStream = null;
        try {
            String wholeFilePath = filePath + File.separator + fileName;
            File dir = new File(filePath);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File outputFile = new File(wholeFilePath);
            boolean isFileExist = outputFile.exists();
            boolean canUpload = true;
            if (isFileExist) {
                canUpload = outputFile.delete();
            }
            if (canUpload) {
                int available = 0;
                outStream = new BufferedOutputStream(new FileOutputStream(outputFile), 2048);
                byte[] buffer = new byte[2048];
                while ((available = inStream.read(buffer)) > 0) {
                    if (available < 2048)
                        outStream.write(buffer, 0, available);
                    else {
                        outStream.write(buffer, 0, 2048);
                    }
                }
                result = true;
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            try {
                if (inStream != null) {
                    inStream.close();
                }
                if (outStream != null) {
                    outStream.close();
                }
            } catch (Exception ex) {
                logger.error(e.getMessage(), e);
            }
        } finally {
            try {
                if (inStream != null) {
                    inStream.close();
                }
                if (outStream != null) {
                    outStream.close();
                }
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
        return result;
    }
}
