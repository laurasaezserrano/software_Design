package es.deusto.ingenieria.sd.ecoembes.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

import es.deusto.ingenieria.sd.ecoembes.dto.LoginRequestDTO;
import es.deusto.ingenieria.sd.ecoembes.entity.Employee;

@Service
public class AuthService {

	// Employees registered in the system (key: email)
	private Map<String, Employee> employees = new HashMap<>();

	// Employees who are logged in (key: token). This is the server "state"
	private Map<String, Employee> loggedEmployees = new HashMap<>();

	public AuthService() {
		employees.put("ana@ecoembes.com", new Employee(1, "Ana", "ana@ecoembes.com", "1234"));
		employees.put("luis@ecoembes.com", new Employee(2, "Luis", "luis@ecoembes.com", "1234"));
	}

	// Returns the token if the credentials are correct
	public Optional<String> login(LoginRequestDTO credentials) {
		Employee employee = employees.get(credentials.getEmail());

		if (employee == null) {
			return Optional.empty();
		}
		if (!employee.checkPassword(credentials.getPassword())) {
			return Optional.empty();
		}

		String token = String.valueOf(System.currentTimeMillis());
		loggedEmployees.put(token, employee);
		return Optional.of(token);
	}

	// Removes the token. Returns false if the token did not exist
	public boolean logout(String token) {
		if (loggedEmployees.containsKey(token)) {
			loggedEmployees.remove(token);
			return true;
		}
		return false;
	}

	public Optional<Employee> getEmployeeByToken(String token) {
		return Optional.ofNullable(loggedEmployees.get(token));
	}
}
