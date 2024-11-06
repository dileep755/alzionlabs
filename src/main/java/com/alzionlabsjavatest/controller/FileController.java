package com.alzionlabsjavatest.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.alzionlabsjavatest.bean.FileDownloadResponse;
import com.alzionlabsjavatest.service.FileService;

@RestController
@RequestMapping("/api/files")
public class FileController {
    @Autowired
    private FileService fileService;
    
    @Value("${file.domain.url}")
    private String domainUrl;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file,
                                             @RequestParam("passcode") String passcode) {
        try {
            String uniqueUrl = fileService.uploadFile(file, passcode);
            return ResponseEntity.ok(domainUrl + uniqueUrl+"?passcode="+passcode);
        } catch (Exception e) {
        	e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload file.");
        }
    }

    @GetMapping("/download/{uniqueUrl}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable String uniqueUrl,
                                               @RequestParam("passcode") String passcode) {
    	 try {
    	        FileDownloadResponse fileDownloadResponse = fileService.downloadFile(uniqueUrl, passcode);

    	        HttpHeaders headers = new HttpHeaders();
    	        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
    	        headers.setContentDispositionFormData("attachment", fileDownloadResponse.getFileName());
    	        
    	        return new ResponseEntity<>(fileDownloadResponse.getFileData(), headers, HttpStatus.OK);
    	    } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
