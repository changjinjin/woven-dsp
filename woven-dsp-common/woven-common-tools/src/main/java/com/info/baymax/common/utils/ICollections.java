package com.info.baymax.common.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 说明：集合工具类. <br>
 *
 * @author yjw@jusfoun.com
 * @date 2017年9月16日 上午11:32:23
 */
public class ICollections {

    /**
     * 说明： 判断集合中是否有数据. <br>
     *
     * @param collection 需要判断的集合
     * @return 集合不为空返回true，否则返回false
     * @author yjw@jusfoun.com
     * @date 2017年9月16日 上午11:32:37
     */
    public static boolean hasElements(Collection<?> collection) {
        return collection != null && !collection.isEmpty() && collection.size() > 0;
    }

    /**
     * 说明：判断集合中是否没有数据.. <br>
     *
     * @param collection 需要判断的集合
     * @return 集合为空返回true，否则返回false
     * @author yjw@jusfoun.com
     * @date 2018年9月30日 下午2:24:03
     */
    public static boolean hasNoElements(Collection<?> collection) {
        return !hasElements(collection);
    }

    /**
     * 说明：集合转数组. <br>
     *
     * @param collection 集合数据
     * @return 数组
     * @author yjw@jusfoun.com
     * @date 2018年8月29日 下午4:02:16
     */
    @SuppressWarnings("unchecked")
    public static <T> T[] toArray(Collection<T> collection) {
        return (T[]) collection.toArray();
    }

    /**
     * 说明：将一种数据集合转化成另一种数据集合. <br>
     *
     * @param collection 源集合
     * @param convertor  转化器
     * @return 转化结果集
     * @author yjw@jusfoun.com
     * @date 2018年8月29日 下午5:11:43
     */
    public static <T, R> List<R> convertToList(Collection<T> collection, Function<T, R> convertor) {
        if (hasElements(collection)) {
            return collection//
                .stream()//
                .map(t -> convertor.apply(t))//
                .collect(Collectors.toList());
        }
        return null;
    }

    /**
     * 说明：将一种数据集合转化成另一种数据集合. <br>
     *
     * @param collection 源集合
     * @param convertor  转化器
     * @return 转化结果集
     * @author yjw@jusfoun.com
     * @date 2018年8月29日 下午5:11:43
     */
    public static <T, R> Set<R> convertToSet(Collection<T> collection, Function<T, R> convertor) {
        if (hasElements(collection)) {
            return collection//
                .stream()//
                .map(t -> convertor.apply(t))//
                .collect(Collectors.toSet());
        }
        return null;
    }

    /**
     * 说明： 将字符串装换成指定类型的数据集合. <br>
     *
     * @param str       需要转化的字符串
     * @param separator 分隔符
     * @param function  转化函数
     * @return 转化结果
     * @author yjw@jusfoun.com
     * @date 2018年8月16日 下午5:52:45
     */
    public static <T> List<T> strToList(String str, String separator, Function<String, T> convertor) {
        if (StringUtils.isNotEmpty(str)) {
            return Arrays//
                .stream(StringUtils.split(str, separator))//
                .map(t -> convertor.apply(t))//
                .collect(Collectors.toList());
        }
        return null;
    }

    /**
     * 说明：字符串转Byte数组. <br>
     *
     * @param str       字符串
     * @param separator 分隔符
     * @return Byte数组
     * @author yjw@jusfoun.com
     * @date 2018年9月5日 上午9:43:44
     */
    public static List<Byte> strToByteList(String str, String separator) {
        return strToList(str, separator, Byte::valueOf);
    }

    /**
     * 说明：字符串转Short数组. <br>
     *
     * @param str       字符串
     * @param separator 分隔符
     * @return Short数组
     * @author yjw@jusfoun.com
     * @date 2018年9月5日 上午9:43:44
     */
    public static List<Short> strToShortList(String str, String separator) {
        return strToList(str, separator, Short::valueOf);
    }

