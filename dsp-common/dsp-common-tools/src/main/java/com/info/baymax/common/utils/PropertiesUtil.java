package com.info.baymax.common.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.util.Properties;

public class PropertiesUtil {

    public static Properties loadFromFile(String file) {
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            throw new RuntimeException("load properties file " + file + " failed", e);
        } catch (IOException e) {
            throw new RuntimeException("load properties file " + file + " failed", e);
        }
        return properties;
    }

    public static Properties loadFromString(String str) {
        Properties properties = new Properties();
        try {
            properties.load(new StringReader(str));
        } catch (FileNotFoundException e) {
            throw new RuntimeException("load properties string " + str + " failed", e);
        } catch (IOException e) {
            throw new RuntimeException("load properties string " + str + " failed", e);
        }
        return properties;
    }
}
