package com.epam.esm.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Certificate implements Serializable {

    private static final long serialVersionUID = -6102006421982197342L;
    private Long id;
    private String name;
    private String description;
    private Instant createDate;
    private Instant lastUpdateDate;
    private int duration;
    private List<Tag> tags = new ArrayList<>();

}
