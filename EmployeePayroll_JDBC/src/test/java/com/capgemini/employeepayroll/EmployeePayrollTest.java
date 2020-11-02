package com.capgemini.employeepayroll;

import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.capgemini.employeepayroll.EmployeePayrollService.statementType;

public class EmployeePayrollTest {

	private EmployeePayrollService employeePayrollService;
	private List<EmployeePayroll> employeeList = new ArrayList<>();

	@Before
	public void init() {
		employeePayrollService = EmployeePayrollService.getInstance();
	}

	// To test the retrieved entries from database
	@Test
	public void givenEmployeePayrollInDBWhenRetrievedShouldMatchEmployeeCount() throws DataBaseException {

		employeeList = employeePayrollService.readData();
		Assert.assertEquals(3, employeeList.size());
	}

	// To test whether database is updated for a given entry or not using statement
	@Test
	public void givenUpdatedSalaryWhenUpdatedShouldSyncWithDatabase() throws DataBaseException {
		employeeList = employeePayrollService.readData();
		employeePayrollService.updateData("Diya", 3000000.00, statementType.STATEMENT);
		boolean result = employeePayrollService.check(employeeList, "Diya", 3000000.00);
		assertTrue(result);
	}

	// To test whether database is updated for a given entry or not using prepared
	// statement
	@Test
	public void givenUpdatedSalaryWhenUpdatedUsingPreparedStatementShouldSyncWithDatabase() throws DataBaseException {
		employeeList = employeePayrollService.readData();
		employeePayrollService.updateData("Diya", 2000000.00, statementType.PREPARED_STATEMENT);
		boolean result = employeePayrollService.check(employeeList, "Diya", 2000000.00);
		assertTrue(result);
	}
	
	// To test the data retrieved when start date of employee given
		@Test
		public void givenDateRangeWhenRetrievedEmployeeDataShouldMatchEmployeeCount() throws DataBaseException {
			employeeList = employeePayrollService.getEmployeeDataByDate(LocalDate.of(2018, 01, 01), LocalDate.now());
			Assert.assertEquals(3, employeeList.size());
		}
}
