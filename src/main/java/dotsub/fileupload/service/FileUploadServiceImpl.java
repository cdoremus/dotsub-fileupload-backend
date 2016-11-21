package dotsub.fileupload.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
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
	// set upload dir and path
	private final String UPLOAD_DIR = "upload-dir";
	private final Path rootLocation;
	 
	@Autowired
	public FileUploadServiceImpl(FileUploadMetatdataRepository repository) {
		this.repository = repository;
		this.rootLocation = Paths.get(UPLOAD_DIR);
	}

	@Override
	public FileUploadMetadata uploadFile(MultipartFile file) throws FileUploadException {
		if (file == null || file.isEmpty()) {
        	throw new FileUploadException("Null or empty file to upload");
		}
		String filename = file.getOriginalFilename();
		LOG.info(String.format("Uploading file: %s", filename));
		
    	Path path = this.rootLocation.resolve(filename);
    	LOG.info(String.format("File upload path: %s", path));
        try {
        	// allow overwriting of existing file
        	Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
       		throw new FileUploadException(String.format("Problem uploading file %s to path %s", filename, path), e);
        }
        
        FileUploadMetadata metadata = new FileUploadMetadata();
        metadata.setFilename(filename);
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

    @Override
    public Resource loadFileAsResource(String filename) {
        try {
            Path file = rootLocation.resolve(filename);
            Resource resource = new UrlResource(file.toUri());
            if(resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new FileUploadException(String.format("Failed to load file %s", filename));
            }
        } catch (MalformedURLException e) {
        	throw new FileUploadException(String.format("Failed to load file %s due to bad URL", filename), e);
        }
    }


}
