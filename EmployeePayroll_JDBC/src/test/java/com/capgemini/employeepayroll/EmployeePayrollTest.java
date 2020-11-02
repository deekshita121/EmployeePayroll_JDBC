package com.capgemini.employeepayroll;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class EmployeePayrollTest 
{
    
	private EmployeePayrollService employeePayrollService;
	private List<EmployeePayroll> employeeList;

	@Before
	public void init() {
		employeePayrollService = new EmployeePayrollService();
	}

	@Test
	public void givenEmployeePayrollInDB_WhenRetrievedShouldMatchEmployeeCount() {
		employeeList = employeePayrollService.readData();
		Assert.assertEquals(3, employeeList.size());
	}

}
