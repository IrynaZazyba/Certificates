package com.epam.esm.service;

import com.epam.esm.dto.CertificateDto;
import com.epam.esm.dto.FilterDto;
import com.epam.esm.util.Paginator;


import java.util.List;

public interface CertificateService {

    CertificateDto getOne(Long id);

    List<CertificateDto> getAll(Paginator paginator);

    CertificateDto create(CertificateDto certificate);

    void delete(Long id);

    List<CertificateDto> filter(Paginator paginator, FilterDto filterDto);

    CertificateDto update(CertificateDto certificateDto);

}
