
package com.info.baymax.common.utils.io;

import com.info.baymax.common.utils.ICollections;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * 说明： 文件数据流操作，文件复制，文件路径创建，文件重命名等. <br>
 *
 * @date 2017年9月6日 上午11:33:55
 */
public class IoUtils {
    /**
     * 从输入流写到输出流
     *
     * @param in  输入流
     * @param out 输出流
     * @param b   是否关闭输入输出流
     * @throws IOException
     */
    public static void write(InputStream in, OutputStream out, boolean b) throws IOException {
        byte[] buf = new byte[4096];
        int len = -1;
        while ((len = in.read(buf)) != -1) {
            out.write(buf, 0, len);
        }
        if (b) {
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.flush();
                out.close();
            }
        }
    }

    /**
     * 从输入流写到输出流
     *
     * @param in  输入流
     * @param out 输出流
     * @throws IOException
     */
    public static void write(InputStream in, OutputStream out) throws IOException {
        write(in, out, true);
    }

    /**
     * 从输入流写到本地文件输出流
     *
     * @param in      输入流
     * @param outFile 输出本地文件
     * @throws IOException
     */
    public static void write(InputStream in, File outFile) throws IOException {
        write(in, new FileOutputStream(outFile));
    }

    /**
     * 从本地文件输入流写到输出流
     *
     * @param inFile 本地文件
     * @param out    输出流
     * @throws IOException
     */
    public static void write(File inFile, OutputStream out) throws IOException {
        write(new FileInputStream(inFile), out);
    }

    /**
     * 向文件中写入字符串，并指定编码
     *
     * @param in      字符串
     * @param file    目标文件
     * @param charset 字符集
     * @throws Exception
     * @author yjw
     * @date 2015-9-17 下午8:04:17
     */
    public static void write(String in, File file, String charset) throws Exception {
        PrintWriter out = new PrintWriter(
            new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), charset)));
        out.write(in);
        out.flush();
        IOUtils.closeQuietly(out);
    }

    /**
     * 向文件中写入字符串，并指定编码,默认字符集utf-8
     *
     * @param in   字符串
     * @param file 目标文件
     * @throws Exception
     * @author yjw
     * @date 2015-9-17 下午8:04:17
     */
    public static void write(String in, File file) throws Exception {
        write(in, file, "utf-8");
    }

    /**
     * 文件copy
     *
     * @param in  输入流
     * @param out 输出流
     * @throws IOException
     */
    public static void copy(InputStream in, OutputStream out) throws IOException {
        write(in, out);
    }

    /**
     * 文件copy
     *
     * @param src  源文件
     * @param dest 目标文件
     * @throws IOException
     * @throws Exception
     */
    public static void copy(File src, File dest) throws IOException {
        write(new FileInputStream(src), new FileOutputStream(dest));
    }

    /**
     * 文件copy
     *
     * @param src  源文件路径
     * @param dest 目标文件路径
     * @throws IOException
     * @throws Exception
     */
    public static void copy(String src, String dest) throws IOException {
        copy(new File(src), new File(dest));
    }

    /**
     * 文件copy
     *
     * @param src  源文件
     * @param dest 目标文件路径
     * @throws IOException
     * @throws Exception
     */
    public static void copy(File src, String dest) throws IOException {
        copy(src, new File(dest));
    }

    /**
     * 文件copy
     *
     * @param src  源文件路径
     * @param dest 目标文件
     * @throws IOException
     * @throws Exception
     */
    public static void copy(String src, File dest) throws IOException {
        copy(new File(src), dest);
    }

    /**
     * 创建一个空文件目录
     *
     * @param dir 目录文件
     */
    public static void makdir(File dir) {
        if (!dir.exists()) {
            dir.mkdir();
        }
    }

    /**
     * 创建多层目录
     *
     * @param dir 目录文件
     */
    public static void makdirs(File dir) {
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    /**
     * 创建多层目录
     *
     * @param dir 目录绝对路径
     */
    public static void makdirs(String dir) {
        makdirs(new File(dir));
    }

    public static void createFileSafely(File file) throws IOException {
        if (file.exists()) {
            return;
        }
        File parentFile = file.getParentFile();
        if (!parentFile.exists()) {
            makdirs(parentFile);
        }
        file.createNewFile();

    }

    public static void createFile(String filePath) throws IOException {
        createFileSafely(new File(filePath));
    }

    /**
     * 重命名文件
     *
     * @param filePath1 原文件名
     * @param filePath2 新文件名
     * @throws Exception 异常
     */
    public static boolean rename(String filePath1, String filePath2) {
        return new File(filePath1).renameTo(new File(filePath2));
    }

    /**
     * 重命名文件
     *
     * @param file      原文件
     * @param filePath2 新文件名
     * @throws Exception 异常
     */
    public static boolean rename(File file, String filePath2) {
        return file.renameTo(new File(filePath2));
    }

    /**
     * 重命名文件
     *
     * @param filePath1 原文件
     * @param file      新文件
     * @throws Exception 异常
     */
    public static boolean rename(String filePath1, File file) {
        return new File(filePath1).renameTo(file);
    }

    /**
     * <pre>
     * Description:
     *       递归删除目录下的所有文件及子目录下所有文件
     * </pre>
     *
     * @param file 将要删除的文件目录
     * @param b    是否递归删除
     * @return
     * @author yjw
     * @date 2015-9-18 下午4:40:21
     */
    public static boolean delete(File file, boolean b) {
        if (file != null && file.isDirectory() && b) {
            // 递归删除目录中的子目录下
            String[] list = file.list();
            if (list != null) {
                for (String children : list) {
                    if (!delete(new File(file, children), b)) {
                        return false;
                    }
                }
            }
        }
        // 目录此时为空，可以删除
        if (file != null) {
            return file.delete();
        }
        return false;
    }

    /**
     * 删除文件和目录，如果目录下有子文件，遍历删除
     *
     * @param file 将要删除的文件或目录
     * @return
     * @author yjw
     * @date 2015-9-18 下午4:40:21
     */
    public static boolean delete(File file) {
        return delete(file, true);
    }

    /* * Object 操作方法 **/
    public static void writeObject(Object obj, ObjectOutputStream oos) throws IOException {
        oos.writeObject(obj);
        IOUtils.closeQuietly(oos);
    }

    public static void writeObject(Object obj, OutputStream outputStream) throws IOException {
        writeObject(obj, new ObjectOutputStream(outputStream));
        IOUtils.closeQuietly(outputStream);
    }

    public static void writeObject(Object obj, File file) throws IOException {
        createFileSafely(file);
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        writeObject(obj, new ObjectOutputStream(fileOutputStream));
        IOUtils.closeQuietly(fileOutputStream);
    }

    public static void writeObject(Object obj, String filePath) throws IOException {
        writeObject(obj, new File(filePath));
    }

    public static Object readObject(ObjectInputStream objectInputStream) throws ClassNotFoundException, IOException {
        Object obj = objectInputStream.readObject();
        IOUtils.closeQuietly(objectInputStream);
        return obj;
    }

    public static Object readObject(InputStream inputStream) throws ClassNotFoundException, IOException {
        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
        Object obj = objectInputStream.readObject();
        IOUtils.closeQuietly(objectInputStream);
        return obj;
    }

    public static Object readObject(File file) throws ClassNotFoundException, IOException {
        createFileSafely(file);
        FileInputStream fileInputStream = new FileInputStream(file);
        Object obj = readObject(fileInputStream);
        IOUtils.closeQuietly(fileInputStream);
        return obj;
    }

    public static Object readObject(String filePath) throws ClassNotFoundException, IOException {
        return readObject(new File(filePath));
    }

    public static String readlines(InputStream input) throws IOException {
        List<String> readLines = IOUtils.readLines(input);
        String allLine = "";
        if (ICollections.hasElements(readLines)) {
            for (String line : readLines) {
                allLine += line;
            }
            return allLine;
        }
        IOUtils.closeQuietly(input);
        return null;
    }

    public static String readlines(File file) throws IOException {
        FileInputStream input = new FileInputStream(file);
        String readlines = readlines(input);
        IOUtils.closeQuietly(input);
        return readlines;
    }

    public static String readlines(String filePath) throws IOException {
        return readlines(new File(filePath));
    }

    public static void writeLines(Collection<?> lines, String lineEnding, OutputStream output, String encoding)
        throws IOException {
        IOUtils.writeLines(lines, lineEnding, output, encoding);
    }

    public static void writeLines(Collection<?> lines, String lineEnding, File file, String encoding)
        throws IOException {
        createFileSafely(file);
        FileOutputStream output = new FileOutputStream(file);
        writeLines(lines, lineEnding, output, encoding);
        IOUtils.closeQuietly(output);
    }

    public static void writeLines(Collection<?> lines, String lineEnding, String filePath, String encoding)
        throws IOException {
        writeLines(lines, lineEnding, new File(filePath), encoding);
    }

    public static void writeLines(Collection<?> lines, OutputStream output) throws IOException {
        IOUtils.writeLines(lines, null, output, Charsets.UTF_8.name());
    }

    public static void writeLines(Collection<?> lines, File file) throws IOException {
        createFileSafely(file);
        writeLines(lines, null, file, Charsets.UTF_8.name());
    }

    public static void writeLines(Collection<?> lines, String filePath) throws IOException {
        writeLines(lines, new File(filePath));
    }

    public static void writeLines(String filePath, String... lines) throws IOException {
        writeLines(Arrays.asList(lines), new File(filePath));
    }

    // public static void main(String[] args) {
    // System.out.println(delete(new File("D:/usr"), false));
    // System.out.println(delete(new File("D:/usr"), true));
    // }
}
