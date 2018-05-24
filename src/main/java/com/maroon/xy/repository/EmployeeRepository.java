package com.maroon.xy.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.maroon.xy.model.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

}
