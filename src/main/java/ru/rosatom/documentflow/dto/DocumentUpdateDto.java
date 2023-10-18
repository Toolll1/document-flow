package ru.rosatom.documentflow.dto;

import lombok.Data;
import org.springframework.lang.Nullable;
import ru.rosatom.documentflow.models.DocAttributeValues;
import ru.rosatom.documentflow.models.DocChanges;
import ru.rosatom.documentflow.models.DocType;
import ru.rosatom.documentflow.models.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class DocumentUpdateDto {
    @Nullable
    private String title;
    @Nullable
    private String documentPath;
    @Nullable
    private LocalDate date;
    @Nullable
    private User owner;
    @Nullable
    private DocType docType;
    @Nullable
    private List<DocChanges> changes = new ArrayList<>();
    @Nullable
    private List<DocAttributeValues> attributeValues = new ArrayList<>();
}
