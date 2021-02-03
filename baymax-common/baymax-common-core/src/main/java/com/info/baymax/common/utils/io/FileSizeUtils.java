package com.info.baymax.common.utils.io;

import java.io.File;
import java.io.FileInputStream;
import java.text.DecimalFormat;

/**
 * 说明：文件大小计算工具类，用于文件长度KB,MB,GB,TB,PB等转换. <br>
 *
 * @date 2017年9月6日 上午11:33:24
 */
public class FileSizeUtils {
    /**
     * 取得文件大小
     *
     * @param f 读取的文件
     * @return 文件长度
     * @throws Exception
     */
    public static long getFileSizes(File f) throws Exception {
        long s = 0;
        if (f.exists()) {
            new FileInputStream(f).available();
        } else {
            f.createNewFile();
        }
        return s;
    }

    /**
     * 取得文件夹所有数据总大小
     *
     * @param f 读取的文件
     * @return 文件长度
     * @throws Exception
     */
    public static long getFileSize(File f) throws Exception {
        long size = 0;
        File flist[] = f.listFiles();
        if (flist != null) {
            for (int i = 0; i < flist.length; i++) {
                if (flist[i].isDirectory()) {
                    size = size + getFileSize(flist[i]);
                } else {
                    size = size + flist[i].length();
                }
            }
        }
        return size;
    }

    /**
     * 格式化文件大小
     *
     * @param fileS 文件长度
     * @return 文件大小格式化字符串
     */
    public static String FormetFileSize(long fileS) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "K";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "M";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "G";
        }
        return fileSizeString;
    }

    /**
     * 递归求取目录文件个数
     *
     * @param f 读取的文件
     * @return 文件字节长度
     */
    public static long getlist(File f) {
        long size = 0;
        File flist[] = f.listFiles();
        if (flist != null) {
            size = flist.length;
            for (int i = 0; i < flist.length; i++) {
                if (flist[i].isDirectory()) {
                    size = size + getlist(flist[i]);
                    size--;
                }
            }
        }
        return size;
    }

}
