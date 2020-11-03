package com.capgemini.employeepayroll;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.capgemini.employeepayroll.DataBaseException.exceptionType;

public class EmployeePayrollService {

	private static EmployeePayrollService employeePayrollService;
	PreparedStatement preparedStatement;

	private EmployeePayrollService() {

	}

	// To get instance for EmployeePayrollService
	public static EmployeePayrollService getInstance() {
		if (employeePayrollService == null)
			employeePayrollService = new EmployeePayrollService();
		return employeePayrollService;
	}

	public enum statementType {
		STATEMENT, PREPARED_STATEMENT
	}

	// To read payroll Data from database
	public List<EmployeePayroll> readData() throws DataBaseException {
		String sqlQuery = "SELECT * FROM employee_payroll";
		List<EmployeePayroll> employeePayrollList = new ArrayList<>();
		try (Connection connection = DataBase.getConnection()) {
			Statement statement = connection.createStatement();
			ResultSet result = statement.executeQuery(sqlQuery);
			employeePayrollList = getResultSet(result);
		} catch (SQLException e) {
			throw new DataBaseException("Unable to execute query!!", exceptionType.EXECUTE_QUERY);
		}
		return employeePayrollList;
	}

	// To get employee data by employee name
	public List<EmployeePayroll> getEmployeeDataByName(String name) throws DataBaseException {
		List<EmployeePayroll> employeePayrollListByName = null;
		if (preparedStatement == null)
			preparedStatemenToGetEmployeeDataByName();
		ResultSet result = null;
		try {
			preparedStatement.setString(1, name);
			result = preparedStatement.executeQuery();
		} catch (SQLException e) {
			throw new DataBaseException("Unable to execute query!!", exceptionType.EXECUTE_QUERY);
		}
		employeePayrollListByName = getResultSet(result);
		return employeePayrollListByName;
	}

	private List<EmployeePayroll> getResultSet(ResultSet result) throws DataBaseException {
		List<EmployeePayroll> employeePayrollList = new ArrayList<>();
		try {
			while (result.next()) {
				int id = result.getInt("id");
				String name = result.getString("name");
				double salary = result.getDouble("salary");
				LocalDate start = result.getDate("start").toLocalDate();
				employeePayrollList.add(new EmployeePayroll(id, name, salary, start));
			}
		} catch (SQLException e) {
			throw new DataBaseException("Unable to execute query!!", exceptionType.EXECUTE_QUERY);
		}
		return employeePayrollList;
	}

	// To update data in the database
	public int updateData(String name, double salary, statementType type) throws DataBaseException {
		if (type.equals(statementType.STATEMENT))
			return updateUsingStatement(name, salary);
		if (type.equals(statementType.PREPARED_STATEMENT))
			return updateUsingPreparedStatement(name, salary);
		return 0;
	}

	private int updateUsingStatement(String name, double salary) throws DataBaseException {
		String sqlQuery = String.format("UPDATE employee_payroll SET salary = %.2f WHERE NAME = '%s';", salary, name);
		int ap = 0;
		try (Connection connection = DataBase.getConnection()) {
			Statement statement = connection.createStatement();

			ap = statement.executeUpdate(sqlQuery);
		} catch (SQLException | DataBaseException e) {
			throw new DataBaseException("Unable to execute query!!", exceptionType.EXECUTE_QUERY);
		}
		return ap;
	}

	private int updateUsingPreparedStatement(String name, double salary) throws DataBaseException {
		String sql = "UPDATE employee_payroll SET salary = ? WHERE name = ?";
		try (Connection connection = DataBase.getConnection()) {
			PreparedStatement preparedStatementUpdate = connection.prepareStatement(sql);
			preparedStatementUpdate.setDouble(1, salary);
			preparedStatementUpdate.setString(2, name);
			return preparedStatementUpdate.executeUpdate();
		} catch (SQLException e) {
			throw new DataBaseException("Unable to execute query!!", exceptionType.EXECUTE_QUERY);
		}
	}

	// To check the database after updating
	public boolean check(List<EmployeePayroll> employeePayrollList, String name, double salary)
			throws DataBaseException {
		EmployeePayroll employeeObj = getEmployee(employeePayrollList, name);
		employeeObj.setSalary(salary);
		return employeeObj.equals(getEmployee(readData(), name));
	}

	private EmployeePayroll getEmployee(List<EmployeePayroll> employeeList, String name) {
		EmployeePayroll employee = employeeList.stream().filter(employeeObj -> ((employeeObj.getName()).equals(name)))
				.findFirst().orElse(null);
		return employee;
	}

	// Prepared statement for employee payroll data
	private void preparedStatemenToGetEmployeeDataByName() throws DataBaseException {
		String sql = "SELECT * FROM employee_payroll WHERE name = ?";
		try (Connection connection = DataBase.getConnection()) {
			preparedStatement = connection.prepareStatement(sql);
		} catch (SQLException e) {
			throw new DataBaseException("Unable to execute query!!", exceptionType.EXECUTE_QUERY);
		}
	}

