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

	public boolean assignDumpstersToPlant(long plantId, List<Long> dumpsterIds, Employee employee) {
		RecyclingPlant plant = plantService.findPlant(plantId);
		if (plant == null) {
			return false;
		}
		if (dumpsterIds == null || dumpsterIds.isEmpty()) {
			return false;
		}

		List<Dumpster> selected = new ArrayList<>();
		for (Long id : dumpsterIds) {
			Dumpster dumpster = dumpsterService.findDumpster(id);
			if (dumpster == null) {
				return false;
			}
			selected.add(dumpster);
		}

		LocalDate today = LocalDate.now();
		float capacity = plant.getCapacityByDate(today);
		Assignment assignment = new Assignment(nextId, today, plant, employee, selected);
		float neededTons = assignment.getTotalContainers() * TONS_PER_CONTAINER;

		if (capacity == 0 || neededTons > capacity) {
			return false;
		}

		plant.setCapacity(today, capacity - neededTons);
		assignments.add(assignment);
		nextId++;

		System.out.println("Notification to " + plant.getName() + ": " + assignment.getTotalDumpsters()
				+ " dumpsters and " + assignment.getTotalContainers() + " containers. Assigned by "
				+ employee.getEmail());

		return true;
	}
}
