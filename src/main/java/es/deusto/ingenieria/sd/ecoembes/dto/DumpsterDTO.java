package es.deusto.ingenieria.sd.ecoembes.dto;

public class DumpsterDTO {
		private long id;
	private String location;
	private String status;

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

}
