package com.imaginnovate.CodingTest.Entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
@Data
@Getter
@Setter
@Entity
public class Employee {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="employee_id",nullable=false)
	private Long id;
	
	@Column(name="employee_fname")
	private String firstName;
	
	@Column(name="employee_lname")
	private String lastName;
	
	@ElementCollection
	@CollectionTable(name="emplooyee_phone_numbers", joinColumns=@JoinColumn(name="employee_id"))
    @Column(name = "phone_number")
    private Set<String> phoneNumbers;
	
	@Column(name="doj")
	private LocalDate doj;
	
	@Column(name="email")
	private String emailId;
	
	@Column(name="salary")
	private Double salary;
	

	
}
