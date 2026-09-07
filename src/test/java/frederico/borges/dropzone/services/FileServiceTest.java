package frederico.borges.dropzone.services;

import frederico.borges.dropzone.entities.FileEntity;
import frederico.borges.dropzone.entities.Transfer;
import frederico.borges.dropzone.repositories.FileRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.mock.web.MockMultipartFile;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FileServiceTest {

    @Mock
    private FileRepository fileRepository;

    @Mock
    private SupabaseStorageService supabaseStorageService;

    @InjectMocks
    private FileService fileService;

    // =========================
    // UPLOAD
    // =========================

    @Test
    void shouldUploadFile() throws Exception {

        Transfer transfer = new Transfer();

        transfer.setCode("123456");

        MockMultipartFile file = new MockMultipartFile(
                "files",
                "teste.txt",
                "text/plain",
                "Hello DropZone".getBytes());

        fileService.uploadFiles(
                new MockMultipartFile[] { file },
                transfer);

        verify(supabaseStorageService)
                .uploadFile(
                        anyString(),
                        any(byte[].class),
                        eq("text/plain"));

        ArgumentCaptor<FileEntity> captor = ArgumentCaptor.forClass(FileEntity.class);

        verify(fileRepository)
                .save(captor.capture());

        FileEntity savedFile = captor.getValue();

        assertEquals(
                "teste.txt",
                savedFile.getFilename());

        assertEquals(
                file.getSize(),
                savedFile.getSize());

        assertEquals(
                "text/plain",
                savedFile.getContentType());

        assertEquals(
                transfer,
                savedFile.getTransfer());

        assertNotNull(
                savedFile.getStoragePath());

        assertNotNull(
                savedFile.getCreatedAt());
    }

    // =========================
    // FIND BY ID
    // =========================

    @Test
    void shouldFindFileById() {

        FileEntity file = new FileEntity();

        file.setId(1L);
        file.setFilename("teste.txt");

        when(fileRepository.findById(1L))
                .thenReturn(Optional.of(file));

        FileEntity result = fileService.findById(1L);

        assertNotNull(result);

        assertEquals(
                1L,
                result.getId());

        assertEquals(
                "teste.txt",
                result.getFilename());

        verify(fileRepository)
                .findById(1L);
    }

    // =========================
    // FICHEIRO VAZIO
    // =========================

    @Test
    void shouldIgnoreEmptyFile() throws Exception {

        Transfer transfer = new Transfer();
        transfer.setCode("123456");

        MockMultipartFile file =
                new MockMultipartFile(
                        "files",
                        "empty.txt",
                        "text/plain",
                        new byte[0]
                );

        fileService.uploadFiles(
                new MockMultipartFile[]{file},
                transfer
        );

        verifyNoInteractions(
                supabaseStorageService
        );

        verify(
                fileRepository,
                never()
        ).save(any(FileEntity.class));
    }

    // =========================
    // DOWNLOAD
    // =========================

    @Test
    void shouldDownloadFile() {

        FileEntity file = new FileEntity();

        file.setId(1L);
        file.setFilename("teste.txt");
        file.setStoragePath("abc123-teste.txt");

        byte[] content =
                "Hello DropZone".getBytes();

        when(supabaseStorageService.downloadFile(
                "abc123-teste.txt"))
                .thenReturn(content);

        byte[] result =
                fileService.downloadFile(file);

        assertArrayEquals(
                content,
                result
        );

        verify(
                supabaseStorageService
        ).downloadFile(
                "abc123-teste.txt"
        );
    }

    // =========================
    // DELETE
    // =========================

    @Test
    void shouldDeleteFile() {

        FileEntity file = new FileEntity();

        file.setId(1L);
        file.setFilename("teste.txt");
        file.setStoragePath("abc123-teste.txt");

        fileService.deleteFile(file);

        verify(
                supabaseStorageService
        ).deleteFile(
                "abc123-teste.txt"
        );
    }
}