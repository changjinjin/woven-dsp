package com.info.baymax.common.elasticsearch.multiple.entity.elasticsearch;

import com.info.baymax.common.elasticsearch.routing.entity.TStudent;
import com.info.baymax.common.persistence.mybatis.type.varchar.VarcharVsDateTimeTypeHandler;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.ibatis.type.JdbcType;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import tk.mybatis.mapper.annotation.ColumnType;

import javax.persistence.Table;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "t_student")
@Document(indexName = "t_student")
public class EsStudent extends TStudent {
	private static final long serialVersionUID = 1438013543314550814L;

	@Field(type = FieldType.Text)
	private String tags;

	@ColumnType(jdbcType = JdbcType.VARCHAR, typeHandler = VarcharVsDateTimeTypeHandler.class)
	@Override
	public Date getBirth() {
		return super.getBirth();
	}

	@ColumnType(jdbcType = JdbcType.VARCHAR, typeHandler = VarcharVsDateTimeTypeHandler.class)
	@Override
	public Date getUpdateTime() {
		return super.getUpdateTime();
	}

}
