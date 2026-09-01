package frederico.borges.dropzone.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class SupabaseStorageService {

    private final RestClient restClient;

    public SupabaseStorageService() {
        this.restClient = RestClient.builder().build();
    }

    @Value("${SUPABASE_URL}")
    private String supabaseUrl;

    @Value("${SUPABASE_SECRET_KEY}")
    private String supabaseSecretKey;

    @Value("${SUPABASE_BUCKET}")
    private String bucket;

    private String buildUploadUrl(String filename) {
        return supabaseUrl
                + "/storage/v1/object/"
                + bucket
                + "/"
                + filename;
    }

    public void uploadFile(String filename, byte[] fileBytes, String contentType) {

        String url = buildUploadUrl(filename);

        restClient.post()
                .uri(url)
                .header("Authorization", "Bearer " + supabaseSecretKey)
                .header("apikey", supabaseSecretKey)
                .header("Content-Type", contentType)
                .body(fileBytes)
                .retrieve()
                .toBodilessEntity();
    }

    public byte[] downloadFile(String filename) {

        String url = buildUploadUrl(filename);

        return restClient.get()
                .uri(url)
                .header("Authorization", "Bearer " + supabaseSecretKey)
                .header("apikey", supabaseSecretKey)
                .retrieve()
                .body(byte[].class);
    }

}
