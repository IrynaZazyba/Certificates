package com.epam.esm.dao;

import com.epam.esm.domain.Certificate;
import com.epam.esm.domain.Filter;
import com.epam.esm.domain.Tag;

import java.util.List;
import java.util.Map;

public interface CertificateDao extends Dao<Certificate> {

    void insertTagLink(Long certificateId, List<Long> tagsId);

    void deleteTagLink(Long id);

    Map<Certificate, List<Tag>> filterCertificate(Filter filter);

    void update(Certificate certificate);
}
