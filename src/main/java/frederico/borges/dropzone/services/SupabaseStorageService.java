
package frederico.borges.dropzone.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class SupabaseStorageService {

    private static final Logger log = LoggerFactory.getLogger(SupabaseStorageService.class);

    private final RestClient restClient;

    @Value("${SUPABASE_URL}")
    private String supabaseUrl;

    @Value("${SUPABASE_SECRET_KEY}")
    private String supabaseSecretKey;

    @Value("${SUPABASE_BUCKET}")
    private String bucket;

    public SupabaseStorageService() {

        this.restClient = RestClient.builder().build();
    }

    // =========================
    // URL
    // =========================

    private String buildStorageUrl(String filename) {

        return supabaseUrl
                + "/storage/v1/object/"
                + bucket
                + "/"
                + filename;
    }

    // =========================
    // HEADERS
    // =========================

    private RestClient.RequestHeadersSpec<?> addHeaders(
            RestClient.RequestHeadersSpec<?> request) {

        return request
                .header(
                        "Authorization",
                        "Bearer " + supabaseSecretKey)
                .header(
                        "apikey",
                        supabaseSecretKey);
    }

    // =========================
    // UPLOAD
    // =========================

    public void uploadFile(
            String filename,
            byte[] fileBytes,
            String contentType) {

        String url = buildStorageUrl(filename);

        log.debug(
                "Upload para Storage iniciado: filename={}, size={}",
                filename,
                fileBytes.length);

        addHeaders(
                restClient.post()
                        .uri(url)
                        .header(
                                "Content-Type",
                                contentType)
                        .body(fileBytes))
                .retrieve()
                .toBodilessEntity();

        log.info(
                "Upload para Storage concluído: filename={}",
                filename);
    }

    // =========================
    // DOWNLOAD
    // =========================

    public byte[] downloadFile(
            String filename) {

        String url = buildStorageUrl(filename);

        log.debug(
                "Download do Storage iniciado: filename={}",
                filename);

        byte[] fileBytes = addHeaders(
                restClient.get()
                        .uri(url))
                .retrieve()
                .body(byte[].class);

        log.info(
                "Download do Storage concluído: filename={}",
                filename);

        return fileBytes;
    }

    // =========================
    // DELETE
    // =========================

    public void deleteFile(
            String filename) {

        String url =
                buildStorageUrl(filename);

        log.debug(
                "Delete do Storage iniciado: filename={}",
                filename);


        addHeaders(
                restClient.delete()
                        .uri(url)
        )
        .retrieve()
        .toBodilessEntity();


        log.info(
                "Ficheiro eliminado do Storage: filename={}",
                filename);
    }
}
