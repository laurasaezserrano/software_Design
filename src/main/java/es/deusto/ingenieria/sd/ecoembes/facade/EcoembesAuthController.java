package es.deusto.ingenieria.sd.ecoembes.facade;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import es.deusto.ingenieria.sd.ecoembes.dto.*;


import jakarta.validation.Valid;

@RequestMapping("/auth")
@RestController
public class EcoembesAuthController {

    private final EcoembesAuthController authService;
    

    public EcoembesAuthController(EcoembesAuthController authService) {
        this.authService = authService;
    }

    // Login: returns a token
    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody LoginRequestDTO credentials) {
    	LoginRequestDTO loginCredentials = new LoginRequestDTO(credentials.getEmail(), credentials.getPassword());

    	ResponseEntity<String> token = authService.login(loginCredentials);

        if (token != null) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    // Log out: removes the token
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestParam String token) {
        boolean loggedOut = authService.logout(token) != null;

        if (loggedOut) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    
}