package com.epam.esm.dto;

import com.epam.esm.domain.Sorting;
import com.epam.esm.domain.SortingField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FilterDto {

    private SortingField sort;
    private Sorting order;
    private String[] tagNames;
    private String name;
    private String description;

}
