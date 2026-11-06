package es.deusto.ingenieria.sd.ecoembes.dto;

import java.time.LocalDate;

public class RecyclingPlantDTO {
	private long id;
	private String name;
	private String location;
	private float capacity;
	private LocalDate date;

	//CONSTRUCTORS
	public RecyclingPlantDTO() {
	}

	public RecyclingPlantDTO(long id, String name, String location, float capacity, LocalDate date) {
		this.id = id;
		this.name = name;
		this.location = location;
		this.capacity = capacity;
		this.date = date;
	}

	public RecyclingPlantDTO(long id, String name, String location) {
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

	//Capacity
	public float getCapacity() {
		return capacity;
	}

	public void setCapacity(float capacity) {
		this.capacity = capacity;
	}
	
	//Date
	public LocalDate getDate() {
		return date;
	}
	
	public void setDate(LocalDate date) {
		this.date = date;
	}
	

}
