package com.epam.esm.domain;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Builder
@Data
public class Filter implements Serializable {

    private static final long serialVersionUID = 6153623301682034270L;
    private String sort;
    private String order;
    private String tagName;
    private String name;
    private String description;

}
