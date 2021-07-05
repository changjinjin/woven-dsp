package com.info.baymax.common.elasticsearch.multiple.entity.mysql;

import com.info.baymax.common.elasticsearch.routing.entity.TStudent;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.ibatis.type.JdbcType;
import org.hibernate.annotations.Comment;
import tk.mybatis.mapper.annotation.ColumnType;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "t_student")
@Comment("学生表")
public class MysqlStudent extends TStudent {
	private static final long serialVersionUID = -4331111419192946188L;

	@ColumnType(jdbcType = JdbcType.DATE)
	@Override
	public Date getBirth() {
		return super.getBirth();
	}

	@ColumnType(jdbcType = JdbcType.TIMESTAMP)
	@Override
	public Date getUpdateTime() {
		return super.getUpdateTime();
	}

}
