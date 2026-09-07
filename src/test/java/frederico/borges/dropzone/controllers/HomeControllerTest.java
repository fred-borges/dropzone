package frederico.borges.dropzone.controllers;

import frederico.borges.dropzone.entities.FileEntity;
import frederico.borges.dropzone.entities.Transfer;
import frederico.borges.dropzone.exceptions.TransferExpiredException;
import frederico.borges.dropzone.services.FileService;
import frederico.borges.dropzone.services.TransferService;

import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class HomeControllerTest {

    // =========================
    // HOME
    // =========================

    @Test
    void shouldReturnHomePage() {

        FileService fileService = mock(FileService.class);

        TransferService transferService = mock(TransferService.class);

        HomeController controller = new HomeController(
                fileService,
                transferService);

        String result = controller.home();

        assertEquals(
                "index",
                result);
    }

    // =========================
    // RECEBER PÁGINA
    // =========================

    @Test
    void shouldReturnReceivePage() {

        FileService fileService = mock(FileService.class);

        TransferService transferService = mock(TransferService.class);

        HomeController controller = new HomeController(
                fileService,
                transferService);

        String result = controller.receivePage();

        assertEquals(
                "receive",
                result);
    }

    // =========================
    // ENVIAR FICHEIROS
    // =========================

    @Test
    void shouldUploadFiles() throws Exception {

        FileService fileService = mock(FileService.class);

        TransferService transferService = mock(TransferService.class);

        HomeController controller = new HomeController(
                fileService,
                transferService);

        Transfer transfer = new Transfer();

        transfer.setCode("123456");

        MockMultipartFile file = new MockMultipartFile(
                "files",
                "teste.txt",
                "text/plain",
                "Hello DropZone".getBytes());

        when(
                transferService.getTransferByCode(
                        "123456"))
                .thenReturn(transfer);

        ResponseEntity<String> response = controller.sendFiles(
                new MockMultipartFile[] { file },
                "123456");

        assertEquals(
                200,
                response.getStatusCode().value());

        assertEquals(
                "Ficheiros enviados com sucesso.",
                response.getBody());

        verify(
                transferService)
                .getTransferByCode("123456");

        verify(
                fileService)
                .uploadFiles(
                        new MockMultipartFile[] { file },
                        transfer);
    }

    // =========================
    // NENHUM FICHEIRO
    // =========================

    @Test
    void shouldRejectEmptyFileList() {

        FileService fileService = mock(FileService.class);

        TransferService transferService = mock(TransferService.class);

        HomeController controller = new HomeController(
                fileService,
                transferService);

        ResponseEntity<String> response = controller.sendFiles(
                new MockMultipartFile[] {},
                "123456");

        assertEquals(
                400,
                response.getStatusCode().value());

        assertEquals(
                "Nenhum ficheiro foi enviado.",
                response.getBody());

        verifyNoInteractions(
                transferService,
                fileService);
    }

    // =========================
    // TRANSFERÊNCIA EXPIRADA
    // =========================

    @Test
    void shouldReturnExpiredTransferError() {

        FileService fileService = mock(FileService.class);

        TransferService transferService = mock(TransferService.class);

        HomeController controller = new HomeController(
                fileService,
                transferService);

        when(
                transferService.getTransferByCode(
                        "123456"))
                .thenThrow(
                        new TransferExpiredException(
                                "Transfer expired"));

        ResponseEntity<String> response = controller.sendFiles(
                new MockMultipartFile[] {
                        new MockMultipartFile(
                                "files",
                                "teste.txt",
                                "text/plain",
                                "teste".getBytes())
                },
                "123456");

        assertEquals(
                410,
                response.getStatusCode().value());

        assertEquals(
                "Esta transferência expirou.",
                response.getBody());
    }

    // =========================
    // RECEBER TRANSFERÊNCIA
    // =========================

    @Test
    void shouldLoadReceiveTransfer() {

        FileService fileService = mock(FileService.class);

        TransferService transferService = mock(TransferService.class);

        HomeController controller = new HomeController(
                fileService,
                transferService);

        Transfer transfer = new Transfer();

        transfer.setCode("123456");

        FileEntity file = new FileEntity();

        file.setId(1L);
        file.setFilename("teste.txt");

        transfer.setFiles(
                List.of(file));

        when(
                transferService.getTransferByCode(
                        "123456"))
                .thenReturn(transfer);

        Model model = new ExtendedModelMap();

        String result = controller.receiveTransfer(
                "123456",
                model);

        assertEquals(
                "receive",
                result);

        assertEquals(
                transfer,
                model.getAttribute("transfer"));

        assertEquals(
                transfer.getFiles(),
                model.getAttribute("files"));

        verify(
                transferService)
                .getTransferByCode("123456");
    }

    // =========================
    // DOWNLOAD
    // =========================

    @Test
    void shouldDownloadFile() {

        FileService fileService = mock(FileService.class);

        TransferService transferService = mock(TransferService.class);

        HomeController controller = new HomeController(
                fileService,
                transferService);

        Transfer transfer = new Transfer();

        transfer.setCode("123456");
        transfer.setExpiresAt(
                LocalDateTime.now().plusHours(24));

        FileEntity file = new FileEntity();

        file.setId(1L);
        file.setFilename("teste.txt");
        file.setContentType("text/plain");
        file.setTransfer(transfer);

        byte[] content = "Hello DropZone".getBytes();

        when(
                fileService.findById(1L))
                .thenReturn(file);

        when(
                fileService.downloadFile(file))
                .thenReturn(content);

        when(
                transferService.getTransferByCode(
                        "123456"))
                .thenReturn(transfer);

        ResponseEntity<byte[]> response = controller.downloadFile(
                "123456",
                1L);

        assertEquals(
                200,
                response.getStatusCode().value());

        assertArrayEquals(
                content,
                response.getBody());

        verify(
                fileService)
                .downloadFile(file);

        verify(
                transferService)
                .getTransferByCode("123456");
    }

    // =========================
    // TRANSFERÊNCIA INEXISTENTE
    // =========================

    @Test
    void shouldReturnNotFoundWhenTransferDoesNotExist() {

        FileService fileService = mock(FileService.class);

        TransferService transferService = mock(TransferService.class);

        HomeController controller = new HomeController(
                fileService,
                transferService);

        when(
                transferService.getTransferByCode(
                        "999999"))
                .thenThrow(
                        new RuntimeException(
                                "Transfer not found"));

        ResponseEntity<String> response = controller.sendFiles(
                new MockMultipartFile[] {
                        new MockMultipartFile(
                                "files",
                                "teste.txt",
                                "text/plain",
                                "teste".getBytes())
                },
                "999999");

        assertEquals(
                404,
                response.getStatusCode().value());
    }

    // =========================
    // RECEIVE - NÃO ENCONTRADA
    // =========================

    @Test
    void shouldShowErrorWhenReceiveTransferDoesNotExist() {

        FileService fileService = mock(FileService.class);

        TransferService transferService = mock(TransferService.class);

        HomeController controller = new HomeController(
                fileService,
                transferService);

        when(
                transferService.getTransferByCode(
                        "999999"))
                .thenThrow(
                        new RuntimeException(
                                "Transfer not found"));

        Model model = new ExtendedModelMap();

        String result = controller.receiveTransfer(
                "999999",
                model);

        assertEquals(
                "receive",
                result);

        assertEquals(
                "Transferência não encontrada.",
                model.getAttribute("error"));
    }

    // =========================
    // RECEIVE - EXPIRADA
    // =========================

    @Test
    void shouldShowErrorWhenReceiveTransferIsExpired() {

        FileService fileService = mock(FileService.class);

        TransferService transferService = mock(TransferService.class);

        HomeController controller = new HomeController(
                fileService,
                transferService);

        when(
                transferService.getTransferByCode(
                        "123456"))
                .thenThrow(
                        new TransferExpiredException(
                                "Transfer expired"));

        Model model = new ExtendedModelMap();

        String result = controller.receiveTransfer(
                "123456",
                model);

        assertEquals(
                "receive",
                result);

        assertEquals(
                "Esta transferência expirou.",
                model.getAttribute("error"));
    }

    // =========================
    // DOWNLOAD - CÓDIGO ERRADO
    // =========================

    @Test
    void shouldReturnNotFoundWhenDownloadCodeIsWrong() {

        FileService fileService = mock(FileService.class);

        TransferService transferService = mock(TransferService.class);

        HomeController controller = new HomeController(
                fileService,
                transferService);

        Transfer transfer = new Transfer();

        transfer.setCode("123456");

        FileEntity file = new FileEntity();

        file.setId(1L);
        file.setFilename("teste.txt");
        file.setTransfer(transfer);

        when(
                fileService.findById(1L))
                .thenReturn(file);

        ResponseEntity<byte[]> response = controller.downloadFile(
                "999999",
                1L);

        assertEquals(
                404,
                response.getStatusCode().value());

        verify(
                fileService,
                never())
                .downloadFile(file);
    }

    // =========================
    // DOWNLOAD - EXPIRADA
    // =========================

    @Test
    void shouldReturnGoneWhenDownloadTransferIsExpired() {

        FileService fileService = mock(FileService.class);

        TransferService transferService = mock(TransferService.class);

        HomeController controller = new HomeController(
                fileService,
                transferService);

        Transfer transfer = new Transfer();

        transfer.setCode("123456");

        FileEntity file = new FileEntity();

        file.setId(1L);
        file.setFilename("teste.txt");
        file.setContentType("text/plain");
        file.setTransfer(transfer);

        when(
                fileService.findById(1L))
                .thenReturn(file);

        when(
                transferService.getTransferByCode(
                        "123456"))
                .thenThrow(
                        new TransferExpiredException(
                                "Transfer expired"));

        ResponseEntity<byte[]> response = controller.downloadFile(
                "123456",
                1L);

        assertEquals(
                410,
                response.getStatusCode().value());

        verify(
                fileService,
                never())
                .downloadFile(file);
    }
}