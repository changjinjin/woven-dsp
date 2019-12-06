package com.merce.woven.common.mybatis.genid;

import com.merce.woven.common.utils.SnowFlake;

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
        SnowFlake idWorker = new SnowFlake(0, 0);
        return idWorker.nextId();
    }
}