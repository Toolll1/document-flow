package ru.rosatom.documentflow.controllers;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("@documentProcessSecurityService.isCanManageProcess(#documentId, authentication.principal.id) && hasAuthority('USER')")
    public DocProcessDto createNewProcess(@PathVariable Long documentId, @PathVariable Long recipientId) {
        return modelMapper.map(
                documentProcessService.createNewProcess(documentId, recipientId),
                DocProcessDto.class
        );
    }

    @GetMapping("{documentId}/processes")
    @PreAuthorize("@documentProcessSecurityService.isHasAccess(#documentId, authentication.principal.id) && hasAuthority('USER')")
    public Collection<DocProcessDto> findProcessByDocumentId(@PathVariable Long documentId) {
        return documentProcessService.findProcessesByDocumentId(documentId)
                .stream()
                .map(docProcess -> modelMapper.map(docProcess, DocProcessDto.class))
                .collect(Collectors.toList());
    }

    @GetMapping("/processes/{processId}")
    @PreAuthorize("@documentProcessSecurityService.isHasAccess(#processId, authentication.principal.id)")
    public DocProcessDto findProcessById(@PathVariable Long processId) {
        return modelMapper.map(documentProcessService.findProcessById(processId), DocProcessDto.class);
    }

    @DeleteMapping("/processes/{processId}")
    @PreAuthorize("@documentProcessSecurityService.isCanManageProcess(#processId, authentication.principal.id) " +
            "&& !@documentProcessSecurityService.isProcessDone(#processId) && hasAuthority('USER')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProcess(@PathVariable Long processId) {
        documentProcessService.deleteProcess(processId);
    }

    @PatchMapping("/processes/{processId}/send-to-approve")
    @PreAuthorize("@documentProcessSecurityService.isHasAccess(#processUpdateRequestDto.processId, authentication.principal.id) && hasAuthority('USER')")
    public void sendToApprove(ProcessUpdateRequestDto processUpdateRequestDto) {
        ProcessUpdateRequest processUpdateRequest = modelMapper.map(processUpdateRequestDto, ProcessUpdateRequest.class);
        documentProcessService.sendToApprove(processUpdateRequest);
    }

    @PatchMapping("/processes/{processId}/approve")
    @PreAuthorize("@documentProcessSecurityService.isRecipient(#processUpdateRequestDto.processId, authentication.principal.id) && hasAuthority('USER')")
    public void approve(ProcessUpdateRequestDto processUpdateRequestDto) {
        ProcessUpdateRequest processUpdateRequest = modelMapper.map(processUpdateRequestDto, ProcessUpdateRequest.class);
        documentProcessService.approve(processUpdateRequest);
    }

    @PatchMapping("/processes/{processId}/reject")
    @PreAuthorize("@documentProcessSecurityService.isRecipient(#processUpdateRequestDto.processId, authentication.principal.id) && hasAuthority('USER')")
    public void reject(ProcessUpdateRequestDto processUpdateRequestDto) {
        ProcessUpdateRequest processUpdateRequest = modelMapper.map(processUpdateRequestDto, ProcessUpdateRequest.class);
        documentProcessService.reject(processUpdateRequest);
    }

    @PatchMapping("/processes/{processId}/send-to-correction")
    @PreAuthorize("@documentProcessSecurityService.isRecipient(#processUpdateRequestDto.processId, authentication.principal.id) && hasAuthority('USER')")
    public void sendToCorrection(ProcessUpdateRequestDto processUpdateRequestDto) {
        ProcessUpdateRequest processUpdateRequest = modelMapper.map(processUpdateRequestDto, ProcessUpdateRequest.class);
        documentProcessService.sendToCorrection(processUpdateRequest);
    }

    @PatchMapping("/processes/{processId}/delegate-to-other-user")
    @PreAuthorize("@documentProcessSecurityService.isRecipient(#processUpdateRequestDto.processId, authentication.principal.id) && hasAuthority('USER')")
    public DocProcessDto delegateToOtherUser(ProcessUpdateRequestDto processUpdateRequestDto,
                                    @RequestParam Long recipientId) {
        ProcessUpdateRequest processUpdateRequest = modelMapper.map(processUpdateRequestDto, ProcessUpdateRequest.class);
        return modelMapper.map(
                documentProcessService.delegateToOtherUser(processUpdateRequest, recipientId),
                DocProcessDto.class
        );
    }
}
