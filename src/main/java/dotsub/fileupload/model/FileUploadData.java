package dotsub.fileupload.model;

import java.nio.file.Path;
import java.time.LocalDate;

public class FileUploadData {

	private long id;
	
	private String title;
	
	private String description;
	
	private Path file;
	
	private LocalDate creationDate = LocalDate.now();
	
	public FileUploadData() {
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Path getFile() {
		return file;
	}

	public void setFile(Path file) {
		this.file = file;
	}

	public LocalDate getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(LocalDate creationDate) {
		this.creationDate = creationDate;
	}
	
	
}
