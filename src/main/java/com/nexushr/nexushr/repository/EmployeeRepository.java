package com.nexushr.nexushr.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.nexushr.nexushr.entity.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}