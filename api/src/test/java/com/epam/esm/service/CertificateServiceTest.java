package com.epam.esm.service;

import com.epam.esm.domain.Certificate;
import com.epam.esm.domain.Filter;
import com.epam.esm.domain.SortingField;
import com.epam.esm.domain.Tag;
import com.epam.esm.dto.CertificateDto;
import com.epam.esm.dto.FilterDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.dto.mapper.CertificateMapper;
import com.epam.esm.dto.mapper.FilterMapper;
import com.epam.esm.repository.CertificateDao;
import com.epam.esm.service.impl.CertificateServiceImpl;
import com.epam.esm.util.Paginator;
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

    private static Certificate certificate;
    private static CertificateDto certificateDto;
    private static TagDto boatDto;
    private static Tag boat;
    private static Paginator paginator;

    @Mock
    private CertificateDao mockCertificateDao;
    @Mock
    private CertificateMapper certificateMapper;
    @Mock
    private FilterMapper filterMapper;
    @InjectMocks
    private CertificateServiceImpl certificateService;

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
        paginator = new Paginator(10, 1);
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
        Mockito.when(mockCertificateDao.getAll(paginator)).thenReturn(certificateList);

        Assertions.assertEquals(certificateDtoList, certificateService.getAll(paginator));
        Mockito.verify(mockCertificateDao, times(1)).getAll(paginator);

    }

    @Test
    void deleteCertificate() {
        Mockito.doNothing().when(mockCertificateDao).deleteById(5L);

        certificateService.delete(5L);

        Mockito.verify(mockCertificateDao, times(1)).deleteById(5L);
    }

    @Test
    void createCertificate() {
        Mockito.when(certificateMapper.toModel(certificateDto)).thenReturn(certificate);
        Mockito.when(mockCertificateDao.save(certificate)).thenReturn(certificate);
        Mockito.when(certificateMapper.fromModel(certificate)).thenReturn(certificateDto);

        Assertions.assertEquals(certificateDto, certificateService.create(certificateDto));
        Mockito.verify(mockCertificateDao, times(1)).save(certificate);
    }

    @Test
    void updateCertificate() {
        Mockito.when(mockCertificateDao.getOne(1L)).thenReturn(Optional.of(certificate));
        Mockito.when(certificateMapper.map(certificateDto, certificate)).thenReturn(certificate);
        Mockito.when(mockCertificateDao.save(certificate)).thenReturn(certificate);
        Mockito.when(certificateMapper.fromModel(certificate)).thenReturn(certificateDto);

        Assertions.assertEquals(certificateDto, certificateService.update(certificateDto));
        Mockito.verify(mockCertificateDao, times(1)).save(certificate);
    }

    @Test
    void filter() {
        List<CertificateDto> certificateDtoList = Collections.singletonList(certificateDto);
        List<Certificate> certificateList = Collections.singletonList(certificate);
        FilterDto filterDto = FilterDto.builder().name("test").sort(SortingField.DATE).build();
        Filter filter = Filter.builder().name("test").sort(SortingField.DATE).build();

        Mockito.when(filterMapper.toModel(filterDto)).thenReturn(filter);
        Mockito.when(mockCertificateDao.findByFilter(paginator, filter)).thenReturn(certificateList);
        Mockito.when(certificateMapper.fromModel(certificate)).thenReturn(certificateDto);

        Assertions.assertEquals(certificateDtoList, certificateService.filter(paginator, filterDto));
        Mockito.verify(mockCertificateDao, times(1)).findByFilter(paginator, filter);

    }

}
