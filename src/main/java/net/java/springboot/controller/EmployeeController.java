package net.java.springboot.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.java.springboot.models.Department;
import net.java.springboot.models.Employee;
import net.java.springboot.projectException.ResourceNotFoundException;
import net.java.springboot.repositories.DepartmentRepository;
import net.java.springboot.repositories.EmployeeRepository;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/v1/")
public class EmployeeController {
	@Autowired
	EmployeeRepository employeeRepository;
	
	@Autowired
	private DepartmentRepository departmentRepository;
	
	//Get all employees
	@GetMapping("/getEmployees")
	public List<Employee> getAllEmployee() {
		return employeeRepository.findAll();
	}
	
	//Get employee by id, throw an exception if could not find Employee with given id
	@GetMapping("/getEmployee/{id}")
	public ResponseEntity<Employee> getEmployeeById(@PathVariable long id) {
		Employee employee = employeeRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Employee not exist with id: " + id));
		return ResponseEntity.ok(employee);
	}
	
	//Add employee to repository, *Note that this employee don't work for any department, depart attribute will be default: 0
	@PostMapping("/addEmployee")
	public Employee createEmployee(@RequestBody Employee employee) {
			return employeeRepository.save(employee);
	}
	
	//Add employee to department
	@PutMapping("/addEmployeeToDepart/{departId}/{employeeId}")
	public ResponseEntity<Employee> addEmployee(@PathVariable Long departId,@PathVariable Long employeeId) {
		//Find department with given id, throw an exception if could not find Department with given id
		Department departmentFound = departmentRepository.findById(departId)
				.orElseThrow(() -> new ResourceNotFoundException("Depart not exist with id: " + departId));	
		//Find employee with given id, throw an exception if could not find Employee with given id
		Employee employeeFound = employeeRepository.findById(employeeId)
				.orElseThrow(() -> new ResourceNotFoundException("Employee not exist with id: " + employeeId));
		
		if(departmentFound.getBasicSalary() < employeeFound.getSalary())
				throw new ResourceNotFoundException("Employee's salary is more than basic depart's salary");
		
		//Change depart attribute
		employeeFound.setDepart(departmentFound.getId());
		
		//Add to depart
		departmentFound.getEmployee().add(employeeFound);
		departmentRepository.save(departmentFound);
		return ResponseEntity.ok(employeeFound);
	}
	
	//Remove employee from department
	@PutMapping("/removeEmployeeFromDepart/{departId}/{employeeId}")
	public ResponseEntity<Employee> removeEmployee(@PathVariable Long departId,@PathVariable Long employeeId) {
		//Find department with given id, throw an exception if could not find Department with given id
		Department departmentFound = departmentRepository.findById(departId)
				.orElseThrow(() -> new ResourceNotFoundException("Depart not exist with id: " + departId));	
		//Find employee with given id, throw an exception if could not find Employee with given id
		Employee employeeFound = employeeRepository.findById(employeeId)
				.orElseThrow(() -> new ResourceNotFoundException("Employee not exist with id: " + employeeId));
			
		if(departmentFound.getBasicSalary() < employeeFound.getSalary())
				throw new ResourceNotFoundException("Employee's salary is more than basic depart's salary");
			
		//Change depart attribute to 0
		employeeFound.setDepart(0);
			
		//Add to depart
		departmentFound.getEmployee().remove(employeeFound);
		departmentRepository.save(departmentFound);
		return ResponseEntity.ok(employeeFound);
	}
	
	//Update employee, throw an exception if could not find Employee with given id
	@PutMapping("/updateEmployee/{id}")
	public ResponseEntity<Employee> updateEmployee(@PathVariable Long id, @RequestBody Employee employeeDetails) {
		Employee employee = employeeRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Employee not exist with id: " + id));
			
		employee.setFirstName(employeeDetails.getFirstName());
		employee.setLastName(employeeDetails.getLastName());
		employee.setBirthDay(employeeDetails.getBirthDay());
		employee.setEmail(employeeDetails.getEmail());
		employee.setGender(employeeDetails.getGender());
		employee.setPhone(employeeDetails.getPhone());
		employee.setRole(employeeDetails.getRole());
		employee.setSalary(employeeDetails.getSalary());
			
		Employee updatedEmployee = employeeRepository.save(employee);
		return ResponseEntity.ok(updatedEmployee);
	}
	
	//Delete all employee in repository
	@DeleteMapping("/deleteAllEmployee")
	public ResponseEntity<Map<String, Boolean>> deleteAllEmployee() {
		employeeRepository.deleteAll();
			
		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted all employees", Boolean.TRUE);
		return ResponseEntity.ok(response);
	}
	
	//Delete an employee, throw an exception if could not find Employee with given id
	@DeleteMapping("/deleteEmployee/{id}")
	public ResponseEntity<Map<String, Boolean>> deleteEmployee(@PathVariable Long id) {
		Employee employee = employeeRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Employee not exist with id: " + id));
		employeeRepository.delete(employee);
		
		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted employee with id: " + employee.getId(), Boolean.TRUE);
		return ResponseEntity.ok(response);
	}
}
