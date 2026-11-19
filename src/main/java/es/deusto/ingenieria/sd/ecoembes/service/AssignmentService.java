package es.deusto.ingenieria.sd.ecoembes.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;

import es.deusto.ingenieria.sd.ecoembes.entity.Assignment;
import es.deusto.ingenieria.sd.ecoembes.entity.Dumpster;
import es.deusto.ingenieria.sd.ecoembes.entity.Employee;
import es.deusto.ingenieria.sd.ecoembes.entity.RecyclingPlant;

@Service
public class AssignmentService {

	// Simulation:Suppose that each weighs 0.1 tons (Claude idea for the simulation)
	private static final float TONS_PER_CONTAINER = 0.1f;

	private DumpsterService dumpsterService;
	private RecyclingPlantService plantService;

	private List<Assignment> assignments = new ArrayList<>();
	private long nextId = 1;

	public AssignmentService(DumpsterService dumpsterService, RecyclingPlantService plantService) {
		this.dumpsterService = dumpsterService;
		this.plantService = plantService;
	}

	// Returns the id of the new assignment
	public long assignDumpstersToPlant(long plantId, List<Long> dumpsterIds, Employee employee) {
	    if (dumpsterIds == null || dumpsterIds.isEmpty()) {
	        throw new IllegalArgumentException("No dumpsters given");
	    }
	    RecyclingPlant plant = plantService.findPlant(plantId);
	    if (plant == null) {
	        throw new NoSuchElementException("Recycling plant not found");
	    }

	    List<Dumpster> selected = new ArrayList<>();
	    for (Long id : dumpsterIds) {
	        Dumpster dumpster = dumpsterService.findDumpster(id);
	        if (dumpster == null) {
	            throw new NoSuchElementException("Dumpster " + id + " not found");
	        }
	        selected.add(dumpster);
	    }

	    LocalDate today = LocalDate.now();
	    float capacity = plant.getCapacityByDate(today);
	    Assignment assignment = new Assignment(nextId, today, plant, employee, selected);
	    float neededTons = assignment.getTotalContainers() * TONS_PER_CONTAINER;

	    // Check if plant has enough capacity
	    if (neededTons > capacity) {
	        throw new IllegalStateException("Not enough capacity in the recycling plant");
	    }

	    // Update capacity, notify plant, and record assignment
	    plant.setCapacity(today, capacity - neededTons);
	    plant.notifyAssignment(assignment);
	    assignments.add(assignment);
	    nextId++;

	    return assignment.getId();
	}
}
