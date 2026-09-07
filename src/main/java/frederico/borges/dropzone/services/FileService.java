
package frederico.borges.dropzone.services;

import frederico.borges.dropzone.entities.FileEntity;
import frederico.borges.dropzone.entities.Transfer;
import frederico.borges.dropzone.repositories.FileRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class FileService {

    private static final Logger log = LoggerFactory.getLogger(FileService.class);

    private final FileRepository fileRepository;
    private final SupabaseStorageService supabaseStorageService;

    public FileService(
            FileRepository fileRepository,
            SupabaseStorageService supabaseStorageService) {

        this.fileRepository = fileRepository;
        this.supabaseStorageService = supabaseStorageService;
    }

    // =========================
    // UPLOAD
    // =========================

    public void uploadFiles(
            MultipartFile[] files,
            Transfer transfer) throws Exception {

        log.info(
                "Upload iniciado: code={}, files={}",
                transfer.getCode(),
                files.length);

        for (MultipartFile file : files) {

            if (file.isEmpty()) {

                log.warn(
                        "Ficheiro vazio ignorado: code={}",
                        transfer.getCode());

                continue;
            }

            String originalFilename = file.getOriginalFilename();

            if (originalFilename == null ||
                    originalFilename.isBlank()) {

                originalFilename = "file";
            }

            String safeFilename = originalFilename.replaceAll(
                    "[^a-zA-Z0-9._-]",
                    "_");

            String storageFilename = UUID.randomUUID()
                    + "-"
                    + safeFilename;

            log.debug(
                    "A enviar ficheiro: name={}, size={}, code={}",
                    originalFilename,
                    file.getSize(),
                    transfer.getCode());

            supabaseStorageService.uploadFile(
                    storageFilename,
                    file.getBytes(),
                    file.getContentType());

            FileEntity fileEntity = new FileEntity();

            fileEntity.setFilename(
                    originalFilename);

            fileEntity.setStoragePath(
                    storageFilename);

            fileEntity.setSize(
                    file.getSize());

            fileEntity.setContentType(
                    file.getContentType());

            fileEntity.setCreatedAt(
                    LocalDateTime.now());

            fileEntity.setTransfer(
                    transfer);

            fileRepository.save(
                    fileEntity);

            log.info(
                    "Ficheiro guardado: name={}, code={}",
                    originalFilename,
                    transfer.getCode());
        }

        log.info(
                "Upload concluído: code={}",
                transfer.getCode());
    }

    // =========================
    // PROCURAR FICHEIRO
    // =========================

    public FileEntity findById(Long id) {

        return fileRepository.findById(id)
                .orElseThrow();
    }

    // =========================
    // DOWNLOAD
    // =========================

    public byte[] downloadFile(
            FileEntity file) {

        log.info(
                "Download iniciado: name={}, id={}",
                file.getFilename(),
                file.getId());

        byte[] fileBytes =
                supabaseStorageService.downloadFile(
                        file.getStoragePath());

        log.info(
                "Download concluído: name={}, id={}",
                file.getFilename(),
                file.getId());

        return fileBytes;
    }

    
    // =========================
    // ELIMINAR FICHEIRO
    // =========================

    public void deleteFile(FileEntity file) {

        log.info(
                "A eliminar ficheiro: name={}, id={}",
                file.getFilename(),
                file.getId());

        supabaseStorageService.deleteFile(
                file.getStoragePath());

        log.info(
                "Ficheiro eliminado do Storage: name={}, id={}",
                file.getFilename(),
                file.getId());
    }

}
