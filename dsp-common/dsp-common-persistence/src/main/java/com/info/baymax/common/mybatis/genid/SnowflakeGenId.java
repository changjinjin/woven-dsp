package com.info.baymax.common.mybatis.genid;

import com.info.baymax.common.utils.Snowflake;

import tk.mybatis.mapper.genid.GenId;

/**
 * 雪花算法策略
 *
 * @author jingwei.yang
 * @date 2019年7月1日 上午10:19:08
 */
public class SnowflakeGenId implements GenId<Long> {
    @Override
    public Long genId(String s, String s1) {
        return Snowflake.getInstance().nextId();
    }
}