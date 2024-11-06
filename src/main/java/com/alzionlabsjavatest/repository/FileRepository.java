package com.alzionlabsjavatest.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.alzionlabsjavatest.entity.FileEntity;

public interface FileRepository extends JpaRepository<FileEntity, Long> {
    Optional<FileEntity> findByUniqueUrl(String uniqueUrl);
    List<FileEntity> findByExpirationTimeBefore(LocalDateTime now);
}
