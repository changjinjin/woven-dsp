package com.info.baymax.dsp.data.platform.service.impl;

import com.alibaba.fastjson.JSON;
import com.info.baymax.common.core.exception.ServiceException;
import com.info.baymax.common.core.result.ErrType;
import com.info.baymax.common.core.saas.SaasContext;
import com.info.baymax.common.persistence.mybatis.mapper.MyIdableMapper;
import com.info.baymax.common.persistence.service.criteria.example.ExampleQuery;
import com.info.baymax.common.persistence.service.entity.EntityClassServiceImpl;
import com.info.baymax.common.queryapi.query.field.FieldGroup;
import com.info.baymax.common.queryapi.query.sql.SqlQuery;
import com.info.baymax.common.utils.ICollections;
import com.info.baymax.dsp.data.consumer.entity.CustDataSource;
import com.info.baymax.dsp.data.consumer.service.CustDataSourceService;
import com.info.baymax.dsp.data.consumer.service.DataApplicationService;
import com.info.baymax.dsp.data.dataset.bean.FieldMapping;
import com.info.baymax.dsp.data.dataset.entity.core.DataSource;
import com.info.baymax.dsp.data.dataset.entity.core.Dataset;
import com.info.baymax.dsp.data.dataset.service.core.DataSourceService;
import com.info.baymax.dsp.data.dataset.service.core.DatasetService;
import com.info.baymax.dsp.data.platform.bean.ApplyConfiguration;
import com.info.baymax.dsp.data.platform.entity.DataResource;
import com.info.baymax.dsp.data.platform.entity.DataService;
import com.info.baymax.dsp.data.platform.entity.SourceType;
import com.info.baymax.dsp.data.platform.mybatis.mapper.DataResourceMapper;
import com.info.baymax.dsp.data.platform.mybatis.mapper.DataServiceMapper;
import com.info.baymax.dsp.data.platform.service.DataResourceService;
import com.info.baymax.dsp.data.platform.service.DataServiceService;
import com.info.baymax.dsp.data.platform.util.GenerateRandomStr;
import com.info.baymax.dsp.data.sys.entity.security.Customer;
import com.info.baymax.dsp.data.sys.service.security.CustomerService;
import com.merce.woven.utils.JsonBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author: guofeng.wu
 * @Date: 2019/12/18
 */
@Slf4j
@Service
@Transactional(rollbackOn = Exception.class)
public class DataServiceServiceImpl extends EntityClassServiceImpl<DataService> implements DataServiceService {

    @Autowired
    private DataServiceMapper dataServiceMapper;
    @Autowired
    private DataResourceMapper resourceMapper;
    @Autowired
    private DataResourceService dataResourceService;
    @Autowired
    private CustomerService consumerService;
    @Autowired
    private DatasetService datasetService;
    @Autowired
    private DataApplicationService dataApplicationService;
    @Autowired
    private CustDataSourceService custDataSourceService;
    @Autowired
    private DataSourceService dataSourceService;

    @Override
    public MyIdableMapper<DataService> getMyIdableMapper() {
        return dataServiceMapper;
    }

    @Override
    public DataService saveOrUpdate(DataService t) {
        List<FieldMapping> fieldMappings = t.getFieldMappings();
        if (ICollections.hasElements(fieldMappings)) {
            t.setFieldMappings(fieldMappings.stream().filter(f -> StringUtils.isNotEmpty(f.getTargetField()))
                .collect(Collectors.toList()));
        }
        return DataServiceService.super.saveOrUpdate(t);
    }

    @Override
    public List<DataService> querySpecialDataService(Integer[] type, Integer[] status, Integer[] isRunning) {
        return selectList(ExampleQuery.builder(DataService.class).forUpdate(true).fieldGroup(
            FieldGroup.builder().andIn("type", type).andIn("status", status).andIn("isRunning", isRunning)));
    }

    @Override
    public void updateDataServiceRunningStatus(Long id, Integer isRunning) {
        DataService record = new DataService();
        record.setIsRunning(isRunning);
        record.setId(id);
        updateByPrimaryKeySelective(record);
    }

    @Override
    public void recoverDataService() {
        DataService record = new DataService();
        record.setIsRunning(0);
        updateByExampleSelective(record, ExampleQuery.builder().fieldGroup(FieldGroup.builder().andEqualTo("status", 1)
            .andGroup(FieldGroup.builder().andEqualTo("isRunning", 1).orEqualTo("scheduleType", "cron"))));
    }

    @Override
    public void updateStatusByApplicationId(Long applicationId, Integer status, Integer isRunning) {
        DataService record = new DataService();
        record.setStatus(status);
        record.setIsRunning(isRunning);
        updateByExampleSelective(record,
            ExampleQuery.builder().fieldGroup(FieldGroup.builder().andEqualTo("applicationId", applicationId)));
    }

    @Override
    public String exportDataServiceByIds(List<Long> ids, Integer type) {

        List<DataService> dataServiceList = selectByPrimaryKeys(ids);
        List<DataResource> dataResourceList = dataResourceService.selectDataResourceListByIds(ids);
        List<Long> resourceIdList = new ArrayList<>();
        if(null != dataResourceList && dataResourceList.size() > 0){
            for(DataResource entity : dataResourceList){
                resourceIdList.add(entity.getId());
            }
        }
        List<DataResource> newDataResourceList = null;
        if(null != resourceIdList && resourceIdList.size() > 0){
            newDataResourceList = dataResourceService.selectByPrimaryKeys(resourceIdList);
        }
        return exportExcel(dataServiceList, newDataResourceList, type);
    }

    @Override
    public List<String> importExcelByPush(String custId, File excelFile, Long custDataSourceId, String custTableName) throws Exception {
        List<String> result = new ArrayList<>();
        //读取excel数据
        ImportJobVo importJobVo = importExcelByPush(excelFile, custId, custDataSourceId, custTableName);
        //过滤数据记录
        ImportJobVo checkImportJobVo = datasetRelationCheck(importJobVo);
        //导入数据到数据库中(导入策略：若对象存在，则更新;若不存在，则添加)
        result = importJobToDb(checkImportJobVo, custId);
        return result;
    }

    @Override
    public List<String> importExcelByPull(String custId, File excelFile, Long custAppId) throws Exception {
        List<String> result = new ArrayList<>();
        //读取excel数据
        ImportJobVo importJobVo = importJobExcelByPull(excelFile, custId, custAppId);
        //过滤数据记录
        ImportJobVo checkImportJobVo = datasetRelationCheck(importJobVo);
        //导入数据到数据库中(导入策略：若对象存在，则更新;若不存在，则添加)
        result = importJobToDb(checkImportJobVo, custId);
        return result;
    }

