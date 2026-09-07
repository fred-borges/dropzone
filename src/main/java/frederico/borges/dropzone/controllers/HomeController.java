
package frederico.borges.dropzone.controllers;

import frederico.borges.dropzone.entities.Transfer;
import frederico.borges.dropzone.services.FileService;
import frederico.borges.dropzone.services.SupabaseStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import frederico.borges.dropzone.entities.FileEntity;
import frederico.borges.dropzone.repositories.FileRepository;
import frederico.borges.dropzone.services.TransferService;

import java.time.LocalDateTime;
import java.util.List;

@Controller
public class HomeController {

    private final FileRepository fileRepository;

    public HomeController(
            FileService fileService,
            SupabaseStorageService supabaseStorageService,
            FileRepository fileRepository
    ) {
        this.fileService = fileService;
        this.supabaseStorageService = supabaseStorageService;
        this.fileRepository = fileRepository;
    }

    @Autowired
    private SupabaseStorageService supabaseStorageService;

    @Autowired
    private TransferService transferService;

    @Autowired
    private FileService fileService;

    @GetMapping
    public String home(Model model) {

        List<FileEntity> files = fileRepository.findAll();

        model.addAttribute("files", files);

        return "index";
    }

    @PostMapping("/send_files")
    public ResponseEntity<String> send_files(
            @RequestParam(value = "files", required = false) MultipartFile[] files,
            @RequestParam("code") String code
    ) {
        if (files == null || files.length == 0) {
            return ResponseEntity.badRequest()
                    .body("Nenhum arquivo foi enviado.");
        }

        Transfer transfer = transferService.getTransferByCode(code);

        try {

            for (MultipartFile file : files) {

                if (file.isEmpty()) {
                    continue;
                }

                String originalFilename = file.getOriginalFilename();

                String storageFilename = originalFilename
                        .replaceAll("[^a-zA-Z0-9._-]", "_");

                supabaseStorageService.uploadFile(
                        storageFilename,
                        file.getBytes(),
                        file.getContentType()
                );

                FileEntity fileEntity = new FileEntity();

                fileEntity.setFilename(originalFilename);
                fileEntity.setStoragePath(storageFilename);
                fileEntity.setSize(file.getSize());
                fileEntity.setContentType(file.getContentType());
                fileEntity.setCreatedAt(LocalDateTime.now());
                fileEntity.setTransfer(transfer);

                fileRepository.save(fileEntity);
            }

            return ResponseEntity.ok("Ficheiros enviados com sucesso.");

        } catch (Exception e) {

            return ResponseEntity.internalServerError()
                    .body("Erro ao enviar ficheiros: " + e.getMessage());
        }
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable Long id) {

        FileEntity file = fileRepository.findById(id)
                .orElseThrow();

        byte[] fileBytes = supabaseStorageService.downloadFile(
                file.getStoragePath()
        );

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + file.getFilename() + "\""
                )
                .contentType(
                        MediaType.parseMediaType(file.getContentType())
                )
                .body(fileBytes);
    }
}
