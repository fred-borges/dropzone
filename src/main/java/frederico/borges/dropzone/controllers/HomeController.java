package frederico.borges.dropzone.controllers;

import frederico.borges.dropzone.entities.FileEntity;
import frederico.borges.dropzone.entities.Transfer;
import frederico.borges.dropzone.repositories.FileRepository;
import frederico.borges.dropzone.services.SupabaseStorageService;
import frederico.borges.dropzone.services.TransferService;
import frederico.borges.dropzone.exceptions.TransferExpiredException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.UUID;

@Controller
public class HomeController {

        
        private final FileRepository fileRepository;
        private final SupabaseStorageService supabaseStorageService;
        private final TransferService transferService;

        public HomeController(
                        SupabaseStorageService supabaseStorageService,
                        FileRepository fileRepository,
                        TransferService transferService) {

                this.supabaseStorageService = supabaseStorageService;
                this.fileRepository = fileRepository;
                this.transferService = transferService;
        }

        // =========================
        // HOME
        // =========================

        @GetMapping
        public String home() {
                return "index";
        }

        // =========================
        // ENVIAR FICHEIROS
        // =========================

        @PostMapping("/send_files")
        @ResponseBody
        public ResponseEntity<String> send_files(
                        @RequestParam(value = "files", required = false) MultipartFile[] files,
                        @RequestParam("code") String code) {

                if (files == null || files.length == 0) {

                        return ResponseEntity
                                        .badRequest()
                                        .body("Nenhum ficheiro foi enviado.");
                }

                try {

                        Transfer transfer = transferService.getTransferByCode(code);

                        for (MultipartFile file : files) {

                                if (file.isEmpty()) {
                                        continue;
                                }

                                String originalFilename = file.getOriginalFilename();

                                if (originalFilename == null ||
                                                originalFilename.isBlank()) {

                                        originalFilename = "file";
                                }

                                String safeFilename = originalFilename
                                                .replaceAll(
                                                                "[^a-zA-Z0-9._-]",
                                                                "_");

                                String storageFilename = UUID.randomUUID()
                                                + "-"
                                                + safeFilename;

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
                        }

                        return ResponseEntity.ok(
                                        "Ficheiros enviados com sucesso.");

                } catch (TransferExpiredException e) {

                        return ResponseEntity
                                        .status(410)
                                        .body("Esta transferência expirou.");

                } catch (RuntimeException e) {

                        return ResponseEntity
                                        .notFound()
                                        .build();

                } catch (Exception e) {

                        return ResponseEntity
                                        .internalServerError()
                                        .body(
                                                        "Erro ao enviar ficheiros.");
                }
        }

        // =========================
        // RECEBER TRANSFERÊNCIA
        // =========================

        @GetMapping("/receive")
        public String receivePage() {

                return "receive";
        }

        @GetMapping("/receive/{code}")
        public String receiveTransfer(
                        @PathVariable String code,
                        Model model) {

                try {

                        Transfer transfer = transferService.getTransferByCode(code);

                        model.addAttribute(
                                        "transfer",
                                        transfer);

                        model.addAttribute(
                                        "files",
                                        transfer.getFiles());

                        return "receive";

                } catch (TransferExpiredException e) {

                        model.addAttribute(
                                        "error",
                                        "Esta transferência expirou.");

                        return "receive";

                } catch (RuntimeException e) {

                        model.addAttribute(
                                        "error",
                                        "Transferência não encontrada.");

                        return "receive";
                }
        }

        // =========================
        // DOWNLOAD
        // =========================

        @GetMapping("/download/{code}/{id}")
        public ResponseEntity<byte[]> downloadFile(
                        @PathVariable String code,
                        @PathVariable Long id) {

                FileEntity file = fileRepository.findById(id)
                                .orElseThrow();

                Transfer transfer = file.getTransfer();

                // Verifica se o código corresponde
                // à transferência e se ainda está válida.
                if (!transfer.getCode().equals(code)) {

                        return ResponseEntity
                                        .notFound()
                                        .build();
                }

                transferService.getTransferByCode(
                                transfer.getCode());

                byte[] fileBytes = supabaseStorageService.downloadFile(
                                file.getStoragePath());

                return ResponseEntity.ok()
                                .header(
                                                HttpHeaders.CONTENT_DISPOSITION,
                                                "attachment; filename=\""
                                                                + file.getFilename()
                                                                + "\"")
                                .contentType(
                                                MediaType.parseMediaType(
                                                                file.getContentType()))
                                .body(fileBytes);
        }

        // =========================
        // TESTE DELETE
        // =========================

        @GetMapping("/test-delete")
        @ResponseBody
        public String testDelete() {

                supabaseStorageService.deleteFile(
                        "49529c5b-7b40-42d1-a5a7-373d013c2a45-CertidaoNascimentoCaioAtualizado.pdf");

                return "Ficheiro apagado";
        }

}
