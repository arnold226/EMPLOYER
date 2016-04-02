/**
 * This class holds employment information
 * 
 * @author arnold
 */

package com.arnold;

public class EmploymentInformation {

	private String first_name;
	private String last_name;
	private String salary;
	private String start_date;
	
	
	/**
	 * No args constructor
	 */
	public EmploymentInformation(){
		
	}
	
	
	/**
	 * args contructor
	 * @param first_name
	 * @param last_name
	 * @param salary
	 * @param start_date
	 */
	public EmploymentInformation(String first_name, String last_name, String salary, String start_date) {
		super();
		this.first_name = first_name;
		this.last_name = last_name;
		this.salary = salary;
		this.start_date = start_date;
	}

	
	//getters and setters
	public String getFirst_name() {
		return first_name;
	}


	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}


	public String getLast_name() {
		return last_name;
	}


	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}


	public String getSalary() {
		return salary;
	}


	public void setSalary(String salary) {
		this.salary = salary;
	}


	public String getStart_date() {
		return start_date;
	}


	public void setStart_date(String start_date) {
		this.start_date = start_date;
	}

	
	/**
	 * toString method with json format
	 */
	@Override
	public String toString() {
		return "{\"first_name\":\"" + first_name + "\", \"last_name\":\"" + last_name + "\", \"salary\":\"" + salary
				+ "\", \"start_employment_date\":\"" + start_date + "\"}";
	}
	
	
	
	
	
	

}
