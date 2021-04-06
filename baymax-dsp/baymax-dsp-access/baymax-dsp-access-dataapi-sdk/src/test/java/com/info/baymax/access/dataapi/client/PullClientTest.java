package com.info.baymax.access.dataapi.client;

import com.info.baymax.common.core.page.IPage;
import com.info.baymax.common.core.result.MapEntity;
import com.info.baymax.common.queryapi.query.aggregate.AggQuery;
import com.info.baymax.common.queryapi.query.aggregate.AggType;
import com.info.baymax.common.queryapi.query.field.FieldGroup;
import com.info.baymax.common.queryapi.query.record.RecordQuery;
import com.info.baymax.common.queryapi.query.sql.SqlQuery;
import com.info.baymax.dsp.access.dataapi.client.DefaultPullClient;
import com.info.baymax.dsp.access.dataapi.client.PullClient;
import com.info.baymax.dsp.access.dataapi.client.PullClientException;
import org.junit.jupiter.api.Test;

public class PullClientTest {

    // 服务地址
    private final String baseUrl = "http://10.34.93.25:8000/";

    // 从用户的注册的应用信息里面获取
    private final String accessKey = "08aa0825-feff-4784-aff6-537aab6ff997";
    // 注册应用时RSA生成的公钥和私钥，私钥用于加密每次secertkey请求的秘钥，公钥用于此处解密，解密后的字符串用于数据的AES解密
    private final String publicKey = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAJL/1Sytnq01Ojyeq7SDgxfF5+lcZzWM+3FWU1s/9O8Wrvw9U87Tc9d9p0RAMs8Z4tI3a68REYqOGl+8TigOPh8CAwEAAQ==";

    // 数据服务ID
    private final Long dataServiceId = 743311142591397888L;

    // 绑定IP
    private final String hosts = "10.34.93.25";

    // 是否启用报文加密，如果启用需要先请求秘钥，使用秘钥对加密报文解密
    private final boolean encrypted = true;

    private final PullClient client = new DefaultPullClient(baseUrl);

    @Test
    public void secertkey() throws PullClientException {
        String secertkey = client.secertkey(accessKey, publicKey);
        System.out.println(secertkey);
    }

