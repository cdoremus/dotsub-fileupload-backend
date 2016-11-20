package dotsub.fileupload.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileUploadRepository extends JpaRepository<FileUploadData, Long> {

	FileUploadData findByTitle(String title);

	FileUploadData findByFilename(String fileName);
}
