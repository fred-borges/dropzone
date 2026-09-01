package frederico.borges.dropzone.repositories;

import frederico.borges.dropzone.entities.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<FileEntity, Long> {
}
