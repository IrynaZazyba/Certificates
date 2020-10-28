package com.epam.esm.dao;

import com.epam.esm.dao.impl.CertificateDaoImpl;
import com.epam.esm.dao.mapper.CertificateRowMapper;
import com.epam.esm.dao.mapper.CertificateExtractor;
import com.epam.esm.domain.Certificate;
import com.epam.esm.domain.Filter;
import com.epam.esm.exception.ResourceNotFoundException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import java.time.LocalDateTime;
import java.util.List;

public class CertificateDaoImplTest {

    private EmbeddedDatabase embeddedDatabase;
    private JdbcTemplate jdbcTemplate;
    private CertificateRowMapper tagRowMapper;
    private CertificateExtractor certificateExtractor;
    private CertificateDao certificateDao;

    @Before
    public void setUp() {
        embeddedDatabase = new EmbeddedDatabaseBuilder()
                .addDefaultScripts()
                .setType(EmbeddedDatabaseType.H2)
                .build();

        this.jdbcTemplate = new JdbcTemplate(embeddedDatabase);
        this.tagRowMapper = new CertificateRowMapper();
        this.certificateExtractor = new CertificateExtractor();
        this.certificateDao = new CertificateDaoImpl(jdbcTemplate, tagRowMapper, certificateExtractor);
    }

    @After
    public void tearDown() {
        embeddedDatabase.shutdown();
    }

    @Test
    public void testFindOnePositive() {
        Assertions.assertNotNull(certificateDao.getOne(121L));
    }

    @Test
    public void testFindOneNegative() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> certificateDao.getOne(11L));
    }

    @Test
    public void getAllCertificates() {
        Assertions.assertNotNull(certificateDao.getAllCertificates());
        Assertions.assertEquals(4, certificateDao.getAllCertificates().size());
    }

    @Test
    public void insertCertificate() {
        Certificate certificate = Certificate.builder()
                .name("name")
                .description("description")
                .createDate(LocalDateTime.now())
                .lastUpdateDate(LocalDateTime.now()).duration(5).build();
        Long id = certificateDao.insertCertificate(certificate);
        Assertions.assertNotNull(certificateDao.getOne(id));
    }

    @Test
    public void deleteCertificate() {
        Assertions.assertNotNull(certificateDao.getOne(124L));
        certificateDao.deleteCertificateLink(124L);
        certificateDao.deleteCertificate(124L);
        Assertions.assertThrows(ResourceNotFoundException.class, () -> certificateDao.getOne(124L));
    }

    @Test
    public void updateCertificate() {
        Certificate updated = Certificate.builder()
                .id(124L)
                .lastUpdateDate(LocalDateTime.now())
                .name("updatedName")
                .build();
        certificateDao.updateCertificate(updated);
        Assertions.assertEquals(updated.getName(), certificateDao.getOne(124L).getName());
    }

    @Test
    public void filterCertificate_OptionalParams_OneCertificate() {
        Filter filter = Filter.builder().searchDescription("sea").tagName("tag6").build();
        Assertions.assertNotNull(certificateDao.filterCertificate(filter));
    }

    @Test
    public void filterCertificate_OptionalParams_SeveralCertificates() {
        Filter filter = Filter.builder().searchName("pd").build();
        Assertions.assertEquals(4, certificateDao.filterCertificate(filter).size());
    }

    @Test
    public void filterCertificate_AllParams() {
        Filter filter = Filter.builder()
                .sort("name")
                .order("ASC")
                .searchDescription("boat")
                .tagName("tag3")
                .searchName("pd2").build();
        Assertions.assertNotNull(certificateDao.filterCertificate(filter));
    }

    @Test
    public void deleteCertificateLink() {
        Assertions.assertNotNull(certificateDao.getOne(124L));
        Assertions.assertThrows(Exception.class, () -> certificateDao.deleteCertificate(124L));
        certificateDao.deleteCertificateLink(124L);
        certificateDao.deleteCertificate(124L);
        Assertions.assertThrows(ResourceNotFoundException.class, () -> certificateDao.getOne(124L));
    }

    @Test
    public void insertCertificateTagLink() {
        Long certificateId = 124L;
        List<Long> tagsId = List.of(111L, 112L);
        certificateDao.insertCertificateTagLink(certificateId, tagsId);
        String query = "SELECT count(`certificate_id`) FROM `certificate_has_tag` WHERE certificate_id=?";
        int rowCount = this.jdbcTemplate.queryForObject(query, Integer.class,124);
        Assertions.assertEquals(3,rowCount);
    }

}
