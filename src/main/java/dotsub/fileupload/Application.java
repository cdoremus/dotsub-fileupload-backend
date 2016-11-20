package dotsub.fileupload;

import dotsub.fileupload.service.StorageProperties;
import dotsub.fileupload.service.StorageService;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }


	@Bean
	CommandLineRunner init(StorageService fileUploadService) {
		return (args) -> {
            fileUploadService.deleteAll();
            fileUploadService.init();
		};
	}

}


