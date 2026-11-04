package es.deusto.ingenieria.sd.ecoembes.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import es.deusto.ingenieria.sd.ecoembes.dto.DumpsterCreationDTO;
import es.deusto.ingenieria.sd.ecoembes.dto.DumpsterDTO;
import es.deusto.ingenieria.sd.ecoembes.dto.DumpsterUpdateDTO;
import es.deusto.ingenieria.sd.ecoembes.dto.DumpsterUsageDTO;
import es.deusto.ingenieria.sd.ecoembes.entity.Dumpster;
import es.deusto.ingenieria.sd.ecoembes.entity.DumpsterUsage;
import es.deusto.ingenieria.sd.ecoembes.entity.FillLevel;

@Service
public class DumpsterService {

	// There is no database yet, so dumpsters are stored in a list
	private List<Dumpster> dumpsters = new ArrayList<>();
	private long nextId = 1;

	public DumpsterService() {
		// Example data
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

	// Update from the sensor. Returns false if the dumpster does not exist or the status is not valid
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

	// Usage of a dumpster between two dates
	public Optional<List<DumpsterUsageDTO>> getDumpsterUsage(long id, LocalDate start, LocalDate end) {
		Dumpster dumpster = findDumpster(id);
		if (dumpster == null) {
			return Optional.empty();
		}

		List<DumpsterUsageDTO> result = new ArrayList<>();
		for (DumpsterUsage usage : dumpster.getUsageBetween(start, end)) {
			result.add(new DumpsterUsageDTO(usage.getDate(), usage.getContainers(), usage.getFillLevel().name()));
		}
		return Optional.of(result);
	}

	// Returns the dumpster with that id, or null if it does not exist
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
