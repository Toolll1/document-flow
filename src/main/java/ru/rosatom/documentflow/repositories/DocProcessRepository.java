package ru.rosatom.documentflow.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.rosatom.documentflow.models.DocProcess;

import java.util.List;

@Repository
public interface DocProcessRepository extends CrudRepository<DocProcess, Long> {

    List<DocProcess> findAllByDocumentId(Long documentId);

    List<DocProcess> findAllBySenderId(Long senderId);

    List<DocProcess> findAllByRecipientUserId(Long recipientId);

}
