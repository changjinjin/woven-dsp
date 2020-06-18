//package com.info.baymax.data.elasticsearch.repository;
//
//import java.util.Date;
//
//import org.springframework.beans.factory.annotation.Autowired;
//
//import com.info.baymax.data.elasticsearch.AbstractBootTest;
//
//public class MetricsEntityRepositoryTest extends AbstractBootTest {
//	@Autowired
//	private MetricsEntityRepository repository;
//
//	@Override
//	public void insert() {
//		for (int i = 0; i < 10000; i++) {
//			repository.save(MetricsEntity.builder().title("title_" + i).weight(i).birth(new Date()).build());
//		}
//	}
//
//	@Override
//	public void select() {
//	}
//
//	@Override
//	public void update() {
//	}
//
//	@Override
//	public void delete() {
//	}
//
//}