    private ImportJobVo importJobExcelByPull(File excelFile, String custId, Long custAppId) throws Exception{
        ImportJobVo importJobVo = new ImportJobVo();
        List<DataService> jobList = new ArrayList<>();
        List<DataResource> jobPoolList = new ArrayList<>();

        importJobVo.setJobList(jobList);
        importJobVo.setJobPoolList(jobPoolList);
        List<String> errorStringList = new ArrayList<>();
        Customer customer = consumerService.selectByPrimaryKey(custId);
        if(null == customer || "".equals(customer)){
            throw new ServiceException(ErrType.ENTITY_NOT_EXIST, "消费者不存在");
        }

        /*
         * workbook:工作簿,就是整个Excel文档
         * sheet:工作表
         * row:行
         * cell:单元格
         */
        InputStream is = new FileInputStream(excelFile);//创建输入流对象
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook(is);
        //获取Sheet数量
        int sheetNum = xssfWorkbook.getNumberOfSheets();

        //这是最外层
        long xuhao = 0;//临时的序号
        List<String> linshiName = new ArrayList<>();//临时用来存放名字和下标对应关系的集合
        //数据集不存在的的数据资源信息
        List<Long> uselessDataResourceIds = new ArrayList<>();

        for (int index = 0; index < sheetNum; index++) {

            XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(index);
            if (xssfSheet == null) {
                continue;
            }
            //创建二维数组保存所有读取到的行列数据，外层存行数据，内层存单元格数据
            List<List<String>> dataList = new ArrayList<List<String>>();
            for (int rowIndex = 1; rowIndex < xssfSheet.getLastRowNum() + 1; rowIndex++) {
                XSSFRow xssfRow = xssfSheet.getRow(rowIndex);
                if (xssfRow == null) {
                    continue;
                }
                List<String> cellList = new ArrayList<String>();
                for (int cellIndex = 0; cellIndex < xssfRow.getLastCellNum(); cellIndex++) {
                    XSSFCell xssfCell = xssfRow.getCell(cellIndex);
                    cellList.add(getString(xssfCell));
                }
                dataList.add(cellList);
            }
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            if (index == 0) {  //数据服务信息表
                xuhao = 100;
                for (int i = 0; i < dataList.size(); i++) {
                    boolean isError = false;//校验当入行是否出错
                    DataService dataService = new DataService();
                    xuhao = xuhao + 1;
                    dataService.setId(xuhao);//先给个临时的序号
                    for (int j = 0; j < dataList.get(i).size(); j++) {
                        if (j == 0) {
                            if(null == dataList.get(i).get(j) || "".equals(dataList.get(i).get(j))){
                                errorStringList.add("数据服务信息表,第" + (i + 1) + "行，【主键】不存在;") ;
                                isError = true;
                            }else{
                                Long id = Long.parseLong(dataList.get(i).get(j));
                                dataService.setId(id);
                            }
                        } else if (j == 1) {
                            if (null != dataList.get(i).get(j) && !dataList.get(i).get(j).equals("")) {
                                dataService.setDescription(dataList.get(i).get(j));
                            }
                        }
                        else if (j == 2) {
                            if(null == dataList.get(i).get(j) || "".equals(dataList.get(i).get(j))){
                                errorStringList.add("数据服务信息表,第" + (i + 1) + "行，【名称】不存在;") ;
                                isError = true;
                            }else{
                                dataService.setName(dataList.get(i).get(j));
                            }
                        }
                        else if (j == 3) {
                            if(null != dataList.get(i).get(j) && !"".equals(dataList.get(i).get(j))){
                                dataService.setAuditMind(dataList.get(i).get(j));
                            }
                        } else if (j == 4) {
                            if(null != dataList.get(i).get(j) && !"".equals(dataList.get(i).get(j))){
                                dataService.setCursorVal(dataList.get(i).get(j));
                            }
                        }
                        else if (j == 5) {
                            if(null == dataList.get(i).get(j) || "".equals(dataList.get(i).get(j))){
                                errorStringList.add("数据服务信息表,第" + (i + 1) + "行，【数据资源ID】不存在;") ;
                                isError = true;
                            }else{
                                dataService.setDataResId(Long.parseLong(dataList.get(i).get(j)));
                            }
                        }
                        else if (j == 6) {
                            if(null == dataList.get(i).get(j) || "".equals(dataList.get(i).get(j))){
                                errorStringList.add("数据服务信息表,第" + (i + 1) + "行，【服务过期时间】不存在;") ;
                                isError = true;
                            }else{
                                dataService.setExpiredTime(Long.parseLong(dataList.get(i).get(j)));
                            }
                        }
                        else if (j == 7) {
                            if(null != dataList.get(i).get(j) && !"".equals(dataList.get(i).get(j))){
                                dataService.setScheduleType(dataList.get(i).get(j));
                            }
                        } else if (j == 8) {
                            if(null != dataList.get(i).get(j) && !"".equals(dataList.get(i).get(j))){
                                Map<String, String> map = JsonBuilder.getInstance().fromJson(dataList.get(i).get(j), Map.class);
                                dataService.setServiceConfiguration(map);
                            }
                        } else if (j == 9) {
                            if(null == dataList.get(i).get(j) || "".equals(dataList.get(i).get(j))){
                                errorStringList.add("数据服务信息表,第" + (i + 1) + "行，【数据类型】不能为空;") ;
                                isError = true;
                            }else{
                                dataService.setSourceType(dataList.get(i).get(j));
                            }
                        }
                        else if (j == 10) {
                            if(null == dataList.get(i).get(j) || "".equals(dataList.get(i).get(j))){
                                errorStringList.add("数据服务信息表,第" + (i + 1) + "行，【服务启动类型】不能为空;") ;
                                isError = true;
                            }else if(!dataList.get(i).get(j).equals("0.0")){
                                errorStringList.add("数据服务信息表,第" + (i + 1) + "行，【服务启动类型】不正确, 不是申请服务;") ;
                                isError = true;
                            }else{
                                if(dataList.get(i).get(j).endsWith(".0")){
                                    dataService.setType(Integer.valueOf(dataList.get(i).get(j).substring(0, dataList.get(i).get(j).length() - 2)));
                                }else{
                                    dataService.setType(Integer.valueOf(dataList.get(i).get(j)));
                                }
                            }
                        }
                        else if (j == 11) {
                            if(null != dataList.get(i).get(j) && !"".equals(dataList.get(i).get(j))){
                                List<FieldMapping> fieldMappings = JsonBuilder.getInstance().fromJson(dataList.get(i).get(j), List.class);
                                dataService.setFieldMappings(fieldMappings);
                            }
                        }
                        else if (j == 12) {
                            if(null == dataList.get(i).get(j) || "".equals(dataList.get(i).get(j))){
                                errorStringList.add("数据服务信息表,第" + (i + 1) + "行，【消费者申请记录ID】不存在;") ;
                            }else{
                                Long applicationId = Long.parseLong(dataList.get(i).get(j));
                                dataService.setApplicationId(applicationId);
                            }
                        }
                        else if (j == 13) {
                            if(null != dataList.get(i).get(j) && !"".equals(dataList.get(i).get(j))){
                                ApplyConfiguration applyConfiguration = JsonBuilder.getInstance().fromJson(dataList.get(i).get(j), ApplyConfiguration.class);
                                applyConfiguration.setCustId(custId);
                                applyConfiguration.setCustName(customer.getName());
                                applyConfiguration.setCustAppId(custAppId);
                                dataService.setApplyConfiguration(applyConfiguration);
                            }
                        }
                    }
                    //字段信息初始值（默认值）设置
                    dataService.setEnabled(1);//启用
                    dataService.setCustId(custId);//导入消费者id
                    dataService.setIsRunning(0);//未运行
                    dataService.setStatus(0);//待部署
                    dataService.setLastModifier(SaasContext.getCurrentUsername());
                    dataService.setLastModifiedTime(new Date());
                    dataService.setTenantId(SaasContext.getCurrentTenantId());
                    dataService.setOwner(SaasContext.getCurrentUserId());
                    if(!isError){
                        jobList.add(dataService);
                    }
                }
            }

            if (index == 1) {//数据资源信息表--Start
                xuhao = 200;
                for (int i = 0; i < dataList.size(); i++) {
                    boolean isError = false;//校验数据记录是否出错
                    DataResource dataResource = new DataResource();
                    xuhao = xuhao + 1;
                    dataResource.setId(xuhao);
                    for (int j = 0; j < dataList.get(i).size(); j++) {
                        if (j == 0) {
                            if(null == dataList.get(i).get(j) || "".equals(dataList.get(i).get(j))){
                                errorStringList.add("数据资源信息表,第" + (i + 1) + "行，【主键】不存在;") ;
                                isError = true;
                            }else{
                                Long id = Long.parseLong(dataList.get(i).get(j));
                                dataResource.setId(id);
                            }
                        }
                        else if (j == 1) {
                            if (null != dataList.get(i).get(j) && !dataList.get(i).get(j).equals(" ")) {
                                dataResource.setDescription(dataList.get(i).get(j));
                            }
                        }
                        else if (j == 2) {
                            if(null == dataList.get(i).get(j) || "".equals(dataList.get(i).get(j))){
                                errorStringList.add("数据资源信息表,第" + (i + 1) + "行，【名称】不存在;") ;
                                isError = true;
                            }else{
                                dataResource.setName(dataList.get(i).get(j));
                            }
                        }else if (j == 3) {
                            if(null != dataList.get(i).get(j) && !"".equals(dataList.get(i).get(j))){
                                Map<String, String> baseConfiguration = JsonBuilder.getInstance().fromJson(dataList.get(i).get(j), Map.class);
                                dataResource.setBaseConfiguration(baseConfiguration);
                            }
                        }
                        else if (j == 4) {
                            if(null == dataList.get(i).get(j) || "".equals(dataList.get(i).get(j))){
                                errorStringList.add("数据资源信息表,第" + (i + 1) + "行，【数据集ID】不存在;");
                                isError = true;
                            }else{
                                dataResource.setDatasetId(dataList.get(i).get(j));
                            }
                        } else if (j == 5) {
                            if(null == dataList.get(i).get(j) || "".equals(dataList.get(i).get(j))){
                                errorStringList.add("数据资源信息表,第" + (i + 1) + "行，【数据集名称】不存在;");
                                isError = true;
                            }else{
                                String name = dataList.get(i).get(j);
                                String sourceType = dataList.get(i).get(17);
                                if(StringUtils.isEmpty(sourceType)){
                                    errorStringList.add("数据资源信息表,第" + (i + 1) + "行，【数据类型】不存在,无法匹配数据集;");
                                    isError = true;
                                    uselessDataResourceIds.add(Long.parseLong(dataList.get(i).get(0)));
                                }
                                if("DATASOURCE".equals(sourceType)){
                                    DataSource dataSource = dataSourceService.selectEntityByName(name);
                                    if(null == dataSource){
                                        errorStringList.add("数据资源信息表,第" + (i + 1) + "行, 数据源信息记录不存在;");
                                        isError = true;
                                        uselessDataResourceIds.add(Long.parseLong(dataList.get(i).get(0)));
                                    }else{
                                        dataResource.setDatasetName(dataList.get(i).get(j));
                                    }
                                } else if("DATASET".equals(sourceType)){
                                    Dataset dataset = datasetService.selectEntityByName(name);
                                    if(null == dataset){
                                        errorStringList.add("数据资源信息表,第" + (i + 1) + "行, 数据集信息记录不存在;");
                                        isError = true;
                                        uselessDataResourceIds.add(Long.parseLong(dataList.get(i).get(0)));
                                    }else{
                                        dataResource.setDatasetName(dataList.get(i).get(j));
                                    }
                                }else{
                                    errorStringList.add("数据资源信息表,第" + (i + 1) + "行, 【数据类型】填写错误;");
                                    isError = true;
                                    uselessDataResourceIds.add(Long.parseLong(dataList.get(i).get(0)));
                                }
                            }
                        } else if (j == 6) {
                            dataResource.setEncoder(dataList.get(i).get(j));
                        } else if (j == 7) {
                            if(null == dataList.get(i).get(j) || "".equals(dataList.get(i).get(j))){
                                errorStringList.add("数据资源信息表,第" + (i + 1) + "行，【失效时间】不存在;");
                                isError = true;
                            }else{
                                dataResource.setExpiredTime(Long.parseLong(dataList.get(i).get(j)));
                            }
                        } else if (j == 8) {
                            if(null != dataList.get(i).get(j) && !"".equals(dataList.get(i).get(j))){
                                List<FieldMapping> fieldMappings = JsonBuilder.getInstance().fromJson(dataList.get(i).get(j), List.class);
                                dataResource.setFieldMappings(fieldMappings);
                            }
                        } else if (j == 9) {
                            if(null != dataList.get(i).get(j) && !"".equals(dataList.get(i).get(j))){
                                dataResource.setIncrementField(dataList.get(i).get(j));
                            }
                        } else if (j == 10) {
                            if(null != dataList.get(i).get(j) && !"".equals(dataList.get(i).get(j))){
                                if("1.0".equals(dataList.get(i).get(j)) || "1".equals(dataList.get(i).get(j))){
                                    dataResource.setIsPull(1);
                                }else{
                                    dataResource.setIsPull(0);
                                }
                            }
                        } else if (j == 11) {
                            if(null != dataList.get(i).get(j) && !"".equals(dataList.get(i).get(j))){
                                if("1.0".equals(dataList.get(i).get(j)) || "1".equals(dataList.get(i).get(j))){
                                    dataResource.setIsPush(1);
                                }else{
                                    dataResource.setIsPush(0);
                                }
                            }
                        }
                        else if (j == 12) {
                            if(null != dataList.get(i).get(j) && !"".equals(dataList.get(i).get(j))){
                                Map<String, String> publishConfiguration = JsonBuilder.getInstance().fromJson(dataList.get(i).get(j), Map.class);
                                dataResource.setPublishConfiguration(publishConfiguration);
                            }
                        } else if (j == 13) {
                            if(null != dataList.get(i).get(j) && !"".equals(dataList.get(i).get(j))){
                                String[] strArray = dataList.get(i).get(j).split(",");
                                Integer[] intArray = (Integer[]) ConvertUtils.convert(strArray, Integer.class);
                                dataResource.setPullServiceMode(intArray);
                            }
                        } else if (j == 14) {
                            if(null != dataList.get(i).get(j) && !"".equals(dataList.get(i).get(j))){
                                String[] strArray = dataList.get(i).get(j).split(",");
                                Integer[] intArray = (Integer[]) ConvertUtils.convert(strArray, Integer.class);
                                dataResource.setPushServiceMode(intArray);
                            }
                        } else if (j == 15) {
                            if(null != dataList.get(i).get(j) && !"".equals(dataList.get(i).get(j))){
                                SqlQuery query = JsonBuilder.getInstance().fromJson(dataList.get(i).get(j), SqlQuery.class);
                                dataResource.setQuery(query);
                            }
                        } else if (j == 16) {
                            if(null == dataList.get(i).get(j) || "".equals(dataList.get(i).get(j))){
                                errorStringList.add("数据资源信息表,第" + (i + 1) + "行，【数据来源】不存在;");
                                isError = true;
                            }else{
                                dataResource.setSource(dataList.get(i).get(j));
                            }
                        } else if (j == 17) {
                            if(null == dataList.get(i).get(j) || "".equals(dataList.get(i).get(j))){
                                errorStringList.add("数据资源信息表,第" + (i + 1) + "行，【数据类型】不存在;") ;
                                isError = true;
                            }else{
                                String sourceType = dataList.get(i).get(j);
                                if("DATASET".equals(sourceType)){
                                    dataResource.setSourceType(SourceType.DATASET);
                                }else if("DATASOURCE".equals(sourceType)){
                                    dataResource.setSourceType(SourceType.DATASOURCE);
                                }
                            }
                        } else if (j == 18) {
                            if(null == dataList.get(i).get(j) || "".equals(dataList.get(i).get(j))){
                                errorStringList.add("数据资源信息表,第" + (i + 1) + "行，【数据集存储引擎】不存在;") ;
                                isError = true;
                            }else{
                                dataResource.setStorage(dataList.get(i).get(j));
                            }
                        } else if (j == 19) {
                            if(null != dataList.get(i).get(j) && !"".equals(dataList.get(i).get(j))){
                                if(dataList.get(i).get(j).endsWith(".0")){
                                    dataResource.setType(Integer.valueOf(dataList.get(i).get(j).substring(0, dataList.get(i).get(j).length() - 2)));
                                }else{
                                    dataResource.setType(Integer.valueOf(dataList.get(i).get(j)));
                                }
                            }
                        }
                    }
                    //字段信息初始值（默认值）设置
                    dataResource.setEnabled(1);//启用
                    dataResource.setOpenStatus(1);//已开放
                    dataResource.setLastModifier(SaasContext.getCurrentUsername());
                    dataResource.setLastModifiedTime(new Date());
                    dataResource.setTenantId(SaasContext.getCurrentTenantId());
                    dataResource.setOwner(SaasContext.getCurrentUserId());
                    if(!isError){
                        jobPoolList.add(dataResource);
                    }
                }
            }
        }

        if(null != jobList && jobList.size() > 0){
            Iterator<DataService> iterator = jobList.iterator();
            while(iterator.hasNext()){
                DataService dataServiceEntity = iterator.next();
                Long dataResId = dataServiceEntity.getDataResId();
                if(uselessDataResourceIds.contains(dataResId)){
                    iterator.remove();
                }
            }
        }

        importJobVo.setJobList(jobList);
        importJobVo.setJobPoolList(jobPoolList);
        importJobVo.setErrorStringList(errorStringList);
        is.close();
        if (errorStringList.size() > 0) {
            StringBuilder sb = new StringBuilder();
            for (String error : errorStringList) {
                sb.append(error)
                        .append("<br/>");
            }
            throw new RuntimeException(sb.toString());
        }
        return importJobVo;
    }

