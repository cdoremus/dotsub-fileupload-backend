## Web Service Endpoints for Angular 2 File Upload Form Application using Spring Boot and Spring Rest Controller


This project is a standalone web service used by the Angular 2 client to upload files to a server and save metatdata about uploaded files in a database. 

### Technologies used
- Gradle for building the app, running the embedded server and running tests and other checks
- Spring Boot - the base framework
- Spring Dependency Injection - using annotations
- Spring Data JPA - mediates database persistence
- Hibernate with H2 in-memory database
- JUnit and Spring testing tools - for unit and integration tests
- Checkstyle - for linting and other code quality checks

### Running the application back end

```bash
# clone this repo
$ git clone https://github.com/cdoremus/dotsub-fileupload-backend.git

# change directory
$ cd dotsub-fileupload-backend

# Create an upload-dir folder to hold the uploaded files
mkdir upload-dir

# build the application and start the embedded Tomcat server
$ ./gradlew bootRun

```

### Design of the Web Service
At a high level, this application's request/response flow looks like this:

RestFileUploadController -> FileUploadService -> FileUploadRepository

Here are the details
- *RestFileUploadController* - A Spring RestController that exposes web service endpoints to handle RESTful JSON requests and responses. Input validation is done to assure valid data and HTML escaping is done to prevent persistence of an XSS attack vector. This class also contains exception handler methods annotated with @ExceptionHandler. 
- *FileUploadService* - service intermediary between the controller and repository. This interface is implemented by FileUploadServiceImpl. The implementation class catches RuntimeException and rethrows them as an Exception that can be handled one one of the controller's exception handlers.
- *FileUploadRepository* - takes care of database persistence using Spring Data JPA
- *FileUploadMetadata* - JPA entity object holding the uploaded file metadata. It is populated by the controller from the client request or received from the service via the repository class. The metatdata is passed back to clients using a JSON representation of this class.

### Web Service Endpoints
The web service is currently configured to serve endpoints at the http://localhost:8080 URL.
 * /uploadservice/upload - for uploading a file and creating a metadata record in the database. The new record is returned as JSON including the record id, filename and creation date.
 * /uploadservice/findAll - for finding all metatdata records. A JSON array of records is returned
* /uploadservice/saveData - for saving an uploaded file's record. After it is persisted, the saved record is returned as JSON.
 * /uploadservice/files/{filename:.+} - for serving an uploaded file to be downloaded by the user of the Angular 2 client.
 * /uploadservice/find/{id} - for finding an uploaded file's record by record id.
 * /uploadservice/findByFilename/ - for finding an uploaded file's record by filename.

## Application Development Details

## Testing
Run all unit tests

```bash
gradlew test
```


## Linting and other checks

Do lint and other Checkstyle checks 
```bash
gradlew check
```

## Bundling and deploying the application
Bundle and deploy the application by following these steps:
&nbsp;
1. Create a Spring Boot jar file for standalone deployment 
```bash
gradlew bootRepackage
```
The built jar file will be found in the build/libs folder.
&nbsp;
2. Move the jar file to a deployment directory and create an *upload-dir* folder to hold the uploaded files
```bash
mkdir upload-dir
```
&nbsp;
 3. Run the jar file from the command line:
```bash
java -jar ./dotsub-fileupload-0.1.0.jar 
```

