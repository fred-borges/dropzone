package frederico.borges.dropzone.controllers;

import frederico.borges.dropzone.entities.Transfer;
import frederico.borges.dropzone.services.TransferService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transfer")
public class TransferController {

    private final TransferService transferService;

    public TransferController(TransferService transferService) {
        this.transferService = transferService;
    }

    @PostMapping
    public Transfer createTransfer() {
        return transferService.createTransfer();
    }

    @GetMapping("/{code}")
    public Transfer getTransfer(@PathVariable String code) {
        return transferService.getTransferByCode(code);
    }

    @DeleteMapping("/{code}")
    public ResponseEntity<Void> deleteTransfer(
            @PathVariable String code) {

        transferService.deleteTransfer(code);

        return ResponseEntity.noContent().build();
    }

}