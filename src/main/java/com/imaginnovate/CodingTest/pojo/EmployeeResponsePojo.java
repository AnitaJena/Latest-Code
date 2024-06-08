package com.imaginnovate.CodingTest.pojo;

import lombok.Data;
@Data
public class EmployeeResponsePojo {

	private Long employeeCode;
	private String firstName; 
	private String lastName; 
	private Double yearlySalary; 
	private Double taxAmount; 
	private Double cessAmount;
}
