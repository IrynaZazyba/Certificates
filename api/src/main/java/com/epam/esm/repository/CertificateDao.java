package com.epam.esm.repository;

import com.epam.esm.domain.Certificate;
import com.epam.esm.domain.Filter;
import com.epam.esm.util.Paginator;

import java.util.List;

public interface CertificateDao extends Dao<Certificate> {

    Certificate save(Certificate certificate);

    void deleteById(Long id);

    List<Certificate> findByFilter(Paginator paginator,
                                   Filter filter);
}
