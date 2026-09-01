package frederico.borges.dropzone.controllers;

import frederico.borges.dropzone.services.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
public class HomeController {

    @Autowired
    private FileService fileService;

    @GetMapping
    public String home() {
        return "index";
    }

    @PostMapping("/send_files")
    public ResponseEntity<String> send_files(
            @RequestParam("file") MultipartFile file
    ) {

        if (file.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body("Nenhum arquivo foi enviado.");
        }

        try {
            String path = fileService.saveFile(file);

            return ResponseEntity.ok(
                    "Arquivo salvo em: " + path
            );

        } catch (IOException e) {
            return ResponseEntity.internalServerError()
                    .body("Erro ao salvar o arquivo.");
        }
    }
}
