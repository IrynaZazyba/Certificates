package com.epam.esm.service;

import com.epam.esm.dao.CertificateDao;
import com.epam.esm.domain.Certificate;
import com.epam.esm.domain.Tag;
import com.epam.esm.dto.CertificateDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.dto.mapper.CertificateMapper;
import com.epam.esm.service.impl.CertificateServiceImpl;
import com.epam.esm.service.validation.impl.CertificateValidatorImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class CertificateServiceTest {

    @Mock
    private CertificateDao mockCertificateDao;
    @Mock
    private CertificateMapper certificateMapper;
    @Mock
    private TagService mockTagService;
    @Mock
    private CertificateValidatorImpl mockCertificateValidator;
    @InjectMocks
    private CertificateServiceImpl certificateService;


    @Test
    void getOne() {
        Certificate certificate = Certificate.builder().id(1L).name("name").description("description")
                .createDate(LocalDateTime.now()).lastUpdateDate(LocalDateTime.now()).duration(5).build();
        CertificateDto certificateDto = CertificateDto.builder().id(1L).name("name").description("description")
                .createDate(LocalDateTime.now()).lastUpdateDate(LocalDateTime.now()).duration(5).build();
        Mockito.when(certificateMapper.fromModel(certificate)).thenReturn(certificateDto);
        Mockito.when(mockCertificateDao.getOne(1L)).thenReturn(certificate);
        Assertions.assertEquals(certificateDto, certificateService.getOne(1L));
    }

    @Test
    void getAllTags() {
        Certificate certificate = Certificate.builder().id(1L).name("name").description("description")
                .createDate(LocalDateTime.now()).lastUpdateDate(LocalDateTime.now()).duration(5).build();
        CertificateDto certificateDto = CertificateDto.builder().id(1L).name("name").description("description")
                .createDate(LocalDateTime.now()).lastUpdateDate(LocalDateTime.now()).duration(5).build();
        List<CertificateDto> certificateDtoList = Collections.singletonList(certificateDto);
        List<Certificate> certificateList = Collections.singletonList(certificate);
        Mockito.when(certificateMapper.fromModel(certificate)).thenReturn(certificateDto);
        Mockito.when(mockCertificateDao.getAllCertificates()).thenReturn(certificateList);
        Assertions.assertEquals(certificateDtoList, certificateService.getAll());
    }

    @Test
    void deleteCertificate() {
        Mockito.doNothing().when(mockCertificateDao).deleteCertificateLink(5L);
        Mockito.doNothing().when(mockCertificateDao).deleteCertificate(5L);
        certificateService.deleteCertificate(5L);
        Mockito.verify(mockCertificateDao, times(1)).deleteCertificateLink(5L);
        Mockito.verify(mockCertificateDao, times(1)).deleteCertificate(5L);
    }

    @Test
    void createCertificate() {
        Certificate certificate = Certificate.builder().name("name").description("description")
                .createDate(LocalDateTime.now()).lastUpdateDate(LocalDateTime.now()).duration(5).build();
        Tag boatDto = Tag.builder().name("boat").build();
        certificate.setTags(Collections.singletonList(boatDto));
        CertificateDto certificateDto = CertificateDto.builder().name("name").description("description")
                .createDate(LocalDateTime.now()).lastUpdateDate(LocalDateTime.now()).duration(5).build();
        TagDto boat = TagDto.builder().name("boat").build();
        certificateDto.setTags(Collections.singletonList(boat));

        List<Long> insertedTagsId = new ArrayList<>();
        insertedTagsId.add(7L);

        Mockito.when(mockCertificateValidator.validate(certificateDto)).thenReturn(true);
        Mockito.when(certificateMapper.toModel(certificateDto)).thenReturn(certificate);
        Mockito.when(mockCertificateDao.insertCertificate(certificate)).thenReturn(5L);
        Mockito.when(mockTagService.createTag(boat)).thenReturn(7L);
        Mockito.doNothing().when(mockCertificateDao).insertCertificateTagLink(5L, insertedTagsId);
        Assertions.assertEquals(5L, certificateService.createCertificate(certificateDto));
    }

    @Test
    void updateCertificate() {
        Certificate certificate = Certificate.builder().id(11L).name("name").description("description")
                .lastUpdateDate(LocalDateTime.now()).duration(5).build();
        Tag boatDto = Tag.builder().name("boat").build();
        certificate.setTags(Collections.singletonList(boatDto));
        CertificateDto certificateDto = CertificateDto.builder().id(11L).name("name").description("description")
                .duration(5).build();
        TagDto boat = TagDto.builder().name("boat").build();
        certificateDto.setTags(Collections.singletonList(boat));
        List<Long> insertedTagsId = new ArrayList<>();
        insertedTagsId.add(7L);

        Mockito.when(certificateMapper.toModel(certificateDto)).thenReturn(certificate);
        Mockito.doNothing().when(mockCertificateDao).updateCertificate(certificate);
        Mockito.when(mockTagService.createTag(boat)).thenReturn(7L);
        Mockito.doNothing().when(mockCertificateDao).insertCertificateTagLink(11L, insertedTagsId);

        certificateService.updateCertificate(certificateDto);

        Mockito.verify(mockCertificateDao, times(1)).updateCertificate(certificate);
        Mockito.verify(mockCertificateDao,
                times(1)).insertCertificateTagLink(11L, insertedTagsId);
    }

}
