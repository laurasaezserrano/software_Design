package es.deusto.ingenieria.sd.ecoembes.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

//import es.deusto.ingenieria.sd.ecoembes.dto.DumpsterCreationDTO;
import es.deusto.ingenieria.sd.ecoembes.dto.DumpsterDTO;
//import es.deusto.ingenieria.sd.ecoembes.dto.DumpsterUpdateDTO;
import es.deusto.ingenieria.sd.ecoembes.dto.DumpsterUsageDTO;
import es.deusto.ingenieria.sd.ecoembes.entity.Dumpster;
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
	}

	private void createExample(String location, String postalCode, int capacity, int containers, FillLevel level) {
		Dumpster dumpster = new Dumpster(nextId, location, postalCode, capacity);
		dumpster.update(LocalDate.now(), containers, level);
		dumpsters.add(dumpster);
		nextId++;
	}

	public DumpsterDTO createDumpster(FillLevel fillLevel, int numContainers, DumpsterDTO dto, Dumpster d) {
		Dumpster dumpster = new Dumpster(nextId, dto.getLocation(), d.getPostalCode(), d.getCapacity());
		dumpsters.add(dumpster);
		nextId++;

		return toDTO(dumpster);
	}

	public boolean updateDumpster(long id, DumpsterDTO dto) {
		Dumpster dumpster = findDumpster(id);
		if (dumpster == null) {
			return false;
		}

		FillLevel level;
		try {
			level = FillLevel.valueOf(dto.getStatus().toUpperCase());
		} catch (IllegalArgumentException e) {
			return false;
		}

		dumpster.update(LocalDate.now(), dto.getNumContainers(), level);
		return true;
	}

	public List<DumpsterDTO> getAllDumpsters() {
		List<DumpsterDTO> result = new ArrayList<>();
		for (Dumpster dumpster : dumpsters) {
			result.add(toDTO(dumpster));
		}
		return result;
	}
	
	public boolean updateDumpster(long dumpsterId, FillLevel fillLevel, int numContainers) {
		Dumpster dumpster = findDumpster(dumpsterId);
		if (dumpster == null) {
			return false;
		}
		dumpster.update(LocalDate.now(), numContainers, fillLevel);
		return true;
	}

	// Usage of a dumpster between two dates
	public ResponseEntity<List<DumpsterUsageDTO>> getDumpsterUsage(long id, LocalDate start, LocalDate end) {
		Dumpster dumpster = findDumpster(id);
		if (dumpster == null) {
			return ResponseEntity.notFound().build();
		}

		List<DumpsterUsageDTO> result = new ArrayList<>();
		for (DumpsterUsage usage : dumpster.getUsageBetween(start, end)) {
			result.add(new DumpsterUsageDTO(usage.getDate(), usage.getContainers(), usage.getFillLevel()));
		}
		return ResponseEntity.ok(result);
	}

	public Dumpster findDumpster(long id) {
		for (Dumpster dumpster : dumpsters) {
			if (dumpster.getId() == id) {
				return dumpster;
			}
		}
		return null;
	}

	private DumpsterDTO toDTO(Dumpster dumpster) {
		return new DumpsterDTO(dumpster.getId(), dumpster.getLocation(), dumpster.getFillLevel().name());
	}
}
