package com.info.baymax.common.comp.base.crud;

import com.info.baymax.common.entity.id.Idable;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;

/**
 * 基础的crud基类
 *
 * @param <ID> 主键类型
 * @param <T>  操作类型
 * @author jingwei.yang
 * @date 2020年10月30日 上午11:09:24
 * @since 0.1.3
 */
@RestController
public interface BaseCrudController<ID extends Serializable, T extends Idable<ID>> extends BaseSaveController<ID, T>,
    BaseUpdateController<ID, T>, BaseDeleteController<ID, T>, BasePageController<ID, T>, BaseInfoController<ID, T> {
}
