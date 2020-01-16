package com.info.baymax.dsp.data.consumer.beans.source;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * cmd = "date +\"%Y-%m-%d %H:%M:%S\" -d \"-1day\""
 */
public class DynamicReplacer {

    static Pattern DATE_EXPR = Pattern.compile("#DATE\\((.*)\\)#", Pattern.CASE_INSENSITIVE);
    static Pattern SHELL_EXPR = Pattern.compile("#SHELL\\((.*)\\)#", Pattern.CASE_INSENSITIVE);

    public static String replace(String str) {
        Matcher date = DATE_EXPR.matcher(str);
        if (date.find()) {
            String format = date.group(1);
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            try {
                return date.replaceAll(sdf.format(new Date()));
            } catch (Exception e) {
                return str;
            }
        }
        Matcher shell = SHELL_EXPR.matcher(str);
        if (shell.find()) {
            String cmd = shell.group(1);
            try {
                Process p = Runtime.getRuntime().exec(cmd);
                BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
                str = br.readLine().trim();
            } catch (IOException e) {
            }
        }
        return str;
    }
}
