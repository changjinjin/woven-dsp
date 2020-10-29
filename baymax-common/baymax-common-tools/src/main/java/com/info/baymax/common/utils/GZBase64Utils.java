package com.info.baymax.common.utils;

import org.apache.commons.io.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class GZBase64Utils {

    public static final String DEFAULT_ENCODING = "UTF-8";

    public static String uncompressString(String zippedBase64Str) throws IOException {
        String result;
        byte[] bytes = Base64.getDecoder().decode(zippedBase64Str);
        GZIPInputStream zi = null;
        try {
            zi = new GZIPInputStream(new ByteArrayInputStream(bytes));
            result = IOUtils.toString(zi, DEFAULT_ENCODING);
        } finally {
            IOUtils.closeQuietly(zi);
        }
        return result;
    }

    public static String compressString(String srcTxt)
            throws IOException {
        ByteArrayOutputStream rstBao = new ByteArrayOutputStream();
        GZIPOutputStream zos = new GZIPOutputStream(rstBao);
        zos.write(srcTxt.getBytes(DEFAULT_ENCODING));
        IOUtils.closeQuietly(zos);

        byte[] bytes = rstBao.toByteArray();
        return Base64.getEncoder().encodeToString(bytes);
    }

    public static void main(String[] args) throws Exception {
        String original = "{\n" +
                "    \"action\": \"STATUS\",\n" +
                "    \"parameter\": {\n" +
                "        \"outputRecords\": 0,\n" +
                "        \"outputs\": {\n" +
                "            \"dataflow_1\": {\n" +
                "                \"s3_sink\": {\n" +
                "                    \"dataset\": [\n" +
                "                        {\n" +
                "                            \"datasetEntity\": {\n" +
                "                                \"id\": \"1feae1f1-acbb-4478-af5a-09e8ca025a58\",\n" +
                "                                \"name\": \"qa_sink_dataset_qa_test_navy_20181219023331242\",\n" +
                "                                \"description\": \"qa_sink_dataset_qa_test_navy_20181219023331242\",\n" +
                "                                \"createdTime\": 1545186946576,\n" +
                "                                \"lastUpdatedTime\": 1545186946576,\n" +
                "                                \"schemaId\": \"0c4f1f8f-b3ba-496d-b396-383d86e89194\",\n" +
                "                                \"schemaName\": \"qa_analysis_schema_2018_v1_001\",\n" +
                "                                \"schemaEntity\": {\n" +
                "                                    \"id\": \"0c4f1f8f-b3ba-496d-b396-383d86e89194\",\n" +
                "                                    \"name\": \"qa_analysis_schema_2018_v1_001\",\n" +
                "                                    \"description\": \"struct<executionId:string,flowId:string,modelId:string,modelName:string,detailId:string,name:string,processDataType:string,processDataId:string,ruleId:string,outputLimit:bigint,qualityType:string,qualityRank:int,badRatio:int,priority:int,sourceRowCount:bigint,analysisRowCount:bigint,rowCount:bigint,outputId:string,outputData:string>\",\n" +
                "                                    \"createdTime\": 1545186946574,\n" +
                "                                    \"lastUpdatedTime\": 1545186946574,\n" +
                "                                    \"version\": 0,\n" +
                "                                    \"expiredTime\": 2592000,\n" +
                "                                    \"fields\": [\n" +
                "                                        {\n" +
                "                                            \"name\": \"executionId\",\n" +
                "                                            \"type\": \"string\",\n" +
                "                                            \"alias\": \"executionId\"\n" +
                "                                        },\n" +
                "                                        {\n" +
                "                                            \"name\": \"flowId\",\n" +
                "                                            \"type\": \"string\",\n" +
                "                                            \"alias\": \"flowId\"\n" +
                "                                        },\n" +
                "                                        {\n" +
                "                                            \"name\": \"modelId\",\n" +
                "                                            \"type\": \"string\",\n" +
                "                                            \"alias\": \"modelId\"\n" +
                "                                        },\n" +
                "                                        {\n" +
                "                                            \"name\": \"modelName\",\n" +
                "                                            \"type\": \"string\",\n" +
                "                                            \"alias\": \"modelName\"\n" +
                "                                        },\n" +
                "                                        {\n" +
                "                                            \"name\": \"detailId\",\n" +
                "                                            \"type\": \"string\",\n" +
                "                                            \"alias\": \"detailId\"\n" +
                "                                        },\n" +
                "                                        {\n" +
                "                                            \"name\": \"name\",\n" +
                "                                            \"type\": \"string\",\n" +
                "                                            \"alias\": \"name\"\n" +
                "                                        },\n" +
                "                                        {\n" +
                "                                            \"name\": \"processDataType\",\n" +
                "                                            \"type\": \"string\",\n" +
                "                                            \"alias\": \"processDataType\"\n" +
                "                                        },\n" +
                "                                        {\n" +
                "                                            \"name\": \"processDataId\",\n" +
                "                                            \"type\": \"string\",\n" +
                "                                            \"alias\": \"processDataId\"\n" +
                "                                        },\n" +
                "                                        {\n" +
                "                                            \"name\": \"ruleId\",\n" +
                "                                            \"type\": \"string\",\n" +
                "                                            \"alias\": \"ruleId\"\n" +
                "                                        },\n" +
                "                                        {\n" +
                "                                            \"name\": \"outputLimit\",\n" +
                "                                            \"type\": \"long\",\n" +
                "                                            \"alias\": \"outputLimit\"\n" +
                "                                        },\n" +
                "                                        {\n" +
                "                                            \"name\": \"qualityType\",\n" +
                "                                            \"type\": \"string\",\n" +
                "                                            \"alias\": \"qualityType\"\n" +
                "                                        },\n" +
                "                                        {\n" +
                "                                            \"name\": \"qualityRank\",\n" +
                "                                            \"type\": \"integer\",\n" +
                "                                            \"alias\": \"qualityRank\"\n" +
                "                                        },\n" +
                "                                        {\n" +
                "                                            \"name\": \"badRatio\",\n" +
                "                                            \"type\": \"integer\",\n" +
                "                                            \"alias\": \"badRatio\"\n" +
                "                                        },\n" +
                "                                        {\n" +
                "                                            \"name\": \"priority\",\n" +
                "                                            \"type\": \"integer\",\n" +
                "                                            \"alias\": \"priority\"\n" +
                "                                        },\n" +
                "                                        {\n" +
                "                                            \"name\": \"sourceRowCount\",\n" +
                "                                            \"type\": \"long\",\n" +
                "                                            \"alias\": \"sourceRowCount\"\n" +
                "                                        },\n" +
                "                                        {\n" +
                "                                            \"name\": \"analysisRowCount\",\n" +
                "                                            \"type\": \"long\",\n" +
                "                                            \"alias\": \"analysisRowCount\"\n" +
                "                                        },\n" +
                "                                        {\n" +
                "                                            \"name\": \"rowCount\",\n" +
                "                                            \"type\": \"long\",\n" +
                "                                            \"alias\": \"rowCount\"\n" +
                "                                        },\n" +
                "                                        {\n" +
                "                                            \"name\": \"outputId\",\n" +
                "                                            \"type\": \"string\",\n" +
                "                                            \"alias\": \"outputId\"\n" +
                "                                        },\n" +
                "                                        {\n" +
                "                                            \"name\": \"outputData\",\n" +
                "                                            \"type\": \"string\",\n" +
                "                                            \"alias\": \"outputData\"\n" +
                "                                        }\n" +
                "                                    ],\n" +
                "                                    \"tags\": []\n" +
                "                                },\n" +
                "                                \"sliceTime\": \"\",\n" +
                "                                \"sliceType\": \"H\",\n" +
                "                                \"expiredTime\": 2592000,\n" +
                "                                \"rowNumber\": 1,\n" +
                "                                \"byteSize\": 4990,\n" +
                "                                \"storage\": \"HDFS\",\n" +
                "                                \"storageConfiguration\": {\n" +
                "                                    \"path\": \"/tmp/qaoutput/qa_test_navy_20181219023331242/20181219023546\",\n" +
                "                                    \"outputDatasetDir\": \"woven/qaoutput\",\n" +
                "                                    \"qualityAnalysisBadRatio\": \"Some(20)\",\n" +
                "                                    \"analysisRowCount\": \"Some(5)\",\n" +
                "                                    \"format\": \"parquet\",\n" +
                "                                    \"datasetType\": \"overview\",\n" +
                "                                    \"qualityAnalysisResult\": \"Some(20)\",\n" +
                "                                    \"badRowCount\": \"Some(1)\"\n" +
                "                                },\n" +
                "                                \"columnStats\": [],\n" +
                "                                \"histogram\": [],\n" +
                "                                \"tags\": []\n" +
                "                            },\n" +
                "                            \"action\": \"non\",\n" +
                "                            \"fieldLineage\": [\n" +
                "                                {\n" +
                "                                    \"dataset\": \"qa_sink_dataset_qa_test_navy_20181219023331242\",\n" +
                "                                    \"field\": \"executionId\",\n" +
                "                                    \"flowId\": \"7ab4a1e5-7130-40e6-9d8e-fc078b85a762\",\n" +
                "                                    \"taskId\": \"48781b30-9b79-473a-a1d2-411485e35bf3\",\n" +
                "                                    \"ancestors\": []\n" +
                "                                },\n" +
                "                                {\n" +
                "                                    \"dataset\": \"qa_sink_dataset_qa_test_navy_20181219023331242\",\n" +
                "                                    \"field\": \"flowId\",\n" +
                "                                    \"flowId\": \"7ab4a1e5-7130-40e6-9d8e-fc078b85a762\",\n" +
                "                                    \"taskId\": \"48781b30-9b79-473a-a1d2-411485e35bf3\",\n" +
                "                                    \"ancestors\": []\n" +
                "                                },\n" +
                "                                {\n" +
                "                                    \"dataset\": \"qa_sink_dataset_qa_test_navy_20181219023331242\",\n" +
                "                                    \"field\": \"modelId\",\n" +
                "                                    \"flowId\": \"7ab4a1e5-7130-40e6-9d8e-fc078b85a762\",\n" +
                "                                    \"taskId\": \"48781b30-9b79-473a-a1d2-411485e35bf3\",\n" +
                "                                    \"ancestors\": []\n" +
                "                                },\n" +
                "                                {\n" +
                "                                    \"dataset\": \"qa_sink_dataset_qa_test_navy_20181219023331242\",\n" +
                "                                    \"field\": \"modelName\",\n" +
                "                                    \"flowId\": \"7ab4a1e5-7130-40e6-9d8e-fc078b85a762\",\n" +
                "                                    \"taskId\": \"48781b30-9b79-473a-a1d2-411485e35bf3\",\n" +
                "                                    \"ancestors\": []\n" +
                "                                },\n" +
                "                                {\n" +
                "                                    \"dataset\": \"qa_sink_dataset_qa_test_navy_20181219023331242\",\n" +
                "                                    \"field\": \"detailId\",\n" +
                "                                    \"flowId\": \"7ab4a1e5-7130-40e6-9d8e-fc078b85a762\",\n" +
                "                                    \"taskId\": \"48781b30-9b79-473a-a1d2-411485e35bf3\",\n" +
                "                                    \"ancestors\": []\n" +
                "                                },\n" +
                "                                {\n" +
                "                                    \"dataset\": \"qa_sink_dataset_qa_test_navy_20181219023331242\",\n" +
                "                                    \"field\": \"name\",\n" +
                "                                    \"flowId\": \"7ab4a1e5-7130-40e6-9d8e-fc078b85a762\",\n" +
                "                                    \"taskId\": \"48781b30-9b79-473a-a1d2-411485e35bf3\",\n" +
                "                                    \"ancestors\": []\n" +
                "                                },\n" +
                "                                {\n" +
                "                                    \"dataset\": \"qa_sink_dataset_qa_test_navy_20181219023331242\",\n" +
                "                                    \"field\": \"processDataType\",\n" +
                "                                    \"flowId\": \"7ab4a1e5-7130-40e6-9d8e-fc078b85a762\",\n" +
                "                                    \"taskId\": \"48781b30-9b79-473a-a1d2-411485e35bf3\",\n" +
                "                                    \"ancestors\": []\n" +
                "                                },\n" +
                "                                {\n" +
                "                                    \"dataset\": \"qa_sink_dataset_qa_test_navy_20181219023331242\",\n" +
                "                                    \"field\": \"processDataId\",\n" +
                "                                    \"flowId\": \"7ab4a1e5-7130-40e6-9d8e-fc078b85a762\",\n" +
                "                                    \"taskId\": \"48781b30-9b79-473a-a1d2-411485e35bf3\",\n" +
                "                                    \"ancestors\": []\n" +
                "                                },\n" +
                "                                {\n" +
                "                                    \"dataset\": \"qa_sink_dataset_qa_test_navy_20181219023331242\",\n" +
                "                                    \"field\": \"ruleId\",\n" +
                "                                    \"flowId\": \"7ab4a1e5-7130-40e6-9d8e-fc078b85a762\",\n" +
                "                                    \"taskId\": \"48781b30-9b79-473a-a1d2-411485e35bf3\",\n" +
                "                                    \"ancestors\": []\n" +
                "                                },\n" +
                "                                {\n" +
                "                                    \"dataset\": \"qa_sink_dataset_qa_test_navy_20181219023331242\",\n" +
                "                                    \"field\": \"outputLimit\",\n" +
                "                                    \"flowId\": \"7ab4a1e5-7130-40e6-9d8e-fc078b85a762\",\n" +
                "                                    \"taskId\": \"48781b30-9b79-473a-a1d2-411485e35bf3\",\n" +
                "                                    \"ancestors\": []\n" +
                "                                },\n" +
                "                                {\n" +
                "                                    \"dataset\": \"qa_sink_dataset_qa_test_navy_20181219023331242\",\n" +
                "                                    \"field\": \"qualityType\",\n" +
                "                                    \"flowId\": \"7ab4a1e5-7130-40e6-9d8e-fc078b85a762\",\n" +
                "                                    \"taskId\": \"48781b30-9b79-473a-a1d2-411485e35bf3\",\n" +
                "                                    \"ancestors\": []\n" +
                "                                },\n" +
                "                                {\n" +
                "                                    \"dataset\": \"qa_sink_dataset_qa_test_navy_20181219023331242\",\n" +
                "                                    \"field\": \"qualityRank\",\n" +
                "                                    \"flowId\": \"7ab4a1e5-7130-40e6-9d8e-fc078b85a762\",\n" +
                "                                    \"taskId\": \"48781b30-9b79-473a-a1d2-411485e35bf3\",\n" +
                "                                    \"ancestors\": []\n" +
                "                                },\n" +
                "                                {\n" +
                "                                    \"dataset\": \"qa_sink_dataset_qa_test_navy_20181219023331242\",\n" +
                "                                    \"field\": \"badRatio\",\n" +
                "                                    \"flowId\": \"7ab4a1e5-7130-40e6-9d8e-fc078b85a762\",\n" +
                "                                    \"taskId\": \"48781b30-9b79-473a-a1d2-411485e35bf3\",\n" +
                "                                    \"ancestors\": []\n" +
                "                                },\n" +
                "                                {\n" +
                "                                    \"dataset\": \"qa_sink_dataset_qa_test_navy_20181219023331242\",\n" +
                "                                    \"field\": \"priority\",\n" +
                "                                    \"flowId\": \"7ab4a1e5-7130-40e6-9d8e-fc078b85a762\",\n" +
                "                                    \"taskId\": \"48781b30-9b79-473a-a1d2-411485e35bf3\",\n" +
                "                                    \"ancestors\": []\n" +
                "                                },\n" +
                "                                {\n" +
                "                                    \"dataset\": \"qa_sink_dataset_qa_test_navy_20181219023331242\",\n" +
                "                                    \"field\": \"sourceRowCount\",\n" +
                "                                    \"flowId\": \"7ab4a1e5-7130-40e6-9d8e-fc078b85a762\",\n" +
                "                                    \"taskId\": \"48781b30-9b79-473a-a1d2-411485e35bf3\",\n" +
                "                                    \"ancestors\": []\n" +
                "                                },\n" +
                "                                {\n" +
                "                                    \"dataset\": \"qa_sink_dataset_qa_test_navy_20181219023331242\",\n" +
                "                                    \"field\": \"analysisRowCount\",\n" +
                "                                    \"flowId\": \"7ab4a1e5-7130-40e6-9d8e-fc078b85a762\",\n" +
                "                                    \"taskId\": \"48781b30-9b79-473a-a1d2-411485e35bf3\",\n" +
                "                                    \"ancestors\": []\n" +
                "                                },\n" +
                "                                {\n" +
                "                                    \"dataset\": \"qa_sink_dataset_qa_test_navy_20181219023331242\",\n" +
                "                                    \"field\": \"rowCount\",\n" +
                "                                    \"flowId\": \"7ab4a1e5-7130-40e6-9d8e-fc078b85a762\",\n" +
                "                                    \"taskId\": \"48781b30-9b79-473a-a1d2-411485e35bf3\",\n" +
                "                                    \"ancestors\": []\n" +
                "                                },\n" +
                "                                {\n" +
                "                                    \"dataset\": \"qa_sink_dataset_qa_test_navy_20181219023331242\",\n" +
                "                                    \"field\": \"outputId\",\n" +
                "                                    \"flowId\": \"7ab4a1e5-7130-40e6-9d8e-fc078b85a762\",\n" +
                "                                    \"taskId\": \"48781b30-9b79-473a-a1d2-411485e35bf3\",\n" +
                "                                    \"ancestors\": []\n" +
                "                                },\n" +
                "                                {\n" +
                "                                    \"dataset\": \"qa_sink_dataset_qa_test_navy_20181219023331242\",\n" +
                "                                    \"field\": \"outputData\",\n" +
                "                                    \"flowId\": \"7ab4a1e5-7130-40e6-9d8e-fc078b85a762\",\n" +
                "                                    \"taskId\": \"48781b30-9b79-473a-a1d2-411485e35bf3\",\n" +
                "                                    \"ancestors\": []\n" +
                "                                }\n" +
                "                            ],\n" +
                "                            \"datasetName\": \"qa_sink_dataset_qa_test_navy_20181219023331242\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"datasetEntity\": {\n" +
                "                                \"id\": \"eaee7fe6-ffce-4596-b2f4-d6a7dee77a96\",\n" +
                "                                \"name\": \"qa_sink_RuleCustomEL_20181219023545971\",\n" +
                "                                \"description\": \"woven/qaoutput/qa_sink_RuleCustomEL_20181219023545971\",\n" +
                "                                \"createdTime\": 1545186948186,\n" +
                "                                \"lastUpdatedTime\": 1545186948186,\n" +
                "                                \"schemaId\": \"228c585e-9896-4229-8235-3df6d00e026a\",\n" +
                "                                \"schemaName\": \"qa_sink_RuleCustomEL_20181219023545971\",\n" +
                "                                \"schemaEntity\": {\n" +
                "                                    \"id\": \"228c585e-9896-4229-8235-3df6d00e026a\",\n" +
                "                                    \"name\": \"qa_sink_RuleCustomEL_20181219023545971\",\n" +
                "                                    \"description\": \"struct<NAME:string,AGE:int,ID:int,CITY:string>\",\n" +
                "                                    \"createdTime\": 1545186948186,\n" +
                "                                    \"lastUpdatedTime\": 1545186948186,\n" +
                "                                    \"version\": 0,\n" +
                "                                    \"expiredTime\": 2592000,\n" +
                "                                    \"fields\": [\n" +
                "                                        {\n" +
                "                                            \"name\": \"NAME\",\n" +
                "                                            \"type\": \"string\",\n" +
                "                                            \"alias\": \"NAME\"\n" +
                "                                        },\n" +
                "                                        {\n" +
                "                                            \"name\": \"AGE\",\n" +
                "                                            \"type\": \"integer\",\n" +
                "                                            \"alias\": \"AGE\"\n" +
                "                                        },\n" +
                "                                        {\n" +
                "                                            \"name\": \"ID\",\n" +
                "                                            \"type\": \"integer\",\n" +
                "                                            \"alias\": \"ID\"\n" +
                "                                        },\n" +
                "                                        {\n" +
                "                                            \"name\": \"CITY\",\n" +
                "                                            \"type\": \"string\",\n" +
                "                                            \"alias\": \"CITY\"\n" +
                "                                        }\n" +
                "                                    ],\n" +
                "                                    \"tags\": []\n" +
                "                                },\n" +
                "                                \"sliceTime\": \"\",\n" +
                "                                \"sliceType\": \"H\",\n" +
                "                                \"expiredTime\": 2592000,\n" +
                "                                \"rowNumber\": 1,\n" +
                "                                \"byteSize\": 910,\n" +
                "                                \"storage\": \"HDFS\",\n" +
                "                                \"storageConfiguration\": {\n" +
                "                                    \"path\": \"/tmp/qaoutput/records/qa_test_navy_20181219023331242/20181219023548\",\n" +
                "                                    \"outputDatasetDir\": \"woven/qaoutput\",\n" +
                "                                    \"format\": \"parquet\"\n" +
                "                                },\n" +
                "                                \"columnStats\": [],\n" +
                "                                \"histogram\": [],\n" +
                "                                \"tags\": []\n" +
                "                            },\n" +
                "                            \"action\": \"non\",\n" +
                "                            \"fieldLineage\": [\n" +
                "                                {\n" +
                "                                    \"dataset\": \"qa_sink_RuleCustomEL_20181219023545971\",\n" +
                "                                    \"field\": \"NAME\",\n" +
                "                                    \"flowId\": \"7ab4a1e5-7130-40e6-9d8e-fc078b85a762\",\n" +
                "                                    \"taskId\": \"48781b30-9b79-473a-a1d2-411485e35bf3\",\n" +
                "                                    \"ancestors\": []\n" +
                "                                },\n" +
                "                                {\n" +
                "                                    \"dataset\": \"qa_sink_RuleCustomEL_20181219023545971\",\n" +
                "                                    \"field\": \"AGE\",\n" +
                "                                    \"flowId\": \"7ab4a1e5-7130-40e6-9d8e-fc078b85a762\",\n" +
                "                                    \"taskId\": \"48781b30-9b79-473a-a1d2-411485e35bf3\",\n" +
                "                                    \"ancestors\": []\n" +
                "                                },\n" +
                "                                {\n" +
                "                                    \"dataset\": \"qa_sink_RuleCustomEL_20181219023545971\",\n" +
                "                                    \"field\": \"ID\",\n" +
                "                                    \"flowId\": \"7ab4a1e5-7130-40e6-9d8e-fc078b85a762\",\n" +
                "                                    \"taskId\": \"48781b30-9b79-473a-a1d2-411485e35bf3\",\n" +
                "                                    \"ancestors\": []\n" +
                "                                },\n" +
                "                                {\n" +
                "                                    \"dataset\": \"qa_sink_RuleCustomEL_20181219023545971\",\n" +
                "                                    \"field\": \"CITY\",\n" +
                "                                    \"flowId\": \"7ab4a1e5-7130-40e6-9d8e-fc078b85a762\",\n" +
                "                                    \"taskId\": \"48781b30-9b79-473a-a1d2-411485e35bf3\",\n" +
                "                                    \"ancestors\": []\n" +
                "                                }\n" +
                "                            ],\n" +
                "                            \"datasetName\": \"qa_sink_RuleCustomEL_20181219023545971\"\n" +
                "                        }\n" +
                "                    ]\n" +
                "                }\n" +
                "            }\n" +
                "        },\n" +
                "        \"executionId\": \"48781b30-9b79-473a-a1d2-411485e35bf3\",\n" +
                "        \"finishedTask\": 1,\n" +
                "        \"inputBytes\": 0,\n" +
                "        \"processId\": \"80141d86-0336-11e9-82e5-3497f69e0a24\",\n" +
                "        \"inputRecords\": 0,\n" +
                "        \"outputBytes\": 0,\n" +
                "        \"totalTask\": 2,\n" +
                "        \"status\": \"RUNNING\",\n" +
                "        \"elapsedTime\": 12360\n" +
                "    }\n" +
                "}";

        System.out.println("original text length:" + original.length());
        String compressed = compressString(original);
        System.out.println("compressed text length:" + compressed.length());
        System.out.println("compressed text:" + compressed);
        String uncompressed = uncompressString(compressed);
        System.out.println("uncompressed text length:" + uncompressed.length());
        System.out.println("uncompressed text: " + uncompressed);
    }
}
