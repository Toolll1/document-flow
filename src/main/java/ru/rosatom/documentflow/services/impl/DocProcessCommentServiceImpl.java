package ru.rosatom.documentflow.services.impl;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;
import ru.rosatom.documentflow.models.DocProcessComment;
import ru.rosatom.documentflow.models.User;
import ru.rosatom.documentflow.services.DocProcessCommentService;
import ru.rosatom.documentflow.services.DocumentProcessService;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class DocProcessCommentServiceImpl implements DocProcessCommentService {

    private final DocumentProcessService docProcess;
    @Override
    public DocProcessComment createComment(String text, User user){
        return  DocProcessComment.builder()
                .authorComment(user)
                .textComment(text)
                .date(LocalDateTime.now())
                .build();
    }


}
