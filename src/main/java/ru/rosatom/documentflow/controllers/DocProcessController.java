package ru.rosatom.documentflow.controllers;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;
import ru.rosatom.documentflow.dto.DocProcessDto;
import ru.rosatom.documentflow.services.DocumentProcessService;

@RestController
@RequestMapping("/v1/document")
@AllArgsConstructor
public class DocProcessController {

    private final DocumentProcessService documentProcessService;
    private final ModelMapper modelMapper;


    @PostMapping("/{documentId}/recipient/{recipientId}/new-process")
    public DocProcessDto createNewProcess(@PathVariable Long documentId, @PathVariable Long recipientId) {
        return modelMapper.map(
                documentProcessService.createNewProcess(documentId, recipientId),
                DocProcessDto.class
        );
    }

    @PatchMapping("/process/{processId}/send-to-approve")
    public void sendToApprove(@PathVariable Long processId) {
        documentProcessService.sendToApprove(processId);
    }

    @PatchMapping("/process/{processId}/approve")
    public void approve(@PathVariable Long processId) {
        documentProcessService.approve(processId);
    }

}
