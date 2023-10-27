package ru.rosatom.documentflow.controllers;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.rosatom.documentflow.dto.DocProcessDto;
import ru.rosatom.documentflow.dto.ProcessUpdateRequestDto;
import ru.rosatom.documentflow.models.ProcessUpdateRequest;
import ru.rosatom.documentflow.services.DocumentProcessService;
import ru.rosatom.documentflow.services.impl.DocumentProcessSecurityService;

import java.util.Collection;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/document")
@AllArgsConstructor
public class DocProcessController {

    private final DocumentProcessService documentProcessService;
    private final DocumentProcessSecurityService documentProcessSecurityService;
    private final ModelMapper modelMapper;


    @PostMapping("/{documentId}/recipient/{recipientId}/new-process")
    @PreAuthorize("documentProcessSecurityService.isCanCreateProcess(#documentId, authentication.principal.id)")
    public DocProcessDto createNewProcess(@PathVariable Long documentId, @PathVariable Long recipientId) {
        return modelMapper.map(
                documentProcessService.createNewProcess(documentId, recipientId),
                DocProcessDto.class
        );
    }

    @GetMapping("{documentId}/processes")
    @PreAuthorize("documentProcessSecurityService.isHasAccess(#documentId, authentication.principal.id)")
    public Collection<DocProcessDto> findProcessByDocumentId(@PathVariable Long documentId) {
        return documentProcessService.findProcessesByDocumentId(documentId)
                .stream()
                .map(docProcess -> modelMapper.map(docProcess, DocProcessDto.class))
                .collect(Collectors.toList());
    }

    @PatchMapping("/processes/{processId}/send-to-approve")
    @PreAuthorize("documentProcessSecurityService.isHasAccess(#processUpdateRequestDto.processId, authentication.principal.id)")
    public void sendToApprove(ProcessUpdateRequestDto processUpdateRequestDto) {
        ProcessUpdateRequest processUpdateRequest = modelMapper.map(processUpdateRequestDto, ProcessUpdateRequest.class);
        documentProcessService.sendToApprove(processUpdateRequest);
    }

    @PatchMapping("/processes/{processId}/approve")
    @PreAuthorize("documentProcessSecurityService.isHasAccess(#processUpdateRequestDto.processId, authentication.principal.id)")
    public void approve(ProcessUpdateRequestDto processUpdateRequestDto) {
        ProcessUpdateRequest processUpdateRequest = modelMapper.map(processUpdateRequestDto, ProcessUpdateRequest.class);
        documentProcessService.approve(processUpdateRequest);
    }

    @PatchMapping("/processes/{processId}/reject")
    @PreAuthorize("documentProcessSecurityService.isHasAccess(#processUpdateRequestDto.processId, authentication.principal.id)")
    public void reject(ProcessUpdateRequestDto processUpdateRequestDto) {
        ProcessUpdateRequest processUpdateRequest = modelMapper.map(processUpdateRequestDto, ProcessUpdateRequest.class);
        documentProcessService.reject(processUpdateRequest);
    }

    @PatchMapping("/processes/{processId}/send-to-correction")
    @PreAuthorize("documentProcessSecurityService.isHasAccess(#processUpdateRequestDto.processId, authentication.principal.id)")
    public void sendToCorrection(ProcessUpdateRequestDto processUpdateRequestDto) {
        ProcessUpdateRequest processUpdateRequest = modelMapper.map(processUpdateRequestDto, ProcessUpdateRequest.class);
        documentProcessService.sendToCorrection(processUpdateRequest);
    }


}
