package com.epam.esm.dto.mapper;

import com.epam.esm.domain.Tag;
import com.epam.esm.dto.TagDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Data
@AllArgsConstructor
public class TagMapper {

    public TagDto fromModelWithoutCertificate(Tag tag) {
        return TagDto.builder().id(tag.getId()).name(tag.getName()).build();
    }

    public Tag toModelWithoutCertificate(TagDto tag) {
        return Tag.builder().id(tag.getId()).name(tag.getName()).build();
    }

    public List<Tag> mapFromDto(List<TagDto> tags){
       return tags.stream().map(this::toModelWithoutCertificate).collect(Collectors.toList());
    }

    public List<TagDto> mapToModel(List<Tag> tags){
        return tags.stream().map(this::fromModelWithoutCertificate).collect(Collectors.toList());
    }

}
