package net.java.springboot.models;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "Depart")
public class Department {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Column(name = "name")
	String name;
	
	@Column(name = "basicSalary")
	private double basicSalary;
	
	@OneToMany(targetEntity = Employee.class, cascade = CascadeType.ALL)
	@JoinColumn(name = "depart_fk", referencedColumnName = "id")
	Set<Employee> employee;
	
	//Default Constructor
	public Department() {
		super();
	}

	//Constructor with name and basic salary
	public Department(String name, double basicSalary) {
		super();
		this.name = name;
		this.basicSalary = basicSalary;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getBasicSalary() {
		return basicSalary;
	}

	public void setBasicSalary(double basicSalary) {
		this.basicSalary = basicSalary;
	}

	public Set<Employee> getEmployee() {
		return employee;
	}

	public void setEmployee(Set<Employee> employee) {
		this.employee = employee;
	}
}
