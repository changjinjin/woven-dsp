package com.info.baymax.common.persistence.mybatis.genid;

import com.info.baymax.common.utils.Snowflake;
import tk.mybatis.mapper.genid.GenId;

/**
 * UUID 主键策略
 *
 * @author jingwei.yang
 * @date 2019年7月1日 上午10:19:08
 */
public class UuidGenId implements GenId<String> {
    @Override
    public String genId(String s, String s1) {
		return Snowflake.stringId();
    }
}