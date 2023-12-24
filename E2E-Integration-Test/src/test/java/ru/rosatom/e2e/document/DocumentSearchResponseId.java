package ru.rosatom.e2e.document;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.rosatom.e2e.attributeValues.AttributeValues;
import ru.rosatom.e2e.docType.DocType;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class DocumentSearchResponseId {
    private Integer id;
    private String name;
    private String documentPath;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime date;
    private Integer idOrganization;
    private Integer ownerId;
    private DocType docType;
    private List<AttributeValues> attributeValues = new ArrayList<>();
    private DocProcessStatus finalDocStatus;
}
