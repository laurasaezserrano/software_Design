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
		employees.put("ana@ecoembes.com", new Employee(1, "Ana", "ana@ecoembes.com", "1234"));
		employees.put("luis@ecoembes.com", new Employee(2, "Luis", "luis@ecoembes.com", "1234"));
	}

	// Returns the token when the credentials correct
	public ResponseEntity<String> login(LoginRequestDTO credentials) {
		Employee employee = employees.get(credentials.getEmail());

		if (employee == null) {
			return ResponseEntity.status(401).body("Invalid credentials");
		}
		
//		if (!employee.checkPassword(credentials.getPassword())) {
//			return Optional.empty();
//		}

		String token = String.valueOf(System.currentTimeMillis());
		loggedEmployees.put(token, employee);
		return ResponseEntity.ok(token);
	}
	
	// Login with email and password, returns a token if successful, otherwise returns null
	public String login(String email, String password) {
		Employee employee = employees.get(email);

		if (employee == null || !employee.checkPassword(password)) {
			return null;
		}

		String token = String.valueOf(System.currentTimeMillis());
		loggedEmployees.put(token, employee);
		return token;
	}

	// Removes the token and returns false if token not exist
	
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
