package ru.rosatom.documentflow.controllers;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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

    @PostMapping("/process/{processId}/send-to-approve")
    public DocProcessDto sendToApprove(@PathVariable Long processId) {
        return modelMapper.map(
                documentProcessService.sendToApprove(processId),
                DocProcessDto.class
        );
    }

}
