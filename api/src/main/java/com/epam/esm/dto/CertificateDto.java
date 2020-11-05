package com.epam.esm.dto;

import com.epam.esm.domain.Certificate;
import com.epam.esm.dto.mapper.TagMapper;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CertificateDto {

    private static final String DATE_TIME_PATTERN = "yyyy-MM-dd'T'hh:mm:ss.SSS'Z'";
    private static final String DATE_TIME_TIMEZONE = "UTC";

    @JsonIgnore
    private TagMapper tagMapper;

    private Long id;
    private String name;
    private String description;
    @JsonFormat(pattern = DATE_TIME_PATTERN, timezone = DATE_TIME_TIMEZONE)
    private Instant createDate;
    @JsonFormat(pattern = DATE_TIME_PATTERN, timezone = DATE_TIME_TIMEZONE)
    private Instant lastUpdateDate;
    private int duration;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<TagDto> tags;

    public TagMapper getTagMapper() {
        return tagMapper;
    }

    @Autowired
    public void setTagMapper(TagMapper tagMapper) {
        this.tagMapper = tagMapper;
    }

    public CertificateDto(Certificate certificate) {
        this.id = certificate.getId();
        this.name = certificate.getName();
        this.description = certificate.getDescription();
        this.createDate = certificate.getCreateDate();
        this.lastUpdateDate = certificate.getLastUpdateDate();
        this.duration = certificate.getDuration();
        this.tags = certificate.getTags()
                .stream().map(tagMapper::fromModelWithoutCertificate).collect(Collectors.toList());
    }


}
