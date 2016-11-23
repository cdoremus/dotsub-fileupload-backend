package dotsub.fileupload.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface FileUploadMetatdataRepository extends JpaRepository<FileUploadMetadata, Long> {

	@Transactional(readOnly = true)
	FileUploadMetadata findByTitle(String title);

	@Transactional(readOnly = true)
	FileUploadMetadata findByFilename(String fileName);
}
