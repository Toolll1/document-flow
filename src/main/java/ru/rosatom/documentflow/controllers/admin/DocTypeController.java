package ru.rosatom.documentflow.controllers.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.rosatom.documentflow.models.DocType;
import ru.rosatom.documentflow.services.DocTypeService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/doctypes")
public class DocTypeController {

    private final DocTypeService docTypeService;

    @GetMapping
    public List<DocType> getAllDocTypes() {
        return docTypeService.getAllDocTypes();
    }

    @GetMapping("/{docTypeId}")
    public DocType getDocType(@PathVariable Long docTypeId) {
        log.info("Request received to get DocType with ID: {}", docTypeId);
        return docTypeService.getDocTypeById(docTypeId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DocType createDocType(@Valid @RequestBody DocType docType) {
        log.info("Request received to create a DocType: {}", docType);
        return docTypeService.createDocType(docType);
    }

    @PutMapping("/{docTypeId}")
    public DocType updateDocType(
            @PathVariable Long docTypeId,
            @Valid @RequestBody DocType docType
    ) {
        log.info("Request received to update DocType with ID: {}. Updated DocType: {}", docTypeId, docType);
        return docTypeService.updateDocType(docTypeId, docType);
    }

    @DeleteMapping("/{docTypeId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteDocType(@PathVariable Long docTypeId) {
        log.info("Request received to delete DocType with ID: {}", docTypeId);
        docTypeService.deleteDocType(docTypeId);
    }
}