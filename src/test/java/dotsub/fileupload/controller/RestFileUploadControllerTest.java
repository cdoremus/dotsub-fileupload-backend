package dotsub.fileupload.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Map;

import dotsub.fileupload.repository.DataNotFoundException;
import dotsub.fileupload.repository.FileUploadMetadata;
import dotsub.fileupload.repository.FileUploadMetatadataRepositoryException;
import dotsub.fileupload.service.FileUploadService;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class RestFileUploadControllerTest {

	
    @Autowired
    private RestFileUploadController controller;
    
    @MockBean
    private FileUploadService service;
    

    @Autowired
    private MockMvc mockMvc;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
		
	}

	@Test
	@Ignore // This needs to be correctly implemented
	public void testHandleUpload() throws Exception {
        MockMultipartFile file = new MockMultipartFile("data", "testfile.txt", "text/plain", "some xml".getBytes());

        mockMvc.perform(MockMvcRequestBuilders.fileUpload("/uploadservice/upload")
        	 .file(file)
        	 .param("uploadedFile", file.getBytes().toString()))	
        	.andDo(print())
            .andExpect(status().isOk());
	}

	@Test
	public void testFindAll() throws Exception {
		given(service.findAllMetatdata()).willReturn(
				Arrays.asList(new FileUploadMetadata(), new FileUploadMetadata())
				);
		
		this.mockMvc.perform(get("/uploadservice/findAll")).andDo(print()).andExpect(status().isOk());
		
	}

	@Test
	public void testSaveMetatdata() throws Exception{
		FileUploadMetadata data = new FileUploadMetadata();
		data.setFilename("file1");
		FileUploadMetadata newData = new FileUploadMetadata();
		newData.setId(100L);		
		newData.setFilename("file1");
		given(service.saveMetatdata(data)).willReturn(newData);

		String today = LocalDate.now().toString();
		this.mockMvc.perform(post("/uploadservice/saveData")
			.param("id", "0")
			.param("title", "File title")
			.param("description", "File description")
			.param("filename", "Filename.png")
			.param("createDate", today))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(content().json("{\"id\":0,\"title\":\"File title\",\"description\":\"File description\",\"filename\":\"Filename.png\",\"createDate\":\"2016-11-21\"}"));
	
	}

	@Test
	public void testFindById() throws Exception {
		
		given(service.findMetatdataById(any())).willReturn(new FileUploadMetadata());
		
		this.mockMvc.perform(get("/uploadservice/find/1")).andDo(print()).andExpect(status().isOk());
	}

	@Test
	@SuppressWarnings("rawtypes" )
	public void testServeFile() throws Exception {
		String file = "testfile.txt";
		Resource resource = Mockito.mock(Resource.class);		
		given(service.loadFileAsResource(file)).willReturn(resource);
		
		ResponseEntity foundResouce = controller.serveFile(file);
		
		assertThat(foundResouce).isNotNull();
		assertThat(foundResouce.getStatusCode()).isEqualTo(HttpStatus.OK);
	}

	@Test
	@SuppressWarnings("unchecked")
	public void testHandleFileUploadException() throws Exception {
	   given(service.findMetatdataById(1L)).willThrow(FileUploadMetatadataRepositoryException.class);
	   
		this.mockMvc.perform(get("/uploadservice/find/1")).andDo(print()).andExpect(status().isInternalServerError());
	   
	}

	@Test
	@SuppressWarnings("unchecked")
	public void testHandleDataNotFoundException() throws Exception {
	   given(service.findMetatdataById(1L)).willThrow(DataNotFoundException.class);
	   
		this.mockMvc.perform(get("/uploadservice/find/1")).andDo(print()).andExpect(status().isNotFound());
	}

	@Test
	public void testBuildErrorMap() {
		
		Map<String, Object> errorMap = controller.buildErrorMap(new RuntimeException(), HttpStatus.NOT_FOUND);

		assertThat(errorMap.get("status").equals(HttpStatus.NOT_FOUND));
	}

	@Test
	public void testBuildErrorResponse() {
		
		ResponseEntity<Map<String, Object>> errorResponse = 
				controller.buildErrorResponse(new RuntimeException("Something is wrong"), HttpStatus.INTERNAL_SERVER_ERROR);
		
		assertThat(errorResponse.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
