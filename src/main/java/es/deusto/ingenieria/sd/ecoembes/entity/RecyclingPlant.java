package es.deusto.ingenieria.sd.ecoembes.entity;

import java.time.LocalDate;
import java.util.Objects;

public class RecyclingPlant {

	private long id;
	private String name;
	private String location;
	private float capacity;
	private LocalDate date;

	//CONSTRUCTORS
	public RecyclingPlant(long id, String name, String location, float capacity, LocalDate date) {
		this.id = id;
		this.name = name;
		this.location = location;
		this.setCapacity(capacity);
		this.setDate(LocalDate.now());
	}

	public RecyclingPlant(long id, String name, String location) {
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


	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || getClass() != obj.getClass()) return false;
		return id == ((RecyclingPlant) obj).id;
	}

	@Override
	public int hashCode() { return Objects.hash(id); }

	public float getCapacity() {
		return capacity;
	}
	
	public float getCapacityByDate(LocalDate date) {
		if (this.date.equals(date)) {
			return capacity;
		} else {
			return 0;
		}
	}

	public void setCapacity(float capacity) {
		this.capacity = capacity;
	}
	
	public void setCapacity(LocalDate date, float capacity) {
		this.date = date;
		this.capacity = capacity;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}
}
