package es.deusto.ingenieria.sd.ecoembes.facade;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.deusto.ingenieria.sd.ecoembes.dto.*;


import jakarta.validation.Valid;

@RestController
public class EcoembesAuthController {

    private final EcoembesAuthController authService;
    

    public EcoembesAuthController(EcoembesAuthController authService) {
        this.authService = authService;
    }

    // Login: returns a token
    @PostMapping("/auth/login")
    public ResponseEntity<String> login(@Valid @RequestBody LoginRequestDTO credentials) {
    	LoginRequestDTO loginCredentials = new LoginRequestDTO(credentials.getEmail(), credentials.getPassword());

    	ResponseEntity<String> token = authService.login(loginCredentials);

        if (token != null) {
            return new ResponseEntity<>(token.getToken(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    // Log out: removes the token
    @PostMapping("/auth/logout")
    public ResponseEntity<Void> logout(@RequestParam String token) {
        boolean loggedOut = authService.logout(token) != null;

        if (loggedOut) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    
}