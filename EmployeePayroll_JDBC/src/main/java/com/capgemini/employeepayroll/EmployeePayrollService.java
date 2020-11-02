package com.capgemini.employeepayroll;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EmployeePayrollService {

	public static void main(String[] args) {
		// Welcome Message
		System.out.println("Welcome to Employee Payroll Service");

	}
	
	public enum statementType {
		STATEMENT, PREPARED_STATEMENT
	}

	List<EmployeePayroll> employeePayrollList = null;
	// To read payroll Data from database
	public List<EmployeePayroll> readData() {
		String sqlQuery = "SELECT * FROM employee_payroll";
		List<EmployeePayroll> employeePayrollList = new ArrayList<>();
		DataBase obj = new DataBase();
		try(Connection con =  (Connection) obj.getConnection()) {
			Statement statement = (Statement) con.createStatement();
			ResultSet result = statement.executeQuery(sqlQuery);
			while (result.next()) {
				int id = result.getInt("id");
				String name = result.getString("name");
				double salary = result.getDouble("salary");
				LocalDate start = result.getDate("start").toLocalDate();
				employeePayrollList.add(new EmployeePayroll(id, name, salary, start));
				
			}
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		return employeePayrollList;
	}
	
	public int updateData(String name, Double salary) {
		String sqlQuery = String.format("UPDATE employee_payroll SET salary = %.2f WHERE NAME = '%s';", salary, name);
		try (Connection connection = DataBase.getConnection()) {
			Statement statement = connection.createStatement();
			return statement.executeUpdate(sqlQuery);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	private int updateUsingStatement(String name, double salary) {
		String sqlQuery = String.format("UPDATE employee_payroll SET salary = %.2f WHERE NAME = '%s';", salary, name);
		try (Connection connection = DataBase.getConnection()) {
			Statement statement = connection.createStatement();
			return statement.executeUpdate(sqlQuery);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public int updateUsingPreparedStatement(String name, double salary, statementType preparedStatement) {
		String sql = "UPDATE employee_payroll SET salary = ? WHERE name = ?";
		try (Connection connection = DataBase.getConnection()) {
			PreparedStatement preparedStatementUpdate = connection.prepareStatement(sql);
			preparedStatementUpdate.setDouble(1, salary);
			preparedStatementUpdate.setString(2, name);
			return preparedStatementUpdate.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public boolean check(List<EmployeePayroll> employeeList, String name, double salary) {
		// TODO Auto-generated method stub
		EmployeePayroll employeeObj = getEmployee(employeeList, name);
		employeeObj.setSalary(salary);
		return employeeObj.equals(getEmployee(readData(), name));
		
	}
	
	private EmployeePayroll getEmployee(List<EmployeePayroll> employeeList, String name) {
		EmployeePayroll employee = employeeList.stream().filter(employeeObj -> ((employeeObj.getName()).equals(name)))
				.findFirst().orElse(null);
		return employee;
	}
}