    /**
     * <pre>
     * <b>预期SQL语句：</b>
     *  SELECT
     *  	STATUS, RELATESUBSYSTEMKEY, SYSTEMKEY, DESCRIPTION, PKEY, UPDATE_TIME, NAME
     *  FROM
     *  	#{dataServiceId}
     *  WHERE
     *  	STATUS = 0
     *  	AND PKEY > 10
     *  	AND (PKEY BETWEEN 10 AND 30 OR SYSTEMKEY = 2)
     *  	AND PKEY IN ('2', '4', '6', '8', '10', '12', '14', '16', '18', '20', '22')
     *  ORDER BY
     *  	PKEY, UPDATE_TIME
     *  LIMIT
     *      0,20
     * </pre>
     *
     * <pre>
     * <b>请求报文：</b>
     *  {
     *      "accessKey": "08aa0825-feff-4784-aff6-537aab6ff997",
     *      "dataServiceId": 743311142591397900,
     *      "timestamp": 1597392592510,
     *      "encrypted": true,
     *      "query": {
     *          "selectProperties": [
     *              "STATUS",
     *              "RELATESUBSYSTEMKEY",
     *              "SYSTEMKEY",
     *              "DESCRIPTION",
     *              "PKEY",
     *              "UPDATE_TIME",
     *              "NAME"
     *          ],
     *          "fieldGroup": {
     *              "counter": 4,
     *              "andOr": "AND",
     *              "fields": [
     *                  {
     *                      "andOr": "AND",
     *                      "name": "STATUS",
     *                      "oper": "EQUAL",
     *                      "value": [ "0" ],
     *                      "index": 1,
     *                      "group": false
     *                  },
     *                  {
     *                      "andOr": "AND",
     *                      "name": "PKEY",
     *                      "oper": "GREATER_THAN",
     *                      "value": [ "1" ],
     *                      "index": 2,
     *                      "group": false
     *                  },
     *                  {
     *                      "andOr": "AND",
     *                      "name": "PKEY",
     *                      "oper": "IN",
     *                      "value": [ "2", "4", "6", "8", "10", "12", "14", "16", "18", "20", "22" ],
     *                      "index": 4,
     *                      "group": false
     *                  }
     *              ],
     *              "fieldGroups": [
     *                  {
     *                      "counter": 2,
     *                      "andOr": "AND",
     *                      "fields": [
     *                          {
     *                              "andOr": "AND",
     *                              "name": "PKEY",
     *                              "oper": "BETWEEN",
     *                              "value": [ "10", "30" ],
     *                              "index": 1,
     *                              "group": false
     *                          },
     *                          {
     *                              "andOr": "OR",
     *                              "name": "SYSTEMKEY",
     *                              "oper": "EQUAL",
     *                              "value": ["2"],
     *                              "index": 2,
     *                              "group": false
     *                          }
     *                      ],
     *                      "index": 3,
     *                      "group": false
     *                  }
     *              ],
     *              "index": 0,
     *              "group": false
     *          },
     *          "ordSort": [
     *              {
     *                  "name": "PKEY",
     *                  "order": "ASC"
     *              },
     *              {
     *                  "name": "UPDATE_TIME",
     *                  "order": "ASC"
     *              }
     *          ],
     *          "pageable": {
     *              "pageable": true,
     *              "pageNum": 1,
     *              "pageSize": 20
     *          }
     *      }
     *  }
     * </pre>
     *
     * <pre>
     * <b>响应报文：</b>
     *  {
     *      "ok": true,
     *      "status": 0,
     *      "message": "Request successfully.",
     *      "details": "",
     *      "content": {
     *          "pageable": true,
     *          "pageNum": 1,
     *          "pageSize": 10,
     *          "totalCount": "126",
     *          "totalPage": 13,
     *          "list": [
     *              {
     *                  "STATUS": "0",
     *                  "RELATESUBSYSTEMKEY": "61",
     *                  "SYSTEMKEY": "1",
     *                  "DESCRIPTION": "",
     *                  "PKEY": "2",
     *                  "UPDATE_TIME": "2019-08-16 14:08:36",
     *                  "NAME": "PSCADA"
     *              },
     *              {
     *                  "STATUS": "0",
     *                  "RELATESUBSYSTEMKEY": "62",
     *                  "SYSTEMKEY": "1",
     *                  "DESCRIPTION": "1500V",
     *                  "PKEY": "4",
     *                  "UPDATE_TIME": "2019-08-16 14:08:36",
     *                  "NAME": "1500V"
     *              },
     *              {
     *                  "STATUS": "0",
     *                  "RELATESUBSYSTEMKEY": "60",
     *                  "SYSTEMKEY": "1",
     *                  "DESCRIPTION": "33KV",
     *                  "PKEY": "6",
     *                  "UPDATE_TIME": "2019-08-16 14:08:36",
     *                  "NAME": "33KV"
     *              },
     *              {
     *                  "STATUS": "0",
     *                  "RELATESUBSYSTEMKEY": "61",
     *                  "SYSTEMKEY": "1",
     *                  "DESCRIPTION": "400V",
     *                  "PKEY": "8",
     *                  "UPDATE_TIME": "2019-08-16 14:08:36",
     *                  "NAME": "400V"
     *              },
     *              {
     *                  "STATUS": "0",
     *                  "RELATESUBSYSTEMKEY": "60",
     *                  "SYSTEMKEY": "1",
     *                  "DESCRIPTION": "高压系统",
     *                  "PKEY": "10",
     *                  "UPDATE_TIME": "2019-08-16 14:08:36",
     *                  "NAME": "36KV"
     *              },
     *              {
     *                  "STATUS": "0",
     *                  "RELATESUBSYSTEMKEY": "60",
     *                  "SYSTEMKEY": "1",
     *                  "DESCRIPTION": "110KV",
     *                  "PKEY": "12",
     *                  "UPDATE_TIME": "2019-08-16 14:08:36",
     *                  "NAME": "110KV"
     *              },
     *              {
     *                  "STATUS": "0",
     *                  "RELATESUBSYSTEMKEY": "5",
     *                  "SYSTEMKEY": "2",
     *                  "DESCRIPTION": "",
     *                  "PKEY": "14",
     *                  "UPDATE_TIME": "2019-08-16 14:08:36",
     *                  "NAME": "BAS"
     *              },
     *              {
     *                  "STATUS": "0",
     *                  "RELATESUBSYSTEMKEY": "48",
     *                  "SYSTEMKEY": "2",
     *                  "DESCRIPTION": "隧道系统",
     *                  "PKEY": "16",
     *                  "UPDATE_TIME": "2019-08-16 14:08:36",
     *                  "NAME": "TVS"
     *              },
     *              {
     *                  "STATUS": "0",
     *                  "RELATESUBSYSTEMKEY": "48",
     *                  "SYSTEMKEY": "2",
     *                  "DESCRIPTION": "隧道系统模式",
     *                  "PKEY": "18",
     *                  "UPDATE_TIME": "2019-08-16 14:08:36",
     *                  "NAME": "TVS"
     *              },
     *              {
     *                  "STATUS": "0",
     *                  "RELATESUBSYSTEMKEY": "159",
     *                  "SYSTEMKEY": "2",
     *                  "DESCRIPTION": "大系统",
     *                  "PKEY": "20",
     *                  "UPDATE_TIME": "2019-08-16 14:08:36",
     *                  "NAME": "ACN"
     *              }
     *          ],
     *          "nextPage": true,
     *          "prevPage": false,
     *          "offset": "0"
     *      }
     *  }
     * </pre>
     */
    @Test
    public void pullRecords() {
        try {
            // 构建查询条件 where
            FieldGroup fieldGroup = FieldGroup.builder()//
                .andEqualTo("STATUS", "0")// AND STATUS = 0
                .andGreaterThan("PKEY", "1")// AND PKEY > 10
                .andGroup(// 子条件括号括起来的
                    FieldGroup.builder()//
                        .andBetween("PKEY", "10", "30")// AND PKEY Between 10 AND 30
                        .orEqualTo("SYSTEMKEY", "2")// OR SYSTEMKEY = 2
                )//
                .andIn("PKEY", new String[]{"2", "4", "6", "8", "10", "12", "14", "16", "18", "20", "22"});

            RecordQuery query = RecordQuery.builder()//
                .selectProperties("STATUS", "RELATESUBSYSTEMKEY", "SYSTEMKEY", "DESCRIPTION", "PKEY", "UPDATE_TIME",
                    "NAME")// 查询的字段列表
                .fieldGroup(fieldGroup)// where 条件
                .orderByAsc("PKEY", "UPDATE_TIME")// 排序条件
                .page(1, 20)// 分页
                ;

            IPage<MapEntity> pullRecords = client.pullRecords(accessKey, publicKey, dataServiceId, encrypted, hosts,
                query);
            System.out.println(pullRecords);
        } catch (PullClientException e) {
            e.printStackTrace();
        }
    }

