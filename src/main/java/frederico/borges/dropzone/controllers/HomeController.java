package frederico.borges.dropzone.controllers;

import frederico.borges.dropzone.entities.FileEntity;
import frederico.borges.dropzone.entities.Transfer;
import frederico.borges.dropzone.exceptions.TransferExpiredException;
import frederico.borges.dropzone.services.FileService;
import frederico.borges.dropzone.services.TransferService;
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

@Controller
public class HomeController {

        private final FileService fileService;
        private final TransferService transferService;

        public HomeController(
                        FileService fileService,
                        TransferService transferService) {

                this.fileService = fileService;
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
        public ResponseEntity<String> sendFiles(
                        @RequestParam(value = "files", required = false) MultipartFile[] files,
                        @RequestParam("code") String code) {

                if (files == null || files.length == 0) {

                        return ResponseEntity
                                        .badRequest()
                                        .body("Nenhum ficheiro foi enviado.");
                }

                try {

                        Transfer transfer = transferService.getTransferByCode(code);

                        fileService.uploadFiles(
                                        files,
                                        transfer);

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
                                        .body("Erro ao enviar ficheiros.");
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

                try {

                FileEntity file =
                        fileService.findById(id);

                Transfer transfer =
                        file.getTransfer();

                if (!transfer.getCode().equals(code)) {

                        return ResponseEntity
                                .notFound()
                                .build();
                }

                transferService.getTransferByCode(
                        transfer.getCode());

                byte[] fileBytes =
                        fileService.downloadFile(file);

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

                } catch (TransferExpiredException e) {

                return ResponseEntity
                        .status(410)
                        .build();

                } catch (RuntimeException e) {

                return ResponseEntity
                        .notFound()
                        .build();
                }
                }
}
