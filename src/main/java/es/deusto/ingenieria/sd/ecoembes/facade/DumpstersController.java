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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/dumpsters")
@Tag(name = "Dumpsters and Recycling Plants", description = "Operations related to dumpsters and recycling plants")
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

    //POST create a new dumpster
    @Operation(
		summary = "Create a new dumpster", 
		description = "Creates a new dumpster with the specified fill level and number of containers.",
		responses = {
			@ApiResponse(responseCode = "201", description = "Dumpster created successfully"),
			@ApiResponse(responseCode = "401", description = "Unauthorized access - invalid token")
				})
    
    @PostMapping("/dumpsters")
    public ResponseEntity<DumpsterDTO> createDumpster(
    		@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Authentication token of the employee", required = true)
    		@RequestBody String token,
    		@Parameter(description = "Fill level of the dumpster", required = true)
    		@PathVariable FillLevel fillLevel,
    		@Parameter(description = "Number of containers in the dumpster", required = true)
    		@PathVariable int numContainers,
    		@Parameter(description = "Details of the dumpster to be created", required = true)
    		@RequestBody DumpsterDTO dto,
    		@Parameter(description = "Details of the dumpster to be created", required = true)
    		@RequestBody Dumpster d) {
        Optional<Employee> employee = authService.getEmployeeByToken(token);

        if (employee.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        DumpsterDTO newDumpster = dumpsterService.createDumpster(fillLevel, numContainers, dto, d);

        return new ResponseEntity<>(newDumpster, HttpStatus.CREATED);
    }
    
    //GET all dumpsters
    @Operation(
    	summary = "Get all dumpsters", 
    	description = "Returns a list of all dumpsters in the system.",
    	responses = {
			@ApiResponse(responseCode = "200", description = "Successfully retrieved list of dumpsters"),
			@ApiResponse(responseCode = "401", description = "Unauthorized access - invalid token")
		})
    
    @GetMapping("/dumpsters")
    public ResponseEntity<List<DumpsterDTO>> getAllDumpsters(@RequestParam String token) {
    	// get all dumpsters possibly with authentication check
		Optional<Employee> employee = authService.getEmployeeByToken(token);

		if (employee.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}

		List<DumpsterDTO> dumpsters = dumpsterService.getAllDumpsters();
		return new ResponseEntity<>(dumpsters, HttpStatus.OK);
    }

    //GET dumpster usage data
    @Operation(
		summary = "Get dumpster usage", 
		description = "Returns the usage data for a specific dumpster within a given date range.",
		responses =  {
			@ApiResponse(responseCode = "200", description = "Successfully retrieved dumpster usage data"),
			@ApiResponse(responseCode = "401", description = "Unauthorized access - invalid token"),
			@ApiResponse(responseCode = "404", description = "Dumpster not found")
		})
    
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
    
    //GET all recycling plants
    @Operation(
    	summary = "Get recycling plants",
    	description = "Returns a list of all recycling plants in the system.",
    	responses = {
			@ApiResponse(responseCode = "200", description = "Successfully retrieved list of recycling plants"),
			@ApiResponse(responseCode = "401", description = "Unauthorized access - invalid token")
					})
    @GetMapping("/recycling-plants")
    public ResponseEntity<List<RecyclingPlantDTO>> getRecyclingPlants(@RequestParam String token) {
		Optional<Employee> employee = authService.getEmployeeByToken(token);

		if (employee.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}

		return new ResponseEntity<>(plantService.getAllPlants(), HttpStatus.OK);
	}
    
    //GET recycling plant capacity
    @Operation(
		summary = "Get recycling plant capacity",
		description = "Returns the capacity of a specific recycling plant.",
		responses = {
			@ApiResponse(responseCode = "200", description = "Successfully retrieved recycling plant capacity"),
			@ApiResponse(responseCode = "401", description = "Unauthorized access - invalid token"),
			@ApiResponse(responseCode = "404", description = "Recycling plant not found")
		})
    
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
    
    //POST assign dumpsters to recycling plant
    @Operation(
    	summary = "Assign dumpsters to recycling plant",
    	description = "Assigns a list of dumpsters to a specific recycling plant.",
    	responses = {
				@ApiResponse(responseCode = "204", description = "Successfully assigned dumpsters to recycling plant"),
				@ApiResponse(responseCode = "400", description = "Bad request - invalid dumpster IDs or plant ID"),
				@ApiResponse(responseCode = "401", description = "Unauthorized access - invalid token")
					})
    
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
