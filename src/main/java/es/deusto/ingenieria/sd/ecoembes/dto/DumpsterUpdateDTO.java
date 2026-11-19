package es.deusto.ingenieria.sd.ecoembes.dto;

import es.deusto.ingenieria.sd.ecoembes.entity.FillLevel;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class DumpsterUpdateDTO {
	@Min(0)
	private int numContainers;
	@NotNull
	private FillLevel fillLevel;

	//CONSTRUCTORS
	public DumpsterUpdateDTO() {
	}

	public DumpsterUpdateDTO(int numContainers, FillLevel fillLevel) {
		this.numContainers = numContainers;
		this.fillLevel = fillLevel;
	}

	//GETTERS AND SETTERS
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
