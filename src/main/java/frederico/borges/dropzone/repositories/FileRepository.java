package frederico.borges.dropzone.repositories;

import frederico.borges.dropzone.entities.FileEntity;
import frederico.borges.dropzone.entities.Transfer;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<FileEntity, Long> {
    List<FileEntity> findByTransfer(Transfer transfer);
    
    List<FileEntity> findByTransferIsNull();
}
