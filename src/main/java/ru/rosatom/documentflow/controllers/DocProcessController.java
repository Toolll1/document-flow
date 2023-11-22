package ru.rosatom.documentflow.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
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
@Tag(name = "Бизнес процесс")
public class DocProcessController {

    private final DocumentProcessService documentProcessService;
    private final ModelMapper modelMapper;

    @Operation(summary = "Создать запрос на согласование")
    @PostMapping("/{documentId}/recipient/{recipientId}/new-process")
    @PreAuthorize(
            "@documentProcessSecurityService.isCanManageProcess(#documentId, authentication.principal.id) && hasAuthority('USER')")
    @SecurityRequirement(name = "JWT")
    public DocProcessDto createNewProcess(
            @PathVariable @Parameter(description = "ID документа") Long documentId,
            @PathVariable @Parameter(description = "ID получателя") Long recipientId) {
        return modelMapper.map(
                documentProcessService.createNewProcess(documentId, recipientId), DocProcessDto.class);
    }

    @Operation(summary = "Посмотреть все процессы по документу")
    @GetMapping("{documentId}/processes")
    @PreAuthorize("@documentProcessSecurityService.isHasAccessToProcess(#documentId, authentication.principal.id) && hasAuthority('USER')")
    @SecurityRequirement(name = "JWT")
    public Collection<DocProcessDto> findProcessByDocumentId(@PathVariable Long documentId) {
        return documentProcessService.findProcessesByDocumentId(documentId)
                .stream()
                .map(docProcess -> modelMapper.map(docProcess, DocProcessDto.class))
                .collect(Collectors.toList());
    }

    @Operation(summary = "Получить процесс по ID")
    @GetMapping("/processes/{processId}")
    @PreAuthorize("@documentProcessSecurityService.isHasAccessToProcess(#processId, authentication.principal.id)")
    @SecurityRequirement(name = "JWT")
    public DocProcessDto findProcessById(@PathVariable Long processId) {
        return modelMapper.map(documentProcessService.findProcessById(processId), DocProcessDto.class);
    }

    @Operation(summary = "Найти входящий процесс")
    @GetMapping("/processes/incoming")
    @SecurityRequirement(name = "JWT")
    public List<DocProcessDto> findIncomingProcesses(@AuthenticationPrincipal User currentUser) {
        return documentProcessService.getIncomingProcessesByUserId(currentUser.getId()).stream()
                .map(docProcess -> modelMapper.map(docProcess, DocProcessDto.class))
                .collect(Collectors.toList());
    }

    @Operation(summary = "Найти исходящий процесс")
    @GetMapping("/processes/outgoing")
    @SecurityRequirement(name = "JWT")
    public List<DocProcessDto> findOutgoingProcesses(@AuthenticationPrincipal User currentUser) {
        return documentProcessService.getOutgoingProcessesByUserId(currentUser.getId()).stream()
                .map(docProcess -> modelMapper.map(docProcess, DocProcessDto.class))
                .collect(Collectors.toList());
    }

    @Operation(summary = "Удалить процесс")
    @DeleteMapping("/processes/{processId}")
    @PreAuthorize("@documentProcessSecurityService.isCanManageProcess(#processId, authentication.principal.id) " +
            "&& !@documentProcessSecurityService.isProcessDone(#processId) && hasAuthority('USER')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @SecurityRequirement(name = "JWT")
    public void deleteProcess(@PathVariable Long processId) {
        documentProcessService.deleteProcess(processId);
    }

    @Operation(summary = "Отправить документ на согласование")
    @PatchMapping("/processes/{processId}/send-to-approve")
    @PreAuthorize("@documentProcessSecurityService.isHasAccessToProcess(#processUpdateRequestDto.processId, authentication.principal.id) && hasAuthority('USER')")
    @SecurityRequirement(name = "JWT")
    public DocProcessDto sendToApprove(ProcessUpdateRequestDto processUpdateRequestDto) {
        ProcessUpdateRequest processUpdateRequest = modelMapper.map(processUpdateRequestDto, ProcessUpdateRequest.class);
        documentProcessService.sendToApprove(processUpdateRequest);
        return modelMapper.map(documentProcessService.findProcessById(processUpdateRequest.getProcessId()),
                DocProcessDto.class);
    }

    @Operation(summary = "Согласовать документ")
    @PatchMapping("/processes/{processId}/approve")
    @PreAuthorize("@documentProcessSecurityService.isRecipient(#processUpdateRequestDto.processId, authentication.principal.id) && hasAuthority('USER')")
    @SecurityRequirement(name = "JWT")
    public DocProcessDto approve(ProcessUpdateRequestDto processUpdateRequestDto) {
        ProcessUpdateRequest processUpdateRequest = modelMapper.map(processUpdateRequestDto, ProcessUpdateRequest.class);
        documentProcessService.approve(processUpdateRequest);
        return modelMapper.map(documentProcessService.findProcessById(processUpdateRequest.getProcessId()),
                DocProcessDto.class);
    }

    @Operation(summary = "Отклонить документ")
    @PatchMapping("/processes/{processId}/reject")
    @PreAuthorize("@documentProcessSecurityService.isRecipient(#processUpdateRequestDto.processId, authentication.principal.id) && hasAuthority('USER')")
    @SecurityRequirement(name = "JWT")
    public DocProcessDto reject(ProcessUpdateRequestDto processUpdateRequestDto) {
        ProcessUpdateRequest processUpdateRequest = modelMapper.map(processUpdateRequestDto, ProcessUpdateRequest.class);
        documentProcessService.reject(processUpdateRequest);
        return modelMapper.map(documentProcessService.findProcessById(processUpdateRequest.getProcessId()),
                DocProcessDto.class);
    }

    @Operation(summary = "Отправить документ на доработку")
    @PatchMapping("/processes/{processId}/send-to-correction")
    @PreAuthorize("@documentProcessSecurityService.isRecipient(#processUpdateRequestDto.processId, authentication.principal.id) && hasAuthority('USER')")
    @SecurityRequirement(name = "JWT")
    public DocProcessDto sendToCorrection(ProcessUpdateRequestDto processUpdateRequestDto) {
        ProcessUpdateRequest processUpdateRequest = modelMapper.map(processUpdateRequestDto, ProcessUpdateRequest.class);
        documentProcessService.sendToCorrection(processUpdateRequest);
        return modelMapper.map(documentProcessService.findProcessById(processUpdateRequest.getProcessId()),
                DocProcessDto.class);
    }


}
