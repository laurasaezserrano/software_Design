package es.deusto.ingenieria.sd.ecoembes.dto;

import java.time.LocalDate;

public class PlantCapacityDTO {

	private long plantId;
	private LocalDate date;
	private double capacityTons;

	//CONSTRUCTORS
	public PlantCapacityDTO() {
		
	}

	public PlantCapacityDTO(long plantId, LocalDate date, double capacityTons) {
		this.plantId = plantId;
		this.date = date;
		this.capacityTons = capacityTons;
	}

	//GETTERS AND SETTERS
	//Plant ID
	public long getPlantId() { 
		return plantId; 
		}
	
	public void setPlantId(long plantId) { 
		this.plantId = plantId; 
		}
	
	//Date
	public LocalDate getDate() { 
		return date; 
		}
	
	public void setDate(LocalDate date) { 
		this.date = date; 
		}
	
	//Capacity in tons
	public double getCapacityTons() { return 
			capacityTons; 
	}
	
	public void setCapacityTons(double capacityTons) { 
		this.capacityTons = capacityTons; 
		}
}
