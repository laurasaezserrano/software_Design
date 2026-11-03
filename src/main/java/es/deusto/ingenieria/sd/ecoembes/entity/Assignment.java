package es.deusto.ingenieria.sd.ecoembes.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Assignment {

	private long id;
	private LocalDate date;
	private RecyclingPlant plant;
	private Employee employee;
	private List<Dumpster> dumpsters;
	
	//CONSTRUCTORS
	public Assignment() {
		this.dumpsters = new ArrayList<>();
	}

	public Assignment(long id, LocalDate date, RecyclingPlant plant, Employee employee, List<Dumpster> dumpsters) {
		this.id = id;
		this.date = date;
		this.plant = plant;
		this.employee = employee;
		this.dumpsters = dumpsters != null ? dumpsters : new ArrayList<>();
	}
	
	//GETTERS AND SETTERS
	//Id
	public long getId() { 
		return id; 
		}
	
	public void setId(long id) { 
		this.id = id; 
		}
	
	//Date
	public LocalDate getDate() { 
		return date; 
		}
	
	public void setDate(LocalDate date) { 
		this.date = date; 
		}
	
	//Plant
	public RecyclingPlant getPlant() { 
		return plant; 
		}
	
	public void setPlant(RecyclingPlant plant) { 
		this.plant = plant; 
		}

	//Employee
	public Employee getEmployee() { 
		return employee; 
		}
	
	public void setEmployee(Employee employee) { 
		this.employee = employee; 
		}

	//Dumpsters
	public List<Dumpster> getDumpsters() { 
		return dumpsters; 
		}
	
	public void setDumpsters(List<Dumpster> dumpsters) {
		this.dumpsters = dumpsters != null ? dumpsters : new ArrayList<>();
	}
	
	// Auxiliary methods for dumpsters
	public int getTotalDumpsters() { return dumpsters.size(); }

	public int getTotalContainers() {
		return dumpsters.stream().mapToInt(Dumpster::getContainers).sum();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || getClass() != obj.getClass()) return false;
		return id == ((Assignment) obj).id;
	}

	@Override
	public int hashCode() { return Objects.hash(id); }
}
