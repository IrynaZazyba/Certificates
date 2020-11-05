package com.epam.esm.service;

import com.epam.esm.dao.CertificateDao;
import com.epam.esm.domain.Certificate;
import com.epam.esm.domain.Tag;
import com.epam.esm.dto.CertificateDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.dto.mapper.CertificateMapper;
import com.epam.esm.service.impl.CertificateServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.internal.verification.VerificationModeFactory.times;

@ExtendWith(MockitoExtension.class)
public class CertificateServiceTest {

    @Mock
    private CertificateDao mockCertificateDao;
    @Mock
    private CertificateMapper certificateMapper;
    @Mock
    private TagService mockTagService;
    @InjectMocks
    private CertificateServiceImpl certificateService;
    private static Certificate certificate;
    private static CertificateDto certificateDto;
    private static TagDto boatDto;
    private static Tag boat;

    @BeforeAll
    static void initData() {
        certificate = Certificate.builder().id(1L).name("name").description("description")
                .createDate(Instant.now()).lastUpdateDate(Instant.now()).duration(5).build();
        certificateDto = CertificateDto.builder().id(1L).name("name").description("description")
                .createDate(Instant.now()).lastUpdateDate(Instant.now()).duration(5).build();
        boat = Tag.builder().name("boat").build();
        boatDto = TagDto.builder().id(7L).name("boat").build();
        certificate.setTags(Collections.singletonList(boat));
        certificateDto.setTags(Collections.singletonList(boatDto));
    }

    @Test
    void getOne() {
        Mockito.when(certificateMapper.fromModel(certificate)).thenReturn(certificateDto);
        Mockito.when(mockCertificateDao.getOne(1L)).thenReturn(Optional.of(certificate));

        Assertions.assertEquals(certificateDto, certificateService.getOne(1L));
        Mockito.verify(mockCertificateDao, times(1)).getOne(1L);

    }

    @Test
    void getAllTags() {
        List<CertificateDto> certificateDtoList = Collections.singletonList(certificateDto);
        List<Certificate> certificateList = Collections.singletonList(certificate);

        Mockito.when(certificateMapper.fromModel(certificate)).thenReturn(certificateDto);
        Mockito.when(mockCertificateDao.getAll()).thenReturn(certificateList);

        Assertions.assertEquals(certificateDtoList, certificateService.getAll());
        Mockito.verify(mockCertificateDao, times(1)).getAll();

    }

    @Test
    void deleteCertificate() {
        Mockito.doNothing().when(mockCertificateDao).deleteCertificateLink(5L);
        Mockito.doNothing().when(mockCertificateDao).delete(5L);

        certificateService.deleteCertificate(5L);

        Mockito.verify(mockCertificateDao, times(1)).deleteCertificateLink(5L);
        Mockito.verify(mockCertificateDao, times(1)).delete(5L);
    }

    @Test
    void createCertificate() {

        Mockito.when(certificateMapper.toModel(certificateDto)).thenReturn(certificate);
        Mockito.when(mockCertificateDao.insert(certificate)).thenReturn(certificate);
        Mockito.when(mockTagService.createTag(boatDto)).thenReturn(boatDto);
        Mockito.doNothing().when(mockCertificateDao)
                .insertCertificateTagLink(1L, Collections.singletonList(7L));
        Mockito.when(certificateMapper.fromModel(certificate)).thenReturn(certificateDto);

        Assertions.assertEquals(certificateDto, certificateService.createCertificate(certificateDto));
        Mockito.verify(mockCertificateDao, times(1)).insert(certificate);

    }

    @Test
    void updateCertificate() {

        Mockito.when(certificateMapper.toModel(certificateDto)).thenReturn(certificate);
        Mockito.doNothing().when(mockCertificateDao).update(certificate);
        Mockito.when(mockTagService.createTag(boatDto)).thenReturn(boatDto);
        Mockito.doNothing().when(mockCertificateDao)
                .insertCertificateTagLink(1L, Collections.singletonList(7L));

        certificateService.updateCertificate(certificateDto);

        Mockito.verify(mockCertificateDao, times(1)).update(certificate);
        Mockito.verify(mockCertificateDao,
                times(1)).insertCertificateTagLink(1L, Collections.singletonList(7L));
    }

}
