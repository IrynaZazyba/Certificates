package com.epam.esm.service.impl;

import com.epam.esm.domain.Certificate;
import com.epam.esm.domain.Filter;
import com.epam.esm.dto.CertificateDto;
import com.epam.esm.dto.FilterDto;
import com.epam.esm.dto.mapper.CertificateMapper;
import com.epam.esm.dto.mapper.FilterMapper;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.repository.CertificateDao;
import com.epam.esm.service.CertificateService;
import com.epam.esm.util.Paginator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CertificateServiceImpl implements CertificateService {

    private final CertificateDao certificateDao;
    private final CertificateMapper certificateMapper;
    private final FilterMapper filterMapper;

    @Override
    public CertificateDto getOne(Long id) {
        Certificate one = certificateDao.getOne(id).orElseThrow(() ->
                new ResourceNotFoundException("Resource not found", id));
        return certificateMapper.fromModel(one);
    }

    @Override
    public List<CertificateDto> getAll(Paginator paginator) {
        return certificateDao.getAll(paginator).stream().map(certificateMapper::fromModel).collect(Collectors.toList());
    }

    @Transactional
    @Override
    public CertificateDto create(CertificateDto certificate) {
        Certificate model = certificateMapper.toModel(certificate);
        model.setCreateDate(Instant.now());
        model.setLastUpdateDate(Instant.now());
        Certificate insertedCertificate = certificateDao.save(model);
        return certificateMapper.fromModel(insertedCertificate);
    }

    @Transactional
    @Override
    public CertificateDto update(CertificateDto certificateDto) {
        Certificate certificate = certificateDao.getOne(certificateDto.getId())
                .map(c -> certificateMapper.map(certificateDto, c)).orElseThrow();
        certificate.setLastUpdateDate(Instant.now());
        return certificateMapper.fromModel(certificateDao.save(certificate));
    }

    @Transactional
    @Override
    public void delete(Long id) {
        certificateDao.deleteById(id);
    }

    @Override
    public List<CertificateDto> filter(Paginator paginator, FilterDto filterDto) {
        Filter filter = filterMapper.toModel(filterDto);
        List<Certificate> byFilter = certificateDao.findByFilter(paginator, filter);
        return byFilter.stream().map(certificateMapper::fromModel).collect(Collectors.toList());
    }

}
