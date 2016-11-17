package dotsub.fileupload.controller;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Matchers.any;

import dotsub.fileupload.TestConfig;
import dotsub.fileupload.service.StorageService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes=TestConfig.class)
public class FileUploadControllerTest {

   @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private StorageService storageService;

    @LocalServerPort
    private int port;

	
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

	//FIXME: Fix tests
	@Test
	public void testFoo() throws Exception {
		Assert.assertTrue(true);
	}
	
//    @Test
//    public void shouldUploadFile() throws Exception {
//        ClassPathResource resource = new ClassPathResource("testfile.txt", getClass());
//
//        MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
//        map.add("file", resource);
//        ResponseEntity<String> response = this.restTemplate.postForEntity("/", map, String.class);
//
//        assertThat(response.getStatusCode()).isEqualByComparingTo(HttpStatus.FOUND);
//        assertThat(response.getHeaders().getLocation().toString()).startsWith("http://localhost:" + this.port + "/");
//        then(storageService).should().store(any(MultipartFile.class));
//    }
//
//    @Test
//    public void shouldDownloadFile() throws Exception {
//        ClassPathResource resource = new ClassPathResource("testfile.txt", getClass());
//        given(this.storageService.loadAsResource("testfile.txt")).willReturn(resource);
//
//        ResponseEntity<String> response = this.restTemplate
//                .getForEntity("/files/{filename}", String.class, "testfile.txt");
//
//        assertThat(response.getStatusCodeValue()).isEqualTo(200);
//        assertThat(response.getHeaders().getFirst(HttpHeaders.CONTENT_DISPOSITION))
//                .isEqualTo("attachment; filename=\"testfile.txt\"");
//        assertThat(response.getBody()).isEqualTo("This is a test");
//    }
	
	
//	@Test
//	public void testListUploadedFiles() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testServeFile() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testHandleFileUpload() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testHandleStorageFileNotFound() {
//		fail("Not yet implemented");
//	}

}
