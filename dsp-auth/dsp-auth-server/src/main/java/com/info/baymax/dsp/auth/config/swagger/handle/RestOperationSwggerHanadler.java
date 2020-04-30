package com.info.baymax.dsp.auth.config.swagger.handle;

import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.info.baymax.common.service.criteria.example.ExampleQuery;
import com.info.baymax.common.utils.ICollections;
import com.info.baymax.dsp.common.swagger.handle.SwaggerHandler;
import com.info.baymax.dsp.data.sys.entity.security.PermOperationRef;
import com.info.baymax.dsp.data.sys.entity.security.RestOperation;
import com.info.baymax.dsp.data.sys.service.security.PermOperationRefService;
import com.info.baymax.dsp.data.sys.service.security.RestOperationService;

import io.swagger.models.HttpMethod;
import io.swagger.models.Operation;
import io.swagger.models.Path;
import io.swagger.models.Swagger;
import lombok.extern.slf4j.Slf4j;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * 自定义Swagger文档处理器
 *
 * @author jingwei.yang
 * @date 2020年4月24日 上午9:52:34
 */
@Slf4j
@Component
public class RestOperationSwggerHanadler implements SwaggerHandler {

	@Autowired
	private RestOperationService restOperationService;
	@Autowired
	private PermOperationRefService permOperationRefService;

	@Value("${spring.application.name}")
	private String serviceName = "default";

	@Override
	public void handle(Swagger swagger) {
		if (swagger == null) {
			log.warn("swagger instance does not exist, swagger instance handle shiped!");
		}
		persist(serviceName, parse(serviceName, swagger));
	}

	private List<RestOperation> parse(String serviceName, Swagger swagger) {
		String basePath = swagger.getBasePath();
		Map<String, Path> paths = swagger.getPaths();
		final List<RestOperation> list = Lists.newArrayList();
		if (!paths.isEmpty()) {
			paths.forEach((p, path) -> {
				Map<HttpMethod, Operation> operationMap = path.getOperationMap();
				if (!operationMap.isEmpty()) {
					operationMap.forEach((m, o) -> {
						list.add(new RestOperation(serviceName, Docket.DEFAULT_GROUP_NAME, o.getTags(), m.name(),
								basePath, p, o.getSummary(), o.getDescription(), o.getOperationId(), o.getConsumes(),
								o.getProduces(), o.isDeprecated()));
					});
				}
			});
		}
		return list;
	}

	/**
	 * 清理掉已经删除的接口信息
	 * 
	 * @param serviceName 服务名称
	 * @param reservedIds 需要保留的数据的ID列表（ID对于每一个RestOperation是唯一且不可变的）
	 */
	private void clear(String serviceName, Long[] reservedIds) {
		permOperationRefService.delete(
				ExampleQuery.builder(PermOperationRef.class).fieldGroup().andNotIn("operationId", reservedIds).end());
		restOperationService.delete(ExampleQuery.builder(RestOperation.class).fieldGroup()
				.andEqualTo("serviceName", serviceName).andNotIn("id", reservedIds).end());
	}

	@Transactional
	private void persist(String serviceName, List<RestOperation> list) {
		if (ICollections.hasElements(list)) {
			Long[] reservedIds = list.stream().map(t -> t.getId()).toArray(Long[]::new);
			clear(serviceName, reservedIds);
			for (RestOperation t : list) {
				restOperationService.saveOrUpdate(t);
			}
		}
	}
}
