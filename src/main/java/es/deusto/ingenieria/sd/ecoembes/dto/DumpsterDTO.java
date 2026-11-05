package es.deusto.ingenieria.sd.ecoembes.dto;

import es.deusto.ingenieria.sd.ecoembes.entity.FillLevel;

public class DumpsterDTO {
	private long id;
	private String location;
	private String postalCode;
	private int capacity;
	private int numContainers;
	private FillLevel fillLevel;

	//CONSTRUCTORS
	public DumpsterDTO() {
	}

	public DumpsterDTO(long id, String location, String postalCode, int capacity, int numContainers, FillLevel fillLevel) {
		this.id = id;
		this.location = location;
		this.postalCode = postalCode;
		this.capacity = capacity;
		this.numContainers = numContainers;
		this.fillLevel = fillLevel;
	}

	//GETTERS AND SETTERS
	//Id
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	//Location
	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	//PostalCode
	public String getPostalCode() {
		return postalCode;
	}
	
	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}
	
	//Capacity
	public int getCapacity() {
		return capacity;
	}
	
	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}
	
	//NumContainers
	public int getNumContainers() {
		return numContainers;
	}
	
	public void setNumContainers(int numContainers) {
		this.numContainers = numContainers;
	}
	
	//FillLevel
	public FillLevel getFillLevel() {
		return fillLevel;
	}
	
	public void setFillLevel(FillLevel fillLevel) {
		this.fillLevel = fillLevel;
	}
	
}
