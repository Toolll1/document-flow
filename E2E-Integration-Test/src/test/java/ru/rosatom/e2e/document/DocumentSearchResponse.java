package ru.rosatom.e2e.document;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@NoArgsConstructor
@Data
public class DocumentSearchResponse {
    @JsonProperty("content")
    private List<Document> documents;
}
