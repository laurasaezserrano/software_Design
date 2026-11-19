package es.deusto.ingenieria.sd.ecoembes.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import es.deusto.ingenieria.sd.ecoembes.entity.Dumpster;
import es.deusto.ingenieria.sd.ecoembes.entity.DumpsterCreation;
import es.deusto.ingenieria.sd.ecoembes.entity.DumpsterUsage;
import es.deusto.ingenieria.sd.ecoembes.entity.FillLevel;

@Service
public class DumpsterService {

	private List<Dumpster> dumpsters = new ArrayList<>();
	private long nextId = 1;

	public DumpsterService() {
		
		// Examples provided by CLAUDE
		createExample("Gran Via 1, Bilbao", "48001", 10, 3, FillLevel.GREEN);
		createExample("Calle Mayor 5, Bilbao", "48001", 10, 7, FillLevel.ORANGE);
		createExample("Avenida Sabino Arana 10, Bilbao", "48013", 12, 12, FillLevel.RED);
		
		for (Dumpster d : dumpsters) {
		    d.update(LocalDate.now().minusDays(2), 2, FillLevel.GREEN);
		    d.update(LocalDate.now().minusDays(1), 5, FillLevel.ORANGE);
		}
		// End of CLAUDE examples
		
	}

	private void createExample(String location, String postalCode, int capacity, int containers, FillLevel level) {
		Dumpster dumpster = new Dumpster(nextId, location, postalCode, capacity);
		dumpster.update(LocalDate.now(), containers, level);
		dumpsters.add(dumpster);
		nextId++;
	}

	public Dumpster createDumpster(FillLevel fillLevel, int numContainers, Dumpster d) {
		Dumpster dumpster = new Dumpster(nextId, d.getLocation(), d.getPostalCode(), d.getCapacity());
		dumpsters.add(dumpster);
		nextId++;

		return (dumpster);
	}

	//NEED CORRECTION
	public boolean updateDumpster(long id, Dumpster d) {
		Dumpster dumpster = findDumpster(id);
		if (dumpster == null) {
			return false;
		}

		FillLevel level;
		try {
			level = FillLevel.valueOf(d.getFillLevel().name());
		} catch (IllegalArgumentException e) {
			return false;
		}

		dumpster.update(LocalDate.now(), d.getNumContainers(), level);
		return true;
	}

	public List<Dumpster> getAllDumpsters() {
		List<Dumpster> result = new ArrayList<>();
		for (Dumpster dumpster : dumpsters) {
			result.add((dumpster));
		}
		return result;
	}
	
	public ResponseEntity<Void> updateDumpster(long dumpsterId, FillLevel fillLevel, int numContainers) {
	    Dumpster dumpster = findDumpster(dumpsterId);
	    if (dumpster == null) {
	        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	    }
	    if (numContainers < 0 || numContainers > dumpster.getCapacity()) {
	        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	    }

	    dumpster.update(LocalDate.now(), numContainers, fillLevel);
	    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	public List<DumpsterUsage> getDumpsterUsage(long id, LocalDate start, LocalDate end) {
	    Dumpster dumpster = findDumpster(id);
	    if (dumpster == null) {
	        return null;                       // controller turns this into 404
	    }
	    List<DumpsterUsage> result = new ArrayList<>();
	    for (DumpsterUsage u : dumpster.getUsageBetween(start, end)) {
	        result.add(new DumpsterUsage(u.getDate(), u.getContainers(), u.getFillLevel()));
	    }
	    return result;
	}
	
	public FillLevel getDumpstersStatus(String postalCode, LocalDate date) {
	    FillLevel worstLevel = FillLevel.GREEN;

	    for (Dumpster dumpster : dumpsters) {
	        if (dumpster.getPostalCode().equals(postalCode)) {
	            FillLevel level = dumpster.getFillLevelByDate(date);
	            if (level.ordinal() > worstLevel.ordinal()) {
	                worstLevel = level; 
	            }
	        }
	    }

	    return worstLevel;
	}

	public Dumpster findDumpster(long id) {
		for (Dumpster dumpster : dumpsters) {
			if (dumpster.getId() == id) {
				return dumpster;
			}
		}
		return null;
	}

	//NEED CORRECTION
	public Dumpster createDumpster(DumpsterCreation dumpsterCreation) {
		Dumpster dumpster = new Dumpster(nextId, dumpsterCreation.getLocation(), dumpsterCreation.getPostalCode(), dumpsterCreation.getCapacity());
		dumpster.update(LocalDate.now(), dumpsterCreation.getNumContainers(), dumpsterCreation.getFillLevel());
		dumpsters.add(dumpster);
		nextId++;
		return (dumpster);
		
	}
}
