//package com.merce.woven.common.base.tree;
//
//import java.io.Serializable;
//import java.util.List;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.jusfoun.common.base.id.Idable;
//import com.jusfoun.common.message.exception.ControllerException;
//import com.jusfoun.common.message.exception.ServiceException;
//import com.jusfoun.common.message.result.BaseResponse;
//import com.jusfoun.common.message.result.ErrType;
//
//import io.swagger.annotations.ApiOperation;
//import io.swagger.annotations.ApiParam;
//
///**
// * 说明： 实现树结构数据查询Controller基类. <br>
// *
// * @author yjw@jusfoun.com
// * @date 2018年7月12日 下午2:08:27
// */
//@RestController
//public interface BaseTreeableSelectController<T extends Treeable<T> & Idable<PK>, PK extends Serializable> {
//
//	public static final Logger log = LoggerFactory.getLogger(BaseTreeableSelectController.class);
//
//	/**
//	 * 说明： 获取树图操作业务实例. <br>
//	 *
//	 * @author yjw@jusfoun.com
//	 * @date 2018年7月26日 下午2:11:41
//	 * @return 树图操作业务实例
//	 */
//	TreeableAndIdableService<T, PK> getTreeableIdableService();
//
//	/**
//	 * 说明：根据父节点ID查询列表. <br>
//	 *
//	 * @author yjw@jusfoun.com
//	 * @date 2018年7月24日 上午10:58:38
//	 * @param parentId
//	 *            父节点ID
//	 * @return 树根节点及后代节点
//	 */
//	@ApiOperation(value = "根据父节点ID查询列表", notes = "根据父节点ID查询列表", hidden = false)
//	@PostMapping("getByParentId")
//	default BaseResponse<List<T>> getByParentId(//
//			@ApiParam(value = "父节点ID", defaultValue = "0") @RequestParam(defaultValue = "0") PK parentId //
//	) {
//		List<T> list = null;
//		try {
//			list = getTreeableIdableService().selectByParentId(parentId);
//		} catch (ServiceException e) {
//			log.error("根据父节点ID查询子集失败", e);
//			throw new ControllerException(ErrType.ENTITY_QUERY_LIST_ERROR);
//		}
//		return BaseResponse.success(list);
//	}
//
//	/**
//	 * 说明： 获取树数据. <br>
//	 *
//	 * @author yjw@jusfoun.com
//	 * @date 2018年7月24日 上午10:58:38
//	 * @param rootId
//	 *            根节点主键
//	 * @param keyword
//	 *            检索关键字
//	 * @return 树根节点及后代节点
//	 */
//	@ApiOperation(value = "获取树数据", notes = "根据根节点主键和关键字查询树根节点及后代节点，如果主根节点主键为空则查询主根及后代节点，如果关键字不为空，则根据关键字查询所有符合的节点并返回树状数据结构", hidden = false)
//	@PostMapping("getTree")
//	default BaseResponse<T> getTree(//
//			@ApiParam(value = "根节点主键", defaultValue = "0") @RequestParam(defaultValue = "0") PK rootId, //
//			@ApiParam(value = "检索关键字") @RequestParam(required = false) String keyword//
//	) {
//		T root = null;
//		try {
//			root = getTreeableIdableService().selectTree(rootId, keyword);
//		} catch (ServiceException e) {
//			log.error("根据根节点ID查询子集失败", e);
//			throw new ControllerException(ErrType.ENTITY_QUERY_INFO_ERROR);
//		}
//		return BaseResponse.success(root);
//	}
//
//}
