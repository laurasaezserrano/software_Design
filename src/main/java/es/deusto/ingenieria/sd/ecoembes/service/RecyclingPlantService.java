package es.deusto.ingenieria.sd.ecoembes.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import es.deusto.ingenieria.sd.ecoembes.dto.RecyclingPlantDTO;
import es.deusto.ingenieria.sd.ecoembes.entity.RecyclingPlant;

@Service
public class RecyclingPlantService {

	private List<RecyclingPlant> plants = new ArrayList<>();

	public RecyclingPlantService() {
		RecyclingPlant plasSB = new RecyclingPlant(1, "PlasSB Ltd.", "Bilbao");
		RecyclingPlant contSocket = new RecyclingPlant(2, "ContSocket Ltd.", "Vitoria");
		
		plasSB.setDate(LocalDate.now());
		contSocket.setDate(LocalDate.now());
		

		for (int i = 0; i < 10; i++) {
			plasSB.setCapacity(LocalDate.now().plusDays(i), 50);
			contSocket.setCapacity(LocalDate.now().plusDays(i), 30);
		}

		plants.add(plasSB);
		plants.add(contSocket);
	}

	public List<RecyclingPlantDTO> getAllPlants() {
		List<RecyclingPlantDTO> result = new ArrayList<>();
		for (RecyclingPlant plant : plants) {
			result.add(new RecyclingPlantDTO(plant.getId(), plant.getName(), plant.getLocation(), plant.getCapacityByDate(plant.getDate()), plant.getDate()));
		}
		return result;
	}

	public float getPlantCapacity(long plantId, LocalDate date) {
		RecyclingPlant plant = findPlant(plantId);
		if (plant == null) {
			return -1; // Plant not found
		}

		float capacity = plant.getCapacityByDate(date);
		return capacity;
	}

	public RecyclingPlant findPlant(long id) {
		for (RecyclingPlant plant : plants) {
			if (plant.getId() == id) {
				return plant;
			}
		}
		return null;
	}
}
