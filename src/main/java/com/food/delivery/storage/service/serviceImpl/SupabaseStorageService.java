package com.food.delivery.storage.service.serviceImpl;

import com.food.delivery.storage.service.FileStorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.UUID;

@Service
@ConditionalOnProperty(
        name = "storage.provider",
        havingValue = "supabase",
        matchIfMissing = true
)
public class SupabaseStorageService implements FileStorageService {

    @Value("${supabase.url}")
    private String supabaseUrl;

    @Value("${supabase.key}")
    private String apiKey;

    @Value("${supabase.bucket}")
    private String bucket;

    @Override
    public String uploadFile(MultipartFile file) {

        try {

            if (file == null || file.isEmpty()) {
                throw new RuntimeException("File is empty");
            }

            // ✅ Extract extension safely
            String originalName = file.getOriginalFilename();
            String extension = "";

            if (originalName != null && originalName.contains(".")) {
                extension = originalName.substring(originalName.lastIndexOf("."));
            }

            // ✅ UUID filename (SAFE)
            String fileName = UUID.randomUUID() + extension;

            // ✅ Upload URL
            String uploadUrl = supabaseUrl
                    + "/storage/v1/object/"
                    + bucket
                    + "/"
                    + fileName;

            // ✅ HTTP Request
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(uploadUrl))
                    .header("Authorization", "Bearer " + apiKey)
                    .header("Content-Type", file.getContentType())
                    .header("x-upsert", "true")
                    .PUT(HttpRequest.BodyPublishers.ofByteArray(file.getBytes()))
                    .build();

            HttpClient client = HttpClient.newHttpClient();

            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200 || response.statusCode() == 201) {

                // ✅ Public URL (no original filename needed)
                return supabaseUrl
                        + "/storage/v1/object/public/"
                        + bucket
                        + "/"
                        + fileName;

            } else {
                throw new RuntimeException("Upload failed: " + response.body());
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean deleteFile(String fileName) {
        try {
            if (fileName.contains("/")) {
                fileName = fileName.substring(fileName.lastIndexOf("/") + 1);
            }

            String deleteUrl = supabaseUrl
                    + "/storage/v1/object/"
                    + bucket
                    + "/"
                    + fileName;

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(deleteUrl))
                    .header("Authorization", "Bearer " + apiKey)
                    .DELETE()
                    .build();

            HttpClient client = HttpClient.newHttpClient();

            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());

            return response.statusCode() == 200
                    || response.statusCode() == 204;

        } catch (Exception e) {

            e.printStackTrace();
            return false;
        }
    }
}