package dotsub.fileupload.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dotsub.fileupload.repository.DataNotFoundException;
import dotsub.fileupload.repository.FileUploadMetadata;
import dotsub.fileupload.repository.FileUploadMetatadataRepositoryException;
import dotsub.fileupload.repository.FileUploadMetatdataRepository;

@Service
public class FileUploadServiceImpl implements FileUploadService {
	
	private final FileUploadMetatdataRepository repository;
	
	@Autowired
	public FileUploadServiceImpl(FileUploadMetatdataRepository repository) {
		this.repository = repository;
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
