package com.imaginnovate.CodingTest.pojo;
import java.math.BigDecimal;
import java.util.Set;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;

import lombok.Data;

@Data
public class EmployeeRequestPojo {

	@NotBlank(message = "Firstname is mandatory")
	@Pattern(regexp="^[a-zA-Z]{3,30}",message="firstName length must be 3")  
	private String firstName;
	
	@NotBlank(message = "Lastname is mandatory")
	@Pattern(regexp="^[a-zA-Z]{3,30}",message="lastName length must be 3")  
	private String lastName;
	
	@NotNull(message = "doj is mandatory")
	private String doj;
	
	@NotBlank(message = "EmailId is mandatory")
	@Pattern(regexp = "^([\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4})?$", message = "Invalid EmailId format")
	private String emailId;
	
	@NotNull(message = "salary is mandatory")
    @Positive
	private Double salary;	
	
	private Set<String> phoneNumbers;

}
