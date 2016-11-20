package dotsub.fileupload.service;

import java.util.List;

import dotsub.fileupload.repository.DataNotFoundException;
import dotsub.fileupload.repository.FileUploadMetadata;
import dotsub.fileupload.repository.FileUploadMetatadataRepositoryException;

public interface FileUploadService {

	List<FileUploadMetadata> findAllMetatdata() throws FileUploadMetatadataRepositoryException, DataNotFoundException;
	
	FileUploadMetadata saveMetatdata(FileUploadMetadata data) throws FileUploadMetatadataRepositoryException;
	
	FileUploadMetadata findMetatdataById(Long id) throws FileUploadMetatadataRepositoryException, DataNotFoundException;
	
	FileUploadMetadata findMetatdataByFilename(String filename) throws FileUploadMetatadataRepositoryException, DataNotFoundException;
	
	void deleteMetatdata(Long id) throws FileUploadMetatadataRepositoryException;
	
	
}
