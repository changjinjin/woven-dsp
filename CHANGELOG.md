
## 2020-12-03
### Other Changes
- perf(pom): pom slimming


## 2020-12-02
### Other Changes
- perf(query): supplementary documents
- build(build): add docs to package
- [maven-release-plugin] prepare for next development iteration
- [maven-release-plugin] prepare release v0.1.3
- [maven-release-plugin] rollback the release of v0.1.3
- [maven-release-plugin] prepare for next development iteration
- [maven-release-plugin] prepare release v0.1.3
- build(build): add scm settings
- build(build): modify shell script
- build(build): modify shell script


## 2020-12-01
### Other Changes
- build(build): skip shell srcript
- refactor(dsp): format rest api path
- build(scm): format build version
- build(scm): format build version
- build(pom): modify scm config


## 2020-11-30
### Bug Fixes
- fix(sql): fix naming sql parameter parse bug


## 2020-11-25
### Features
- feat(build): add release plugin

### Other Changes
- perf(dataset): change scope of injection
- perf(baymax-parent): add more motheds


## 2020-11-23
### Other Changes
- build(baymax): add deploy plugin
- build(maven): add maven plugin
- refactor(baymax): modify method deleteByIds


## 2020-11-19
### Other Changes
- build(baymax): remove shell execute
- build(dsp): change gitlog
- build(dsp): change gitlog
- build(baymax): add gitlog shell execution for maven
- build(baymax): add gitlog shell


## 2020-11-16
### Other Changes
- add deploy config for packaging


## 2020-11-13
### Other Changes
- modify base Controller
- modify base Controller
- modify pom.xml


## 2020-11-12
### Bug Fixes
- fix serialize config bug
- fix enabled field modify error
- fix deserialization issues for SnowFlakeIdEntity.java
- fix duplicate name
- fix like condition bug for example query


## 2020-11-11
### Other Changes
- modify feign client intercepter
- change Initializer package


## 2020-11-10
### Bug Fixes
- fix IPageable bug when set pageable=false

### Other Changes
- add Initializer for some things init


## 2020-11-09
### Other Changes
- adjust dependencies and remove duplicate code
- adjust dependencies and remove duplicate code


## 2020-11-06
### Other Changes
- add baymax-dsp-data-sys dependency for dataapi


## 2020-11-05
### Bug Fixes
- fix null pointer exception

### Other Changes
- cas firstpage need to manual redirect
- remove unneeded dependency
- modify pom.xml of baymax-parent
- modify pom.xml of baymax-parent
- modify easyjson-core version
- modify sqlhelper version


## 2020-11-04
### Other Changes
- add class SwaggerHandleProperties


## 2020-11-03
### Other Changes
- modify auth-server config


## 2020-11-02
### Bug Fixes
- fix swagger bug

### Other Changes
- add method to BaseIdableService.java
- change package path
- change package path
- add spring-boot-configuration-processor dependency
- update conf file
- add locale config
- modify exception handle code


## 2020-10-30
### Other Changes
- modify base code for controller
- modify base code for controller


## 2020-10-29
### Other Changes
- delete some code
- submit some configurations
- modify project structure
- modify project structure


## 2020-10-27
### Bug Fixes
- fix session bug

### Other Changes
- modify module version ref


## 2020-10-26
### Other Changes
- add filter


## 2020-10-23
### Bug Fixes
- fix bug for toJson
- fix bugs for query by example with logic delete field

### Other Changes
- update boot script
- update version attribute


## 2020-10-20
### Other Changes
- reference project version from same variable
- extract feign into a separate module


## 2020-10-14
### Other Changes
- add front routes to white list
- update build script for cas-gateway
- update build script for cas-gateway
- update build script for cas-gateway


## 2020-10-13
### Other Changes
- update user query from SaasContext


## 2020-10-12
### Other Changes
- nothing to do
- add more method
- add more method
- update db config for dsp-cas-gateway


## 2020-10-10
### Other Changes
- first commit for cas


## 2020-10-09
### Bug Fixes
- fix method not found error
- fix method not found error


## 2020-09-24
### Other Changes
- add more method to JsonUtils


## 2020-09-23
### Bug Fixes
- fix date type parameter comparison bug for mybatis example query
- fix date type parameter comparison bug for mybatis example query


## 2020-09-14
### Other Changes
- reference project version from same variable


## 2020-08-25
### Other Changes
- modify wrong use of StringUtils
- clear PagingRequest after query


## 2020-08-24
### Bug Fixes
- fix in condition bug
- fix pull data client encrypt error
- fix pull data client encrypt error
- fix error import
- fix snowball dependency bug

