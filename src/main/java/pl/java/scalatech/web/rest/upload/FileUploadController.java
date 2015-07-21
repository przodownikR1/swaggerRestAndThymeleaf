package pl.java.scalatech.web.rest.upload;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Optional;

import lombok.extern.slf4j.Slf4j;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

@Slf4j
@RestController
@RequestMapping("/api/uploader")
@Api(value = "Uploader controller", description = "another API allow to upload files ")
public class FileUploadController {
    private final static String DEFAULT_FILE_EXTENSION = "ext";
    private final static int FILE_AND_EXTENSION_LENGTH = 2;
    private final static int EXTENSION_FROM_PAIR_FILE_EXT_NUMBER = 1;
    private final static String SPLIT_FILE_AND_EXTENSION_PATTERN = "\\.(?=[^\\.]+$)";

    @RequestMapping(method = RequestMethod.GET)
    @ApiOperation(value = "upload single file", notes = "info uploader ...", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody String provideUploadInfo() {
        return "You can upload a file by posting to this same URL.";
    }

    @RequestMapping(method = RequestMethod.POST)
    @ApiOperation(value = "upload single file", notes = "optional you can add file name to set other name than orginal file name", httpMethod = "POST", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<String> handleFileUpload(@RequestParam(value = "name") Optional<String> name, @RequestParam("file") MultipartFile file) {
        String fileName = null;
        String fileExtension = null;
        if (getFileExtension(file).isPresent()) {
            fileExtension = getFileExtension(file).orElseGet(() -> DEFAULT_FILE_EXTENSION);
        }

        if (!file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();
                fileName = createUploadFileName(name, file, fileExtension);
                File fileUpload = new File(fileName);
                log.debug("UUU  fileName : {} => location : {} , size : {} ", fileName, fileUpload.getCanonicalPath(), file.getSize());
                try (BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(fileUpload))) {
                    stream.write(bytes);
                    return ResponseEntity.ok().body("You successfully uploaded " + fileName + "!");
                }

            } catch (Exception e) {
                return ResponseEntity.badRequest().body("You failed to upload " + fileName + " => " + e.getMessage());
            }
        }
        return ResponseEntity.badRequest().body("You failed to upload " + fileName + " because the file was empty.");
    }

    private String createUploadFileName(Optional<String> name, MultipartFile file, String fileExtension) {
        return name.orElseGet(() -> file.getName()).concat("." + fileExtension);
    }

    private Optional<String> getFileExtension(MultipartFile file) {
        log.debug("content type : {} , orginal name : {}", file.getContentType(), file.getOriginalFilename());
        String[] fileAttr = file.getOriginalFilename().split(SPLIT_FILE_AND_EXTENSION_PATTERN);
        if (fileAttr.length == FILE_AND_EXTENSION_LENGTH) { return Optional.ofNullable(fileAttr[EXTENSION_FROM_PAIR_FILE_EXT_NUMBER]); }
        return Optional.empty();
    }

}