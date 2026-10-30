package es.deusto.ingenieria.sd.ecoembes.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import es.deusto.ingenieria.sd.ecoembes.dto.LoginRequestDTO;
import es.deusto.ingenieria.sd.ecoembes.dto.LoginResponseDTO;

@Service
public class EcoembesAppService {

    private final Map<String, String> loggedEmployees = new HashMap<>();

    public LoginResponseDTO login(LoginRequestDTO request) {

        String token = String.valueOf(System.currentTimeMillis());

        loggedEmployees.put(token, request.getEmail());

        return new LoginResponseDTO(token);
    }
}