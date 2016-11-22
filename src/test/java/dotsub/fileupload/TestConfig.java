package dotsub.fileupload;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;

import dotsub.fileupload.service.StorageProperties;

@Configuration
@EnableAutoConfiguration
@EnableConfigurationProperties(StorageProperties.class)
public class TestConfig {

}
