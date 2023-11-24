package ru.rosatom.documentflow.services;


import ru.rosatom.documentflow.models.DocProcessComment;
import ru.rosatom.documentflow.models.User;


public interface DocProcessCommentService {
    DocProcessComment createComment(String text, User user);

}