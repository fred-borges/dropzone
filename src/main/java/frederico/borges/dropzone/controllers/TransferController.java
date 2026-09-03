package frederico.borges.dropzone.controllers;

import frederico.borges.dropzone.entities.Transfer;
import frederico.borges.dropzone.services.TransferService;
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
}