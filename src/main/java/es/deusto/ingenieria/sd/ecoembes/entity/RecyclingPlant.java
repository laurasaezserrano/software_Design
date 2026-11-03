package es.deusto.ingenieria.sd.ecoembes.entity;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class RecyclingPlant {

	private long id;
	private String name;
	private String location;
	private Map<LocalDate, Double> capacityByDate; // tons

	//CONSTRUCTORS
	public RecyclingPlant() {
		this.capacityByDate = new HashMap<>();
	}

	public RecyclingPlant(long id, String name, String location) {
		this();
		this.id = id;
		this.name = name;
		this.location = location;
	}

	//GETTERS AND SETTERS
	//Id
	public long getId() { 
		return id; 
		}
	
	public void setId(long id) { 
		this.id = id; 
		}

	//Name
	public String getName() { 
		return name; 
		}
	
	public void setName(String name) { 
		this.name = name; 
		}

	//Location
	public String getLocation() { 
		return location; 
		}
	
	public void setLocation(String location) { 
		this.location = location; 
		}

	//Capacity by date
	public Map<LocalDate, Double> getCapacityByDate() { 
		return capacityByDate; 
		}
	
	public void setCapacityByDate(Map<LocalDate, Double> capacityByDate) {
		this.capacityByDate = capacityByDate != null ? capacityByDate : new HashMap<>();
	}

	//Capacity for a given date
	public void setCapacity(LocalDate date, double tons) { 
		capacityByDate.put(date, tons); 
		}

	
	public Double getCapacity(LocalDate date) { 
		return capacityByDate.get(date); 
		}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || getClass() != obj.getClass()) return false;
		return id == ((RecyclingPlant) obj).id;
	}

	@Override
	public int hashCode() { return Objects.hash(id); }
}
