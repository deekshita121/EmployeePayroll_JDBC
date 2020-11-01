package com.capgemini.employeepayroll;

import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;

import com.mysql.jdbc.Connection;

public class DataBase 
{
	public static void main(String [] args) {
	 String url = "jdbc:mysql://localhost:3306/payroll_service?useSSL=false";
	 String userName = "root";
	 String password = "Dhruv@876A";
	 java.sql.Connection connection = null;
	
		try {
			//Driver Loading
			Class.forName("com.mysql.cj.jdbc.Driver"); 
			System.out.println("Driver Loaded");
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		listDrivers();
		
		try {
			System.out.println("Connecting to database:"+url);
			connection = DriverManager.getConnection(url, userName, password);
			System.out.println("Successful");
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

	private static void listDrivers() {
		// TODO Auto-generaDriverthod stub
		Enumeration<Driver> driverList = DriverManager.getDrivers();
		while(driverList.hasMoreElements()) {
			Driver driverClass = (Driver) driverList.nextElement();
			System.out.println(" "+driverClass.getClass().getName());
		}
	}
}
