package ru.rosatom.documentflow.configuration;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.rosatom.documentflow.dto.DocProcessDto;
import ru.rosatom.documentflow.models.DocProcess;

@Configuration
public class ModelMapperConfiguration {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT)
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE);

        modelMapper.typeMap(DocProcess.class, DocProcessDto.class)
                .addMappings(mapper -> mapper.map(
                        src -> src.getRecipientOrganization().getId(),
                        DocProcessDto::setRecipientOrganization));



        return modelMapper;
    }
}