/*
package ru.rosatom.documentflow.services.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import ru.rosatom.documentflow.exceptions.ObjectNotFoundException;
import ru.rosatom.documentflow.models.DocAttribute;
import ru.rosatom.documentflow.models.DocAttributeCreationRequest;
import ru.rosatom.documentflow.models.DocAttributeUpdateRequest;
import ru.rosatom.documentflow.repositories.DocAttributeRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class DocAttributeServiceImplTest {
    @Autowired
    private DocAttributeRepository docAttributeRepository;
    private DocAttributeServiceImpl impl;

    @BeforeEach
    void initImpl() {
        impl = new DocAttributeServiceImpl(docAttributeRepository);
    }

    @Test
    void getAllDocAttributesThenFoundSix() {
        Page<DocAttribute> page = impl.getAllDocAttributes(Optional.of(0), Optional.of("id"));
        assertThat(page.getTotalElements()).isEqualTo(6);
    }

    @Test
    void getDocAttributeById() {
        String name = "Name attribute 1";
        String type = "type attribute 1";
        assertThat(impl.getDocAttributeById(1L).getName()).isNotNull().containsIgnoringCase(name);
        assertThat(impl.getDocAttributeById(1L).getType()).isNotNull().containsIgnoringCase(type);
    }

    @Test
    void getDocTypeByIdThenObjectNotFoundException() {
        assertThatThrownBy(() -> impl.getDocAttributeById(-1L))
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessageContaining("не найден");
    }

    @Test
    void createDocAttribute() {
        DocAttributeCreationRequest creationRequest =
                new DocAttributeCreationRequest("Цена (руб)", "Договор купли-продажи");
        assertNotNull(impl.createDocAttribute(creationRequest));
    }

    @Test
    void updateDocAttribute() {
        DocAttributeUpdateRequest updateRequest =
                new DocAttributeUpdateRequest("Цена (USD)", "Договор купли-продажи");
        assertThat(impl.updateDocAttribute(1L, updateRequest).getName())
                .isNotNull()
                .containsIgnoringCase("Цена (USD)");
        assertThat(impl.updateDocAttribute(1L, updateRequest).getType())
                .isNotNull()
                .containsIgnoringCase("Договор купли-продажи");
    }

    @Test
    void updateDocAttributeThenObjectNotFoundException() {
        DocAttributeUpdateRequest updateRequest =
                new DocAttributeUpdateRequest("Цена (USD)", "Договор купли-продажи");
        assertThatThrownBy(() -> impl.updateDocAttribute(-1L, updateRequest))
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessageContaining("не найден");
    }

    @Test
    void deleteAttribute() {
        boolean isExistBeforeDelete = docAttributeRepository.findById(1L).isPresent();
        impl.deleteDocAttribute(1L);
        boolean isExistAfterDelete = docAttributeRepository.findById(1L).isPresent();
        assertThat(isExistBeforeDelete).isTrue().isNotEqualTo(isExistAfterDelete);
    }

    @Test
    void deleteAttributeThenObjectNotFoundException() {
        assertThatThrownBy(() -> impl.deleteDocAttribute(-1L))
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessageContaining("не найден");
    }

    @Test
    void getDocAttributesByNameThenNotEmpty() {
        assertThat(impl.getDocAttributesByName("Name attribute 1"))
                .isNotEmpty()
                .hasSize(1);
    }

    @Test
    void getDocAttributesByNameThenEmpty() {
        assertThat(impl.getDocAttributesByName("Name attribute 15"))
                .isEmpty();
    }
}*/
