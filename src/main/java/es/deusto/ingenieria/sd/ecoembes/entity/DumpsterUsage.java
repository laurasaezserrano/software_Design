package es.deusto.ingenieria.sd.ecoembes.entity;

import java.time.LocalDate;

public class DumpsterUsage {

	private LocalDate date;
	private int containers;
	private FillLevel fillLevel;

	//CONSTRUCTORS
	public DumpsterUsage() { }

	public DumpsterUsage(LocalDate date, int containers, FillLevel fillLevel) {
		this.date = date;
		this.containers = containers;
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
		return containers;
		}
	
	public void setContainers(int containers) { 
		this.containers = containers; 
		}

	//FillLevel
	public FillLevel getFillLevel() { 
		return fillLevel; 
		}
	
	public void setFillLevel(FillLevel fillLevel) { 
		this.fillLevel = fillLevel; 
		}
}
