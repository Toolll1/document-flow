package ru.rosatom.documentflow.services.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import ru.rosatom.documentflow.exceptions.ObjectNotFoundException;
import ru.rosatom.documentflow.models.*;
import ru.rosatom.documentflow.repositories.DocAttributeRepository;
import ru.rosatom.documentflow.repositories.DocTypeRepository;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class DocTypeServiceImplTest {
    @Autowired
    private DocTypeRepository docTypeRepository;
    @Autowired
    private DocAttributeRepository docAttributeRepository;
    private DocTypeServiceImpl impl;

    @BeforeEach
    void initImpl() {
        impl = new DocTypeServiceImpl(docTypeRepository, docAttributeRepository);
    }

    @Test
    void getAllDocTypesThenGreaterThan0() {
        Page<DocType> page = impl.getAllDocTypes(Optional.of(0), Optional.of("id"));
        assertThat(page.getTotalElements()).isEqualTo(5);
    }

    @Test
    void getAllDocTypesThenIllegalArgumentException() {
        assertThatThrownBy(() -> impl.getAllDocTypes(Optional.of(-1), Optional.of("id")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("less than zero");
    }

    @Test
    void getAllDocTypesThenPropertyReferenceException() {
        assertThatThrownBy(() -> impl.getAllDocTypes(Optional.of(0), Optional.of("type")))
                .isInstanceOf(org.springframework.data.mapping.PropertyReferenceException.class)
                .hasMessageContaining("No property");
    }

    @Test
    void getDocTypeByIdThenFoundOne() {
        String name = "накладная";
        assertThat(impl.getDocTypeById(1L).getName()).isNotNull().containsIgnoringCase(name);
    }

    @Test
    void getDocTypeByIdThenObjectNotFoundException() {
        assertThatThrownBy(() -> impl.getDocTypeById(-1L))
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessageContaining("не найден");
    }

    @Test
    void createDocTypeThenNotNull() {
        DocTypeCreationRequest creationRequest = new DocTypeCreationRequest("договор", AgreementType.ANYONE);
        assertNotNull(impl.createDocType(creationRequest));
    }

    @Test
    void updateDocTypeThenNotNull() {
        DocTypeUpdateRequest updateRequest = new DocTypeUpdateRequest("договор");
        assertThat(impl.updateDocType(1L, updateRequest).getName())
                .isNotNull()
                .containsIgnoringCase("договор");
    }

    @Test
    void updateDocTypeThenObjectNotFoundException() {
        DocTypeUpdateRequest updateRequest = new DocTypeUpdateRequest("договор");
        assertThatThrownBy(() -> impl.updateDocType(-1L, updateRequest))
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessageContaining("не найден");
    }

    @Test
    void deleteDocTypeThenNotNull() {
        boolean isExistBeforeDelete = docTypeRepository.findById(1L).isPresent();
        impl.deleteDocType(1L);
        boolean isExistAfterDelete = docTypeRepository.findById(1L).isPresent();
        assertThat(isExistBeforeDelete).isTrue().isNotEqualTo(isExistAfterDelete);
    }

    @Test
    void deleteDocTypeThenObjectNotFoundException() {
        assertThatThrownBy(() -> impl.deleteDocType(-1L))
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessageContaining("не найден");
    }

    @Test
    void getDocTypesByNameThenNotEmpty() {
        assertThat(impl.getDocTypesByName("Накладная"))
                .isNotEmpty()
                .hasSize(1);
    }

    @Test
    void getDocTypesByNameThenEmpty() {
        assertThat(impl.getDocTypesByName("договор"))
                .isEmpty();
    }

    @Test
    void attributeToTypeThenHasSizeOne() {
        DocAttribute docAttribute = docAttributeRepository.findById(1L).get();
        assertThat(impl.attributeToType(1L, 1L).getAttributes())
                .hasSize(1)
                .containsOnly(docAttribute);
    }

    @Test
    void attributeToTypeThenNoSuchElementExceptionForDocType() {
        assertThatThrownBy(() -> impl.attributeToType(-1L, 1L))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("No value present");
    }

    @Test
    void attributeToTypeThenNoSuchElementExceptionForDocAttribute() {
        assertThatThrownBy(() -> impl.attributeToType(1L, -1L))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("No value present");
    }
}