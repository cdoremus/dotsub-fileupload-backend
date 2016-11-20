package dotsub.fileupload.service;

import java.util.HashMap;
import java.util.Map;

public class FileUploadException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private final Map<String, Object> additionalInfo;
	
	public FileUploadException(String message) {
        super(message);
        additionalInfo = new HashMap<>();
    }

    public FileUploadException(String message, Throwable cause) {
        super(message, cause);
        additionalInfo = new HashMap<>();
    }
	
    public void addInfo(String message, Object info) {
    	additionalInfo.put(message, info);
    }
}
