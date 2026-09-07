package frederico.borges.dropzone.services;

import frederico.borges.dropzone.entities.Transfer;
import frederico.borges.dropzone.repositories.TransferRepository;
import frederico.borges.dropzone.status.TransferStatus;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransferServiceTest {

    @Mock
    private TransferRepository transferRepository;

    @Mock
    private FileService fileService;

    @InjectMocks
    private TransferService transferService;

    // =========================
    // CONFIGURAÇÃO
    // =========================

    @BeforeEach
    void setUp() {
        // Os mocks são criados automaticamente pelo Mockito.
    }

    // =========================
    // CRIAR TRANSFERÊNCIA
    // =========================

    @Test
    void shouldCreateTransfer() {

        when(transferRepository.findByCode(anyString()))
                .thenReturn(Optional.empty());

        when(transferRepository.save(any(Transfer.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Transfer transfer = transferService.createTransfer();

        assertNotNull(transfer);

        assertNotNull(transfer.getCode());

        assertEquals(6, transfer.getCode().length());

        assertNotNull(transfer.getCreatedAt());

        assertNotNull(transfer.getExpiresAt());

        assertEquals(
                TransferStatus.PENDING,
                transfer.getStatus());

        verify(transferRepository)
                .save(any(Transfer.class));
    }

    // =========================
    // PROCURAR TRANSFERÊNCIA
    // =========================

    @Test
    void shouldFindTransferByCode() {

        Transfer transfer = new Transfer();

        transfer.setCode("123456");

        transfer.setCreatedAt(
                java.time.LocalDateTime.now());

        transfer.setExpiresAt(
                java.time.LocalDateTime.now()
                        .plusHours(24));

        transfer.setStatus(
                TransferStatus.PENDING);

        when(transferRepository.findByCode("123456"))
                .thenReturn(Optional.of(transfer));

        Transfer result = transferService.getTransferByCode("123456");

        assertNotNull(result);

        assertEquals(
                "123456",
                result.getCode());

        verify(transferRepository)
                .findByCode("123456");
    }

    @Test
    void shouldThrowExceptionWhenTransferDoesNotExist() {

        when(transferRepository.findByCode("999999"))
                .thenReturn(Optional.empty());

        assertThrows(
                frederico.borges.dropzone.exceptions.TransferNotFoundException.class,
                () -> transferService.getTransferByCode("999999")
        );

        verify(transferRepository)
                .findByCode("999999");
    }

    @Test
    void shouldThrowExceptionWhenTransferIsExpired() {

        Transfer transfer = new Transfer();

        transfer.setCode("123456");

        transfer.setCreatedAt(
                java.time.LocalDateTime.now()
                        .minusHours(25));

        transfer.setExpiresAt(
                java.time.LocalDateTime.now()
                        .minusHours(1));

        transfer.setStatus(
                TransferStatus.PENDING);

        when(transferRepository.findByCode("123456"))
                .thenReturn(Optional.of(transfer));

        assertThrows(
                frederico.borges.dropzone.exceptions.TransferExpiredException.class,
                () -> transferService.getTransferByCode("123456"));

        assertEquals(
                TransferStatus.EXPIRED,
                transfer.getStatus());

        verify(transferRepository)
                .save(transfer);
    }
}