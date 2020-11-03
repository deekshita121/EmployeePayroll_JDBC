package com.capgemini.employeepayroll;

import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
		Assert.assertEquals(4, employeeList.size());
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
		Assert.assertEquals(4, employeeList.size());
	}

	@Test
	public void givenEmployeeDB_WhenRetrievedData_ShouldReturnSumGroupedByGender() throws DataBaseException {
		Map<String, Double> empDataGroupByGender = null;
		empDataGroupByGender = employeePayrollService.getDataGroupedByGender("sum", "salary");
		Double maleSalary = 4200000.00;
		Assert.assertEquals(maleSalary, empDataGroupByGender.get("M"));
		Double femaleSalary = 3150000.00;
		Assert.assertEquals(femaleSalary, empDataGroupByGender.get("F"));
	}

	@Test
	public void givenEmployeeDB_WhenRetrievedData_ShouldReturnAvgGroupedByGender() throws DataBaseException {
		Map<String, Double> empDataGroupByGender = null;
		empDataGroupByGender = employeePayrollService.getDataGroupedByGender("avg", "salary");
		Double maleSalary = 2100000.00;
		Assert.assertEquals(maleSalary, empDataGroupByGender.get("M"));
		Double femaleSalary = 1075000.00;
		Assert.assertEquals(femaleSalary, empDataGroupByGender.get("F"));
	}

	@Test
	public void givenEmployeeDB_WhenRetrievedData_ShouldReturnMinSalaryGroupedByGender() throws DataBaseException {
		Map<String, Double> empDataGroupByGender = null;
		empDataGroupByGender = employeePayrollService.getDataGroupedByGender("min", "salary");
		Double maleSalary = 200000.00;
		Assert.assertEquals(maleSalary, empDataGroupByGender.get("M"));
		Double femaleSalary = 150000.00;
		Assert.assertEquals(femaleSalary, empDataGroupByGender.get("F"));
	}

	@Test
	public void givenEmployeeDB_WhenRetrievedData_ShouldReturnMaxSalaryGroupedByGender() throws DataBaseException {
		Map<String, Double> empDataGroupByGender = null;
		empDataGroupByGender = employeePayrollService.getDataGroupedByGender("max", "salary");
		Double maleSalary = 4000000.00;
		Assert.assertEquals(maleSalary, empDataGroupByGender.get("M"));
		Double femaleSalary = 2000000.00;
		Assert.assertEquals(femaleSalary, empDataGroupByGender.get("F"));
	}

	// To test when a new employee is added to database
	@Test
	public void givenNewEmployeeWhenAddedShouldSyncWithDatabase() throws DataBaseException {
		EmployeePayroll newEmployee = new EmployeePayroll(4, "Charlie", 'M', 4000000.00, LocalDate.now());
		EmployeePayroll employeeData = null;
        employeeData = employeePayrollService.addEmployeeData("Charlie", 'M', 4000000.00, LocalDate.now());
		Assert.assertEquals(4,employeeData.getId());
	}
}
