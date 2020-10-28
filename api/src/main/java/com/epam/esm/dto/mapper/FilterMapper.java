package com.epam.esm.dto.mapper;

import com.epam.esm.domain.Filter;
import com.epam.esm.dto.FilterDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
@AllArgsConstructor
public class FilterMapper {

    public Filter toModel(FilterDto filterDto) {
        return Filter.builder()
                .sort(filterDto.getSort())
                .order(filterDto.getOrder())
                .tagName(filterDto.getTagName())
                .searchDescription(filterDto.getSearchDescription())
                .searchName(filterDto.getSearchName())
                .build();
    }
}
