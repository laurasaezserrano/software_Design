package es.deusto.ingenieria.sd.ecoembes.facade;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.validation.Valid;

public class DumpstersController {

	
	
	
	
	// Update dumpster info
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

    // Usage of a dumpster between two dates (yyyy-MM-dd)
    @GetMapping("/dumpsters/{dumpsterId}/usage")
    public ResponseEntity<List<DumpsterUsageDTO>> getDumpsterUsage(
            @PathVariable("dumpsterId") long dumpsterId,
            @RequestParam("token") String token,
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        Optional<Employee> employee = authService.getEmployeeByToken(token);

        if (employee.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }
    
}
