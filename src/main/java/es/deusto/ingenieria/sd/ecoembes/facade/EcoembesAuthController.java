package es.deusto.ingenieria.sd.ecoembes.facade;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import es.deusto.ingenieria.sd.ecoembes.dto.*;
import es.deusto.ingenieria.sd.ecoembes.entity.Employee;
import es.deusto.ingenieria.sd.ecoembes.service.AssignmentService;
import es.deusto.ingenieria.sd.ecoembes.service.AuthService;
import es.deusto.ingenieria.sd.ecoembes.service.DumpsterService;
import es.deusto.sd.ingenieria. ecoembes.service.RecyclingPlantService;

import jakarta.validation.Valid;

@RestController
public class EcoembesAuthController {

    private final EcoembesAuthController authService;
    private final DumpsterService dumpsterService;
    private final RecyclingPlantService plantService;
    private final AssignmentService assignmentService;

    public EcoembesAuthController(EcoembesAuthController authService, DumpsterService dumpsterService,
                          RecyclingPlantService plantService, AssignmentService assignmentService) {
        this.authService = authService;
        this.dumpsterService = dumpsterService;
        this.plantService = plantService;
        this.assignmentService = assignmentService;
    }

    // Login: returns a token
    @PostMapping("/auth/login")
    public ResponseEntity<String> login(@Valid @RequestBody LoginRequestDTO credentials) {
        Optional<String> token = authService.login(credentials.getEmail(), credentials.getPassword());

        if (token.isPresent()) {
            return new ResponseEntity<>(token.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    // Log out: removes the token
    @PostMapping("/auth/logout")
    public ResponseEntity<Void> logout(@RequestParam("token") String token) {
        boolean loggedOut = authService.logout(token);

        if (loggedOut) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    
}