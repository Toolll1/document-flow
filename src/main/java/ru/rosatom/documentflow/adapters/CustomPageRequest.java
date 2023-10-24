package ru.rosatom.documentflow.adapters;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

public class CustomPageRequest extends PageRequest {
    int from;

    public CustomPageRequest(int from, int size) {
        super(from / size, size, Sort.unsorted());
        this.from = from;
    }
}