    @Override
    public List<DataService> selectByCustIdAndType(String custId, int type) {
        return dataServiceMapper.selectByCustIdAndType(custId, type);
    }

    private String exportExcel(List<DataService> dataServiceList, List<DataResource> dataResourceList, Integer type) {
        //创建XSSFWorkbook工作薄对象
        XSSFWorkbook workbook = new XSSFWorkbook();
        //创建XSSFSheet对象
        XSSFSheet dataServiceSheet = workbook.createSheet("数据服务信息表");
        XSSFSheet dataResourceSheet = workbook.createSheet("数据资源信息表");

        XSSFCellStyle titleStyle = workbook.createCellStyle();        //标题样式
        XSSFFont ztFont = workbook.createFont();
        ztFont.setItalic(false);                     // 设置字体为斜体字
        ztFont.setColor(XSSFFont.COLOR_NORMAL);            // 将字体设置为“红色”
        ztFont.setFontHeightInPoints((short) 16);    // 将字体大小设置为18px
        ztFont.setFontName("宋体");             // 将“宋体”字体应用到当前单元格上
        titleStyle.setFont(ztFont);

        dataServiceSheet.setDefaultColumnWidth(30);//设置单元格宽度
        dataResourceSheet.setDefaultColumnWidth(30);

        //创建行的单元格，从0开始。
        XSSFRow row = dataServiceSheet.createRow(0);
        //创建单元格,从0开始
        XSSFCell cell = row.createCell(0);
        //各个sheet页的表头--Start
        //设置数据服务服务信息表的标题
        String[] jobHeaders = {
                "主键", "描述信息",
                "名称",
                "审核意见", "服务中的增量值",
                "数据资源ID",
                "服务过期时间",
                "调度类型", "服务相关配置", "数据类型",
                "服务启动类型",
                "特殊配置的字段及字段映射关系",
                "消费者申请记录ID","从Application同步过来的一些配置"
        };
        for (int i = 0; i < jobHeaders.length; i++) {
            cell = row.createCell(i);
            cell.setCellValue(jobHeaders[i]);
            cell.setCellStyle(titleStyle);
        }

        //数据资源信息表
        row = dataResourceSheet.createRow(0);
        String[] handleHeaders = {
                "主键", "描述信息",
                "名称",
                "关联数据集时进行的一些基本配置",
                "关联数据集ID", "数据集名称",
                "数据集对应的编码",
                "失效时间", "开放字段及配置字段映射关系",
                "增量字段", "是否支持pull", "是否支持push",
                "数据发布配置信息", "pull服务方式", "push服务方式", "sql查询配置信息",
                "数据来源", "数据类型:DATASET, DATASOURCE", "数据集存储引擎", "数据类型"};
        for (int i = 0; i < handleHeaders.length; i++) {
            cell = row.createCell(i);
            cell.setCellValue(handleHeaders[i]);
            cell.setCellStyle(titleStyle);
        }

        // 日期格式转为字符串输出
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//大写H是24小时制，小写h是12小时制

        //数据服务服务信息表--Start
        if(null != dataServiceList && dataServiceList.size() > 0){
            for (int i = 0; i < dataServiceList.size(); i++) {
                row = dataServiceSheet.createRow(i + 1);
                for (int j = 0; j < 30; j++) {//30为30个字段  ..... +1个延迟类型
                    cell = row.createCell(j);
                    if (j == 0) {
                        Long id = dataServiceList.get(i).getId();
                        cell.setCellValue(id.toString());
                    }else if (j == 1) {
                        if(null != dataServiceList.get(i).getDescription()){
                            cell.setCellValue(dataServiceList.get(i).getDescription());
                        }
                    }
                    else if (j == 2) {
                        cell.setCellValue(dataServiceList.get(i).getName());
                    }
                    else if (j == 3) {
                        if(null != dataServiceList.get(i).getAuditMind()){
                            cell.setCellValue(dataServiceList.get(i).getAuditMind());
                        }
                    } else if (j == 4) {
                        if(null != dataServiceList.get(i).getCursorVal()){
                            cell.setCellValue(dataServiceList.get(i).getCursorVal());
                        }
                    }
                    else if (j == 5) {
                        cell.setCellValue(dataServiceList.get(i).getDataResId().toString());
                    }
                    else if (j == 6) {
                        if(null != dataServiceList.get(i).getExpiredTime()){
                            cell.setCellValue(dataServiceList.get(i).getExpiredTime().toString());
                        }
                    }
                    else if (j == 7) {
                        if(null != dataServiceList.get(i).getScheduleType()){
                            cell.setCellValue(dataServiceList.get(i).getScheduleType());
                        }
                    } else if (j == 8) {
                        if(null != dataServiceList.get(i).getServiceConfiguration()){
                            cell.setCellValue(JSON.toJSON(dataServiceList.get(i).getServiceConfiguration()).toString());
                        }
                    } else if (j == 9) {
                        cell.setCellValue(dataServiceList.get(i).getSourceType());
                    }

                    else if (j == 10) {
                        cell.setCellValue(dataServiceList.get(i).getType());
                    }
                    else if (j == 11){
                        if(null != dataServiceList.get(i).getFieldMappings()){
                            cell.setCellValue(JSON.toJSON(dataServiceList.get(i).getFieldMappings()).toString());
                        }
                    }
                    else if (j == 12){
                        if(null != dataServiceList.get(i).getApplicationId()){
                            cell.setCellValue(dataServiceList.get(i).getApplicationId().toString());
                        }
                    }
                    else if (j == 13){
                        if(null != dataServiceList.get(i).getApplyConfiguration()){
                            cell.setCellValue(JSON.toJSON(dataServiceList.get(i).getApplyConfiguration()).toString());
                        }
                    }
                }
            }
        }
        //数据服务信息表--End

        //数据资源信息表--Start
        if(null != dataResourceList && dataResourceList.size() > 0){
            for (int i = 0; i < dataResourceList.size(); i++) {
                row = dataResourceSheet.createRow(i + 1);
                for (int j = 0; j < 30; j++) {
                    cell = row.createCell(j);
                    if (j == 0) {
                        cell.setCellValue(dataResourceList.get(i).getId().toString());
                    } else if (j == 1) {
                        if(null != dataResourceList.get(i).getDescription()){
                            cell.setCellValue(dataResourceList.get(i).getDescription());
                        }
                    }
                    else if (j == 2) {
                        cell.setCellValue(dataResourceList.get(i).getName());
                    }
                    else if (j == 3) {
                        if(null != dataResourceList.get(i).getBaseConfiguration()){
                            cell.setCellValue(JSON.toJSON(dataResourceList.get(i).getBaseConfiguration()).toString());
                        }
                    }
                    else if (j == 4) {
                        cell.setCellValue(dataResourceList.get(i).getDatasetId());
                    } else if (j == 5) {
                        cell.setCellValue(dataResourceList.get(i).getDatasetName());
                    } else if (j == 6) {
                        cell.setCellValue(dataResourceList.get(i).getEncoder());
                    } else if (j == 7) {
                        if(null != dataResourceList.get(i).getExpiredTime()){
                            cell.setCellValue(dataResourceList.get(i).getExpiredTime().toString());
                        }
                    } else if (j == 8) {
                        if(null != dataResourceList.get(i).getFieldMappings()){
                            cell.setCellValue(JSON.toJSON(dataResourceList.get(i).getFieldMappings()).toString());
                        }
                    } else if (j == 9) {
                        if(null != dataResourceList.get(i).getIncrementField()){
                            cell.setCellValue(dataResourceList.get(i).getIncrementField());
                        }
                    } else if (j == 10) {
                        if(null != dataResourceList.get(i).getIsPull()){
                            cell.setCellValue(dataResourceList.get(i).getIsPull());
                        }
                    } else if (j == 11) {
                        if(null != dataResourceList.get(i).getIsPush()){
                            cell.setCellValue(dataResourceList.get(i).getIsPush());
                        }
                    }
                    else if (j == 12) {
                        if(null != dataResourceList.get(i).getPublishConfiguration()){
                            cell.setCellValue(JSON.toJSON(dataResourceList.get(i).getPublishConfiguration()).toString());
                        }
                    } else if (j == 13) {
                        if(null != dataResourceList.get(i).getPullServiceMode() && !"".equals(dataResourceList.get(i).getPullServiceMode())){
                            Integer[] pullServiceMode = dataResourceList.get(i).getPullServiceMode();
                            String str = "";
                            for(int num : pullServiceMode){
                                str += num + ",";
                            }
                            cell.setCellValue(str.substring(0, str.length() - 1));
                        }
                    } else if (j == 14) {
                        if(null != dataResourceList.get(i).getPushServiceMode() && !"".equals(dataResourceList.get(i).getPushServiceMode())){
                            Integer[] pushServiceMode = dataResourceList.get(i).getPushServiceMode();
                            String str = "";
                            for(int num : pushServiceMode){
                                str += num + ",";
                            }
                            cell.setCellValue(str.substring(0, str.length() - 1));
                        }
                    } else if (j == 15) {
                        if(null != dataResourceList.get(i).getQuery()){
                            cell.setCellValue(JSON.toJSON(dataResourceList.get(i).getQuery()).toString());
                        }
                    } else if (j == 16) {
                        cell.setCellValue(dataResourceList.get(i).getSource());
                    } else if (j == 17) {
                        SourceType sourceType = dataResourceList.get(i).getSourceType();
                        cell.setCellValue(sourceType.toString());
                    } else if (j == 18) {
                        cell.setCellValue(dataResourceList.get(i).getStorage());
                    } else if (j == 19) {
                        cell.setCellValue(dataResourceList.get(i).getType());
                    }
                }
            }
        }
        //数据资源信息表--end

        //文档输出
        FileOutputStream out;
        String returnStr = "";
        try {
            File f = new File(".");
            String pathStr = f.getAbsolutePath().substring(0, f.getAbsolutePath().length() - 1);
            String targetPath = pathStr + "files/export/excel/";
            File targetPathDir = new File(targetPath);
            if (!targetPathDir.exists()) {
                targetPathDir.mkdirs();
            }
            if(type == 0){
                returnStr = targetPath + "pullService_" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + ".xlsx";
            }else{
                returnStr = targetPath + "pushService_" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + ".xlsx";
            }
            out = new FileOutputStream(returnStr);
            workbook.write(out);
            out.close();
        } catch (Exception e) {
            throw new RuntimeException("数据导出异常");
        }
        System.out.println("导出成功");
        return returnStr;
    }

