package ru.rosatom.documentflow.exceptions;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class SomeIdsInArrayNotFoundException extends ObjectNotFoundException{

    private final List<Long> notFoundIds = new ArrayList<>();

    public SomeIdsInArrayNotFoundException(List<Long> notFoundIds, String targetEntitiesName) {
        super(String.format("Один или несколько объектов '%s' не найдены",
                targetEntitiesName));
        this.notFoundIds.addAll(notFoundIds);


    }
}
