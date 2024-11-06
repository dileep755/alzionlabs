package com.alzionlabsjavatest.service;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.alzionlabsjavatest.bean.FileDownloadResponse;
import com.alzionlabsjavatest.configuration.EncryptionService;
import com.alzionlabsjavatest.entity.FileEntity;
import com.alzionlabsjavatest.repository.FileRepository;

@Service
public class FileService {
    @Autowired
    private FileRepository fileRepository;
    @Autowired
    private EncryptionService encryptionService;
    
    @Value("${file.upload.path}")
    private String filePath;

 

    public String uploadFile(MultipartFile file, String passcode) throws Exception {
    	System.out.println(filePath +"filePath");
    	 Path STORAGE_DIR = Paths.get(filePath);
    	String has = encryptionService.generateChecksumMD5(passcode);
        byte[] encryptedData = encryptionService.encrypt(file.getBytes(), has);

        String uniqueUrl = UUID.randomUUID().toString();
        Path filePath = STORAGE_DIR.resolve(uniqueUrl);
        Files.write(filePath, encryptedData);

        FileEntity fileEntity = new FileEntity();
        fileEntity.setUniqueUrl(uniqueUrl);
        fileEntity.setFileName(file.getOriginalFilename());
        fileEntity.setUploadTime(LocalDateTime.now());
        fileEntity.setExpirationTime(LocalDateTime.now().plusHours(48));
        fileRepository.save(fileEntity);

        return uniqueUrl;
    }

   /* public byte[] downloadFile(String uniqueUrl, String passcode) throws Exception {
        Optional<FileEntity> fileEntityOpt = fileRepository.findByUniqueUrl(uniqueUrl);
        if (fileEntityOpt.isEmpty() || fileEntityOpt.get().getExpirationTime().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("File not found or expired.");
        }

        Path filePath = STORAGE_DIR.resolve(uniqueUrl);
        byte[] encryptedData = Files.readAllBytes(filePath);
        String has = encryptionService.generateChecksumMD5(passcode);
        return encryptionService.decrypt(encryptedData, has);
    }*/
    
    
    public FileDownloadResponse downloadFile(String uniqueUrl, String passcode) throws Exception {
        Optional<FileEntity> fileEntityOpt = fileRepository.findByUniqueUrl(uniqueUrl);
        if (fileEntityOpt.isEmpty() || fileEntityOpt.get().getExpirationTime().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("File not found or expired.");
        }
        Path STORAGE_DIR = Paths.get(filePath);

        Path filePath = STORAGE_DIR.resolve(uniqueUrl);
        byte[] encryptedData = Files.readAllBytes(filePath);
        String hashedPasscode = encryptionService.generateChecksumMD5(passcode);
        byte[] decryptedData = encryptionService.decrypt(encryptedData, hashedPasscode);

        return new FileDownloadResponse(decryptedData, fileEntityOpt.get().getFileName());
    }

}
