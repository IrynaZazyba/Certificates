package com.epam.esm.dao;

import com.epam.esm.domain.Certificate;
import com.epam.esm.domain.Filter;
import com.epam.esm.domain.Tag;

import java.util.List;
import java.util.Map;

public interface CertificateDao {

    Certificate getOne(Long id);

    List<Certificate> getAllCertificates();

    Long insertCertificate(Certificate certificate);

    void insertCertificateTagLink(Long certificateId, List<Long> tagsId);

    void deleteCertificateLink(Long id);

    void deleteCertificate(Long id);

    Map<Certificate, List<Tag>> filterCertificate(Filter filter);

    void updateCertificate(Certificate certificate);
}
