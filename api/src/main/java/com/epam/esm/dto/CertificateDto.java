package com.epam.esm.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CertificateDto {

    private static final String DATE_TIME_PATTERN = "yyyy-MM-dd'T'hh:mm:ss.SSS'Z'";
    private static final String DATE_TIME_TIMEZONE = "UTC";
    private Long id;
    private String name;
    private String description;
    @JsonFormat(pattern = DATE_TIME_PATTERN, timezone = DATE_TIME_TIMEZONE)
    private LocalDateTime createDate;
    @JsonFormat(pattern = DATE_TIME_PATTERN, timezone = DATE_TIME_TIMEZONE)
    private LocalDateTime lastUpdateDate;
    private int duration;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<TagDto> tags;

}
