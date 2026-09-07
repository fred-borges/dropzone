package frederico.borges.dropzone.controllers;

import frederico.borges.dropzone.entities.Transfer;
import frederico.borges.dropzone.services.TransferService;
import frederico.borges.dropzone.status.TransferStatus;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TransferControllerTest {

    // =========================
    // CRIAR TRANSFERÊNCIA
    // =========================

    @Test
    void shouldCreateTransfer() {

        TransferService transferService = mock(TransferService.class);

        TransferController controller = new TransferController(transferService);

        Transfer transfer = new Transfer();

        transfer.setId(1L);
        transfer.setCode("123456");
        transfer.setCreatedAt(
                LocalDateTime.now());
        transfer.setExpiresAt(
                LocalDateTime.now().plusHours(24));
        transfer.setStatus(
                TransferStatus.PENDING);

        when(transferService.createTransfer())
                .thenReturn(transfer);

        Transfer result = controller.createTransfer();

        assertNotNull(result);

        assertEquals(
                1L,
                result.getId());

        assertEquals(
                "123456",
                result.getCode());

        assertEquals(
                TransferStatus.PENDING,
                result.getStatus());

        verify(transferService)
                .createTransfer();
    }

    // =========================
    // PROCURAR TRANSFERÊNCIA
    // =========================

    @Test
    void shouldGetTransfer() {

        TransferService transferService = mock(TransferService.class);

        TransferController controller = new TransferController(transferService);

        Transfer transfer = new Transfer();

        transfer.setId(1L);
        transfer.setCode("123456");
        transfer.setStatus(
                TransferStatus.PENDING);

        when(
                transferService.getTransferByCode(
                        "123456"))
                .thenReturn(transfer);

        Transfer result = controller.getTransfer("123456");

        assertNotNull(result);

        assertEquals(
                "123456",
                result.getCode());

        assertEquals(
                TransferStatus.PENDING,
                result.getStatus());

        verify(
                transferService)
                .getTransferByCode("123456");
    }

    // =========================
    // ELIMINAR TRANSFERÊNCIA
    // =========================

    @Test
    void shouldDeleteTransfer() {

        TransferService transferService = mock(TransferService.class);

        TransferController controller = new TransferController(transferService);

        var response = controller.deleteTransfer("123456");

        assertEquals(
                204,
                response.getStatusCode().value());

        verify(
                transferService)
                .deleteTransfer("123456");
    }
}