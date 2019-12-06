package com.merce.woven.common.enums.valuable;

import com.merce.woven.common.enums.labelable.Labelable;

/**
 * 可取常量且可比较大小可取标签常量值枚举接口
 *
 * @author jingwei.yang
 * @date 2019-05-28 14:05
 * @param <T>
 *            目标类型
 */
public interface ValuableAndComparableAndLabelable<T extends Comparable<T>>
		extends ValuableAndComparable<T>, Labelable {

}
