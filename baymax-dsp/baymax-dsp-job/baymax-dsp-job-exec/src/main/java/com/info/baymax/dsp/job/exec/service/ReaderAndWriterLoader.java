package com.info.baymax.dsp.job.exec.service;

import com.info.baymax.dsp.job.exec.reader.CommonReader;
import com.info.baymax.dsp.job.exec.writer.CommonWriter;
import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.Reader;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: haijun
 * @Date: 2019/12/19 18:16
 */
@Component
@Slf4j
public class ReaderAndWriterLoader {

    private static Map<String, Class<? extends CommonReader>> readerCache = new HashMap<>();
    private static Map<String, Class<? extends CommonWriter>> writerCache = new HashMap<>();


    @PostConstruct
    public void initReader(){

        log.info("loading internal reader ...");
        new FastClasspathScanner("com.info.baymax.dsp.job.exec.reader.impl").matchClassesImplementing(CommonReader.class, aClass -> {
            if (!Modifier.isAbstract(aClass.getModifiers())) {
                try {
                    CommonReader reader = aClass.newInstance();
                    readerCache.put(reader.getType().toUpperCase(), aClass);
                    log.info("internal Reader class loaded: " + reader.getType());
                } catch (Exception e) {
                    log.error("load internal Reader " + aClass.getName() + " failed: " + e.getMessage() + ", ignored.");
                }
            }
        }).scan();
        log.info("loading internal reader success, and size :{}", readerCache.size());

    }

    @PostConstruct
    public void initWriter(){

        log.info("loading internal writer ...");
        new FastClasspathScanner("com.info.baymax.dsp.job.exec.writer.impl").matchClassesImplementing(CommonWriter.class, aClass -> {
            if (!Modifier.isAbstract(aClass.getModifiers())) {
                try {
                    CommonWriter writer = aClass.newInstance();
                    writerCache.put(writer.getType().toUpperCase(), aClass);
                    log.info("internal Writer class loaded: " + writer.getType());
                } catch (Exception e) {
                    log.error("load internal Writer " + aClass.getName() + " failed: " + e.getMessage() + ", ignored.");
                }
            }
        }).scan();
        log.info("loading internal writer success, and size :{}", writerCache.size());
    }

    public static CommonReader getReader(String type) throws Exception {
        Class<? extends CommonReader> readerClass = readerCache.get(type.toUpperCase());
        CommonReader reader = (CommonReader) readerClass.newInstance();
        return reader;
    }

    public static CommonWriter getWriter(String type) throws Exception {
        Class<? extends CommonWriter> writerClass = writerCache.get(type.toUpperCase());
        CommonWriter writer = (CommonWriter) writerClass.newInstance();
        return writer;
    }
}
