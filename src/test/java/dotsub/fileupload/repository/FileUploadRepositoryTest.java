package dotsub.fileupload.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest
public class FileUploadRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private FileUploadRepository repository;

    @Test
	public void testCreateAndFindUsingEntityManagerPersistAndFindOne() throws Exception {
    	FileUploadData data = new FileUploadData("title1", "desc1", "file1.txt", "2016-11-01");
    	FileUploadData newData = entityManager.persist(data);
    	
    	assertThat(newData.getId() > 0);
    	
    	FileUploadData found = repository.findOne(newData.getId());
    	
    	assertThat(found.getFilename()).isEqualTo("file1.txt");
    	
	}


    @Test
	public void testCreateAndFindUsingRepositorySaveAndFindOne() throws Exception {
    	FileUploadData data = new FileUploadData("title2", "desc2", "file2.txt", "2016-11-02");
    	FileUploadData newData = repository.save(data);
    	
    	assertThat(newData.getId() > 0);
    	
    	FileUploadData found = repository.findOne(newData.getId());
    	
    	assertThat(found.getTitle()).isEqualTo("title2");
    	
	}
    
    @Test
	public void testCreateAndFindUsingRepositorySaveAndFindByTitle() throws Exception {
    	FileUploadData data = new FileUploadData("title3", "desc3", "file3.txt", "2016-11-03");
    	FileUploadData newData = repository.save(data);
    	
    	assertThat(newData.getId() > 0);
    	
    	FileUploadData found = repository.findByTitle(newData.getTitle());
    	
    	assertThat(found.getDescription()).isEqualTo("desc3");
    	
	}
    
    @Test
	public void testCreateAndFindUsingRepositorySaveAndFindByFilename() throws Exception {
    	FileUploadData data = new FileUploadData("title4", "desc4", "file4.txt", "2016-11-04");
    	FileUploadData newData = repository.save(data);
    	
    	assertThat(newData.getId() > 0);
    	
    	FileUploadData found = repository.findByFilename(newData.getFilename());
    	
    	assertThat(found.getTitle()).isEqualTo("title4");
    	
	}
}