### Other Changes
- add data source query encrypt logic
- add query column length


## 2020-08-21
### Bug Fixes
- fix elasticsearch compatibility issues
- fix elasticsearch compatibility issues


## 2020-08-20
### Bug Fixes
- fix bug about jackson deserialization for empty string to object


## 2020-08-19
### Other Changes
- add demo for pull data by sql
- recovery dataset resource dir tree API
- add sql parameter parse logic


## 2020-08-18
### Other Changes
- change sql query parameters type


## 2020-08-17
### Other Changes
- order modules for packaging
- extract the help logic of public SQL query
- add notes for query api test case


## 2020-08-15
### Other Changes
- change method name
- change sql query api package
- add sql template query logic
- add sql template query logic
- add sql template query logic


## 2020-08-14
### Other Changes
- add data api query client and demo code
- add data api query client and demo code
- delete unneeded properties
- add data api query client and demo code
- add data api query client and demo code


## 2020-08-13
### Bug Fixes
- fix chinese character garbled

### Other Changes
- extract public query API and delete unused code
- add sql step output config


## 2020-08-12
### Other Changes
- change package name
- add data source query api
- add data source query api
- add SQL preview interface


## 2020-08-11
### Other Changes
- Upgrade version


## 2020-08-07
### Bug Fixes
- fix oracle aggregate query bug
- fix oracle aggregate query bug
- fix oracle aggregate query bug

### Other Changes
- add detail info to error response


## 2020-08-06
### Bug Fixes
- fix data pull to adapt oracle clob column
- fix default value for jpa
- fix oracle data reader driver parse bug

### Other Changes
- remove annotation @DefaultValue and correlation logic


## 2020-08-05
### Bug Fixes
- fix jar package conflicts about log4j in snowball


## 2020-08-04
### Other Changes
- Dealing with version compatibility of ES query sorting API


## 2020-08-03
### Other Changes
- Solve the problem of keyword in ES aggregate query field name


## 2020-07-31
### Bug Fixes
- fix ES version adaptation
- fix es sql group by condition error


## 2020-07-30
### Bug Fixes
- fix es sql table name error

### Other Changes
- little changes
- modify flow changes from baymax
- add method to JsonUtils


## 2020-07-29
### Bug Fixes
- fix JSONArray class cast exception
- fix compile error

### Other Changes
- remove sql impl method from mappers
- modify jackson format typehanlder logic


## 2020-07-23
### Other Changes
- add root name for ResourceType


## 2020-07-22
### Other Changes
- delete fastjson usage
- add singleton implementation for Snowflake


## 2020-07-21
### Other Changes
- modify default value judgment logic
- modify jdbc query logic for ElasticSearch


## 2020-07-10
### Bug Fixes
- fix sql assembly error


## 2020-07-09
### Bug Fixes
- fix sql assembly error
- fix response message too long

### Other Changes
- remove havingSorts field for AggQuery.java


## 2020-07-08
### Other Changes
- handle es aggregation result as map
- handle es aggregation result as map
- handle es aggregation result as map


## 2020-07-07
### Bug Fixes
- fix RecordQuery get final properties error

### Other Changes
- modify jest client response result handle logic


## 2020-07-06
### Bug Fixes
- fix api usage error
- fix api usage error

### Other Changes
- modify es sql parse logic
- modify es sql parse logic
- modify es sql parse logic


## 2020-07-03
### Bug Fixes
- fix import error
- fix api usage error
- fix api usage error

### Other Changes
- add agg query logic


## 2020-07-01
### Other Changes
- add elasticsearch query parse logic


## 2020-06-30
### Other Changes
- change springfox-swagger2 to release version


## 2020-06-29
### Other Changes
- add es jdbc support
- add elasticsearch-jdbc support


## 2020-06-24
### Other Changes
- add snowball supports for data pull request
- add snowball supports for data pull request


## 2020-06-19
### Bug Fixes
- fix es version
- fix es version

### Other Changes
- add metrics index config
- add metrics index config
- add metrics index config
- add metrics index config
- add metrics index config
- add jest config


## 2020-06-18
### Bug Fixes
- fix dependencies of some of the beans in the application context form a cycle

### Other Changes
- modify dsp-gateway.yml
- delete jdbc logic for dataset access record
- 1、add dataset dashboard rest api 2、add jest client for es


## 2020-06-10
### Other Changes
- modify enum RuleType
- modify permission fields


## 2020-06-09
### Other Changes
- add log config for sentinel dashboard
- add dsp-sentinel-dashboard module
- add sentinel rule logic


## 2020-06-04
### Other Changes
- modify field name 'method' of RestOperation.java
- modify field name 'method' of RestOperation.java


