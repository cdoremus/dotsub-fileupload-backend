package dotsub.fileupload.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileUploadMetatdataRepository extends JpaRepository<FileUploadMetadata, Long> {

	FileUploadMetadata findByTitle(String title);

	FileUploadMetadata findByFilename(String fileName);
}
