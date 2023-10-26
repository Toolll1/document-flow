package ru.rosatom.documentflow.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.lang.Nullable;
import ru.rosatom.documentflow.models.DocAttributeValues;
import ru.rosatom.documentflow.models.DocType;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DocumentUpdateDto {
    @Nullable
    String title;
    @Nullable
    String documentPath;
    @Nullable
    LocalDateTime date;
    @Nullable
    Long ownerId;
    @Nullable
    DocType docType;
    @Nullable
    String previousVersion;
    @Nullable
    List<DocAttributeValues> attributeValues = new ArrayList<>();
}