package dotsub.fileupload.service;

import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import dotsub.fileupload.repository.DataNotFoundException;
import dotsub.fileupload.repository.FileUploadMetadata;
import dotsub.fileupload.repository.FileUploadMetatadataRepositoryException;

/**
 * 
 * Defines service methods used by the <code>RestFileUploadController</code>
 * to store and serve file uploads and obtain metatdata.
 *
 */
public interface FileUploadService {

	FileUploadMetadata uploadFile(MultipartFile file) throws FileUploadException;
	
	List<FileUploadMetadata> findAllMetatdata() throws FileUploadMetatadataRepositoryException, DataNotFoundException;
	
	FileUploadMetadata saveMetatdata(FileUploadMetadata data) throws FileUploadMetatadataRepositoryException;
	
	FileUploadMetadata findMetatdataById(Long id) throws FileUploadMetatadataRepositoryException, DataNotFoundException;
	
	FileUploadMetadata findMetatdataByFilename(String filename) throws FileUploadMetatadataRepositoryException, DataNotFoundException;
	
	void deleteMetatdata(Long id) throws FileUploadMetatadataRepositoryException;
	
    public Resource loadFileAsResource(String filename) ;
	
}
