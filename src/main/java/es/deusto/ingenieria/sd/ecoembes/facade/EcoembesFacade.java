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
public class EcoembesFacade {

    private final AuthService authService;
    private final DumpsterService dumpsterService;
    private final RecyclingPlantService plantService;
    private final AssignmentService assignmentService;

    public EcoembesFacade(AuthService authService, DumpsterService dumpsterService,
                          RecyclingPlantService plantService, AssignmentService assignmentService) {
        this.authService = authService;
        this.dumpsterService = dumpsterService;
        this.plantService = plantService;
        this.assignmentService = assignmentService;
    }

    // Login: returns a token
    @PostMapping("/auth/login")
    public ResponseEntity<String> login(@Valid @RequestBody LoginDTO credentials) {
        Optional<String> token = authService.login(credentials.email(), credentials.password());

        if (token.isPresent()) {
            return new ResponseEntity<>(token.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    // Logout: removes the token
    @PostMapping("/auth/logout")
    public ResponseEntity<Void> logout(@RequestParam("token") String token) {
        boolean loggedOut = authService.logout(token);

        if (loggedOut) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    // Update dumpster info (called by the simulated sensor, no token)
    @PutMapping("/dumpsters/{dumpsterId}/status")
    public ResponseEntity<Void> updateDumpster(@PathVariable("dumpsterId") long dumpsterId,
                                               @Valid @RequestBody DumpsterUpdateDTO update) {
        dumpsterService.updateDumpster(dumpsterId, update);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Create a new dumpster
    @PostMapping("/dumpsters")
    public ResponseEntity<DumpsterDTO> createDumpster(@RequestParam("token") String token,
                                                      @Valid @RequestBody DumpsterCreationDTO dto) {
        Optional<Employee> employee = authService.getEmployeeByToken(token);

        if (employee.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        DumpsterDTO newDumpster = dumpsterService.createDumpster(dto);

        return new ResponseEntity<>(newDumpster, HttpStatus.CREATED);
    }

    // Get all dumpsters
    @GetMapping("/dumpsters")
    public ResponseEntity<List<DumpsterDTO>> getAllDumpsters(@RequestParam("token") String token) {
        Optional<Employee> employee = authService.getEmployeeByToken(token);

        if (employee.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        return new ResponseEntity<>(dumpsterService.getAllDumpsters(), HttpStatus.OK);
    }

    // Query the usage of a dumpster between two dates (yyyy-MM-dd)
    @GetMapping("/dumpsters/{dumpsterId}/usage")
    public ResponseEntity<List<DumpsterUsageDTO>> getDumpsterUsage(
            @PathVariable("dumpsterId") long dumpsterId,
            @RequestParam("token") String token,
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        Optional<Employee> employee = authService.getEmployeeByToken(token);

        if (employee.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);