package frederico.borges.dropzone.services;

import frederico.borges.dropzone.entities.Transfer;
import frederico.borges.dropzone.repositories.TransferRepository;
import frederico.borges.dropzone.status.TransferStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class TransferService {

    private final TransferRepository transferRepository;

    public TransferService(TransferRepository transferRepository) {
        this.transferRepository = transferRepository;
    }

    public Transfer createTransfer() {

        Transfer transfer = new Transfer();

        int code = (int) (Math.random() * 900000) + 100000;

        transfer.setCode(String.valueOf(code));
        transfer.setCreatedAt(LocalDateTime.now());
        transfer.setExpiresAt(LocalDateTime.now().plusHours(24));
        transfer.setStatus(TransferStatus.PENDING);

        return transferRepository.save(transfer);
    }

    public Transfer getTransferByCode(String code) {

        System.out.println("Código recebido: [" + code + "]");

        Optional<Transfer> result = transferRepository.findByCode(code);

        System.out.println("Encontrou transferência: " + result.isPresent());

        return result.orElseThrow(() -> new RuntimeException("Transfer not found"));
    }
}