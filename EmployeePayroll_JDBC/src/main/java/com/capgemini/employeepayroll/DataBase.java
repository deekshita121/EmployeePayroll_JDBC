package com.capgemini.employeepayroll;

import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;

import com.mysql.jdbc.Connection;

public class DataBase 
{
	
	 static String url = "jdbc:mysql://localhost:3306/payroll_service?useSSL=false";
	 static String userName = "root";
	 static String password = "Dhruv@876A";
	 static java.sql.Connection con = null;
	 
	 
	 public static java.sql.Connection getConnection()
		{
			try {
				//Driver Loading
				Class.forName("com.mysql.cj.jdbc.Driver"); 
				//Making the connection to Database
				con = DriverManager.getConnection(url,userName,password); 
				System.out.println("Connection Successful");
				
			} catch (Exception e) {
				e.printStackTrace();
				
			}
			
			return con;
		}
	
	/*	try {
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
	}*/

/*	public static Connection getConnection() {
		String password ="Dhruv@876A" ;
		String userName="root" ;
		String url = "jdbc:mysql://localhost:3306/payroll_service?useSSL=false";
		Connection con = null;
		// TODO Auto-generated method stub
		try {
			con = (Connection) DriverManager.getConnection(url, userName, password);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return con;
	}
*/

	 
}
