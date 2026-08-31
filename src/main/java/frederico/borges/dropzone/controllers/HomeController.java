package frederico.borges.dropzone.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class HomeController {

    @GetMapping
    public String home() {
        return "index";
    }

    @PostMapping("/send_files")
    public ResponseEntity<String> send_files(
            @RequestParam("file") MultipartFile file) {

        String filename = file.getOriginalFilename();

        return ResponseEntity.ok(
                "O nome do arquivo recebido é: " + filename
        );
    }
}
