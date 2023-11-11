package ru.rosatom.documentflow.controllers;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.rosatom.documentflow.dto.DocProcessDto;
import ru.rosatom.documentflow.dto.ProcessUpdateRequestDto;
import ru.rosatom.documentflow.models.ProcessUpdateRequest;
import ru.rosatom.documentflow.models.User;
import ru.rosatom.documentflow.services.DocumentProcessService;

import java.util.Collection;
import java.util.List;
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

    @GetMapping("/processes/incoming")
    public List<DocProcessDto> findIncomingProcesses(@AuthenticationPrincipal User currentUser) {
        return documentProcessService.getIncomingProcessesByUserId(currentUser.getId()).stream()
                .map(docProcess -> modelMapper.map(docProcess, DocProcessDto.class))
                .collect(Collectors.toList());
    }

    @GetMapping("/processes/outgoing")
    public List<DocProcessDto> findOutgoingProcesses(@AuthenticationPrincipal User currentUser) {
        return documentProcessService.getOutgoingProcessesByUserId(currentUser.getId()).stream()
                .map(docProcess -> modelMapper.map(docProcess, DocProcessDto.class))
                .collect(Collectors.toList());
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
    public DocProcessDto sendToApprove(ProcessUpdateRequestDto processUpdateRequestDto) {
        ProcessUpdateRequest processUpdateRequest = modelMapper.map(processUpdateRequestDto, ProcessUpdateRequest.class);
        documentProcessService.sendToApprove(processUpdateRequest);
        return modelMapper.map(documentProcessService.findProcessById(processUpdateRequest.getProcessId()),
                DocProcessDto.class);
    }

    @PatchMapping("/processes/{processId}/approve")
    @PreAuthorize("@documentProcessSecurityService.isRecipient(#processUpdateRequestDto.processId, authentication.principal.id) && hasAuthority('USER')")
    public DocProcessDto approve(ProcessUpdateRequestDto processUpdateRequestDto) {
        ProcessUpdateRequest processUpdateRequest = modelMapper.map(processUpdateRequestDto, ProcessUpdateRequest.class);
        documentProcessService.approve(processUpdateRequest);
        return modelMapper.map(documentProcessService.findProcessById(processUpdateRequest.getProcessId()),
                DocProcessDto.class);
    }

    @PatchMapping("/processes/{processId}/reject")
    @PreAuthorize("@documentProcessSecurityService.isRecipient(#processUpdateRequestDto.processId, authentication.principal.id) && hasAuthority('USER')")
    public DocProcessDto reject(ProcessUpdateRequestDto processUpdateRequestDto) {
        ProcessUpdateRequest processUpdateRequest = modelMapper.map(processUpdateRequestDto, ProcessUpdateRequest.class);
        documentProcessService.reject(processUpdateRequest);
        return modelMapper.map(documentProcessService.findProcessById(processUpdateRequest.getProcessId()),
                DocProcessDto.class);
    }

    @PatchMapping("/processes/{processId}/send-to-correction")
    @PreAuthorize("@documentProcessSecurityService.isRecipient(#processUpdateRequestDto.processId, authentication.principal.id) && hasAuthority('USER')")
    public DocProcessDto sendToCorrection(ProcessUpdateRequestDto processUpdateRequestDto) {
        ProcessUpdateRequest processUpdateRequest = modelMapper.map(processUpdateRequestDto, ProcessUpdateRequest.class);
        documentProcessService.sendToCorrection(processUpdateRequest);
        return modelMapper.map(documentProcessService.findProcessById(processUpdateRequest.getProcessId()),
                DocProcessDto.class);
    }


}
