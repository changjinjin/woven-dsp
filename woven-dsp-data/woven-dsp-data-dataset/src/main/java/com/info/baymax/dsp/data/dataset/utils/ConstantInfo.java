package com.info.baymax.dsp.data.dataset.utils;

import java.text.SimpleDateFormat;

/**
 * @Author: haijun
 * @Date: 2018/11/14 15:52
 * constant class
 */
public class ConstantInfo {
    public static final String QA_ANALYSIS_SCHEMA_CURRENT = "qa_analysis_schema_2018_v1_001";

    public static final String RESOURCE_DIR_ROOT_SCHEMA = "Schemas" ;
    public static final String RESOURCE_DIR_ROOT_FLOW = "Flows" ;

    public static final String RESOURCE_DIR_ROOT_DATASOURCE = "Datasources" ;
    public static final String RESOURCE_DIR_ROOT_DATASET = "Datasets" ;

    public static final String INSTANCE_TYPE_SCHEMA = "schema";
    public static final String INSTANCE_TYPE_DATASET = "dataset";
    public static final String INSTANCE_TYPE_FLOW = "flow";

    public static final String RES_TYPE_SCHEMA= "schema_dir";
    public static final String RES_TYPE_FLOW= "flow_dir";
    public static final String RES_TYPE_SCHEMA_DATASOURCE = "datasource_dir";
    public static final String RES_TYPE_DATASET = "dataset_dir";

    public static final String QA_OUTPUT_DATASET_DIR = "qaoutput"; //dataset根目录下直接创建一个qaoutput文件夹，目前只支持一级目录,如果包含多级目录会有问题
    public static final String QA_OUTPUT_BASE_DIR = "/tmp/qaoutput"; //hdfs上存放sink结果目录

    public static final String QA_OUTPUT_FLOW_DIR = "Qa_flow";

    public static final String QA_WORKFLOW_DESC = "QA_WORK_FLOW";
    public static final String QA_DATAFLOW_DESC = "QA_DATA_FLOW";

    public static final SimpleDateFormat monthFmt = new SimpleDateFormat("yyyyMM");
    public static final SimpleDateFormat dayFmt = new SimpleDateFormat("yyyyMMdd");

    public static final String QA_SCHEMA_DIE = "qa_schema";
}
