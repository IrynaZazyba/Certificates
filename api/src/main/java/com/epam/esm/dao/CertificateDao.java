package com.epam.esm.dao;

import com.epam.esm.domain.Certificate;
import com.epam.esm.domain.Filter;
import com.epam.esm.domain.Tag;

import java.util.List;
import java.util.Map;

public interface CertificateDao extends Dao<Certificate> {

    void insertCertificateTagLink(Long certificateId, List<Long> tagsId);

    void deleteCertificateLink(Long id);

    Map<Certificate, List<Tag>> filterCertificate(Filter filter);

    void update(Certificate certificate);
}
