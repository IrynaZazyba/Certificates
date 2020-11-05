package com.epam.esm.service;

import com.epam.esm.dto.CertificateDto;
import com.epam.esm.dto.FilterDto;

import java.util.List;

public interface CertificateService {

    CertificateDto getOne(Long id);

    List<CertificateDto> getAll();

    CertificateDto createCertificate(CertificateDto certificate);

    void deleteCertificate(Long id);

    List<CertificateDto> filterCertificate(FilterDto filterDto);

    void updateCertificate(CertificateDto certificateDto);

    void linkTagToCertificate(Long certificateId, Long tagId);
}
