package frederico.borges.dropzone.services;

import org.springframework.scheduling.annotation.Scheduled;
import frederico.borges.dropzone.entities.FileEntity;
import frederico.borges.dropzone.entities.Transfer;
import frederico.borges.dropzone.repositories.FileRepository;
import frederico.borges.dropzone.repositories.TransferRepository;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class CleanupService {

    private final TransferRepository transferRepository;
    private final FileRepository fileRepository;
    private final SupabaseStorageService supabaseStorageService;

    public CleanupService(
            TransferRepository transferRepository,
            FileRepository fileRepository,
            SupabaseStorageService supabaseStorageService) {

        this.transferRepository = transferRepository;
        this.fileRepository = fileRepository;
        this.supabaseStorageService = supabaseStorageService;
    }

    @Scheduled(fixedRate = 10000)
    public void cleanupExpiredTransfers() {

        List<Transfer> expiredTransfers =
                transferRepository.findByExpiresAtBefore(
                        LocalDateTime.now()
                );
        
        for (Transfer transfer : expiredTransfers) {

            List<FileEntity> files = fileRepository.findByTransfer(transfer);

            for (FileEntity file : files) {

                if (file.getStoragePath() != null) {

                    try {
                        supabaseStorageService.deleteFile(
                                file.getStoragePath());

                        System.out.println(
                                "Ficheiro apagado do Storage: "
                                        + file.getStoragePath());

                    } catch (Exception e) {

                        System.out.println(
                                "Ficheiro não encontrado no Storage: "
                                        + file.getStoragePath());
                    }
                }

                fileRepository.delete(file);

                System.out.println(
                        "Registo do ficheiro apagado da BD: "
                                + file.getFilename());
            }

            transferRepository.delete(transfer);

            System.out.println(
                    "Transfer apagada da BD: "
                            + transfer.getCode());
        }

        List<FileEntity> orphanFiles = fileRepository.findByTransferIsNull();

        for (FileEntity file : orphanFiles) {

            if (file.getStoragePath() != null) {

                try {
                    supabaseStorageService.deleteFile(
                            file.getStoragePath());

                    System.out.println(
                            "Ficheiro apagado do Storage: "
                                    + file.getStoragePath());

                } catch (Exception e) {

                    System.out.println(
                            "Ficheiro não encontrado no Storage: "
                                    + file.getStoragePath());
                }
            }

            fileRepository.delete(file);

            System.out.println(
                    "Registo apagado da BD: "
                            + file.getFilename());
        }
    }
}