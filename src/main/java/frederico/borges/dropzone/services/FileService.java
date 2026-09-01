package frederico.borges.dropzone.services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileService {

    private final Path uploadDirectory = Paths.get("uploads");

    public String saveFile(MultipartFile file) throws IOException {

        // Cria a pasta uploads caso ela não exista
        Files.createDirectories(uploadDirectory);

        // Nome original do arquivo
        String filename = file.getOriginalFilename();

        // Caminho completo onde o arquivo será salvo
        Path filePath = uploadDirectory.resolve(filename);

        // Salva o arquivo
        file.transferTo(filePath);

        return filePath.toString();
    }
}