package es.deusto.ingenieria.sd.ecoembes.dto;

import es.deusto.ingenieria.sd.ecoembes.entity.FillLevel;

public class DumpsterCreationDTO {
	private String location;
	private String postalCode;
	private int capacity;
	private int numContainers;
	private FillLevel fillLevel;
	
	public DumpsterCreationDTO() {
		
	}
	
	public DumpsterCreationDTO(String location, String postalCode, int capacity, int numContainers,
			FillLevel fillLevel) {
		super();
		this.location = location;
		this.postalCode = postalCode;
		this.capacity = capacity;
		this.numContainers = numContainers;
		this.fillLevel = fillLevel;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getPostalCode() {
		return postalCode;
	}
	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}
	public int getCapacity() {
		return capacity;
	}
	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}
	public int getNumContainers() {
		return numContainers;
	}
	public void setNumContainers(int numContainers) {
		this.numContainers = numContainers;
	}
	public FillLevel getFillLevel() {
		return fillLevel;
	}
	public void setFillLevel(FillLevel fillLevel) {
		this.fillLevel = fillLevel;
	}

}