## 2020-06-01
### Bug Fixes
- fix example query error
- fix warning code

### Other Changes
- modify password verification mode
- add default GrantedAuthority to ordinary users
- improve rest api documentation


## 2020-05-28
### Other Changes
- add equals and hashCode method for Field
- remove unnecessary verification
- add user delete check logic
- remove unnecessary verification and standardize verification code


## 2020-05-27
### Other Changes
- modify Swagger2HandlerBootstrapper config name


## 2020-05-26
### Bug Fixes
- fix dataapi path
- fix property encoding


## 2020-05-21
### Other Changes
- add wrapped property for Decrypt annotation
- update event trigger for push service


## 2020-05-20
### Other Changes
- validate annotation support list
- resolve conflict
- add event type trigger for push service


## 2020-05-19
### Other Changes
- update for dataservice clear logic
- update expired dataservice to stopped


## 2020-05-18
### Other Changes
- add rocketmq consumer
- add hibernate-validator dependency


## 2020-05-15
### Other Changes
- add validator annotation
- add valid annotation @Cron


## 2020-05-14
### Other Changes
- add entity valid logic


## 2020-05-13
### Bug Fixes
- fix [DSP-63] and update gateway nacos config
- fix the problem of serializing long type into string

### Other Changes
- modify method name
- replace consul to nacos
- modify example query usage
- modify example query api


## 2020-05-07
### Other Changes
- rationalize comment location


## 2020-05-06
### Other Changes
- modify rest operation init logic
- modify rest operation init logic
- modify rest operation init logic
- add data transfer record


## 2020-04-30
### Bug Fixes
- fix the problem of serializing long type into string

### Other Changes
- add invalid data deletion logic


## 2020-04-29
### Bug Fixes
- fix NullStringJsonSerializer override error
- fix json serialization error

### Other Changes
- add "'" for varchar type default value
- add pull log aop
- Fix：异步调用


## 2020-04-28
### Bug Fixes
- fix rest path error from swagger

### Other Changes
- update cursorVal when not null


## 2020-04-27
### Bug Fixes
- fix field name error

### Other Changes
- add gateway metrics
- adjust profile about caching
- implement OAuth2 to intercept the interface authority


## 2020-04-26
### Other Changes
- discard JWT token to reduce header volume
- update close data resource interface


## 2020-04-24
### Bug Fixes
- fix es query error

### Other Changes
- modify rest api namespace
- exclude dataapi rest api from security check
- modify PullResponse logic
- add swagger info into db
- add es test case


## 2020-04-23
### Bug Fixes
- fix Long type data serialization issues when out of JavaScript range

### Other Changes
- modify file ehcahce.xml
- add data pull service encryption and decryption function
- foramt *Starter class name
- add maven repository
- add maven repository
- add maven repository
- add test case
- add auto export function of rest API document
- add simple custom cache demo


## 2020-04-22
### Other Changes
- update storage setting method
- update IPage construct
- SaasContext init & convert dataSource type


## 2020-04-21
### Other Changes
- add swagger public parameter custom configuration
- modify ExampleQuery API usage


## 2020-04-20
### Other Changes
- add okhttp support
- modify the encryption and decryption API
- standardize the use of ExampleQuery related APIs
- delete moduleVersion


## 2020-04-16
### Other Changes
- remove all mybatis mapping XML files


## 2020-04-15
### Other Changes
- update common-conf
- 添加文档注释
- 修改swagger相关配置
- 修改PULL请求接口参数格式
- update startup shell script


## 2020-04-14
### Other Changes
- 添加表和字段的注释功能
- add test case for mybatis
- 添加表和字段的注释功能
- 升级spring-boot版本
- exclude redis health check for consul and specify application mode to avoid the conflict between web-servlet and webflux


## 2020-04-09
### Other Changes
- support kafka&hdfs sink for push service


## 2020-03-27
### Other Changes
- 修改公共业务接口泛型约束


## 2020-03-19
### Other Changes
- 修改实体对象属性处理器为单例模式


## 2020-03-13
### Other Changes
- 修改方法名称避免冲突
- 统一ehcache依赖版本


## 2020-03-12
### Other Changes
- 处理变量引用问题
- 处理EhcacheCache并发情况下创建冲突问题
- 处理内存分页截取下标错误
- 将缓存的实例缓存起来以保证每一个ID的缓存实例化一次


## 2020-03-11
### Other Changes
- 设定脚本文件打包时的权限值
- 添加缓存的配置项
- 添加分页操作辅助类
- 修改树型数据操作API
- 添加缓存API


## 2020-03-09
### Other Changes
- 处理接口复杂继承方法定义重复导致的序列化问题


## 2020-02-25
### Other Changes
- 处理dataset查询条数错误问题


## 2020-02-24
### Other Changes
- 处理审核意见字段
- 删除User,Role保存时预处理注解
- 添加commons-pool2依赖
- 添加审核意见字段


## 2020-02-17
### Other Changes
- update method name to ignore conflict


## 2020-02-14
### Bug Fixes
- fix core dump and support more api for dataSource&custDataApp


## 2020-02-13
### Other Changes
- update shell for JVM memory configuration


## 2020-02-10
### Other Changes
- 添加缓存处理公共代码


## 2020-02-05
### Other Changes
- delete unused import
- support query detail for DataResource


## 2020-02-04
### Other Changes
- update for exception catch


## 2020-01-21
### Other Changes
- update configuration
- stopped dataService still running


## 2020-01-20
### Other Changes
- recover dataService where restart and update executedTimes
- 简化打包配置
- 修改项目主版本号
- 修改like条件匹配规则
- 处理dataset下数据查询资源目录处理逻辑
- 处理dataset下数据查询资源目录处理逻辑
- 修改打包时输出路径
- 添加审批意见属性


## 2020-01-19
### Other Changes
- avoid duplicated name for dataService
- update pull service & cleaner
- update dataService owner
- delete unused column
- 处理maven插件管理策略
- 删除多余的脚本
- update configuration
- 修改baymax-dsp打包assembly ID属性
- 处理用户平台权限校验
- one application generating one dataService


## 2020-01-17
### Other Changes
- update for job status and service status
- 处理用户登录没有权限时策略
- 增量字段添加对float,double,decimal的支持
- 添加jackson序列化定制处理逻辑
- 添加jackson序列化定制处理逻辑
- merge code


## 2020-01-16
### Other Changes
- 同步配置文件
- 修改消费者用户类型
- 解决依赖问题
- 删除oauth2-client模块
- 修改gateway为reactive模式
- add isHide field for Dataset&Schema
- update for multi cluster
- 解决冲突
- 修改项目名称和模块名称
- Hdfs for different cluster, dataService rename


## 2020-01-15
### Other Changes
- 提取swagger公共模块
- update for flow dependency and incrementField
- support api for execution list
- 处理gateway swagger resources无法加载问题
- update for dataService's status and isRunning
- update pull service
- 修改spring-boot-devtools依赖scope属性
- 修改字段长度
- DataService添加custId属性
- 添加退出登录接口
- 处理gateway依赖问题
- 调整swagger依赖
- 修改脚本名称
- 调整依赖
- 调整依赖
- 调整依赖


## 2020-01-14
### Other Changes
- 调整依赖
- 调整依赖
- update for fieldMappings
- 去除缓存策略


## 2020-01-13
### Other Changes
- 自定义TokenServices，解决刷新token失败问题
- update platform & consumer
- update dataService configuration
- update a little
- 修改gateway脚本中模块名称
- update dataService executing
- 修改中间表名称
- update for Hdfs FileSystem


## 2020-01-10
### Other Changes
- check status for flow.execution and get Cursor value
- update for flow.dependencies
- update for creating flow
- 修改消费者密码修改接口位置
- 删除多余的module


## 2020-01-09
### Other Changes
- 添加snakeyaml依赖
- 修改脚本
- update configuration
- change FeignConfiguration
- 修改spring-plugin依赖版本号
- update dependency about netflix


## 2020-01-08
### Other Changes
- update for dataSource tableName
- 更新拉取接口
- 更新拉取接口
- 处理swagger依赖问题
- DataService clear period edit
- update configuration
- commit


## 2020-01-06
### Other Changes
- 修改打包脚本
- update query fieldGroup
- update pom
- 处理数据源链接测试逻辑


## 2020-01-04
### Other Changes
- 处理分页条件失效的问题
- init query fieldGroup
- 修复query.fieldGroup()多层嵌套导致的栈溢出
- 添加数据源数据表名称查询接口
- 去除profiles配置
- 处理maven依赖问题
- 处理数据源类型大小写不一致问题
- update shell
- 统一logback配置
- update scheduler sender


## 2020-01-03
### Other Changes
- tenantName getter
- 修改打包脚本
- update pull
- 修改打包脚本
- 删除多余依赖
- 处理Dataapi配置文件读取不到问题
- 处理ES依赖版本不一致问题
- 解决依赖错误
- 调整ES依赖范围
- 打包mysql依赖
- 添加job相关模块打包逻辑
- 修改gateway防火墙策略
- update for logback
- update for logback
- 统一配置文件
- update for logback
- 处理日志变量读取问题
- update for logback
- ConditionalOnExpression注解不生效