    private ImportJobVo datasetRelationCheck(ImportJobVo importJobVo) {
        ImportJobVo newVo = new ImportJobVo();
        List<DataService> dataServiceList = importJobVo.getJobList();
        List<DataResource> dataResourceList = importJobVo.getJobPoolList();
        List<Long> dataResourceIds = new ArrayList<>();

        //遍历数据资源记录
        if(null != dataResourceList && dataResourceList.size() > 0){
            Iterator<DataResource> iterator = dataResourceList.iterator();
            while(iterator.hasNext()){
                DataResource dataResourceEntity = iterator.next();
                SourceType sourceType = dataResourceEntity.getSourceType();
                String name = dataResourceEntity.getDatasetName();
                System.out.println("sourceType = " + sourceType);
                if("DATASET".equals(sourceType.toString())){
                    Dataset dataset = datasetService.selectEntityByName(name);
                    if(null == dataset){
                        iterator.remove();
                        dataResourceIds.add(dataResourceEntity.getId());
                    }
                }
                if("DATASOURCE".equals(sourceType.toString())){
                    DataSource dataSource = dataSourceService.selectEntityByName(name);
                    if(null == dataSource){
                        iterator.remove();
                        dataResourceIds.add(dataResourceEntity.getId());
                    }
                }
            }
        }

        //遍历数据服务记录
        if(null != dataServiceList && dataServiceList.size() > 0){
            Iterator<DataService> iterator = dataServiceList.iterator();
            while(iterator.hasNext()){
                DataService dataServiceEntity = iterator.next();
                Long dataResId = dataServiceEntity.getDataResId();
                if(dataResourceIds.contains(dataResId)){
                    iterator.remove();
                }
            }
        }

        newVo.setJobList(dataServiceList);
        newVo.setJobPoolList(dataResourceList);
        return newVo;
    }

