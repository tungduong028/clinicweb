package com.example.clinicweb.service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class CloudStorageService {

    private final Storage storage;

    public CloudStorageService() throws IOException {
        GoogleCredentials credentials = GoogleCredentials.getApplicationDefault();
        storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
    }

    public String uploadFile(MultipartFile file) throws IOException {
        String bucketName = "clinicweb";
        String blobName = file.getOriginalFilename();

        BlobInfo blobInfo = BlobInfo.newBuilder(bucketName, blobName).build();
        storage.create(blobInfo, file.getBytes());

        return String.format("https://storage.googleapis.com/%s/%s", bucketName, blobName);
    }
}

