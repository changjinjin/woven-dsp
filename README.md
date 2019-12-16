组件功能描述

DSP: 数据服务共享平台(Data Service Platform 缩写)


-----------公共服务---------------

dsp.registry:  注册中心(可共用baymax注册中心)
dsp.config  :  配置中心(方案待定)

-----------对外开发接口---------------

dsp.platform : 共享管理平台API
dsp.consumer : 共享消费平台API
dsp.dataapi  : 共享数据平台API

-----------内部系统接口---------------

dsp.innerapi : 内部API统一入口(待考虑)

dsp.logserv  : 日志服务(待考虑)

dsp.authserv : 授权中心
 - platform : 共享管理授权
 - customer : 共享消费授权
 - dataserv : 共享数据授权

dsp.jobserv  : 任务调度器(维护任务状态?redis/db)
 - deploy: 任务部署
 - start : 任务启动
 - exec  : 任务执行 (deploy+start)
 - stop  : 任务停止
 - kill  : 任务删除
 - info  : 任务信息 (待定)
 - status: 任务状态 (待定)

dsp.jobexec : 任务执行器
 - run : 任务执行

dsp.dataserv : 数据服务
 - preview: 数据预览
 - search:  数据查询

dsp.service:  基础服务(数据库CRUD)
 - user :   用户相关
 - cust :   消费者相关
 - data :   数据资源相关
 - serv :   数据服务相关
 - jobs :   定时任务
 - auth :   权限相关
 - appl :   数据申请
 - logs :   系统操作日志

dsp.adapter: 数据访问适配
 - hive:
 - hdfs:
 - hbase:
 - jdbc:
 - kafka:
 - es: