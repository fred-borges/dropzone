package frederico.borges.dropzone.repositories;


import frederico.borges.dropzone.entities.Transfer;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TransferRepository extends JpaRepository<Transfer, Long> {

    Optional<Transfer> findByCode(String code);

    List<Transfer> findByExpiresAtBefore(LocalDateTime dateTime);
    
}
