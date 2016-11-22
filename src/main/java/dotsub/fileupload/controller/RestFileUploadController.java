package dotsub.fileupload.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import dotsub.fileupload.repository.DataNotFoundException;
import dotsub.fileupload.repository.FileUploadMetadata;
import dotsub.fileupload.service.FileUploadException;
import dotsub.fileupload.service.FileUploadService;

@RestController
@RequestMapping(value = "/uploadservice")
public class RestFileUploadController {
	private static final Logger LOG = LoggerFactory.getLogger(RestFileUploadController.class);

	private FileUploadService fileUploadService;

	public RestFileUploadController(FileUploadService fileUploadService) {
		this.fileUploadService = fileUploadService;
	}

	@CrossOrigin(origins = { "*" }, 
		methods={RequestMethod.POST, RequestMethod.OPTIONS},
		allowedHeaders={"Origin", "X-Requested-With", "Content-Type", "Accept"})
	@RequestMapping(value = "/upload", method = { RequestMethod.POST, RequestMethod.OPTIONS })
	public FileUploadMetadata handleUpload(@RequestParam("uploadedFile") MultipartFile uploadedFile) {

		FileUploadMetadata metadata = fileUploadService.uploadFile(uploadedFile);

		// return ResponseEntity.ok().build();
		return metadata;
	}

	@CrossOrigin(origins = { "*" })
	@RequestMapping(value = "/findAll", method = RequestMethod.GET)
	public List<FileUploadMetadata> findAll() {
		List<FileUploadMetadata> fileList = null;
		fileList = fileUploadService.findAllMetatdata();
		return fileList;
	}

	@CrossOrigin(origins = { "*" })
	@RequestMapping(value = "/saveData", method = { RequestMethod.POST })
	public FileUploadMetadata saveMetatdata(
			@RequestParam("id") long id,
			@RequestParam("title") String title,
			@RequestParam("description") String description, 
			@RequestParam("filename") String filename,
			@RequestParam("createDate") String createDate) {
//		LOG.info(String.format("ID: %d", id));
//		LOG.info(String.format("Title: %s", title));
//		LOG.info(String.format("Description: %s", description));
//		LOG.info(String.format("Filename: %s", filename));
//		LOG.info(String.format("Create date: %s", createDate));

		FileUploadMetadata data = new FileUploadMetadata();
		data.setId(id);
		data.setTitle(title);
		data.setDescription(description);
		data.setFilename(filename);
		data.setCreateDate(createDate);

		fileUploadService.saveMetatdata(data);

		return data;
	}

	@CrossOrigin(origins = { "*" })
	@RequestMapping(value = "/find/{id}", method = { RequestMethod.GET })
	public FileUploadMetadata findById(@PathVariable("id") long id) {
		LOG.info(String.format("Metatdata search using id: %d", id));

		return fileUploadService.findMetatdataById(id);
	}

	@CrossOrigin(origins = { "*" })
	@RequestMapping(value = "/delete/{id}", method = { RequestMethod.GET })
	public ResponseEntity<String> deleteMetadata(@PathVariable("id") long id) {
		LOG.info(String.format("Metatdata delete for id: %d", id));

		return ResponseEntity.ok().body(String.format("Deleted metatdata with Id %d", id));
	}
	
	@CrossOrigin(origins = { "*" })
	@RequestMapping(value = "/findByFilename", method = { RequestMethod.GET })
	public FileUploadMetadata findByFilename(@RequestParam("filename") String filename) {
		LOG.info(String.format("Metatdata search using filename: %s", filename));

		return fileUploadService.findMetatdataByFilename(filename);
	}

	@CrossOrigin(origins = { "*" })
	@RequestMapping(value = "/files/{filename:.+}", method = { RequestMethod.GET })
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {

        Resource file = fileUploadService.loadFileAsResource(filename);
        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""+file.getFilename()+"\"")
                .body(file);
    }
	
	
	/**
	 * Takes care of FileUploadException and its subclass 
	 * FileUploadMetatadataRepositoryException.
	 * 
	 * @param e  exception thrown
	 * @return ResponseEntity containing exception message and
	 *         HttpStatus.INTERNAL_SERVER_ERROR.
	 */
	@ExceptionHandler(FileUploadException.class)
	public ResponseEntity<Map<String, Object>> handleFileUploadException(
			FileUploadException e) {
		LOG.error("File upload problem", e);
		return buildErrorResponse(e, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	/**
	 * Takes care of DataNotFoundException exceptions.
	 * 
	 * @param e
	 *            exception thrown
	 * @return ResponseEntity containing exception message and
	 *         HttpStatus.NOT_FOUND.
	 */
	@ExceptionHandler(DataNotFoundException.class)
	public ResponseEntity<Map<String, Object>> handleDataNotFoundException(DataNotFoundException e) {
		return buildErrorResponse(e, HttpStatus.NOT_FOUND);
	}
	
	
	protected Map<String, Object> buildErrorMap(Throwable e, HttpStatus status) {
		StringBuilder message = new StringBuilder();		
		if (e != null) {
			message.append(e.getMessage());
			// Add message from cause if present
			if (e.getCause() != null) {
				message.append("; nested exception is ");
				message.append(e.getCause().getMessage());
			}
		}
		Map<String, Object> errorMap = new HashMap<>();
		errorMap.put("timestamp", new Date().getTime());
		errorMap.put("status", status == null ? "" : status.value());
		errorMap.put("error", status == null ? "" : status.getReasonPhrase());
		errorMap.put("message", message.toString());
		errorMap.put("path", "/uploadservice/*");
		return errorMap;
	}

	protected ResponseEntity<Map<String, Object>> buildErrorResponse(Throwable e, HttpStatus status) {
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.setContentType(MediaType.APPLICATION_JSON);
		return new ResponseEntity<>(buildErrorMap(e, status), responseHeaders, status);
	}



}
