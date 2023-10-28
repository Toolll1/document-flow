package ru.rosatom.documentflow.configuration.mappingConfigurations;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import ru.rosatom.documentflow.models.Document;
import ru.rosatom.documentflow.models.User;

import javax.annotation.PostConstruct;

@Configuration
@DependsOn("modelMapperConfiguration")
@RequiredArgsConstructor
public class EntityToIdMappingConfiguration {

    private final ModelMapper modelMapper;

    @PostConstruct
    public void userToLongMapper() {
        modelMapper.createTypeMap(User.class, Long.class)
                .setConverter(context -> context.getSource().getId());
    }

    @PostConstruct
    public void documentToLongMapper() {
        modelMapper.createTypeMap(Document.class, Long.class)
                .setConverter(context -> context.getSource().getId());
    }
}
