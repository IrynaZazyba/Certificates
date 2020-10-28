package com.epam.esm.dto.mapper;

import com.epam.esm.domain.Certificate;
import com.epam.esm.dto.CertificateDto;
import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class CertificateMapper {

    private final TagMapper tagMapper;

    public CertificateMapper(TagMapper tagMapper) {
        this.tagMapper = tagMapper;
    }

    public CertificateDto fromModel(Certificate certificate) {
        return CertificateDto.builder()
                .id(certificate.getId())
                .name(certificate.getName())
                .createDate(certificate.getCreateDate())
                .lastUpdateDate(certificate.getLastUpdateDate())
                .description(certificate.getDescription())
                .duration(certificate.getDuration())
                .build();
    }

    public Certificate toModel(CertificateDto certificate) {
        return Certificate.builder()
                .id(certificate.getId())
                .name(certificate.getName())
                .createDate(certificate.getCreateDate())
                .lastUpdateDate(certificate.getLastUpdateDate())
                .description(certificate.getDescription())
                .duration(certificate.getDuration())
                .build();
    }
}
