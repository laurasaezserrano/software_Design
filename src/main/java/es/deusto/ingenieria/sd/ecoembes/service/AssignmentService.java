package es.deusto.ingenieria.sd.ecoembes.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import es.deusto.ingenieria.sd.ecoembes.entity.Assignment;
import es.deusto.ingenieria.sd.ecoembes.entity.Dumpster;
import es.deusto.ingenieria.sd.ecoembes.entity.Employee;
import es.deusto.ingenieria.sd.ecoembes.entity.RecyclingPlant;

@Service
public class AssignmentService {

	// Simulation: we suppose that each container weighs 0.1 tons
	private static final double TONS_PER_CONTAINER = 0.1;

	private DumpsterService dumpsterService;
	private RecyclingPlantService plantService;

	private List<Assignment> assignments = new ArrayList<>();
	private long nextId = 1;

	public AssignmentService(DumpsterService dumpsterService, RecyclingPlantService plantService) {
		this.dumpsterService = dumpsterService;
		this.plantService = plantService;
	}

	// Returns true if the assignment is done. Returns false if the plant or a dumpster
	// does not exist, or if the plant does not have enough capacity
	public boolean assignDumpstersToPlant(long plantId, List<Long> dumpsterIds, Employee employee) {
		RecyclingPlant plant = plantService.findPlant(plantId);
		if (plant == null) {
			return false;
		}
		if (dumpsterIds == null || dumpsterIds.isEmpty()) {
			return false;
		}

		// Look for all the dumpsters
		List<Dumpster> selected = new ArrayList<>();
		for (Long id : dumpsterIds) {
			Dumpster dumpster = dumpsterService.findDumpster(id);
			if (dumpster == null) {
				return false;
			}
			selected.add(dumpster);
		}

		// Check that there is enough capacity today
		LocalDate today = LocalDate.now();
		Double capacity = plant.getCapacity(today);
		Assignment assignment = new Assignment(nextId, today, plant, employee, selected);
		double neededTons = assignment.getTotalContainers() * TONS_PER_CONTAINER;

		if (capacity == null || neededTons > capacity) {
			return false;
		}

		// Save the assignment and reduce the capacity of the plant
		plant.setCapacity(today, capacity - neededTons);
		assignments.add(assignment);
		nextId++;

		// Simulated notification to the plant
		System.out.println("Notification to " + plant.getName() + ": " + assignment.getTotalDumpsters()
				+ " dumpsters and " + assignment.getTotalContainers() + " containers. Assigned by "
				+ employee.getEmail());

		return true;
	}
}