    /**
     * 导入数据到数据库中.
     * 导入策略： 若对象存在，则根据overridType更新或保留;若不存在，则添加
     *
     * @param importJobVo
     * @param dspCustomerId 消费者id
     * @return
     * @throws Exception
     */
    private List<String> importJobToDb(ImportJobVo importJobVo, String dspCustomerId) {
        List<String> result = new ArrayList<>();//问题列表

        int addDataServiceCount = 0;//数据资源新增条数
        int updateDataServiceCount = 0;//数据资源更新条数
        int addDataResourceCount = 0;//数据服务新增条数
        int updateDataResourceCount = 0;//数据服务更新条数

        Customer customer = consumerService.selectByPrimaryKey(dspCustomerId);

        //数据服务信息表
        for (int i = 0; i < importJobVo.getJobList().size(); i++) {
            DataService dataService = importJobVo.getJobList().get(i);
            //修改必填字段
            dataService.setApplicationId(0L);
            Long serviceId = dataService.getId();

            //数据库中查询是否存在（数据服务信息表）[id] [dspUserId] ,去校验唯一性
            DataService dataServiceSource = dataServiceMapper.selectEntityByCustIdAndSourceId(dspCustomerId, serviceId);
            if(null != dataServiceSource){
                if(!dataServiceSource.getName().equals(dataService.getName())){
                    DataService entity = dataServiceMapper.selectEntityByName(dataService.getName());
                    if(null != entity){
                        dataService.setName(dataService.getName() + "_" + GenerateRandomStr.generateStr(6));
                    }
                }
                dataServiceSource.setDescription(dataService.getDescription());
                dataServiceSource.setName(dataService.getName());
                dataServiceSource.setAuditMind(dataService.getAuditMind());
                dataServiceSource.setCursorVal(dataService.getCursorVal());
                dataServiceSource.setDataResId(dataService.getDataResId());
                dataServiceSource.setExpiredTime(dataService.getExpiredTime());
                dataServiceSource.setScheduleType(dataService.getScheduleType());
                dataServiceSource.setServiceConfiguration(dataService.getServiceConfiguration());
                dataServiceSource.setSourceType(dataService.getSourceType());
                dataServiceSource.setType(dataService.getType());
                dataServiceSource.setFieldMappings(dataService.getFieldMappings());
                dataServiceSource.setApplyConfiguration(dataService.getApplyConfiguration());
                dataServiceMapper.updateByPrimaryKeySelective(dataServiceSource);
                log.info("update dataService: " + dataService.toString());
                updateDataServiceCount++;
            }else{
                dataService.setId(null);
                dataService.setCustId(dspCustomerId);
                dataService.setServiceId(serviceId);
                dataService.setCreateTime(new Date());
                dataService.setCreator(SaasContext.getCurrentUsername());
                DataService entity = dataServiceMapper.selectEntityByName(dataService.getName());
                if(null != entity){
                    dataService.setName(dataService.getName() + "_" + GenerateRandomStr.generateStr(6));
                }else{
                    dataService.setName(dataService.getName());
                }
                dataServiceMapper.insert(dataService);
                log.info("insert dataService: " + dataService.toString());
                addDataServiceCount++;
            }
        }

        //数据资源信息表
        for (int i = 0; i < importJobVo.getJobPoolList().size(); i++) {
            DataResource dataResource = importJobVo.getJobPoolList().get(i);
            dataResource.setCategoryId(0L);
            Long id = dataResource.getId();

            DataResource oldDataResource = dataResourceService.selectByPrimaryKey(id);
            if(null != oldDataResource){
                //update
                if(!oldDataResource.getName().equals(dataResource.getName())){
                    DataResource entity = dataResourceService.selectEntityByName(dataResource.getName());
                    if(null != entity){
                        dataResource.setName(dataResource.getName() + "_" + GenerateRandomStr.generateStr(6));
                    }
                }

                oldDataResource.setDescription(dataResource.getDescription());
                oldDataResource.setName(dataResource.getName());
                oldDataResource.setBaseConfiguration(dataResource.getBaseConfiguration());
                oldDataResource.setDatasetId(dataResource.getDatasetId());
                oldDataResource.setDatasetName(dataResource.getDatasetName());
                oldDataResource.setEncoder(dataResource.getEncoder());
                oldDataResource.setExpiredTime(dataResource.getExpiredTime());
                oldDataResource.setFieldMappings(dataResource.getFieldMappings());
                oldDataResource.setIncrementField(dataResource.getIncrementField());
                oldDataResource.setIsPull(dataResource.getIsPull());
                oldDataResource.setIsPush(dataResource.getIsPush());
                oldDataResource.setPublishConfiguration(dataResource.getPublishConfiguration());
                oldDataResource.setPullServiceMode(dataResource.getPullServiceMode());
                oldDataResource.setPushServiceMode(dataResource.getPushServiceMode());
                oldDataResource.setQuery(dataResource.getQuery());
                oldDataResource.setSource(dataResource.getSource());
                oldDataResource.setSourceType(dataResource.getSourceType());
                oldDataResource.setStorage(dataResource.getStorage());
                oldDataResource.setType(dataResource.getType());

                dataResourceService.updateByPrimaryKeySelective(oldDataResource);
                log.info("update dataResource: " + dataResource.toString());
                updateDataResourceCount++;
            }else{
                DataResource entity = dataResourceService.selectEntityByName(dataResource.getName());
                if(null != entity){
                    dataResource.setName(dataResource.getName() + "_" + GenerateRandomStr.generateStr(6));
                }else{
                    dataResource.setName(dataResource.getName());
                }
                dataResource.setCreateTime(new Date());
                dataResource.setCreator(SaasContext.getCurrentUsername());
                dataResourceService.insert(dataResource);
                log.info("import dataResource: " + dataResource.toString());
                addDataResourceCount++;
            }
        }

        result.add("导入数据服务信息记录 " + addDataServiceCount + " 条, 修改数据服务信息记录 " + updateDataServiceCount + " 条");
        result.add("导入数据资源信息记录 " + addDataResourceCount + " 条, 修改数据服务信息记录 " + updateDataResourceCount + " 条");
        return result;
    }

