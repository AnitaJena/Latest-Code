package com.imaginnovate.CodingTest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.imaginnovate.CodingTest.Entity.Employee;
import java.lang.Long;
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long>{

}
