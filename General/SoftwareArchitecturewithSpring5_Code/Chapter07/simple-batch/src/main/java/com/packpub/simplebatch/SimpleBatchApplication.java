package com.packpub.simplebatch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableBatchProcessing
@SpringBootApplication
@Configuration
public class SimpleBatchApplication {

	public static void main(String[] args) {
		SpringApplication.run(SimpleBatchApplication.class, args);
	}

	@Autowired
	private JobBuilderFactory jobBuilderFactory;

	@Autowired
	private StepBuilderFactory stepBuilderFactory;

	@Bean
	public Step ourBatchStep() {
		return stepBuilderFactory.get("stepPackPub1")
				.tasklet(new Tasklet() {
					public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {
						return null;
					}
				})
				.build();
	}

	@Bean
	public Job job(Step ourBatchStep) throws Exception {
		return jobBuilderFactory.get("jobPackPub1")
				.incrementer(new RunIdIncrementer())
				.start(ourBatchStep)
				.build();
	}
}