	public List<EmployeePayroll> getEmployeeDataByDate(LocalDate start, LocalDate endDate) throws DataBaseException {
		String sqlQuery = String.format("SELECT * FROM employee_payroll WHERE start BETWEEN '%s' AND '%s';",
				Date.valueOf(start), Date.valueOf(endDate));
		List<EmployeePayroll> employeePayrollList = new ArrayList<>();
		try (Connection connection = DataBase.getConnection()) {
			Statement statement = connection.createStatement();
			ResultSet result = statement.executeQuery(sqlQuery);
			employeePayrollList = getResultSet(result);
		} catch (SQLException e) {
			throw new DataBaseException("Unable to execute query!!", exceptionType.EXECUTE_QUERY);
		}
		return employeePayrollList;
	}
	
	public Map<String, Double> getDataGroupedByGender(String operation, String column) throws DataBaseException {
		Map<String, Double> empDataGroupedByGender = new HashMap<>();
		String sqlQuery = String.format("SELECT gender, %s(%s) AS salary_sum FROM employee_payroll GROUP BY gender;",operation,column);
		try (Connection connection = DataBase.getConnection()) {
			Statement statement = connection.createStatement();
			ResultSet result = statement.executeQuery(sqlQuery);
			while (result.next()) {
				empDataGroupedByGender.put(result.getString("gender"), result.getDouble("salary_sum"));
			}
		} catch (SQLException e) {
			throw new DataBaseException("Unable to execute query!!", exceptionType.EXECUTE_QUERY);
		}
		return empDataGroupedByGender;
	}

	
	public EmployeePayroll addEmployeeData(String name, char gender, double salary, LocalDate start)
			throws DataBaseException {

		int employeeId = -1;
		EmployeePayroll employeePayrollData = null;
		String sqlQuery = String.format(
				"INSERT INTO employee_payroll (name,gender,salary,start) VALUES('%s','%s','%s','%s')", name, gender,
				salary, Date.valueOf(start));
		try (Connection connection = DataBase.getConnection()) {
			Statement statement = connection.createStatement();
			int rowAffected = statement.executeUpdate(sqlQuery, statement.RETURN_GENERATED_KEYS);
			if (rowAffected == 1) {
				ResultSet result = statement.getGeneratedKeys();
				if (result.next())
					employeeId = result.getInt(1);
			}
			System.out.println("po");
			employeePayrollData = new EmployeePayroll(employeeId, name, gender, salary, start);
			System.out.println("po");
		} catch (SQLException e) {
			throw new DataBaseException("Unable to execute query!!", exceptionType.EXECUTE_QUERY);
		}
		return employeePayrollData;
	}
	
	public EmployeePayroll addEmployeeToEmployeeAndPayroll(String name, double salary, LocalDate startDate, char gender)
			throws DataBaseException {
		int employeeId = -1;
		Connection connection = null;
		EmployeePayroll employeePayrollData = null;
		connection = DataBase.getConnection();
		try (Statement statement = connection.createStatement()) {
			String sql = String.format(
					"INSERT INTO employee_payroll(name,gender,salary,startDate) VALUES ('%s','%s','%s','%s')", name,
					gender, salary, Date.valueOf(startDate));
			int rowAffected = statement.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
			if (rowAffected == 1) {
				ResultSet resultSet = statement.getGeneratedKeys();
				if (resultSet.next())
					employeeId = resultSet.getInt(1);
			}
		} catch (SQLException e) {
			throw new DataBaseException("Unable to execute query!!", exceptionType.EXECUTE_QUERY);
		}

		try (Statement statement = connection.createStatement()) {
			double deductions = salary * 0.2;
			double taxablePay = salary - deductions;
			double tax = taxablePay * 0.1;
			double netPay = salary - tax;
			String sql = String.format(
					"INSERT INTO payroll_details(employee_id,basic_pay,deductions,taxable_pay,tax,net_pay)VALUES (%s,%s,%s,%s,%s,%s)",
					employeeId, salary, deductions, taxablePay, tax, netPay);
			int rowAffected = statement.executeUpdate(sql);
			if (rowAffected == 1) {
				employeePayrollData = new EmployeePayroll(employeeId, name, salary, startDate);
			}
		} catch (SQLException e) {
			throw new DataBaseException("Unable to execute query!!", exceptionType.EXECUTE_QUERY);
		} finally {
			if (connection != null) {
					try {
						connection.close();
					} catch (SQLException e) {
						throw new DataBaseException("Unable to close database!!", exceptionType.DATABASE_CONNECTION);
					}
			}
		}
		return employeePayrollData;
	}
	
}