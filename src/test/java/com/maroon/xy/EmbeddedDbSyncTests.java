package com.maroon.xy;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.maroon.xy.repository.EmployeeRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EmbeddedDbSyncTests {

	@Autowired
	EmployeeRepository employeeRepository;

	@Test
	public void testEmbeddedDbSyncSuccess() {

		System.out.println(employeeRepository.count());
		assertTrue(employeeRepository.count() > 0);
	}

}
