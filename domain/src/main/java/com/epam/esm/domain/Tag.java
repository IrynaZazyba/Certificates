package com.epam.esm.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Tag implements Serializable {

    private static final long serialVersionUID = -8742791910266118756L;
    private Long id;
    private String name;
    private List<Certificate> certificates = new ArrayList<>();

}
