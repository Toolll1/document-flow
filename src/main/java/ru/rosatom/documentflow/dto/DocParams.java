package ru.rosatom.documentflow.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class DocParams {
    String text;
    LocalDateTime rangeStart;
    LocalDateTime rangeEnd;
    Long creatorId;
    Long typeId;
    Long attributeId;
    String attributeValue;
}
