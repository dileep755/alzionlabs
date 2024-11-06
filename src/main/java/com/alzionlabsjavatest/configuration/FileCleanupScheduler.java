package com.alzionlabsjavatest.configuration;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.alzionlabsjavatest.entity.FileEntity;
import com.alzionlabsjavatest.repository.FileRepository;

@Component
public class FileCleanupScheduler {
    @Autowired
    private FileRepository fileRepository;
    
    @Value("${file.upload.path}")
    private String filePath;

    @Scheduled(fixedRate = 60 * 60 * 1000) 
    public void cleanupExpiredFiles() {
        List<FileEntity> expiredFiles = fileRepository.findByExpirationTimeBefore(LocalDateTime.now());
        expiredFiles.forEach(file -> {
            try {
            	Path STORAGE_DIR = Paths.get(filePath);
                Files.deleteIfExists(STORAGE_DIR.resolve(file.getUniqueUrl()));
                fileRepository.delete(file);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
