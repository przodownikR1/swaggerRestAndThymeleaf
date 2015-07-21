package pl.java.scalatech.web.rest.upload;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.security.Principal;
import java.util.Optional;

import javax.servlet.annotation.MultipartConfig;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import pl.java.scalatech.util.FileOperations;

import com.google.common.io.Files;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

@RestController
@Api(value = "Upload controller", description = "API allow to upload files ")
@RequestMapping("/api/upload")
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024 * 50, maxRequestSize = 1024 * 1024 * 5 * 50)
public class UploaderController {
    private String fileName;

    @RequestMapping(value = "file", method = { RequestMethod.POST, RequestMethod.PUT }, params = "fileonly", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiOperation(value = "upload single file", httpMethod = "POST", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<?> set(Principal principal, @RequestParam MultipartFile multipartFile, UriComponentsBuilder uriBuilder) throws IOException {

        String userId = principal.getName();
        fileName = multipartFile.getName();
        try (InputStream inputStream = multipartFile.getInputStream()) {
            Files.write(FileOperations.convertInputStreamToByte(inputStream), new File(fileName));
        }
        URI uri = uriBuilder.path("/photo").buildAndExpand(userId).toUri();
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(uri);
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/multipleFile", method = RequestMethod.POST, params = "fileonly", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiOperation(value = "upload multiple file", httpMethod = "POST", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public @ResponseBody ResponseEntity<Void> uploadMultipleFileHandler(@RequestParam("name") String[] names, @RequestParam("file") MultipartFile[] files) {
        return ResponseEntity.accepted().build();
    }

    //TODO
    /*
     * @RequestMapping(method = RequestMethod.GET)
     * ResponseEntity<Resource> get(Principal principal) {
     * String userId = principal.getName();
     * HttpHeaders httpHeaders = new HttpHeaders();
     * httpHeaders.setContentType(MediaType.IMAGE_JPEG);
     * InputStream is = FileOperations.convertByteToInputStream(FileOperations.fileToBytes(new File(fileName)));
     * return new ResponseEntity<>(, httpHeaders, HttpStatus.OK);
     * }
     */
    // curl -v -H "Content-Type: multipart/form-data" -F"file=@some-file.jpg" -Fitem='{"name": "Test"};type=application/json' -X POST  http://127.0.0.1:8080/good
    @RequestMapping(value = "/test", method = RequestMethod.POST)
    @ApiOperation(value = "upload test", httpMethod = "POST", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> good(@RequestPart("name") String name, @RequestParam Optional<MultipartFile> file) {
        file.ifPresent(System.out::println);
        return new ResponseEntity<>(name, HttpStatus.OK);
    }

}
