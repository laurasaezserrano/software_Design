package es.deusto.ingenieria.sd.ecoembes.facade;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import es.deusto.ingenieria.sd.ecoembes.dto.LoginRequestDTO;
import es.deusto.ingenieria.sd.ecoembes.service.AuthService;
import jakarta.validation.Valid;

@RequestMapping("/auth")
@RestController
public class EcoembesAuthController {

    private final AuthService authService;

    public EcoembesAuthController(AuthService authService) {
        this.authService = authService;
    }

    // Login: returns a token
    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody LoginRequestDTO credentials) {
        String token = authService.login(credentials.getEmail(), credentials.getPassword());

        if (token != null) {
            return new ResponseEntity<>(token, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    // Log out: removes the token
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestParam String token) {
        boolean loggedOut = authService.logout(token);

        if (loggedOut) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }
}