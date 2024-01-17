package ru.rosatom.documentflow.models;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor (force = true)
@Getter
@Setter
public class CommentCreationRequest {
    private String content;
}
