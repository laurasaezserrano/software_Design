package es.deusto.ingenieria.sd.ecoembes.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Dumpster {

	private long id;
	private String location;     
	private String postalCode;
	private int capacity;         
	private int containers;      
	private FillLevel fillLevel;
	private List<DumpsterUsage> usageHistory;
	
	//CONSTRUCTORS
	public Dumpster() {
		this.usageHistory = new ArrayList<>();
		this.fillLevel = FillLevel.GREEN;
	}

	public Dumpster(long id, String location, String postalCode, int capacity) {
		this();
		this.id = id;
		this.location = location;
		this.postalCode = postalCode;
		this.capacity = capacity;
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
	
	//Postal Code
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

	//Containers
	public int getContainers() { 
		return containers; 
		}
	public void setContainers(int containers) { 
		this.containers = containers; 
		}

	//Fill Level
	public FillLevel getFillLevel() { 
		return fillLevel; 
		}
	
	public void setFillLevel(FillLevel fillLevel) { 
		this.fillLevel = fillLevel; 
		}

	//Usage History
	public List<DumpsterUsage> getUsageHistory() { 
		return usageHistory; 
		}
	
	public void setUsageHistory(List<DumpsterUsage> usageHistory) {
		this.usageHistory = usageHistory != null ? usageHistory : new ArrayList<>();
	}
	
	//Auxiliary methods for usage history
	public void update(LocalDate date, int containers, FillLevel fillLevel) {
		this.containers = containers;
		this.fillLevel = fillLevel;
		this.usageHistory.add(new DumpsterUsage(date, containers, fillLevel));
	}

	//Usage between two dates
	public List<DumpsterUsage> getUsageBetween(LocalDate start, LocalDate end) {
		return usageHistory.stream()
				.filter(u -> !u.getDate().isBefore(start) && !u.getDate().isAfter(end))
				.toList();
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || getClass() != obj.getClass()) return false;
		return id == ((Dumpster) obj).id;
	}

	@Override
	public int hashCode() { return Objects.hash(id); }

	public int getNumContainers() {
		return containers;
	}

	public FillLevel getFillLevelByDate(LocalDate date) {
		for (DumpsterUsage usage : usageHistory) {
			if (usage.getDate().equals(date)) {
				return usage.getFillLevel();
			}
		}
		return FillLevel.GREEN; // Default to GREEN if no record found for the date
	}

}
