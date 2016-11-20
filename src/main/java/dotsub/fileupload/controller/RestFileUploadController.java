package dotsub.fileupload.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import dotsub.fileupload.repository.FileUploadMetatadataRepositoryException;
import dotsub.fileupload.service.FileUploadService;

@RestController
@RequestMapping(value = "/uploadservice")
public class RestFileUploadController {
	private static final Logger LOG = LoggerFactory.getLogger(RestFileUploadController.class);
	
	private FileUploadService fileUploadService;
	
	public RestFileUploadController(FileUploadService fileUploadService) {
		this.fileUploadService = fileUploadService;
	}
	
	@CrossOrigin(origins={"*"})
	@RequestMapping(value="/upload", method={RequestMethod.POST, RequestMethod.OPTIONS})
	public ResponseEntity handleUpload(
			@RequestParam("uploadedFile") MultipartFile uploadedFile) {
		
		//FIXME: Flesh out
		
//		return ResponseEntity.ok().build();
		return null;
	}

	
	
	@RequestMapping(value="/findAll", method=RequestMethod.GET)
	public List<FileUploadMetadata> findAll() {
		List<FileUploadMetadata> fileList = null;
		fileList = fileUploadService.findAllMetatdata();
		return fileList;
	}
	
	@CrossOrigin(origins={"*"})
	@RequestMapping(value="/saveData", method={RequestMethod.POST})
	public FileUploadMetadata saveMetatdata(
			@RequestParam("title") String title,
			@RequestParam("description") String description,
			@RequestParam("filename") String filename,
			@RequestParam("createDate") String createDate) {
		LOG.info(String.format("Title: %s", title));
		LOG.info(String.format("Description: %s", description));
	    LOG.info(String.format("Filename: %s", filename));
	    LOG.info(String.format("Create date: %s", createDate));

		FileUploadMetadata data = new FileUploadMetadata();
		data.setTitle(title);
		data.setDescription(description);
		data.setFilename(filename);
		data.setCreationDate(createDate);
		
		fileUploadService.saveMetatdata(data);
		
		return data;
	}


	@CrossOrigin(origins={"*"})
	@RequestMapping(value="/find/{id}", method={RequestMethod.GET})
	public FileUploadMetadata findById(@PathVariable("id") long id) {
		LOG.info(String.format("Metatdata search using id: %d", id));

		return fileUploadService.findMetatdataById(id);
	}

	@CrossOrigin(origins={"*"})
	@RequestMapping(value="/findByFilename", method={RequestMethod.GET})
	public FileUploadMetadata findByFilename(@RequestParam("filename") String filename) {
		LOG.info(String.format("Metatdata search using filename: %s", filename));

		return fileUploadService.findMetatdataByFilename(filename);
	}
	
    /**
     * Takes care of FileUploadMetatadataRepositoryException exceptions.
     * 
     * @param e exception thrown
     * @return ResponseEntity containing exception message and HttpStatus.INTERNAL_SERVER_ERROR.
     */
    @ExceptionHandler(FileUploadMetatadataRepositoryException.class)
    public ResponseEntity<Map<String, Object>> handleFileUploadMetatadataRepositoryException(FileUploadMetatadataRepositoryException e) {
    	return buildErrorResponse(e, HttpStatus.INTERNAL_SERVER_ERROR);
    }    

    /**
     * Takes care of DataNotFoundException exceptions.
     * 
     * @param e exception thrown
     * @return ResponseEntity containing exception message and HttpStatus.NOT_FOUND.
     */
    @ExceptionHandler(DataNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleDataNotFoundException(DataNotFoundException e) {
    	return buildErrorResponse(e, HttpStatus.NOT_FOUND);
    }

    protected Map<String, Object> buildErrorMap(Throwable e, HttpStatus status) {
        Map<String, Object> errorMap = new HashMap<>();
        errorMap.put("timestamp", new Date().getTime());
        errorMap.put("status", status.value());
        errorMap.put("error", status.getReasonPhrase());
        errorMap.put("message", e.getMessage());
        errorMap.put("path", "/uploadservice/*");
        return errorMap;
    }
  
    protected ResponseEntity<Map<String, Object>> buildErrorResponse(Throwable e, HttpStatus status) {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(buildErrorMap( e, status), responseHeaders, status);
    }
	
//	@CrossOrigin(origins={"*"})
//	@RequestMapping(value="/upload")
//	public String handleUpload(@RequestParam("uploadedFile") MultipartFile uploadedFileRef) {
//		System.out.println("handleUpload() called ...." + uploadedFileRef);
//		LOG.info("handleUpload() called ...." + uploadedFileRef);
//		StringBuilder message = new StringBuilder("");
//		boolean isError = false;
//		
//	    // Get name of uploaded file.
//	    String fileName = uploadedFileRef.getOriginalFilename();
//
//	    // Path where the uploaded file will be stored.
//	    // Lets use home directory for now
//		String userHome = System.getProperty("user.home");
//	    String path = userHome + "/" + fileName;
//	    LOG.info("File path to store: " + path);
//
//	    // This buffer will store the data read from 'uploadedFileRef'
//	    byte[] buffer = new byte[1000];
//
//	    // Now create the output file on the server.
//	    File outputFile = new File(path);
//
//	    FileInputStream reader = null;
//	    FileOutputStream writer = null;
//	    int totalBytes = 0;
//	    try {
//	        outputFile.createNewFile();
//
//	        // Create the input stream to uploaded file to read data from it.
//	        reader = (FileInputStream) uploadedFileRef.getInputStream();
//
//	        // Create writer for 'outputFile' to write data read from
//	        // 'uploadedFileRef'
//	        writer = new FileOutputStream(outputFile);
//
//	        // Iteratively read data from 'uploadedFileRef' and write to
//	        // 'outputFile';            
//	        int bytesRead = 0;
//	        while ((bytesRead = reader.read(buffer)) != -1) {
//	            writer.write(buffer);
//	            totalBytes += bytesRead;
//	        }
//	    } catch (IOException e) {
////	        e.printStackTrace();
//            LOG.error(e.getMessage(), e);
//	        message.append(e.getMessage());
//	        isError = true;
//	    } finally {
//	        try {
//	        	if (reader != null)
//	        		reader.close();
//	        	if (writer != null)
//	        		writer.close();
//	        } catch (IOException e) {
////	            e.printStackTrace();
//	            LOG.error(e.getMessage(), e);
//		        message.append(e.getMessage());
//		        isError = true;
//	        }
//	    }
//	    
//	    if (!isError) {
//	    	message.append("File uploaded successfully! Total Bytes Read=");
//	    	message.append(totalBytes);
//	    }
//	    
//	    return message.toString();
//	}



}
