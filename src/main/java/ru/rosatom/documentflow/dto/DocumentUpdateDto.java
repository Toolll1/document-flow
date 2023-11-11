package ru.rosatom.documentflow.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.lang.Nullable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DocumentUpdateDto {
    @Nullable
    String name;
    @Nullable
    String documentPath;
    @Nullable
    LocalDateTime date;
    @Nullable
    Long docTypeId;
    @Nullable
    String previousVersion;
    @Nullable
    List<DocAttributeValueCreateDto> attributeValues = new ArrayList<>();
}