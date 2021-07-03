package com.epam.esm.dto.mapper;

import com.epam.esm.domain.Certificate;
import com.epam.esm.domain.Tag;
import com.epam.esm.dto.CertificateDto;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@Data
@RequiredArgsConstructor
public class CertificateMapper {

    private final TagMapper tagMapper;

    public CertificateDto fromModel(Certificate certificate) {
        CertificateDto certificateDto = new CertificateDto(certificate);
        certificateDto
                .setTags(Objects.isNull(certificate.getTags()) ? null : tagMapper.mapToModel(certificate.getTags()));
        return certificateDto;
    }

    public Certificate toModel(CertificateDto certificate) {
        return Certificate.builder()
                .id(certificate.getId())
                .name(certificate.getName())
                .createDate(certificate.getCreateDate())
                .lastUpdateDate(certificate.getLastUpdateDate())
                .description(certificate.getDescription())
                .duration(certificate.getDuration())
                .tags(Objects.isNull(certificate.getTags()) ? null : tagMapper.mapFromDto(certificate.getTags()))
                .price(certificate.getPrice())
                .build();
    }

    public Certificate map(CertificateDto certificateDto, Certificate certificate) {
        certificate.setDuration(Objects.nonNull(certificateDto.getDuration()) ?
                certificateDto.getDuration() :
                certificate.getDuration());
        certificate.setName(Objects.nonNull(certificateDto.getName()) ?
                certificateDto.getName() :
                certificate.getName());
        certificate.setDescription(Objects.nonNull(certificateDto.getDescription()) ?
                certificateDto.getDescription() :
                certificate.getDescription());
        certificate.setPrice(certificateDto.getPrice());
        if (Objects.nonNull(certificateDto.getTags())) {
            List<Tag> tags = certificateDto.getTags()
                    .stream()
                    .map(tagMapper::toModelWithoutCertificate)
                    .collect(Collectors.toList());
            certificate.setTags(tags);
        }
        return certificate;
    }

}
