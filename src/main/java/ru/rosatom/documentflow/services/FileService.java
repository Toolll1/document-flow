package ru.rosatom.documentflow.services;

import ru.rosatom.documentflow.models.DocProcess;
import ru.rosatom.documentflow.models.Document;

import java.util.Collection;

public interface FileService {

    Document createFile(Document document, Collection<DocProcess> docProcess);

    Document updateFile(Document newDocument, Document oldDocument, Collection<DocProcess> docProcess);

    void deleteFile(Document document);
}
