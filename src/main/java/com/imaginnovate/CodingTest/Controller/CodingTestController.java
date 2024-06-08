package com.imaginnovate.CodingTest.Controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.imaginnovate.CodingTest.Entity.Employee;
import com.imaginnovate.CodingTest.pojo.EmployeeRequestPojo;
import com.imaginnovate.CodingTest.pojo.EmployeeResponsePojo;
import com.imaginnovate.CodingTest.repository.EmployeeRepository;

@RestController
public class CodingTestController {

	@Autowired
	EmployeeRepository employeeRepository;

	@PostMapping("/saveEmployee")
	ResponseEntity<?> addEmployee(@Valid @RequestBody EmployeeRequestPojo employeeRequestPojo,
			BindingResult bindingResult) {
		Map<String, String> maps = new HashMap<>();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		System.out.println(employeeRequestPojo.toString());
		Employee employee = new Employee();
		if (bindingResult.hasErrors()) { // it will check the validation of pojo class
			bindingResult.getAllErrors().forEach((error) -> {
				String fieldName = ((FieldError) error).getField();
				String errorMessage = error.getDefaultMessage();
				maps.put(fieldName, errorMessage);
			});
			return ResponseEntity.ok(maps);
		}
		// parse the Doj
		employee.setDoj(LocalDate.parse(employeeRequestPojo.getDoj(), formatter));

		BeanUtils.copyProperties(employeeRequestPojo, employee);// here copy from the pojo class property to entity
																// class
		employee = employeeRepository.save(employee); // save the entity in database

		if (employee != null) {
			maps.put("success", "Save Successfully");
		}
		return ResponseEntity.ok(maps);
	}

	@GetMapping(value = "/getEmployeelist")
	public ResponseEntity<?> getEmployeeList() throws Exception {
		List<EmployeeResponsePojo> employeeList = new ArrayList<>();
		List<Employee> list = employeeRepository.findAll();
		for (Employee e : list) {
			
			//calculate current financial
			LocalDate currentFinancialYearStart = LocalDate.of(LocalDate.now().getYear(), 4, 1);
			if (LocalDate.now().getMonthValue() < 4) {
				currentFinancialYearStart = currentFinancialYearStart.minusYears(1);
			}
			LocalDate financialYearEnd = currentFinancialYearStart.plusYears(1).minusDays(1);
			double yearlySalary = calculateYearlySalary(e, currentFinancialYearStart, financialYearEnd);
			double taxAmount = calculateTaxAmount(yearlySalary);
			double cessAmount = (yearlySalary > 2500000) ? (yearlySalary - 2500000) * 0.02 : 0;//calculate cessamount
			EmployeeResponsePojo employeeResponsePojo = new EmployeeResponsePojo();
			employeeResponsePojo.setEmployeeCode(e.getId());
			employeeResponsePojo.setFirstName(e.getFirstName());
			employeeResponsePojo.setLastName(e.getLastName());
			employeeResponsePojo.setTaxAmount((BigDecimal.valueOf(taxAmount).setScale(2, RoundingMode.HALF_UP)).doubleValue());
			employeeResponsePojo.setYearlySalary((BigDecimal.valueOf(yearlySalary).setScale(2, RoundingMode.HALF_UP)).doubleValue());
			employeeResponsePojo.setCessAmount((BigDecimal.valueOf(cessAmount).setScale(2, RoundingMode.HALF_UP)).doubleValue());
			employeeList.add(employeeResponsePojo);
		}
		return ResponseEntity.ok(employeeList);
	}

	public double calculateTaxAmount(double yearlySalary) {
		double taxAmount = 0.0;
		if (yearlySalary <= 250000) {
			taxAmount = 0.0;
		} else if (yearlySalary <= 500000) {
			taxAmount = (yearlySalary - 250000) * 0.05;
		} else if (yearlySalary <= 1000000) {
			taxAmount = 250000 * 0.05 + (yearlySalary - 500000) * 0.10;
		} else {
			taxAmount = 250000 * 0.05 + 500000 * 0.10 + (yearlySalary - 1000000) * 0.20;
		}
		return taxAmount;
	}

	private double calculateYearlySalary(Employee employee, LocalDate financialYearStart, LocalDate financialYearEnd) {
	    LocalDate dateOfJoining = employee.getDoj();
	    double monthlySalary = employee.getSalary();
	    double totalSalary = 0.0;

	    if (dateOfJoining.isBefore(financialYearStart)) {
	        totalSalary = monthlySalary * 12; // If the employee joined before the financial year started, calculate the total salary for the full 12 months.
	    } else if (dateOfJoining.isAfter(financialYearEnd)) {
	        totalSalary = 0.0; // If the employee joined after the financial year ended, the total salary is 0.
	    } else {
	        int monthsWorked = 0;
	        double partialMonthSalary = 0.0;
	        if (dateOfJoining.isAfter(financialYearStart)) {
	            // Calculate the partial month salary
	            int daysInJoiningMonth = dateOfJoining.lengthOfMonth();
	            int daysWorkedInJoiningMonth = daysInJoiningMonth - dateOfJoining.getDayOfMonth() + 1;
	            partialMonthSalary = (daysWorkedInJoiningMonth / (double) daysInJoiningMonth) * monthlySalary;

	            // Calculate the number of full months worked
	            int joiningYear = dateOfJoining.getYear();
	            int joiningMonth = dateOfJoining.getMonthValue();
	            int financialYearEndYear = financialYearEnd.getYear();
	            int financialYearEndMonth = financialYearEnd.getMonthValue();  
	            monthsWorked = (financialYearEndYear - joiningYear) * 12 + (financialYearEndMonth - joiningMonth);
	        } else {
	            // Employee joined exactly at the start of the financial year
	        	monthsWorked = 12;
	        }
	        totalSalary = monthsWorked * monthlySalary + partialMonthSalary;//calculate total salary 
	    }

	    return totalSalary;
	}
}
