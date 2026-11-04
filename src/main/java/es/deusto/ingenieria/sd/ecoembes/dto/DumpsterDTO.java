package es.deusto.ingenieria.sd.ecoembes.dto;

import es.deusto.ingenieria.sd.ecoembes.entity.FillLevel;

public class DumpsterDTO {
	private long id;
	private String location;
	private String status;
	private String postalCode;
	private int capacity;
	private int numContainers;
	private FillLevel fillLevel;

	public DumpsterDTO() {
	}

	public DumpsterDTO(long id, String location, String status) {
		this.id = id;
		this.location = location;
		this.status = status;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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
