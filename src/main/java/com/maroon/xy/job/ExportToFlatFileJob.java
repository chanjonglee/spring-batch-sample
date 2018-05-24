package com.maroon.xy.job;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.support.PassThroughItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import com.maroon.xy.model.Employee;

@Configuration
@EnableBatchProcessing
public class ExportToFlatFileJob {

	@PersistenceContext
	EntityManager entityManager;

	@Autowired
	JobBuilderFactory jobBuilderFactory;

	@Autowired
	StepBuilderFactory stepBuilderFactory;

	@Bean
	public Job exportFileJob(Step step1) {

		return jobBuilderFactory.get("ExportToFlatFileJob").flow(step1).end().build();

	}

	@Bean
	public Job duplicateExportFileJobJob(Step step1) {

		return jobBuilderFactory.get("duplicateExportFileJobJob").flow(step1).end().build();

	}

	@Bean(destroyMethod = "")
	public ItemReader<Employee> reader() {

		JpaPagingItemReader<Employee> reader = new JpaPagingItemReader<>();

		reader.setEntityManagerFactory(entityManager.getEntityManagerFactory());
		reader.setQueryString("select t from Employee t");

		return reader;

	}

	@Bean
	public FlatFileItemWriter<Employee> writer() {

		DelimitedLineAggregator<Employee> lineAggregator = new DelimitedLineAggregator<Employee>();
		lineAggregator.setDelimiter(",");
		BeanWrapperFieldExtractor<Employee> extractor = new BeanWrapperFieldExtractor<>();
		extractor.setNames(new String[] { "id", "name" });
		lineAggregator.setFieldExtractor(extractor);

		return new FlatFileItemWriterBuilder<Employee>().name("createFile")
				.resource(new ClassPathResource("employees.txt")).lineAggregator(lineAggregator).build();

	}

	@Bean
	public Step step1(FlatFileItemWriter<Employee> writer) {
		return stepBuilderFactory.get("step1").<Employee, Employee>chunk(1).reader(reader())
				.processor(new PassThroughItemProcessor<>()).writer(writer()).build();
	}

}
