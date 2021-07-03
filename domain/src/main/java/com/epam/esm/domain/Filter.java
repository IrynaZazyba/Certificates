package com.epam.esm.domain;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Builder
@Data
public class Filter implements Serializable {

    private static final long serialVersionUID = 6153623301682034270L;
    private SortingField sort;
    private Sorting order;
    private String[] tagNames;
    private String name;
    private String description;

}
