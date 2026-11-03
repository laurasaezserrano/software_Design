package es.deusto.ingenieria.sd.ecoembes.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import es.deusto.ingenieria.sd.ecoembes.dto.RecyclingPlantCapacityDTO;
import es.deusto.ingenieria.sd.ecoembes.dto.RecyclingPlantDTO;
import es.deusto.ingenieria.sd.ecoembes.entity.RecyclingPlant;

@Service
public class RecyclingPlantService {

	// Simulated plants (in the next prototypes the data will come from the external servers)
	private List<RecyclingPlant> plants = new ArrayList<>();

	public RecyclingPlantService() {
		RecyclingPlant plasSB = new RecyclingPlant(1, "PlasSB Ltd.", "Bilbao");
		RecyclingPlant contSocket = new RecyclingPlant(2, "ContSocket Ltd.", "Vitoria");

		// Capacity (in tons) for the next 10 days
		for (int i = 0; i < 10; i++) {
			plasSB.setCapacity(LocalDate.now().plusDays(i), 50.0);
			contSocket.setCapacity(LocalDate.now().plusDays(i), 30.0);
		}

		plants.add(plasSB);
		plants.add(contSocket);
	}

	public List<RecyclingPlantDTO> getAllPlants() {
		List<RecyclingPlantDTO> result = new ArrayList<>();
		for (RecyclingPlant plant : plants) {
			result.add(new RecyclingPlantDTO(plant.getId(), plant.getName(), plant.getLocation()));
		}
		return result;
	}

	// Capacity of the plant for today
	public Optional<RecyclingPlantCapacityDTO> getPlantCapacity(long plantId) {
		RecyclingPlant plant = findPlant(plantId);
		if (plant == null) {
			return Optional.empty();
		}

		LocalDate today = LocalDate.now();
		Double capacity = plant.getCapacity(today);
		if (capacity == null) {
			return Optional.empty();
		}

		return Optional.of(new RecyclingPlantCapacityDTO(plantId, today, capacity));
	}

	// Returns the plant with that id, or null if it does not exist
	public RecyclingPlant findPlant(long id) {
		for (RecyclingPlant plant : plants) {
			if (plant.getId() == id) {
				return plant;
			}
		}
		return null;
	}
}
