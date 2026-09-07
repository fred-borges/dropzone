package frederico.borges.dropzone.services;

import frederico.borges.dropzone.entities.Transfer;
import frederico.borges.dropzone.repositories.TransferRepository;
import frederico.borges.dropzone.status.TransferStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

import frederico.borges.dropzone.exceptions.TransferExpiredException;

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
        LocalDateTime now = LocalDateTime.now();

        transfer.setCreatedAt(now);
        transfer.setExpiresAt(now.plusHours(24));
        transfer.setStatus(TransferStatus.PENDING);

        return transferRepository.save(transfer);
    }

    public Transfer getTransferByCode(String code) {

        System.out.println("Código recebido: [" + code + "]");

        Optional<Transfer> result = transferRepository.findByCode(code);

        System.out.println("Encontrou transferência: " + result.isPresent());

        Transfer transfer = result.orElseThrow(
                () -> new RuntimeException("Transfer not found"));

        if (LocalDateTime.now().isAfter(transfer.getExpiresAt())) {

            transfer.setStatus(TransferStatus.EXPIRED);

            transferRepository.save(transfer);

            throw new TransferExpiredException("Transfer expired");
        }

        return transfer;
    }
}