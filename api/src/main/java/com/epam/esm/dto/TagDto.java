package com.epam.esm.dto;

import com.epam.esm.domain.Tag;
import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Component
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TagDto extends RepresentationModel<TagDto> {

    private Long id;
    @NotNull(message = "{error.name.mandatory}")
    @Size(min = 1, max = 50, message = "{error.name.size}")
    private String name;

    @JsonCreator
    public TagDto(Tag tag) {
        this.id = tag.getId();
        this.name = tag.getName();
    }
}
