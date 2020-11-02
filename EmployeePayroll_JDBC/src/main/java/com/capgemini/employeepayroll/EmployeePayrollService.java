package com.capgemini.employeepayroll;

import java.sql.Connection;
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
}
