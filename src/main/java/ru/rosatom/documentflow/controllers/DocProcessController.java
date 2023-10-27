package ru.rosatom.documentflow.controllers;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;
import ru.rosatom.documentflow.dto.DocProcessDto;
import ru.rosatom.documentflow.dto.ProcessUpdateRequestDto;
import ru.rosatom.documentflow.models.ProcessUpdateRequest;
import ru.rosatom.documentflow.services.DocumentProcessService;

import java.util.Collection;
import java.util.stream.Collectors;

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

    @GetMapping("{documentId}/processes")
    public Collection<DocProcessDto> findProcessByDocumentId(@PathVariable Long documentId) {
        return documentProcessService.findProcessesByDocumentId(documentId)
                .stream()
                .map(docProcess -> modelMapper.map(docProcess, DocProcessDto.class))
                .collect(Collectors.toList());
    }

    @PatchMapping("/processes/{processId}/send-to-approve")
    public void sendToApprove(ProcessUpdateRequestDto processUpdateRequestDto) {
        ProcessUpdateRequest processUpdateRequest = modelMapper.map(processUpdateRequestDto, ProcessUpdateRequest.class);
        documentProcessService.sendToApprove(processUpdateRequest);
    }

    @PatchMapping("/processes/{processId}/approve")
    public void approve(ProcessUpdateRequestDto processUpdateRequestDto) {
        ProcessUpdateRequest processUpdateRequest = modelMapper.map(processUpdateRequestDto, ProcessUpdateRequest.class);
        documentProcessService.approve(processUpdateRequest);
    }

    @PatchMapping("/processes/{processId}/reject")
    public void reject(ProcessUpdateRequestDto processUpdateRequestDto) {
        ProcessUpdateRequest processUpdateRequest = modelMapper.map(processUpdateRequestDto, ProcessUpdateRequest.class);
        documentProcessService.reject(processUpdateRequest);
    }

    @PatchMapping("/processes/{processId}/send-to-correction")
    public void sendToCorrection(ProcessUpdateRequestDto processUpdateRequestDto) {
        ProcessUpdateRequest processUpdateRequest = modelMapper.map(processUpdateRequestDto, ProcessUpdateRequest.class);
        documentProcessService.sendToCorrection(processUpdateRequest);
    }


}
