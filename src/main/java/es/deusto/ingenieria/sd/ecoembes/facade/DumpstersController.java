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

import es.deusto.ingenieria.sd.ecoembes.dto.DumpsterCreationDTO;
import es.deusto.ingenieria.sd.ecoembes.dto.PlantCapacityDTO;
import es.deusto.ingenieria.sd.ecoembes.dto.DumpsterDTO;
import es.deusto.ingenieria.sd.ecoembes.dto.DumpsterUsageDTO;
import es.deusto.ingenieria.sd.ecoembes.dto.RecyclingPlantDTO;
import es.deusto.ingenieria.sd.ecoembes.entity.Employee;
import es.deusto.ingenieria.sd.ecoembes.entity.FillLevel;
import es.deusto.ingenieria.sd.ecoembes.service.AssignmentService;
import es.deusto.ingenieria.sd.ecoembes.service.AuthService;
import es.deusto.ingenieria.sd.ecoembes.service.DumpsterService;
import es.deusto.ingenieria.sd.ecoembes.service.RecyclingPlantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
	
	@Operation(
	    summary = "Update dumpster status",
	    description = "Updates the fill level and the number of containers of an existing dumpster.",
	    responses = {
	         @ApiResponse(responseCode = "204", description = "Dumpster status updated successfully"),
	         @ApiResponse(responseCode = "400", description = "Bad request - invalid fill level or number of containers"),
	         @ApiResponse(responseCode = "404", description = "Dumpster not found")
	        })
    @PutMapping("/{dumpsterId}/status")
    public ResponseEntity<Void> updateDumpster( 
	    @Parameter(name = "dumpsterId", description = "Unique identifier of the dumpster", required = true, example = "1")
	    @PathVariable long dumpsterId,
	    @Parameter(name = "token", description = "Session token of the authenticated employee (obtained at login)", required = true)
        @RequestParam("token") String token,
        @Parameter(name = "fillLevel", description = "New fill level of the dumpster (GREEN = almost empty, ORANGE = almost full, RED = full)", required = true)
        @RequestParam FillLevel fillLevel,
        @Parameter(name = "numContainers", description = "New number of containers in the dumpster", required = true)
        @RequestParam int numContainers) {
		
		if (authService.getEmployeeByToken(token).isEmpty()) {
		    return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}
		
		return dumpsterService.updateDumpster(dumpsterId, fillLevel, numContainers);
	    
    }

    //POST create a new dumpster
    @Operation(
		summary = "Create a new dumpster", 
		description = "Creates a new dumpster with the specified fill level and number of containers.",
		responses = {
			@ApiResponse(responseCode = "201", description = "Dumpster created successfully"),
			@ApiResponse(responseCode = "401", description = "Unauthorized access - invalid token"),
			@ApiResponse(responseCode = "400", description = "Bad request - invalid dumpster data")
				})
    
    @PostMapping("/new-dumpster")
    public ResponseEntity<DumpsterDTO> createDumpster(
    		@Parameter(name = "token", description = "Session token of the authenticated employee (obtained at login)", required = true, example = "1a2b3c4d-5e6f-7890-abcd-ef1234567890")
            @RequestParam("token") String token,
//            @io.swagger.v3.oas.annotations.parameters.RequestBody(
//                description = "Information about the dumpster to be created", required = true)
            @RequestBody DumpsterCreationDTO dto){
    	
    	Optional<Employee> employee = authService.getEmployeeByToken(token);
    	
    	if (employee.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}
    	
    	DumpsterDTO newDumpster = dumpsterService.createDumpster(dto);
		
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
    
    @GetMapping("/all-dumpsters")
    public ResponseEntity<List<DumpsterDTO>> getAllDumpsters(
    		@Parameter(name = "token", description = "Session token of the authenticated employee (obtained at login)", required = true)
    		@RequestParam("token") String token) {
    	
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
    
    @GetMapping("/{dumpsterId}/usage")
    public ResponseEntity<List<DumpsterUsageDTO>> getDumpsterUsage(
            @PathVariable long dumpsterId,
            @RequestParam("token") String token,
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        Optional<Employee> employee = authService.getEmployeeByToken(token);

        if (employee.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        
        List<DumpsterUsageDTO> usage = dumpsterService.getDumpsterUsage(dumpsterId, startDate, endDate);
        if (usage == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(usage, HttpStatus.OK);
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
    public ResponseEntity<List<RecyclingPlantDTO>> getRecyclingPlants(
    		@Parameter(name = "token", description = "Session token of the authenticated employee (obtained at login)", required = true)
    		@RequestParam("token") String token) {
    	
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
    public ResponseEntity<PlantCapacityDTO> getRecyclingPlantCapacity(
    		@Parameter(name = "plantId", description = "Unique identifier of the recycling plant", required = true)
            @PathVariable long plantId,
            @Parameter(name = "token", description = "Session token of the authenticated employee (obtained at login)", required = true)
            @RequestParam("token") String token,
            @Parameter(name = "date", description = "Date for which the capacity is requested (ISO format yyyy-MM-dd)", required = true)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
    			
		Optional<Employee> employee = authService.getEmployeeByToken(token);

		if (employee.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}

		float capacity = plantService.getPlantCapacity(plantId, date);

		if (capacity >= 0) {
			return new ResponseEntity<>(new PlantCapacityDTO(plantId, date, capacity), HttpStatus.OK);
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
//			@Parameter(name = "plantId", description = "Unique identifier of the recycling plant", required = true)
            @PathVariable long plantId,
//            @Parameter(name = "token", description = "Session token of the authenticated employee (obtained at login)", required = true)
            @RequestParam("token") String token,
//            @io.swagger.v3.oas.annotations.parameters.RequestBody(
//                description = "List of dumpster Ids to assign to the plant", required = true)               
            @Valid @RequestBody List<Long> dumpsterIds) {
		Optional<Employee> employee = authService.getEmployeeByToken(token);

		if (employee.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}
		
		boolean success = assignmentService.assignDumpstersToPlant(plantId, dumpsterIds, employee.get());

		if (success) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} else {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
    
    // Check dumpster status
    @Operation(summary = "Check dumpster status",
    	    description = "Dumpsters in a postal code area on a given day.")
    @GetMapping("/status")
    public ResponseEntity<List<DumpsterDTO>> getDumpstersStatus(
            @RequestParam("token") String token,
            @RequestParam("postalCode") String postalCode,
            @RequestParam(name="date", defaultValue="2026-11-03") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        if (authService.getEmployeeByToken(token).isEmpty()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(dumpsterService.getDumpstersStatus(postalCode, date), HttpStatus.OK);
    }
    
}
