package com.harpatec.examples;

import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.annotation.ScheduledAnnotationBeanPostProcessor;

@Configuration
public class AppConfiguration {
	
	@Bean
	public SpringWebsocketAdapter springWebsocketAdapter() {
		return new SpringWebsocketAdapter();
	}

	@Bean
	public ScheduledProducer scheduledProducer() {
		return new ScheduledProducer(springWebsocketAdapter());
	}

	@Bean
	public BeanPostProcessor postProcessor() {
		return new ScheduledAnnotationBeanPostProcessor();
	}

	static class ScheduledProducer {

		private final AtomicInteger counter = new AtomicInteger();
		private final SpringWebsocketAdapter adapter;

		public ScheduledProducer(SpringWebsocketAdapter springWebsocketAdapter) {
			this.adapter = springWebsocketAdapter;
		}

		@Scheduled(fixedRate = 3000)
		public void sendMessage() {
			adapter.broadcast("Hello World " + counter.incrementAndGet());
		}
	}

}
