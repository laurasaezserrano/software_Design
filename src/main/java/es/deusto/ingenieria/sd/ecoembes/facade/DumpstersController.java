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
import es.deusto.ingenieria.sd.ecoembes.dto.RecyclingPlantDTO;
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
	
	// Update dumpster info by defining num of containers and fill level
    @PutMapping("/dumpsters/{dumpsterId}/status")
    public ResponseEntity<Void> updateDumpster(@PathVariable("dumpsterId") long dumpsterId,
    		@RequestBody FillLevel fillLevel,
    		@RequestBody int numContainers) {
		dumpsterService.updateDumpster(dumpsterId, fillLevel, numContainers);

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
    
    // Get recycling plants
    @GetMapping("/recycling-plants")
    public ResponseEntity<List<RecyclingPlantDTO>> getRecyclingPlants(@RequestParam("token") String token) {
		Optional<Employee> employee = authService.getEmployeeByToken(token);

		if (employee.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}

		return new ResponseEntity<>(plantService.getAllPlants(), HttpStatus.OK);
	}
    
    // Get recycling plant capacity
	@GetMapping("/recycling-plants/{plantId}/capacity")
	public ResponseEntity<RecyclingPlantCapacityDTO> getRecyclingPlantCapacity(
			@PathVariable("plantId") long plantId,
			@RequestParam("token") String token) {
		Optional<Employee> employee = authService.getEmployeeByToken(token);

		if (employee.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}

		Optional<RecyclingPlantCapacityDTO> capacity = plantService.getPlantCapacity(plantId);

		if (capacity.isPresent()) {
			return new ResponseEntity<>(capacity.get(), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	// Assign dumpsters to specified plant
	@PostMapping("/recycling-plants/{plantId}/dumpsters")
	public ResponseEntity<Void> assignDumpstersToPlant(
			@PathVariable("plantId") long plantId,
			@RequestParam("token") String token,
			@Valid @RequestBody List<DumpsterDTO> dumpsters) {
		Optional<Employee> employee = authService.getEmployeeByToken(token);

		if (employee.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}

		boolean success = assignmentService.assignDumpstersToPlant(plantId, assignmentDTO.getDumpsterIds());

		if (success) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} else {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
    
}