    /**
     * 说明：字符串转Integer数组. <br>
     *
     * @param str       字符串
     * @param separator 分隔符
     * @return Integer数组
     * @author yjw@jusfoun.com
     * @date 2018年9月5日 上午9:43:44
     */
    public static List<Integer> strToIntegerList(String str, String separator) {
        return strToList(str, separator, Integer::valueOf);
    }

    /**
     * 说明：字符串转Long数组. <br>
     *
     * @param str       字符串
     * @param separator 分隔符
     * @return Long数组
     * @author yjw@jusfoun.com
     * @date 2018年9月5日 上午9:43:44
     */
    public static List<Long> strToLongList(String str, String separator) {
        return strToList(str, separator, Long::valueOf);
    }

    /**
     * 说明：字符串转Float数组. <br>
     *
     * @param str       字符串
     * @param separator 分隔符
     * @return Float数组
     * @author yjw@jusfoun.com
     * @date 2018年9月5日 上午9:43:44
     */
    public static List<Float> strToFloatList(String str, String separator) {
        return strToList(str, separator, Float::valueOf);
    }

    /**
     * 说明：字符串转Double数组. <br>
     *
     * @param str       字符串
     * @param separator 分隔符
     * @return Double数组
     * @author yjw@jusfoun.com
     * @date 2018年9月5日 上午9:43:44
     */
    public static List<Double> strToDoubleList(String str, String separator) {
        return strToList(str, separator, Double::valueOf);
    }

    /**
     * 说明：字符串转String数组. <br>
     *
     * @param str       字符串
     * @param separator 分隔符
     * @return String数组
     * @author yjw@jusfoun.com
     * @date 2018年9月5日 上午9:43:44
     */
    public static List<String> strToStringList(String str, String separator) {
        return strToList(str, separator, String::valueOf);
    }

    /**
     * 数据分组
     *
     * @param list    需要分组处理的集合
     * @param perSize 每一组的最大条数
     * @return 分组结果
     */
    public static <T> List<List<T>> groupList(List<T> list, int perSize) {
        ArrayList<List<T>> arrayList = new ArrayList<List<T>>();
        if (hasNoElements(list)) {
            arrayList.add(list);
            return arrayList;
        }

        int totalSize = list.size();
        int batchSize = totalSize / perSize + (totalSize % perSize == 0 ? 0 : 1);

        for (int j = 0; j < batchSize; j++) {
            if (j < batchSize - 1) {
                arrayList.add(list.subList(j * perSize, j * perSize + perSize));
            } else {
                arrayList.add(list.subList(j * perSize, totalSize));
            }
        }
        return arrayList;
    }

    /**
     * 数据分组
     *
     * @param list    需要分组处理的集合
     * @param perSize 每一组的最大条数
     * @return 分组结果
     */
    public static <T> List<T[]> groupArr(T[] arr, int perSize) {
        ArrayList<T[]> arrayList = new ArrayList<T[]>();
        if (arr == null || arr.length == 0) {
            arrayList.add(arr);
            return arrayList;
        }

        int totalSize = arr.length;
        int batchSize = totalSize / perSize + (totalSize % perSize == 0 ? 0 : 1);

        for (int j = 0; j < batchSize; j++) {
            if (j < batchSize - 1) {
                arrayList.add(Arrays.copyOfRange(arr, j * perSize, j * perSize + perSize));
            } else {
                arrayList.add(Arrays.copyOfRange(arr, j * perSize, totalSize));
            }
        }
        return arrayList;
    }

	/*public static void main(String[] args) {
		// List<List<Integer>> group = ICollections.groupList(arr, 5);
		Integer[] arr = new Integer[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 0, 10, 11, 12, 13, 14, 15, 16, };
		List<Integer[]> groupArr = ICollections.groupArr(arr, 5);
		for (Integer[] integers : groupArr) {
			System.err.println(Arrays.toString(integers));
		}
	}*/

}
