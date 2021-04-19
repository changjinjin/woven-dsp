package com.info.baymax.common.webflux.reactive;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

@Slf4j
public class ReactiveTests {

	@Test
	public void testPublisherThread() {
		Scheduler pubScheduler = Schedulers.newSingle("pub-thread");
		Flux.defer(() -> {
			log.info("defer thread:[{}]", Thread.currentThread().getName());
			return Flux.range(1, 4);
		}).filter(e -> {
			log.info("filter thread:[{}]", Thread.currentThread().getName());
			return e % 2 == 0;
		}).publishOn(pubScheduler).subscribe(e -> {
			log.info("subscribe thread:[{}],data :{}", Thread.currentThread().getName(), e);
		});
	}

	@Test
	public void testSubscriberThread() throws InterruptedException {
		Scheduler subScheduler = Schedulers.newSingle("sub-thread");
		Flux.defer(() -> {
			log.info("defer thread:[{}]", Thread.currentThread().getName());
			return Flux.range(1, 4);
		}).filter(e -> {
			log.info("filter thread:[{}]", Thread.currentThread().getName());
			return e % 2 == 0;
		}).subscribeOn(subScheduler).subscribe(e -> {
			log.info("subscribe thread:[{}],data :{}", Thread.currentThread().getName(), e);
		});
		Thread.sleep(10 * 1000);
	}

	@Test
	public void testPublisherAndSubscriberThread() throws InterruptedException {
		Scheduler pubScheduler = Schedulers.newSingle("publisher-thread");
		Scheduler subScheduler = Schedulers.newSingle("subscriber-thread");
		Flux.defer(() -> {
			log.info("defer thread:[{}]", Thread.currentThread().getName());
			return Flux.range(1, 4);
		}).filter(e -> {
			log.info("filter thread:[{}]", Thread.currentThread().getName());
			return e % 2 == 0;
		}).publishOn(pubScheduler).subscribeOn(subScheduler).subscribe(e -> {
			log.info("subscribe thread:[{}],data :{}", Thread.currentThread().getName(), e);
		});
		Thread.sleep(10 * 1000);
	}

	@Test
	public void testFilterThread() {
		Scheduler pubScheduler = Schedulers.newSingle("publisher-thread");
		Flux.defer(() -> {
			log.info("defer thread:[{}]", Thread.currentThread().getName());
			return Flux.range(1, 4);
		}).publishOn(pubScheduler) // NOTE 注意这里放到了filter之前
			.filter(e -> {
				log.info("filter thread:[{}]", Thread.currentThread().getName());
				return e % 2 == 0;
			}).subscribe(e -> {
			log.info("subscribe thread:[{}],data :{}", Thread.currentThread().getName(), e);
		});
	}

}