## 2020-01-02
### Other Changes
- 解决@ConditionalOnProperty不生效问题
- 统一配置文件
- expired time update
- 添加多数据源切面处理逻辑
- 修改过期时间逻辑
- 添加静态资源映射
- 添加多数据源路由逻辑
- 直接压缩到目标目录避免copy
- 修改说明
- 直接压缩到目标目录避免copy
- update DataApplication
- 处理打包文件逻辑
- update
- 添加主从数据源功能
- update pull api


## 2019-12-31
### Other Changes
- 添加主从数据源功能
- 1. fieldMapping can update when applied, 2. transform rule created by manager when associated 3. move application configuration to dataService
- 处理依赖问题
- 处理maven依赖冲突
- update for fieldMapping
- FieldMapping for DataResource
- 晚上打包脚本
- 添加数据服务启用和停用接口


## 2019-12-30
### Other Changes
- update
- 调整打包策略


## 2019-12-27
### Other Changes
- update dataService's status when scheduler failed or succeed
- 更新审批逻辑，更新过期更新逻辑
- 调整配置文件结构


## 2019-12-26
### Bug Fixes
- fix dataresource stop bug

### Other Changes
- 添加ExampleQuery使用案例
- 添加Swagger插件完善文档配置方式
- update pull api
- 处理数据资源添加时关联消费者信息的逻辑


## 2019-12-25
### Other Changes
- resolve conflicts
- 增量更新逻辑;常量类提取
- 修改数据源对象配置信息的数据类型
- 修改DataResource中pushServiceType字段TypeHandler
- 处理数据源添加和修改校验逻辑
- update
- 处理消费者应用接入IP字段类型
- 修改数据资源表结构
- 处理用户登录异常报文格式问题


## 2019-12-24
### Other Changes
- write cursorVal to hdfs and read retry
- support output cursor value
- 角色实体添加clientId属性
- 添加消费端数据查询接口
- 添加数据集目录查询过滤条件
- clean expired DataResource and DataService
- commit for filter step
- 修改mapper.xml配置
- 调整认证相关实体的继承结构
- -m添加管理员,消费者数据服务接口


## 2019-12-23
### Other Changes
- 添加接口报文序列化时字段过滤配置
- commit for schedule
- commit for Feign rest
- update
- 处理编译错误
- 修改消费者信息相关编辑接口


## 2019-12-22
### Other Changes
- commit for exec


## 2019-12-20
### Other Changes
- commit for generate flow
- 更新审批接口
- add pull interface
- 添加数据订阅相关接口
- 删除重复实现的代码和调用
- 解决DataServiceMapper中方法重名问题


## 2019-12-19
### Other Changes
- 添加数据目录管理功能
- update
- 增加推送数据源实体，修改数据应用申请表结构
- update api
- 添加通用接口校验逻辑
- update config
- 修改模板代码
- 添加消费端路由配置
- 添加消费端swagger文档配置


## 2019-12-18
### Other Changes
- 提取controller公共代码简化代码实现
- 处理数据初始化xml读取异常
- 修改配置文件中数据库连接信息
- 用户登录报文添加附加信息
- delete unused code
- 隐藏符合条件查询对象多余的属性
- 处理消费者添加更新是的校验问题
- 添加管理员审核接口
- update api
- update api
- update api
- 添加mybatis相关API样例代码
- 删除冗余代码
- 处理消费者登录逻辑
- update api


## 2019-12-17
### Other Changes
- update a little
- update properties file
- commit for resolve conflict
- update for Dataset
- 添加系统配置文件自定义加载注解功能
- Dataset query update
- import woven-data-3.1.jar
- 修改分页查询相关API，弃用一些冗余API
- update consumer
- update some code
- 处理用户登录信息服务间转发问题


## 2019-12-16
### Other Changes
- 实现swagger basepath属性适配
- 修改实现的swagger AutoConfiguration
- 添加配置access-platform swagger文档
- 注册中心改为consul
- commit for platform and consumer
- add some class
- 实现oauth2 config分离功能


## 2019-12-13
### Other Changes
- commit some DataResource code
- commit some DataResource code
- 处理用户认证信息服务间传递问题
- 调整包名
- 调整包名


## 2019-12-12
### Other Changes
- 删除冗余代码
- 调整auth-server代码结构


## 2019-12-06
### Other Changes
- 拆分common模块代码


## 2019-12-05
### Other Changes
- 初始提交


## 2019-12-02
### Other Changes
- 初始提交

