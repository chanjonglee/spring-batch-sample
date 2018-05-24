package com.maroon.xy.job;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.MetaDataInstanceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.maroon.xy.model.Employee;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class ExportToFlatFileJobTests {

	JobLauncherTestUtils jobLauncherTestUtils;

	@Autowired
	ApplicationContext applicationContext;

	@Autowired
	JpaPagingItemReader<Employee> reader;

	@Autowired
	Job exportFileJob;

	@Autowired
	JobLauncher jobLauncher;

	@Test
	public void testExportToFileTest() throws Exception {

		jobLauncherTestUtils = new JobLauncherTestUtils();
		jobLauncherTestUtils.setJobLauncher(jobLauncher);
		jobLauncherTestUtils.setJob(exportFileJob);

		JobExecution jobExecution = jobLauncherTestUtils.launchJob();

		System.out.println(exportFileJob.getName());
		assertEquals(jobExecution.getStatus(), BatchStatus.COMPLETED);
		assertEquals("1,Jim", FileUtils.readLines(new ClassPathResource("employees.txt").getFile()).get(0));

	}

	@Test
	public void testReader() throws UnexpectedInputException, ParseException, NonTransientResourceException, Exception {
		StepExecution execution = MetaDataInstanceFactory.createStepExecution();

		reader.open(execution.getExecutionContext());

		Employee employee = reader.read();
		assertNotNull(employee);
		assertEquals((Integer) 1, employee.getId());
		assertEquals("Jim", employee.getName());

		/*
		 * 
		 * int count = 0;
		 * 
		 * count = StepScopeTestUtils.doInStepScope(execution, () -> {
		 * 
		 * reader.open(execution.getExecutionContext());
		 * 
		 * Employee employee = reader.read(); assertNotNull(employee);
		 * assertEquals((Integer) 1, employee.getId()); assertNotNull("Jim",
		 * employee.getName());
		 * 
		 * return 0; });
		 */

	}

}
