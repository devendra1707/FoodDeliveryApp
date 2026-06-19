package com.food.delivery.storage.service.serviceImpl;

import com.food.delivery.storage.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import java.io.IOException;
import java.util.UUID;

@Service
@ConditionalOnProperty(
        name = "storage.provider",
        havingValue = "aws"
)
public class S3StorageService implements FileStorageService {

    @Autowired
    private S3Client s3Client;

    @Value("${aws.s3.bucketname}")
    private String bucketName;

    @Override
    public String uploadFile(MultipartFile file) {

        String fileNameExtension =
                file.getOriginalFilename()
                        .substring(file.getOriginalFilename().lastIndexOf(".") + 1);

        String key = UUID.randomUUID() + "." + fileNameExtension;

        try {

            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .contentType(file.getContentType())
                    .build();

            PutObjectResponse response = s3Client.putObject(
                    putObjectRequest,
                    RequestBody.fromBytes(file.getBytes())
            );

            if (response.sdkHttpResponse().isSuccessful()) {
                return "https://" + bucketName + ".s3.amazonaws.com/" + key;
            } else {
                throw new ResponseStatusException(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        "File Upload Failed !!!"
                );
            }

        } catch (IOException ex) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "An error occurred while uploading the file"
            );
        }
    }

    @Override
    public boolean deleteFile(String fileName) {

        try {

            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .build();

            s3Client.deleteObject(deleteObjectRequest);

            return true;

        } catch (Exception e) {

            e.printStackTrace();
            return false;
        }
    }
}