    private ImportJobVo importExcelByPush(File excelFile, String custId, Long custDataSourceId, String custTableName) throws Exception{
        ImportJobVo importJobVo = new ImportJobVo();
        List<DataService> jobList = new ArrayList<>();
        List<DataResource> jobPoolList = new ArrayList<>();

        importJobVo.setJobList(jobList);
        importJobVo.setJobPoolList(jobPoolList);
        List<String> errorStringList = new ArrayList<>();
        Customer customer = consumerService.selectByPrimaryKey(custId);
        if(null == customer || "".equals(customer)){
            throw new ServiceException(ErrType.ENTITY_NOT_EXIST, "消费者不存在");
        }
        CustDataSource custDataSource = custDataSourceService.selectByPrimaryKey(custDataSourceId);
        InputStream is = new FileInputStream(excelFile);//创建输入流对象
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook(is);
        //获取Sheet数量
        int sheetNum = xssfWorkbook.getNumberOfSheets();

        //这是最外层
        long xuhao = 0;//临时的序号
        List<String> linshiName = new ArrayList<>();//临时用来存放名字和下标对应关系的集合，例如job.setPlanId行
        List<Long> uselessDataResourceIds = new ArrayList<>();

        for (int index = 0; index < sheetNum; index++) {

            XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(index);
            if (xssfSheet == null) {
                continue;
            }
            List<List<String>> dataList = new ArrayList<List<String>>();
            for (int rowIndex = 1; rowIndex < xssfSheet.getLastRowNum() + 1; rowIndex++) {
                XSSFRow xssfRow = xssfSheet.getRow(rowIndex);
                if (xssfRow == null) {
                    continue;
                }
                List<String> cellList = new ArrayList<String>();
                for (int cellIndex = 0; cellIndex < xssfRow.getLastCellNum(); cellIndex++) {
                    XSSFCell xssfCell = xssfRow.getCell(cellIndex);
                    cellList.add(getString(xssfCell));
                }
                dataList.add(cellList);
            }
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            if (index == 0) { //数据服务信息表
                xuhao = 100;
                for (int i = 0; i < dataList.size(); i++) {
                    boolean isError = false;//校验当入行是否出错
                    DataService dataService = new DataService();
                    xuhao = xuhao + 1;
                    dataService.setId(xuhao);//先给个临时的序号
                    for (int j = 0; j < dataList.get(i).size(); j++) {
                        if (j == 0) {
                            if(null == dataList.get(i).get(j) || "".equals(dataList.get(i).get(j))){
                                errorStringList.add("数据服务信息表,第" + (i + 1) + "行，【主键】不存在;") ;
                                isError = true;
                            }else{
                                Long id = Long.parseLong(dataList.get(i).get(j));
                                dataService.setId(id);
                            }
                        } else if (j == 1) {
                            if (null != dataList.get(i).get(j) && !dataList.get(i).get(j).equals("")) {
                                dataService.setDescription(dataList.get(i).get(j));
                            }
                        }
                        else if (j == 2) {
                            if(null == dataList.get(i).get(j) || "".equals(dataList.get(i).get(j))){
                                errorStringList.add("数据服务信息表,第" + (i + 1) + "行，【名称】不存在;") ;
                                isError = true;
                            }else{
                                dataService.setName(dataList.get(i).get(j));
                            }
                        }
                        else if (j == 3) {
                            if(null != dataList.get(i).get(j) && !"".equals(dataList.get(i).get(j))){
                                dataService.setAuditMind(dataList.get(i).get(j));
                            }
                        } else if (j == 4) {
                            if(null != dataList.get(i).get(j) && !"".equals(dataList.get(i).get(j))){
                                dataService.setCursorVal(dataList.get(i).get(j));
                            }
                        }
                        else if (j == 5) {
                            if(null == dataList.get(i).get(j) || "".equals(dataList.get(i).get(j))){
                                errorStringList.add("数据服务信息表,第" + (i + 1) + "行，【数据资源ID】不存在;") ;
                                isError = true;
                            }else{
                                dataService.setDataResId(Long.parseLong(dataList.get(i).get(j)));
                            }
                        }
                        else if (j == 6) {
                            if(null == dataList.get(i).get(j) || "".equals(dataList.get(i).get(j))){
                                errorStringList.add("数据服务信息表,第" + (i + 1) + "行，【服务过期时间】不存在;") ;
                                isError = true;
                            }else{
                                dataService.setExpiredTime(Long.parseLong(dataList.get(i).get(j)));
                            }
                        }
                        else if (j == 7) {
                            if(null != dataList.get(i).get(j) && !"".equals(dataList.get(i).get(j))){
                                dataService.setScheduleType(dataList.get(i).get(j));
                            }
                        } else if (j == 8) {
                            if(null != dataList.get(i).get(j) && !"".equals(dataList.get(i).get(j))){
                                Map<String, String> map = JsonBuilder.getInstance().fromJson(dataList.get(i).get(j), Map.class);
                                dataService.setServiceConfiguration(map);
                            }
                        } else if (j == 9) {
                            if(null == dataList.get(i).get(j) || "".equals(dataList.get(i).get(j))){
                                errorStringList.add("数据服务信息表,第" + (i + 1) + "行，【数据类型】不能为空;") ;
                                isError = true;
                            }else{
                                dataService.setSourceType(dataList.get(i).get(j));
                            }
                        }
                        else if (j == 10) {
                            if(null == dataList.get(i).get(j) || "".equals(dataList.get(i).get(j))){
                                errorStringList.add("数据服务信息表,第" + (i + 1) + "行，【服务启动类型】不能为空;") ;
                                isError = true;
                            }else if(!dataList.get(i).get(j).equals("1.0")){
                                errorStringList.add("数据服务信息表,第" + (i + 1) + "行，【服务启动类型】不正确, 不是订阅服务;") ;
                                isError = true;
                            }else{
                                if(dataList.get(i).get(j).endsWith(".0")){
                                    dataService.setType(Integer.valueOf(dataList.get(i).get(j).substring(0, dataList.get(i).get(j).length() - 2)));
                                }else{
                                    dataService.setType(Integer.valueOf(dataList.get(i).get(j)));
                                }
                            }
                        }
                        else if (j == 11) {
                            if(null != dataList.get(i).get(j) && !"".equals(dataList.get(i).get(j))){
                                List<FieldMapping> fieldMappings = JsonBuilder.getInstance().fromJson(dataList.get(i).get(j), List.class);
                                dataService.setFieldMappings(fieldMappings);
                            }
                        }
                        else if (j == 12) {
                            if(null != dataList.get(i).get(j) && !"".equals(dataList.get(i).get(j))){
                                Long applicationId = Long.parseLong(dataList.get(i).get(j));
                                dataService.setApplicationId(applicationId);
                            }else{
                                errorStringList.add("数据服务信息表,第" + (i + 1) + "行，【消费者申请记录ID】不存在;") ;
                            }
                        }
                        else if (j == 13) {
                            if(null != dataList.get(i).get(j) && !"".equals(dataList.get(i).get(j))){
                                ApplyConfiguration applyConfiguration = JsonBuilder.getInstance().fromJson(dataList.get(i).get(j), ApplyConfiguration.class);
                                applyConfiguration.setCustId(custId);
                                applyConfiguration.setCustName(customer.getName());
                                applyConfiguration.setCustDataSourceId(custDataSourceId);
                                applyConfiguration.setCustDataSourceName(custDataSource.getName());
                                if("0".equals(custTableName)){
                                    applyConfiguration.setCustTableName(null);
                                }
                                dataService.setApplyConfiguration(applyConfiguration);
                                log.info(JSON.toJSON(applyConfiguration).toString());
                            }
                        }

                    }
                    dataService.setEnabled(1);//启用
                    dataService.setCustId(custId);//导入消费者id
                    dataService.setIsRunning(0);//未运行
                    dataService.setStatus(0);//待部署
                    dataService.setLastModifier(SaasContext.getCurrentUsername());
                    dataService.setLastModifiedTime(new Date());
                    dataService.setTenantId(SaasContext.getCurrentTenantId());
                    dataService.setOwner(SaasContext.getCurrentUserId());
                    if(!isError){
                        jobList.add(dataService);
                    }
                }
            }

            if (index == 1) {  //数据资源信息表--Start
                xuhao = 200;
                for (int i = 0; i < dataList.size(); i++) {
                    boolean isError = false;//校验数据记录是否出错
                    DataResource dataResource = new DataResource();
                    xuhao = xuhao + 1;
                    dataResource.setId(xuhao);
                    for (int j = 0; j < dataList.get(i).size(); j++) {
                        if (j == 0) {
                            if(null == dataList.get(i).get(j) || "".equals(dataList.get(i).get(j))){
                                errorStringList.add("数据资源信息表,第" + (i + 1) + "行，【主键】不存在;") ;
                                isError = true;
                            }else{
                                Long id = Long.parseLong(dataList.get(i).get(j));
                                dataResource.setId(id);
                            }
                        }
                        else if (j == 1) {
                            if (null != dataList.get(i).get(j) && !dataList.get(i).get(j).equals(" ")) {
                                dataResource.setDescription(dataList.get(i).get(j));
                            }
                        }
                        else if (j == 2) {
                            if(null == dataList.get(i).get(j) || "".equals(dataList.get(i).get(j))){
                                errorStringList.add("数据资源信息表,第" + (i + 1) + "行，【名称】不存在;") ;
                                isError = true;
                            }else{
                                dataResource.setName(dataList.get(i).get(j));
                            }
                        }else if (j == 3) {
                            if(null != dataList.get(i).get(j) && !"".equals(dataList.get(i).get(j))){
                                Map<String, String> baseConfiguration = JsonBuilder.getInstance().fromJson(dataList.get(i).get(j), Map.class);
                                dataResource.setBaseConfiguration(baseConfiguration);
                            }
                        }
                        else if (j == 4) {
                            if(null == dataList.get(i).get(j) || "".equals(dataList.get(i).get(j))){
                                errorStringList.add("数据资源信息表,第" + (i + 1) + "行，【数据集ID】不存在;");
                                isError = true;
                            }else{
                                String datasetId = dataList.get(i).get(j);
                                dataResource.setDatasetId(datasetId);
                            }
                        } else if (j == 5) {
                            if(null == dataList.get(i).get(j) || "".equals(dataList.get(i).get(j))){
                                errorStringList.add("数据资源信息表,第" + (i + 1) + "行，【数据集名称】不存在;");
                                isError = true;
                                uselessDataResourceIds.add(Long.parseLong(dataList.get(i).get(0)));
                            }else{
                                String name = dataList.get(i).get(j);
                                String sourceType = dataList.get(i).get(17);
                                if(StringUtils.isEmpty(sourceType)){
                                    errorStringList.add("数据资源信息表,第" + (i + 1) + "行，【数据类型】不存在, 无法匹配数据集;");
                                    isError = true;
                                    uselessDataResourceIds.add(Long.parseLong(dataList.get(i).get(0)));
                                }
                                if("DATASOURCE".equals(sourceType)){
                                    DataSource dataSource = dataSourceService.selectEntityByName(name);
                                    if(null == dataSource){
                                        errorStringList.add("数据资源信息表,第" + (i + 1) + "行, 数据源信息记录不存在;");
                                        isError = true;
                                        uselessDataResourceIds.add(Long.parseLong(dataList.get(i).get(0)));
                                    }else{
                                        dataResource.setDatasetName(dataList.get(i).get(j));
                                    }
                                } else if("DATASET".equals(sourceType)){
                                    Dataset dataset = datasetService.selectEntityByName(name);
                                    if(null == dataset){
                                        errorStringList.add("数据资源信息表,第" + (i + 1) + "行, 数据集信息记录不存在;");
                                        isError = true;
                                        uselessDataResourceIds.add(Long.parseLong(dataList.get(i).get(0)));
                                    }else{
                                        dataResource.setDatasetName(dataList.get(i).get(j));
                                    }
                                }else{
                                    errorStringList.add("数据资源信息表,第" + (i + 1) + "行, 【数据类型】填写错误;");
                                    isError = true;
                                    uselessDataResourceIds.add(Long.parseLong(dataList.get(i).get(0)));
                                }
                            }
                        } else if (j == 6) {
                            dataResource.setEncoder(dataList.get(i).get(j));
                        } else if (j == 7) {
                            if(null == dataList.get(i).get(j) || "".equals(dataList.get(i).get(j))){
                                errorStringList.add("数据资源信息表,第" + (i + 1) + "行，【失效时间】不存在;");
                                isError = true;
                            }else{
                                dataResource.setExpiredTime(Long.parseLong(dataList.get(i).get(j)));
                            }
                        } else if (j == 8) {
                            if(null == dataList.get(i).get(j) || "".equals(dataList.get(i).get(j))){
                                errorStringList.add("数据资源信息表,第" + (i + 1) + "行，【开放字段及配置字段映射关系】不存在;");
                                isError = true;
                            }else{
                                List<FieldMapping> fieldMappings = JsonBuilder.getInstance().fromJson(dataList.get(i).get(j), List.class);
                                dataResource.setFieldMappings(fieldMappings);
                            }
                        } else if (j == 9) {
                            if(null != dataList.get(i).get(j) && !"".equals(dataList.get(i).get(j))){
                                dataResource.setIncrementField(dataList.get(i).get(j));
                            }
                        } else if (j == 10) {
                            if(null != dataList.get(i).get(j) && !"".equals(dataList.get(i).get(j))){
                                if("1.0".equals(dataList.get(i).get(j)) || "1".equals(dataList.get(i).get(j))){
                                    dataResource.setIsPull(1);
                                }else{
                                    dataResource.setIsPull(0);
                                }
                            }
                        } else if (j == 11) {
                            if(null != dataList.get(i).get(j) && !"".equals(dataList.get(i).get(j))){
                                if("1.0".equals(dataList.get(i).get(j)) || "1".equals(dataList.get(i).get(j))){
                                    dataResource.setIsPush(1);
                                }else{
                                    dataResource.setIsPush(0);
                                }
                            }
                        }
                        else if (j == 12) {
                            if(null != dataList.get(i).get(j) && !"".equals(dataList.get(i).get(j))){
                                Map<String, String> publishConfiguration = JsonBuilder.getInstance().fromJson(dataList.get(i).get(j), Map.class);
                                dataResource.setPublishConfiguration(publishConfiguration);
                            }
                        } else if (j == 13) {
                            if(null != dataList.get(i).get(j) && !"".equals(dataList.get(i).get(j))){
                                String[] strArray = dataList.get(i).get(j).split(",");
                                Integer[] intArray = (Integer[]) ConvertUtils.convert(strArray, Integer.class);
                                dataResource.setPullServiceMode(intArray);
                            }
                        } else if (j == 14) {
                            if(null != dataList.get(i).get(j) && !"".equals(dataList.get(i).get(j))){
                                String[] strArray = dataList.get(i).get(j).split(",");
                                Integer[] intArray = (Integer[]) ConvertUtils.convert(strArray, Integer.class);
                                dataResource.setPushServiceMode(intArray);
                            }
                        } else if (j == 15) {
                            if(null == dataList.get(i).get(j) || "".equals(dataList.get(i).get(j))){
                                errorStringList.add("数据资源信息表,第" + (i + 1) + "行，【sql查询配置信息】不存在;");
                                isError = true;
                            }else{
                                SqlQuery query = JsonBuilder.getInstance().fromJson(dataList.get(i).get(j), SqlQuery.class);
                                dataResource.setQuery(query);
                            }
                        } else if (j == 16) {
                            if(null == dataList.get(i).get(j) || "".equals(dataList.get(i).get(j))){
                                errorStringList.add("数据资源信息表,第" + (i + 1) + "行，【数据来源】不存在;");
                                isError = true;
                            }else{
                                dataResource.setSource(dataList.get(i).get(j));
                            }
                        } else if (j == 17) {
                            if(null == dataList.get(i).get(j) || "".equals(dataList.get(i).get(j))){
                                errorStringList.add("数据资源信息表,第" + (i + 1) + "行，【数据类型】不存在;") ;
                                isError = true;
                            }else{
                                String sourceType = dataList.get(i).get(j);
                                if("DATASET".equals(sourceType)){
                                    dataResource.setSourceType(SourceType.DATASET);
                                }else if("DATASOURCE".equals(sourceType)){
                                    dataResource.setSourceType(SourceType.DATASOURCE);
                                }
                            }
                        } else if (j == 18) {
                            if(null == dataList.get(i).get(j) || "".equals(dataList.get(i).get(j))){
                                errorStringList.add("数据资源信息表,第" + (i + 1) + "行，【数据集存储引擎】不存在;") ;
                                isError = true;
                            }else{
                                dataResource.setStorage(dataList.get(i).get(j));
                            }
                        } else if (j == 19) {
                            if(null != dataList.get(i).get(j) && !"".equals(dataList.get(i).get(j))){
                                if(dataList.get(i).get(j).endsWith(".0")){
                                    dataResource.setType(Integer.valueOf(dataList.get(i).get(j).substring(0, dataList.get(i).get(j).length() - 2)));
                                }else{
                                    dataResource.setType(Integer.valueOf(dataList.get(i).get(j)));
                                }
                            }
                        }
                    }
                    //字段信息初始值（默认值）设置
                    dataResource.setEnabled(1);//启用
                    dataResource.setOpenStatus(1);//已开放
                    dataResource.setLastModifier(SaasContext.getCurrentUsername());
                    dataResource.setLastModifiedTime(new Date());
                    dataResource.setTenantId(SaasContext.getCurrentTenantId());
                    dataResource.setOwner(SaasContext.getCurrentUserId());
                    if(!isError){
                        jobPoolList.add(dataResource);
                    }
                }
            }
        }

        if(null != jobList && jobList.size() > 0){
            Iterator<DataService> iterator = jobList.iterator();
            while(iterator.hasNext()){
                DataService dataServiceEntity = iterator.next();
                Long dataResId = dataServiceEntity.getDataResId();
                if(uselessDataResourceIds.contains(dataResId)){
                    iterator.remove();
                }
            }
        }

        importJobVo.setJobList(jobList);
        importJobVo.setJobPoolList(jobPoolList);
        importJobVo.setErrorStringList(errorStringList);
        is.close();
        if (errorStringList.size() > 0) {
            StringBuilder sb = new StringBuilder();
            for (String error : errorStringList) {
                sb.append(error)
                        .append("<br/>");
            }
            throw new RuntimeException(sb.toString());
        }
        return importJobVo;
    }

    public static String getString(XSSFCell xssfCell) {
        if (xssfCell == null) {
            return "";
        }
        if (xssfCell.getCellTypeEnum() == CellType.NUMERIC) {
            return String.valueOf(xssfCell.getNumericCellValue());
        } else if (xssfCell.getCellTypeEnum() == CellType.BOOLEAN) {
            return String.valueOf(xssfCell.getBooleanCellValue());
        } else {
            return xssfCell.getStringCellValue();
        }
    }
}

class ImportJobVo{

    private List<DataService> jobList;//数据服务信息表

    private List<DataResource> jobPoolList;//数据资源信息表

    private List<String> errorStringList;//导入展示时错误信息列表

    public List<DataService> getJobList() {
        return jobList;
    }

    public void setJobList(List<DataService> jobList) {
        this.jobList = jobList;
    }

    public List<DataResource> getJobPoolList() {
        return jobPoolList;
    }

    public void setJobPoolList(List<DataResource> jobPoolList) {
        this.jobPoolList = jobPoolList;
    }

    public List<String> getErrorStringList() {
        return errorStringList;
    }

    public void setErrorStringList(List<String> errorStringList) {
        this.errorStringList = errorStringList;
    }
}