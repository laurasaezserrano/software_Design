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

import es.deusto.ingenieria.sd.ecoembes.dto.DumpsterDTO;
import es.deusto.ingenieria.sd.ecoembes.dto.DumpsterUsageDTO;
import es.deusto.ingenieria.sd.ecoembes.dto.RecyclingPlantDTO;
import es.deusto.ingenieria.sd.ecoembes.entity.Dumpster;
import es.deusto.ingenieria.sd.ecoembes.entity.Employee;
import es.deusto.ingenieria.sd.ecoembes.entity.FillLevel;
import es.deusto.ingenieria.sd.ecoembes.service.AssignmentService;
import es.deusto.ingenieria.sd.ecoembes.service.AuthService;
import es.deusto.ingenieria.sd.ecoembes.service.DumpsterService;
import es.deusto.ingenieria.sd.ecoembes.service.RecyclingPlantService;
import jakarta.validation.Valid;

public class DumpstersController {

	private final DumpsterService dumpsterService;
    private final RecyclingPlantService plantService;
    private final AssignmentService assignmentService;
    private final AuthService authService;

	
	public DumpstersController(DumpsterService dumpsterService, RecyclingPlantService plantService, AssignmentService assignmentService, AuthService authService) {
		this.dumpsterService = dumpsterService;
		this.plantService = plantService;
		this.assignmentService = assignmentService;
		this.authService = authService;
	}
	
    @PutMapping("/dumpsters/{dumpsterId}/status")
    public ResponseEntity<Void> updateDumpster(@PathVariable long dumpsterId,
    		@RequestBody FillLevel fillLevel,
    		@RequestBody int numContainers) {
		dumpsterService.updateDumpster(dumpsterId, fillLevel, numContainers);

		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		
    }

    @PostMapping("/dumpsters")
    public ResponseEntity<DumpsterDTO> createDumpster(@RequestParam String token,
                                                      @Valid @RequestBody FillLevel fillLevel, int numContainers, DumpsterDTO dto, Dumpster d) {
        Optional<Employee> employee = authService.getEmployeeByToken(token);

        if (employee.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        DumpsterDTO newDumpster = dumpsterService.createDumpster(fillLevel, numContainers, dto, d);

        return new ResponseEntity<>(newDumpster, HttpStatus.CREATED);
    }

    @GetMapping("/dumpsters")
    public ResponseEntity<List<DumpsterDTO>> getAllDumpsters(@RequestParam String token) {
        Optional<Employee> employee = authService.getEmployeeByToken(token);

        if (employee.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        return new ResponseEntity<>(dumpsterService.getAllDumpsters(), HttpStatus.OK);
    }

    @GetMapping("/dumpsters/{dumpsterId}/usage")
    public ResponseEntity<List<DumpsterUsageDTO>> getDumpsterUsage(
            @PathVariable long dumpsterId,
            @RequestParam String token,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        Optional<Employee> employee = authService.getEmployeeByToken(token);

        if (employee.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        
        return new ResponseEntity<List<DumpsterUsageDTO>>(HttpStatus.OK);
    }
    
    @GetMapping("/recycling-plants")
    public ResponseEntity<List<RecyclingPlantDTO>> getRecyclingPlants(@RequestParam String token) {
		Optional<Employee> employee = authService.getEmployeeByToken(token);

		if (employee.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}

		return new ResponseEntity<>(plantService.getAllPlants(), HttpStatus.OK);
	}
    
    @GetMapping("/recycling-plants/{plantId}/capacity")
    public ResponseEntity<Float> getRecyclingPlantCapacity(
			@PathVariable long plantId,
			@RequestParam String token) {
		Optional<Employee> employee = authService.getEmployeeByToken(token);

		if (employee.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}

		float capacity = plantService.getPlantCapacity(plantId);

		if (capacity >= 0) {
			return new ResponseEntity<>(HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
    
	@PostMapping("/recycling-plants/{plantId}/dumpsters")
	public ResponseEntity<Void> assignDumpstersToPlant(
			@PathVariable long plantId,
			@RequestParam String token,
			@Valid @RequestBody List<DumpsterDTO> dumpsters) {
		Optional<Employee> employee = authService.getEmployeeByToken(token);

		if (employee.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}

		List<Long> dumpsterIds = dumpsters.stream().map(DumpsterDTO::getId).toList();
		
		boolean success = assignmentService.assignDumpstersToPlant(plantId, dumpsterIds, employee.get());


		if (success) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} else {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
    
}
