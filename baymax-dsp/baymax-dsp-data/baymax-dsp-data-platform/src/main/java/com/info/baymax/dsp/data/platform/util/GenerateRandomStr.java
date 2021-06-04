package com.info.baymax.dsp.data.platform.util;

/**
 * @ClassName GenergateRandomStr
 * @Deacription 随机生成数字字母组合
 * @Author Administrator
 * @Date 2021/4/7 19:47
 * @Version 1.0
 **/
public class GenerateRandomStr {

    public static String generateStr(int num) {
        char[] arr = {48,49,50,51,52,53,54,55,56,57,//从0到9的数字
                65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,80,81,82,83,84,85,86,87,88,89,90,//从A到Z的数字
                97,98,99,100,101,102,103,104,105,106,107,108,109,110,111,112,113,114,115,116,117,118,119,120,121,122//从a到z的数字
        };
        StringBuilder sb = new StringBuilder();
        int i = 1;
        while(i++ <= num){
            char msg = arr[(int)(Math.random()*62)];
            sb.append(msg);
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        String randomStr = generateStr(6);
        System.out.println("randomStr = " + randomStr);
    }

}
