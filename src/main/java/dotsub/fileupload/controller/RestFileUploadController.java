package dotsub.fileupload.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/uploadservice")
public class RestFileUploadController {
	
	@RequestMapping(value="/upload")
	public String handleUpload() {
		String results = "200";
		
		
		return results;
	}

}
