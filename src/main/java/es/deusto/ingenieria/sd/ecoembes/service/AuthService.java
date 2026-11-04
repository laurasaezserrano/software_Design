package es.deusto.ingenieria.sd.ecoembes.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import es.deusto.ingenieria.sd.ecoembes.dto.LoginRequestDTO;
import es.deusto.ingenieria.sd.ecoembes.entity.Employee;

@Service
public class AuthService {

	private Map<String, Employee> employees = new HashMap<>();

	private Map<String, Employee> loggedEmployees = new HashMap<>();

	public AuthService() {
		employees.put("laura@gmail.com", new Employee(1, "Laura", "laura@gmail.com", "1234"));
		employees.put("carmen@gmail.com", new Employee(2, "Carmen", "carmen@gmail.com", "1234"));
		employees.put("carmen@gmail.com", new Employee(2, "Carmen", "carmen@gmail.com", "1234"));
		employees.put("carmen@gmail.com", new Employee(2, "Carmen", "carmen@gmail.com", "1234"));
	}

	// Returns the token when the credentials ==  correct
	public ResponseEntity<String> login(LoginRequestDTO credentials) {
		Employee employee = employees.get(credentials.getEmail());

		if (employee == null) {
			//if user not found or password incorrect
			return ResponseEntity.status(401).body("Invalid credentials");
		}

		String token = String.valueOf(System.currentTimeMillis());
		loggedEmployees.put(token, employee);
		return ResponseEntity.ok(token);
	}
	
	public String login(String email, String password) {
		Employee employee = employees.get(email);

		if (employee == null || !employee.checkPassword(password)) {
			return null;
		}

		String token = String.valueOf(System.currentTimeMillis());
		loggedEmployees.put(token, employee);
		return token;
	}

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