    /**
     * <pre>
     * <b>预期SQL语句：</b>
     *  SELECT
     *  	SYSTEMKEY, STATUS, COUNT(PKEY) count_pkey, MAX(PKEY) max_pkey,MIN(PKEY) min_pkey
     *  FROM
     *  	#{dataServiceId}
     *  WHERE
     *  	STATUS = 0
     *  	AND PKEY > 10
     *  	AND (PKEY Between 10 AND 30 OR SYSTEMKEY = 2)
     *  	AND PKEY IN ('2', '4', '6', '8', '10', '12', '14', '16', '18', '20', '22')
     *  GROUP BY
     *  	SYSTEMKEY, STATUS
     *  HAVING
     *      count_pkey > 1
     *  ORDER BY
     *  	SYSTEMKEY, count_pkey
     *  LIMIT
     *      0,20
     * </pre>
     *
     * <pre>
     * <b>请求报文：</b>
     * {
     *     "accessKey": "08aa0825-feff-4784-aff6-537aab6ff997",
     *     "dataServiceId": 743311142591397900,
     *     "timestamp": 1597392737530,
     *     "encrypted": true,
     *     "query": {
     *         "aggFields": [
     *             {
     *                 "name": "PKEY",
     *                 "alias": "count_pkey",
     *                 "aggType": "COUNT",
     *                 "distinct": false
     *             },
     *             {
     *                 "name": "PKEY",
     *                 "alias": "max_pkey",
     *                 "aggType": "MAX",
     *                 "distinct": false
     *             },
     *             {
     *                 "name": "PKEY",
     *                 "alias": "min_pkey",
     *                 "aggType": "MIN",
     *                 "distinct": false
     *             }
     *         ],
     *         "groupFields": [
     *             "SYSTEMKEY",
     *             "STATUS"
     *         ],
     *         "havingFieldGroup": {
     *             "counter": 1,
     *             "andOr": "AND",
     *             "fields": [
     *                 {
     *                     "andOr": "AND",
     *                     "name": "count_pkey",
     *                     "oper": "GREATER_THAN",
     *                     "value": ["1"],
     *                     "index": 1,
     *                     "group": false
     *                 }
     *             ],
     *             "index": 0,
     *             "group": false
     *         },
     *         "fieldGroup": {
     *             "counter": 4,
     *             "andOr": "AND",
     *             "fields": [
     *                 {
     *                     "andOr": "AND",
     *                     "name": "STATUS",
     *                     "oper": "EQUAL",
     *                     "value": ["0"],
     *                     "index": 1,
     *                     "group": false
     *                 },
     *                 {
     *                     "andOr": "AND",
     *                     "name": "PKEY",
     *                     "oper": "GREATER_THAN",
     *                     "value": ["1"],
     *                     "index": 2,
     *                     "group": false
     *                 },
     *                 {
     *                     "andOr": "AND",
     *                     "name": "PKEY",
     *                     "oper": "IN",
     *                     "value": ["2","4","6","8","10","12","14","16","18","20","22"],
     *                     "index": 4,
     *                     "group": false
     *                 }
     *             ],
     *             "fieldGroups": [
     *                 {
     *                     "counter": 2,
     *                     "andOr": "AND",
     *                     "fields": [
     *                         {
     *                             "andOr": "AND",
     *                             "name": "PKEY",
     *                             "oper": "BETWEEN",
     *                             "value": ["10","30"],
     *                             "index": 1,
     *                             "group": false
     *                         },
     *                         {
     *                             "andOr": "OR",
     *                             "name": "SYSTEMKEY",
     *                             "oper": "EQUAL",
     *                             "value": ["2"],
     *                             "index": 2,
     *                             "group": false
     *                         }
     *                     ],
     *                     "index": 3,
     *                     "group": false
     *                 }
     *             ],
     *             "index": 0,
     *             "group": false
     *         },
     *         "ordSort": [
     *             {
     *                 "name": "SYSTEMKEY",
     *                 "order": "ASC"
     *             },
     *             {
     *                 "name": "count_pkey",
     *                 "order": "ASC"
     *             }
     *         ],
     *         "pageable": {
     *             "pageable": true,
     *             "pageNum": 1,
     *             "pageSize": 20
     *         }
     *     }
     * }
     * </pre>
     *
     * <pre>
     * <b>响应报文:</b>
     *  {
     *      "ok": true,
     *      "status": 0,
     *      "message": "Request successfully.",
     *      "details": "",
     *      "content": {
     *          "pageable": true,
     *          "pageNum": 1,
     *          "pageSize": 10,
     *          "totalCount": "3",
     *          "totalPage": 1,
     *          "list": [
     *              {
     *                  "STATUS": "0",
     *                  "SYSTEMKEY": "1",
     *                  "count_pkey": "20",
     *                  "max_pkey": "30",
     *                  "min_pkey": "2"
     *              },
     *              {
     *                  "STATUS": "0",
     *                  "SYSTEMKEY": "2",
     *                  "count_pkey": "13",
     *                  "max_pkey": "22",
     *                  "min_pkey": "6"
     *              },
     *              {
     *                  "STATUS": "1",
     *                  "SYSTEMKEY": "4",
     *                  "count_pkey": "3",
     *                  "max_pkey": "20",
     *                  "min_pkey": "2"
     *              }
     *          ],
     *          "nextPage": true,
     *          "prevPage": false,
     *          "offset": "0"
     *      }
     *  }
     * </pre>
     */
    @Test
    public void pullAggs() {
        try {
            // where 条件
            FieldGroup fieldGroup = FieldGroup.builder()//
                .andEqualTo("STATUS", "0")// AND STATUS = 0
                .andGreaterThan("PKEY", "1")// AND PKEY > 10
                .andGroup(// 子条件括号括起来的
                    FieldGroup.builder()//
                        .andBetween("PKEY", "10", "30")// AND PKEY Between 10 AND 30
                        .orEqualTo("SYSTEMKEY", "2")// OR SYSTEMKEY = 2
                )//
                .andIn("PKEY", new String[]{"2", "4", "6", "8", "10", "12", "14", "16", "18", "20", "22"});

            // having 条件
            FieldGroup havingFieldGroup = FieldGroup.builder()//
                .andGreaterThan("count_pkey", "1")// AND PKEY > 10
                ;

            AggQuery query = AggQuery.builder()//
                .fieldGroup(fieldGroup)// where 条件
                // 聚合条件
                .aggField("PKEY", "count_pkey", AggType.COUNT)// 计数: COUNT(PKEY) AS count_pkey
                .aggField("PKEY", "max_pkey", AggType.MAX)// 取最大值: MAX(PKEY) AS max_pkey
                .aggField("PKEY", "min_pkey", AggType.MIN)// 取最小值: MIN(PKEY) AS min_pkey
                .groupFields("SYSTEMKEY", "STATUS")// group by 条件， 分组字段
                .havingFieldGroup(havingFieldGroup)// having 条件，注意：根据聚合后的结果字段进行筛选
                .orderByAsc("SYSTEMKEY", "count_pkey")// order by 排序，注意：只能根据聚合后结果字段排序
                .page(1, 20)// 分页
                ;
            IPage<MapEntity> pullRecords = client.pullAggs(accessKey, publicKey, dataServiceId, encrypted, hosts,
                query);
            System.out.println(pullRecords);
        } catch (PullClientException e) {
            e.printStackTrace();
        }
    }

