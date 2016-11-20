package dotsub.fileupload.repository;

import dotsub.fileupload.service.FileUploadException;

public class FileUploadMetatadataRepositoryException extends FileUploadException {

	private static final long serialVersionUID = 1L;

	public FileUploadMetatadataRepositoryException(String message) {
		super(message);
	}

	public FileUploadMetatadataRepositoryException(String message, Throwable cause) {
		super(message, cause);
	}

}
