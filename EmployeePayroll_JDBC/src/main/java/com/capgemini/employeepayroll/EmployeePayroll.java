package com.capgemini.employeepayroll;

import java.time.LocalDate;

public class EmployeePayroll {
	private int id;
	private String name;
	private Double salary;
	private LocalDate start;

	// Constructor
	public EmployeePayroll(int id, String name, Double salary, LocalDate start) {
		this.id = id;
		this.name = name;
		this.salary = salary;
		this.start = start;
	}

	// Getters and Setters
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getSalary() {
		return salary;
	}

	public void setSalary(Double salary) {
		this.salary = salary;
	}

	public LocalDate getStart() {
		return start;
	}

	public void setStart(LocalDate start) {
		this.start = start;
	}

	@Override
	public String toString() {
		return "EmployeePayroll [id=" + id + ", name=" + name + ", salary=" + salary + ", start=" + start + "]";
	}
}