    /**
     * <pre>
     * <b>预期SQL语句：</b>
     *  SELECT
     *  	id,name,age
     *  FROM
     *  	#{dataServiceId}
     *  WHERE
     *  	name = #{name}
     *  	AND age > #{age}
     *  LIMIT
     *      0,10
     * </pre>
     *
     * <pre>
     * <b>请求报文：</b>
     * {
     *     "accessKey": "08aa0825-feff-4784-aff6-537aab6ff997",
     *     "dataServiceId": 743311142591397900,
     *     "timestamp": 1597392737530,
     *     "encrypted": true,
     *     "query": {
     *         "aggFields": [
     *             {
     *                 "name": "name",
     *                 "alias": "zhangsan"
     *             },
     *             {
     *                 "name": "age",
     *                 "value": 20
     *             }
     *         ],
     *         "pageable": {
     *             "pageable": true,
     *             "pageNum": 1,
     *             "pageSize": 10
     *         }
     *     }
     * }
     * </pre>
     *
     * <pre>
     * <b>响应报文:</b>
     *  {
     *      "ok": true,
     *      "status": 0,
     *      "message": "Request successfully.",
     *      "details": "",
     *      "content": {
     *          "pageable": true,
     *          "pageNum": 1,
     *          "pageSize": 10,
     *          "totalCount": "3",
     *          "totalPage": 1,
     *          "list": [
     *              {
     *                  "id": 1,
     *                  "name": "zhangsan",
     *                  "age": "21"
     *              },
     *              {
     *                  "id": 12,
     *                  "name": "zhangsan",
     *                  "age": "22"
     *              },
     *              {
     *                  "id": 16,
     *                  "name": "zhangsan",
     *                  "age": "28"
     *              }
     *          ],
     *          "nextPage": true,
     *          "prevPage": false,
     *          "offset": "0"
     *      }
     *  }
     * </pre>
     */
    @Test
    public void pullBySql() {
        try {
            SqlQuery query = SqlQuery.builder()//
                .parameter("name", "zhangsan")//
                .parameter("age", 20)//
                .page(1, 20)// 分页
                ;
            IPage<MapEntity> pullBySql = client.pullBySql(accessKey, publicKey, dataServiceId, encrypted, hosts, query);
            System.out.println(pullBySql);
        } catch (PullClientException e) {
            e.printStackTrace();
        }
    }

}
