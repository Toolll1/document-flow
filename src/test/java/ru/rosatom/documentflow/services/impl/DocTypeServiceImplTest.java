package ru.rosatom.documentflow.services.impl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import ru.rosatom.documentflow.exceptions.ObjectNotFoundException;
import ru.rosatom.documentflow.models.DocAttribute;
import ru.rosatom.documentflow.models.DocType;
import ru.rosatom.documentflow.models.DocTypeCreationRequest;
import ru.rosatom.documentflow.models.DocTypeUpdateRequest;
import ru.rosatom.documentflow.repositories.DocAttributeRepository;
import ru.rosatom.documentflow.repositories.DocTypeRepository;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class DocTypeServiceImplTest {
    @Autowired
    private DocTypeRepository docTypeRepository;
    @Autowired
    private DocAttributeRepository docAttributeRepository;

    private DocTypeServiceImpl impl;

    @Test
    void getAllDocTypesThenGreaterThan0() {
        impl = new DocTypeServiceImpl(docTypeRepository, docAttributeRepository);
        Page<DocType> page = impl.getAllDocTypes(Optional.of(0), Optional.of("id"));
        assertThat(page.getTotalElements()).isGreaterThan(0).isEqualTo(5);
    }

    @Test
    void getAllDocTypesThenIllegalArgumentException() {
        impl = new DocTypeServiceImpl(docTypeRepository, docAttributeRepository);
        assertThatThrownBy(() -> impl.getAllDocTypes(Optional.of(-1), Optional.of("id")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("less than zero");
    }

    @Test
    void getAllDocTypesThenPropertyReferenceException() {
        impl = new DocTypeServiceImpl(docTypeRepository, docAttributeRepository);
        assertThatThrownBy(() -> impl.getAllDocTypes(Optional.of(0), Optional.of("type")))
                .isInstanceOf(org.springframework.data.mapping.PropertyReferenceException.class)
                .hasMessageContaining("No property");
    }

    @Test
    void getDocTypeByIdThenFoundOne() {
        impl = new DocTypeServiceImpl(docTypeRepository, docAttributeRepository);
        String name = "накладная";
        assertThat(impl.getDocTypeById(1L).getName()).isNotNull().containsIgnoringCase(name);
    }

    @Test
    void getDocTypeByIdThenObjectNotFoundException() {
        impl = new DocTypeServiceImpl(docTypeRepository, docAttributeRepository);
        assertThatThrownBy(() -> impl.getDocTypeById(-1L))
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessageContaining("не найден");
    }

    @Test
    void createDocTypeThenNotNull() {
        impl = new DocTypeServiceImpl(docTypeRepository, docAttributeRepository);
        DocTypeCreationRequest creationRequest = new DocTypeCreationRequest("договор");
        assertThat(impl.createDocType(creationRequest)).isNotNull();
    }

    @Test
    void updateDocTypeThenNotNull() {
        impl = new DocTypeServiceImpl(docTypeRepository, docAttributeRepository);
        DocTypeUpdateRequest updateRequest = new DocTypeUpdateRequest("договор");
        assertThat(impl.updateDocType(1L, updateRequest).getName())
                .isNotNull()
                .containsIgnoringCase("договор");
    }

    @Test
    void updateDocTypeThenObjectNotFoundException() {
        impl = new DocTypeServiceImpl(docTypeRepository, docAttributeRepository);
        DocTypeUpdateRequest updateRequest = new DocTypeUpdateRequest("договор");
        assertThatThrownBy(() -> impl.updateDocType(-1L, updateRequest))
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessageContaining("не найден");
    }

    @Test
    void deleteDocTypeThenNotNull() {
        impl = new DocTypeServiceImpl(docTypeRepository, docAttributeRepository);
        boolean isExistBeforeDelete = docTypeRepository.findById(1L).isPresent();
        impl.deleteDocType(1L);
        boolean isExistAfterDelete = docTypeRepository.findById(1L).isPresent();
        assertThat(isExistBeforeDelete).isTrue().isNotEqualTo(isExistAfterDelete);
    }

    @Test
    void deleteDocTypeThenObjectNotFoundException() {
        impl = new DocTypeServiceImpl(docTypeRepository, docAttributeRepository);
        assertThatThrownBy(() -> impl.deleteDocType(-1L))
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessageContaining("не найден");
    }

    @Test
    void getDocTypesByNameThenNotEmpty() {
        impl = new DocTypeServiceImpl(docTypeRepository, docAttributeRepository);
        assertThat(impl.getDocTypesByName("Накладная"))
                .isNotEmpty()
                .hasSize(1);
    }

    @Test
    void getDocTypesByNameThenEmpty() {
        impl = new DocTypeServiceImpl(docTypeRepository, docAttributeRepository);
        assertThat(impl.getDocTypesByName("договор"))
                .isEmpty();
    }

    @Test
    void attributeToTypeThenHasSizeOne() {
        impl = new DocTypeServiceImpl(docTypeRepository, docAttributeRepository);
        DocAttribute docAttribute = docAttributeRepository.findById(1L).get();
        assertThat(impl.attributeToType(1L, 1L).getAttributes())
                .hasSize(1)
                .containsOnly(docAttribute);
    }

    @Test
    void attributeToTypeThenNoSuchElementExceptionForDocType() {
        impl = new DocTypeServiceImpl(docTypeRepository, docAttributeRepository);
        assertThatThrownBy(() -> impl.attributeToType(-1L, 1L))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("No value present");
    }

    @Test
    void attributeToTypeThenNoSuchElementExceptionForDocAttribute() {
        impl = new DocTypeServiceImpl(docTypeRepository, docAttributeRepository);
        assertThatThrownBy(() -> impl.attributeToType(1L, -1L))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("No value present");
    }
}