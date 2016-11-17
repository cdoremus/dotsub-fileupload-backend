package dotsub.fileupload;

import dotsub.fileupload.service.FileSystemStorageService;
import dotsub.fileupload.service.StorageProperties;
import dotsub.fileupload.service.StorageService;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
//@Import(HibernateDataConfig.class)
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }


	@Bean
	CommandLineRunner init(StorageService storageService) {
		return (args) -> {
            storageService.deleteAll();
            storageService.init();
		};
	}

	@Bean
	StorageService storageService() {
		return new FileSystemStorageService(storageProperties());
	}
	
	@Bean
	StorageProperties storageProperties() {
		return new StorageProperties();
	}
	

}


