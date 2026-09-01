package frederico.borges.dropzone.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name="files")
@Data
public class FileEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    private String filename;

    private String storagePath;

    private Long size;

    private String contentType;

    private LocalDateTime createdAt;
}
