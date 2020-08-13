package com.info.baymax.dsp.access.platform.web.controller.data;

import com.info.baymax.common.message.exception.ServiceException;
import com.info.baymax.common.message.result.Response;
import com.info.baymax.common.queryapi.page.IPage;
import com.info.baymax.data.elasticsearch.entity.DataTransferRecord;
import com.info.baymax.data.elasticsearch.service.DataTransferRecordService;
import com.info.baymax.dsp.data.platform.bean.GrowthType;
import com.info.baymax.dsp.data.platform.bean.TransferType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Api(tags = "数据管理：数据传输记录管理", description = "数据传输记录管理")
@RestController
@RequestMapping("/record")
public class DataTransferRecordController {

    @Autowired
    private DataTransferRecordService dataTransferRecordService;

    @ApiOperation("用户访问量topN")
    @GetMapping("userVisitTopN")
    public Response<List<Map<String, Object>>> userVisitTopN(
        @ApiParam(value = "查询开始时间", defaultValue = "0") @RequestParam(defaultValue = "0") long start,
        @ApiParam(value = "查询开始时间", defaultValue = "0") @RequestParam(defaultValue = "0") long end,
        @ApiParam(value = "查询条数", defaultValue = "10") @RequestParam(defaultValue = "10") int n,
        @ApiParam(value = "是否逆序", defaultValue = "false") @RequestParam(defaultValue = "false") boolean reverse) {
        return Response.ok(dataTransferRecordService.userVisitTopN(start, end, n, reverse));
    }

    @ApiOperation("数据集访问量topN")
    @GetMapping("datasetVisitTopN")
    public Response<List<Map<String, Object>>> datasetVisitTopN(
        @ApiParam(value = "查询开始时间", defaultValue = "0") @RequestParam(defaultValue = "0") long start,
        @ApiParam(value = "查询开始时间", defaultValue = "0") @RequestParam(defaultValue = "0") long end,
        @ApiParam(value = "查询条数", defaultValue = "10") @RequestParam(defaultValue = "10") int n,
        @ApiParam(value = "是否逆序", defaultValue = "false") @RequestParam(defaultValue = "false") boolean reverse) {
        return Response.ok(dataTransferRecordService.datasetVisitTopN(start, end, n, reverse));
    }

    @ApiOperation("用户访问数据集量topN")
    @GetMapping("userVisitDatasetTopN")
    public Response<List<Map<String, Object>>> userVisitDatasetTopN(
        @ApiParam(value = "消费者ID") @RequestParam(required = true) String custId,
        @ApiParam(value = "查询开始时间", defaultValue = "0") @RequestParam(defaultValue = "0") long start,
        @ApiParam(value = "查询开始时间", defaultValue = "0") @RequestParam(defaultValue = "0") long end,
        @ApiParam(value = "查询条数", defaultValue = "10") @RequestParam(defaultValue = "10") int n,
        @ApiParam(value = "是否逆序", defaultValue = "false") @RequestParam(defaultValue = "false") boolean reverse) {
        return Response.ok(dataTransferRecordService.userVisitDatasetTopN(custId, start, end, n, reverse));
    }

    @ApiOperation("数据集被访问用户量topN")
    @GetMapping("datasetVisitUserTopN")
    public Response<List<Map<String, Object>>> datasetVisitUserTopN(
        @ApiParam(value = "数据集ID") @RequestParam(required = true) String datasetId,
        @ApiParam(value = "查询开始时间", defaultValue = "0") @RequestParam(defaultValue = "0") long start,
        @ApiParam(value = "查询开始时间", defaultValue = "0") @RequestParam(defaultValue = "0") long end,
        @ApiParam(value = "查询条数", defaultValue = "10") @RequestParam(defaultValue = "10") int n,
        @ApiParam(value = "是否逆序", defaultValue = "false") @RequestParam(defaultValue = "false") boolean reverse) {
        return Response.ok(dataTransferRecordService.datasetVisitUserTopN(datasetId, start, end, n, reverse));
    }

    @ApiOperation("数据访问记录查询")
    @GetMapping("page")
    public Response<IPage<DataTransferRecord>> queryList(
        @ApiParam(value = "检索关键字") @RequestParam(required = false) String keyword,
        @ApiParam(value = "数据获取方式", required = false) @RequestParam(required = false) TransferType transferType,
        @ApiParam(value = "增长方式", required = false) @RequestParam(required = false) GrowthType growthType,
        @ApiParam(value = "消费者ID") @RequestParam(required = false) String custId,
        @ApiParam(value = "数据集ID") @RequestParam(required = false) String datasetId,
        @ApiParam(value = "查询开始时间", defaultValue = "0") @RequestParam(defaultValue = "0") long start,
        @ApiParam(value = "查询开始时间", defaultValue = "0") @RequestParam(defaultValue = "0") long end,
        @ApiParam(value = "页码", defaultValue = "1") @RequestParam(defaultValue = "1") int pageNum,
        @ApiParam(value = "页长", defaultValue = "10") @RequestParam(defaultValue = "10") int pageSize)
        throws ServiceException {
        return Response.ok(dataTransferRecordService.queryList(keyword, transferType == null ? "" : transferType.name(),
            growthType == null ? "" : growthType.name(), custId, datasetId, start, end, pageNum, pageSize));
    }

}