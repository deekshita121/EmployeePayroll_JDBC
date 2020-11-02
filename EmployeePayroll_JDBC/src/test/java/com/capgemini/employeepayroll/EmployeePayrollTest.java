package com.capgemini.employeepayroll;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class EmployeePayrollTest 
{
    
	private EmployeePayrollService employeePayrollService;
	private List<EmployeePayroll> employeeList = new ArrayList<>();

	@Before
	public void init() {
		employeePayrollService = new EmployeePayrollService();
	}

	@Test
	public void givenEmployeePayrollInDB_WhenRetrievedShouldMatchEmployeeCount() {
		employeeList = employeePayrollService.readData();
		Assert.assertEquals(3, employeeList.size());
	}

	@Test
	public void givenUpdatedSalaryWhenUpdatedShouldSyncWithDatabase() {
		employeeList = employeePayrollService.readData();
		employeePayrollService.updateData("Diya", 3000000.00);
		boolean result = employeePayrollService.check(employeeList, "Diya", 3000000.00);
		Assert.assertTrue(result);
	}
}
