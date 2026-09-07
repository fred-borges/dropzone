package frederico.borges.dropzone.entities;

import frederico.borges.dropzone.status.TransferStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
public class Transfer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;

    private LocalDateTime createdAt;

    private LocalDateTime expiresAt;

    @Enumerated(EnumType.STRING)
    private TransferStatus status;

    @OneToMany(mappedBy = "transfer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FileEntity> files;
}