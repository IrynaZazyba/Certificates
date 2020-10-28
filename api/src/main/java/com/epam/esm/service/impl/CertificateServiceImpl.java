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
import com.epam.esm.exception.InvalidUserDataException;
import com.epam.esm.service.CertificateService;
import com.epam.esm.service.TagService;
import com.epam.esm.service.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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
    private final Validator<CertificateDto> validator;

    @Override
    public CertificateDto getOne(Long id) {
        Certificate one = certificateDao.getOne(id);
        return certificateMapper.fromModel(one);
    }

    @Override
    public List<CertificateDto> getAll() {
        return certificateDao.getAllCertificates().stream().map(certificateMapper::fromModel).collect(Collectors.toList());
    }

    @Transactional
    @Override
    public Long createCertificate(CertificateDto certificate) {
        if (!validator.validate(certificate)) {
            throw new InvalidUserDataException("Invalid user data");
        }
        List<TagDto> tags = certificate.getTags();
        Certificate model = certificateMapper.toModel(certificate);
        model.setCreateDate(LocalDateTime.now());
        model.setLastUpdateDate(LocalDateTime.now());
        Long insertedCertificateId = certificateDao.insertCertificate(model);
        if (tags != null) {
            List<Long> insertedTagsIds = tags.stream().map(tagService::createTag).collect(Collectors.toList());
            certificateDao.insertCertificateTagLink(insertedCertificateId, insertedTagsIds);
        }
        return insertedCertificateId;
    }

    @Transactional
    @Override
    public void deleteCertificate(Long id) {
        certificateDao.deleteCertificateLink(id);
        certificateDao.deleteCertificate(id);
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
        certificate.setLastUpdateDate(LocalDateTime.now());
        certificateDao.updateCertificate(certificate);
        List<TagDto> tags = certificateDto.getTags();
        if (tags != null) {
            List<Long> insertedTagsIds = tags.stream().map(tagService::createTag).collect(Collectors.toList());
            certificateDao.insertCertificateTagLink(certificateDto.getId(), insertedTagsIds);
        }
    }

    @Override
    public void linkTagToCertificate(Long certificateId, Long tagId) {
        certificateDao.getOne(certificateId);
        tagService.getTag(tagId);
        certificateDao.insertCertificateTagLink(certificateId, Collections.singletonList(tagId));
    }

}
