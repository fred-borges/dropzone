
package frederico.borges.dropzone.services;

import frederico.borges.dropzone.services.FileService;
import frederico.borges.dropzone.entities.FileEntity;
import frederico.borges.dropzone.entities.Transfer;
import frederico.borges.dropzone.exceptions.TransferExpiredException;
import frederico.borges.dropzone.exceptions.TransferNotFoundException;
import frederico.borges.dropzone.repositories.TransferRepository;
import frederico.borges.dropzone.status.TransferStatus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;

@Service
public class TransferService {

    private static final Logger log = LoggerFactory.getLogger(TransferService.class);

        
    private final SecureRandom random = new SecureRandom();
    

    
    private final TransferRepository transferRepository;
    private final FileService fileService;

    public TransferService(
            TransferRepository transferRepository,
            FileService fileService) {

        this.transferRepository = transferRepository;
        this.fileService = fileService;
    }
    


    // =========================
    // CRIAR TRANSFERÊNCIA
    // =========================

    public Transfer createTransfer() {

        Transfer transfer = new Transfer();

        String code = generateUniqueCode();

        LocalDateTime now = LocalDateTime.now();

        transfer.setCode(code);
        transfer.setCreatedAt(now);
        transfer.setExpiresAt(
                now.plusHours(24));
        transfer.setStatus(
                TransferStatus.PENDING);

        Transfer savedTransfer = transferRepository.save(transfer);

        log.info(
                "Transferência criada: code={}",
                code);

        return savedTransfer;
    }

    // =========================
    // PROCURAR TRANSFERÊNCIA
    // =========================

    public Transfer getTransferByCode(
            String code) {

        log.debug(
                "A procurar transferência: code={}",
                code);

        Transfer transfer = transferRepository
                .findByCode(code)
                .orElseThrow(() -> {

                    log.warn(
                            "Transferência não encontrada: code={}",
                            code);

                    return new TransferNotFoundException(
                            "Transfer not found");
                });

        if (LocalDateTime.now()
                .isAfter(transfer.getExpiresAt())) {

            transfer.setStatus(
                    TransferStatus.EXPIRED);

            transferRepository.save(
                    transfer);

            log.warn(
                    "Transferência expirada: code={}",
                    code);

            throw new TransferExpiredException(
                    "Transfer expired");
        }

        log.debug(
                "Transferência encontrada: code={}",
                code);

        return transfer;
    }

    // =========================
    // GERAR CÓDIGO
    // =========================

    private String generateUniqueCode() {

        String code;

        do {

            code = String.valueOf(
                    random.nextInt(900000) + 100000);

        } while (
                transferRepository
                        .findByCode(code)
                        .isPresent()
        );

        return code;
    }

        
    // =========================
    // ELIMINAR TRANSFERÊNCIA
    // =========================

    public void deleteTransfer(String code) {

        log.info(
                "A eliminar transferência: code={}",
                code);

        Transfer transfer =
                getTransferByCode(code);

        if (transfer.getFiles() != null) {

            for (FileEntity file : transfer.getFiles()) {

                fileService.deleteFile(file);

            }
        }

        transferRepository.delete(transfer);

        log.info(
                "Transferência eliminada: code={}",
                code);
    }
    

}
