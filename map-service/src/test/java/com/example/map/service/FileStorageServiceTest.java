package com.example.map.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

class FileStorageServiceTest {

    @TempDir
    Path tempDir;

    @Test
    @DisplayName("파일을 UUID 파일명으로 저장할 수 있다")
    void storeFile() throws IOException {
        FileStorageService service = new FileStorageService(tempDir.toString());
        MockMultipartFile file = new MockMultipartFile(
                "image", "photo.jpg", "image/jpeg", "test-content".getBytes());

        String storedPath = service.store(file);

        assertThat(storedPath).endsWith(".jpg");
        assertThat(Files.exists(Path.of(storedPath))).isTrue();
        assertThat(storedPath).doesNotContain("photo.jpg");
    }

    @Test
    @DisplayName("확장자가 없는 파일도 저장할 수 있다")
    void storeFileWithoutExtension() throws IOException {
        FileStorageService service = new FileStorageService(tempDir.toString());
        MockMultipartFile file = new MockMultipartFile(
                "image", "noext", "application/octet-stream", "data".getBytes());

        String storedPath = service.store(file);

        assertThat(Files.exists(Path.of(storedPath))).isTrue();
    }
}
