package com.epam.esm.service.impl;

import com.epam.esm.dao.CertificateDao;
import com.epam.esm.domain.Certificate;
import com.epam.esm.domain.Filter;
import com.epam.esm.domain.Tag;
import com.epam.esm.dto.CertificateDto;
import com.epam.esm.dto.FilterDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.dto.mapper.CertificateMapper;
import com.epam.esm.dto.mapper.FilterMapper;
import com.epam.esm.dto.mapper.TagMapper;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.service.CertificateService;
import com.epam.esm.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class CertificateServiceImpl implements CertificateService {

    private final CertificateDao certificateDao;
    private final CertificateMapper certificateMapper;
    private final FilterMapper filterMapper;
    private final TagMapper tagMapper;
    private final TagService tagService;

    @Override
    public CertificateDto getOne(Long id) {
        Certificate one = certificateDao.getOne(id).orElseThrow(() -> new ResourceNotFoundException("Resource not found", id));
        return certificateMapper.fromModel(one);
    }

    @Override
    public List<CertificateDto> getAll() {
        return certificateDao.getAll().stream().map(certificateMapper::fromModel).collect(Collectors.toList());
    }

    @Transactional
    @Override
    public CertificateDto createCertificate(CertificateDto certificate) {
        List<TagDto> tags = certificate.getTags();
        Certificate model = certificateMapper.toModel(certificate);
        model.setCreateDate(Instant.now());
        model.setLastUpdateDate(Instant.now());
        Certificate insertedCertificate = certificateDao.insert(model);
        List<Long> insertedTagsIds = tags.stream()
                .map(tagService::createTag)
                .map(TagDto::getId)
                .collect(Collectors.toList());
        certificateDao.insertCertificateTagLink(insertedCertificate.getId(), insertedTagsIds);
        return certificateMapper.fromModel(insertedCertificate);

    }

    @Transactional
    @Override
    public void deleteCertificate(Long id) {
        certificateDao.deleteCertificateLink(id);
        certificateDao.delete(id);
    }

    @Override
    public List<CertificateDto> filterCertificate(FilterDto filterDto) {
        Filter filter = filterMapper.toModel(filterDto);
        List<CertificateDto> certificates = new ArrayList<>();
        Map<Certificate, List<Tag>> certificateListMap = certificateDao.filterCertificate(filter);
        certificateListMap.forEach((k, v) -> {
            Stream<TagDto> tagDtoStream = v.stream().map(tagMapper::fromModelWithoutCertificate);
            List<TagDto> collect = tagDtoStream.collect(Collectors.toList());
            CertificateDto certificateDto = certificateMapper.fromModel(k);
            certificateDto.setTags(collect);
            certificates.add(certificateDto);
        });
        return certificates;
    }

    @Transactional
    @Override
    public void updateCertificate(CertificateDto certificateDto) {
        Certificate certificate = certificateMapper.toModel(certificateDto);
        certificate.setLastUpdateDate(Instant.now());
        certificateDao.update(certificate);
        List<TagDto> tags = certificateDto.getTags();
        List<Long> insertedTagsIds = tags.stream()
                .map(tagService::createTag)
                .map(TagDto::getId)
                .collect(Collectors.toList());
        certificateDao.insertCertificateTagLink(certificateDto.getId(), insertedTagsIds);
    }

    @Override
    public void linkTagToCertificate(Long certificateId, Long tagId) {
        certificateDao.getOne(certificateId);
        tagService.getTag(tagId);
        certificateDao.insertCertificateTagLink(certificateId, Collections.singletonList(tagId));
    }

}
