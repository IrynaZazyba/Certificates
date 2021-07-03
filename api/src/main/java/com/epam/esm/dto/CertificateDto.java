package com.epam.esm.dto;

import com.epam.esm.domain.Certificate;
import com.epam.esm.dto.validation.OnCreate;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
@Validated
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CertificateDto extends RepresentationModel<CertificateDto> {

    private static final String DATE_TIME_PATTERN = "yyyy-MM-dd'T'hh:mm:ss.SSS'Z'";
    private static final String DATE_TIME_TIMEZONE = "UTC";

    private Long id;
    @Size(min = 1, max = 50, message = "{error.name.size}")
    @NotBlank(groups = OnCreate.class, message = "{error.name.mandatory}")
    private String name;
    @Size(min = 1, max = 255, message = "{error.name.size}")
    @NotBlank(groups = OnCreate.class, message = "{error.description.mandatory}")
    private String description;
    @JsonFormat(pattern = DATE_TIME_PATTERN, timezone = DATE_TIME_TIMEZONE)
    private Instant createDate;
    @JsonFormat(pattern = DATE_TIME_PATTERN, timezone = DATE_TIME_TIMEZONE)
    private Instant lastUpdateDate;
    @Min(value = 1, message = "{error.duration.size}")
    @NotNull(groups = OnCreate.class, message = "{error.duration.mandatory}")
    private Integer duration;
    private List<TagDto> tags = new ArrayList<>();
    @DecimalMin(value = "0.1", message = "{error.price}")
    @NotNull(groups = OnCreate.class, message = "{error.price.mandatory}")
    private BigDecimal price;


    public CertificateDto(Certificate certificate) {
        this.id = certificate.getId();
        this.name = certificate.getName();
        this.description = certificate.getDescription();
        this.createDate = certificate.getCreateDate();
        this.lastUpdateDate = certificate.getLastUpdateDate();
        this.duration = certificate.getDuration();
        this.price = certificate.getPrice();
    }

}
