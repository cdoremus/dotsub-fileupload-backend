package dotsub.fileupload.service;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import dotsub.fileupload.repository.DataNotFoundException;
import dotsub.fileupload.repository.FileUploadMetadata;
import dotsub.fileupload.repository.FileUploadMetatadataRepositoryException;
import dotsub.fileupload.repository.FileUploadMetatdataRepository;

@Service
public class FileUploadServiceImpl implements FileUploadService {
	
	private static final Logger LOG = LoggerFactory.getLogger(FileUploadServiceImpl.class);
	private final FileUploadMetatdataRepository repository;
	
	@Autowired
	public FileUploadServiceImpl(FileUploadMetatdataRepository repository) {
		this.repository = repository;
	}

	@Override
	public FileUploadMetadata uploadFile(MultipartFile file) throws FileUploadException {
		if (file == null || file.isEmpty()) {
        	throw new FileUploadException("Null or empty file to upload");
		}
		String filename = file.getOriginalFilename();
		LOG.info(String.format("Uploading file: %s", filename));
    	// upload to home directory
    	String homeDir = System.getProperty("user.home");
    	Path path = FileSystems.getDefault().getPath(homeDir, filename);
        try {
        	// allow overwriting of existing file
        	Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
       		throw new FileUploadException(String.format("Problem uploading file %s to path %s", filename, path), e);
        }
        
        FileUploadMetadata metadata = new FileUploadMetadata();
        metadata.setFilename(filename);
        // TODO: ???? Delete old record for a file that you are overwriting
        //	1. Find record by filename
        //	2. Delete record
        //	3. Save new record
        return repository.save(metadata);
	}


	@Override
	public FileUploadMetadata saveMetatdata(FileUploadMetadata data) throws FileUploadMetatadataRepositoryException {
		try {
			return repository.save(data);
		} catch (RuntimeException e) {
			throw new FileUploadMetatadataRepositoryException(String.format("Problem saving metadata: %s", data), e);
		}
	}

	@Override
	public List<FileUploadMetadata> findAllMetatdata() throws FileUploadMetatadataRepositoryException, DataNotFoundException {
		List<FileUploadMetadata> data = null;
		try {
			data = repository.findAll();
		} catch (RuntimeException e) {
			throw new FileUploadMetatadataRepositoryException("Problem finding all metadata", e);
		}
		if (data == null || data.isEmpty()) {
			throw new DataNotFoundException(String.format("No file metadata found"));
		}
		return data;
	}

	@Override
	public FileUploadMetadata findMetatdataById(Long id) throws FileUploadMetatadataRepositoryException, DataNotFoundException {
		FileUploadMetadata data = null;
		try {
			data = repository.findOne(id);
		} catch (RuntimeException e) {
			throw new FileUploadMetatadataRepositoryException(String.format("Problem finding metadata with id: %d", id), e);
		}
		if (data == null) {
			throw new DataNotFoundException(String.format("Metatdata not found with id: %d", id));
		}
		return data;
	}

	@Override
	public FileUploadMetadata findMetatdataByFilename(String filename) throws FileUploadMetatadataRepositoryException, DataNotFoundException {
		FileUploadMetadata data = null;
		try {
			data = repository.findByFilename(filename);
		} catch (RuntimeException e) {
			throw new FileUploadMetatadataRepositoryException(String.format("Problem finding metadata by file name: %s", filename), e);
		}
		if (data == null) {
			throw new DataNotFoundException(String.format("Metatdata not found with file name: %s", filename));
		}
		return data;
	}

	@Override
	public void deleteMetatdata(Long id) throws FileUploadMetatadataRepositoryException {
		try {
			repository.delete(id);;
		} catch (RuntimeException e) {
			throw new FileUploadMetatadataRepositoryException(String.format("Problem deleting metadata with id: %d", id), e);
		}
	}

}
