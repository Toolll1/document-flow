package ru.rosatom.documentflow.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.rosatom.documentflow.adapters.CommonUtils;
import ru.rosatom.documentflow.models.DocAttributeValues;
import ru.rosatom.documentflow.models.DocProcessStatus;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DocumentDto {

    Long id;
    @NotNull
    @NotEmpty
    String title;
    @NotNull
    @NotEmpty
    String documentPath;
    @NotNull
    @JsonFormat(pattern = CommonUtils.DATE_TIME_PATTERN)
    LocalDateTime date;
    @NotNull
    Long idOrganization;
    Long ownerId;
    @NotNull
    DocTypeDto docTypeDto;
    @NotNull
    List<DocAttributeValues> attributeValues;
    DocProcessStatus finalDocStatus;
}
