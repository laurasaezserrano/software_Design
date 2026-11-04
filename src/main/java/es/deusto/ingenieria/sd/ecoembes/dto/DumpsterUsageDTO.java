package es.deusto.ingenieria.sd.ecoembes.dto;

import java.time.LocalDate;

import es.deusto.ingenieria.sd.ecoembes.entity.FillLevel;

public class DumpsterUsageDTO {
	private LocalDate date;
	private int numContainers;
	private FillLevel fillLevel;

	//CONSTRUCTORS
	public DumpsterUsageDTO() {
	}

	public DumpsterUsageDTO(LocalDate date, int numContainers, FillLevel fillLevel) {
		this.date = date;
		this.numContainers = numContainers;
		this.fillLevel = fillLevel;
	}

	//GETTERS AND SETTERS
	//Date
	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	//Containers
	public int getContainers() {
		return numContainers;
	}

	public void setContainers(int numContainers) {
		this.numContainers = numContainers;
	}

	//Fill Level
	public FillLevel getFillLevel() {
		return fillLevel;
	}

	public void setFillLevel(FillLevel fillLevel) {
		this.fillLevel = fillLevel;
	}

}
