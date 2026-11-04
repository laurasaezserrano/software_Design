package es.deusto.ingenieria.sd.ecoembes.dto;

import es.deusto.ingenieria.sd.ecoembes.entity.FillLevel;

public class DumpsterUsageDTO {
	private String date;
	private int numContainers;
	private FillLevel fillLevel;

	public DumpsterUsageDTO() {
	}

	public DumpsterUsageDTO(String date, int numContainers, FillLevel fillLevel) {
		this.date = date;
		this.numContainers = numContainers;
		this.fillLevel = fillLevel;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public int getContainers() {
		return numContainers;
	}

	public void setContainers(int numContainers) {
		this.numContainers = numContainers;
	}

	public FillLevel getFillLevel() {
		return fillLevel;
	}

	public void setFillLevel(FillLevel fillLevel) {
		this.fillLevel = fillLevel;
	}

}
