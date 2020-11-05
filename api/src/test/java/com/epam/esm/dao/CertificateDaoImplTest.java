package com.epam.esm.dao;

import com.epam.esm.dao.impl.CertificateDaoImpl;
import com.epam.esm.dao.mapper.CertificateExtractor;
import com.epam.esm.dao.mapper.CertificateRowMapper;
import com.epam.esm.domain.Certificate;
import com.epam.esm.domain.Filter;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public class CertificateDaoImplTest {

    private static EmbeddedDatabase embeddedDatabase;
    private static JdbcTemplate jdbcTemplate;
    private static CertificateRowMapper certificateRowMapper;
    private static CertificateExtractor certificateExtractor;
    private static CertificateDao certificateDao;


    @BeforeClass
    public static void setUp() {
        embeddedDatabase = new EmbeddedDatabaseBuilder()
                .addDefaultScripts()
                .setType(EmbeddedDatabaseType.H2)
                .build();
        jdbcTemplate = new JdbcTemplate(embeddedDatabase);
        certificateRowMapper = new CertificateRowMapper();
        certificateExtractor = new CertificateExtractor();
        certificateDao = new CertificateDaoImpl(jdbcTemplate, certificateRowMapper, certificateExtractor);
    }


    @AfterClass
    public static void tearDown() {
        embeddedDatabase.shutdown();
    }

    @Test
    public void testFindOnePositive() {
        Assertions.assertNotNull(certificateDao.getOne(121L));
    }

    @Test
    public void testFindOneNegative() {
        Assertions.assertEquals(Optional.empty(), certificateDao.getOne(11L));
    }

    @Test
    public void getAllCertificates() {
        Assertions.assertNotNull(certificateDao.getAll());
        Assertions.assertEquals(3, certificateDao.getAll().size());
    }

    @Test
    public void insertCertificate() {
        Certificate certificate = Certificate.builder()
                .name("name")
                .description("description")
                .createDate(Instant.now())
                .lastUpdateDate(Instant.now()).duration(5).build();
        Assertions.assertEquals(certificate, certificateDao.insert(certificate));
    }

    @Test
    public void deleteCertificate() {
        Assertions.assertNotNull(certificateDao.getOne(124L));
        certificateDao.deleteCertificateLink(124L);
        certificateDao.delete(124L);
        Assertions.assertEquals(Optional.empty(), certificateDao.getOne(124L));
    }

    @Test
    public void updateCertificate() {
        Certificate updated = Certificate.builder()
                .id(122L)
                .lastUpdateDate(Instant.now())
                .name("updatedName")
                .build();
        certificateDao.update(updated);
        Assertions.assertEquals(updated.getName(), certificateDao.getOne(122L).get().getName());
    }

    @Test
    public void filterCertificate_OptionalParams_OneCertificate() {
        Filter filter = Filter.builder().description("sea").tagName("tag6").build();
        Assertions.assertNotNull(certificateDao.filterCertificate(filter));
    }

    @Test
    public void filterCertificate_OptionalParams_SeveralCertificates() {
        Filter filter = Filter.builder().name("pd").build();
        Assertions.assertEquals(4, certificateDao.filterCertificate(filter).size());
    }

    @Test
    public void filterCertificate_AllParams() {
        Filter filter = Filter.builder()
                .sort("name")
                .order("ASC")
                .description("boat")
                .tagName("tag3")
                .name("pd2").build();
        Assertions.assertNotNull(certificateDao.filterCertificate(filter));
    }

    @Test
    public void deleteCertificateLink() {
        Assertions.assertNotNull(certificateDao.getOne(124L));
        Assertions.assertThrows(Exception.class, () -> certificateDao.delete(124L));
        certificateDao.deleteCertificateLink(124L);
        certificateDao.delete(124L);
        Assertions.assertEquals(Optional.empty(), certificateDao.getOne(124L));
    }

    @Test
    public void insertCertificateTagLink() {
        Long certificateId = 124L;
        List<Long> tagsId = List.of(111L, 112L);
        certificateDao.insertCertificateTagLink(certificateId, tagsId);
        String query = "SELECT count(`certificate_id`) FROM `certificate_has_tag` WHERE certificate_id=?";
        int rowCount = this.jdbcTemplate.queryForObject(query, Integer.class, 124);
        Assertions.assertEquals(3, rowCount);
    }

}
