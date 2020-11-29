package com.epam.esm.repository.impl;

import com.epam.esm.domain.Certificate;
import com.epam.esm.domain.Filter;
import com.epam.esm.domain.Sorting;
import com.epam.esm.domain.SortingField;
import com.epam.esm.repository.CertificateDao;
import com.epam.esm.util.Paginator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CertificateDaoImpl implements CertificateDao {

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public Optional<Certificate> getOne(Long id) {
        Certificate certificate = entityManager.find(Certificate.class, id);
        return Optional.of(certificate);
    }

    @Override
    public List<Certificate> getAll(Paginator paginator) {
        TypedQuery<Certificate> query = entityManager.createQuery("select c from Certificate c", Certificate.class);
        query.setFirstResult(paginator.getStart());
        query.setMaxResults(paginator.getRecordsPerPage());
        return query.getResultList();
    }

    @Override
    public Certificate save(Certificate certificate) {
        entityManager.persist(certificate);
        return certificate;
    }

    @Override
    public void deleteById(Long id) {
        getOne(id).ifPresent(entityManager::remove);
    }


    @Override
    public List<Certificate> findByFilter(Paginator paginator,
                                          Filter filter) {
        TypedQuery<Certificate> query;
        String[] tagNames = filter.getTagNames();
        if (Objects.nonNull(tagNames)) {
            String preparedQuery = "select distinct c from Certificate c " +
                    "join c.tags t where c.name like ?1 and c.description like ?2 and " +
                    "t.name in (?3) GROUP BY 1 HAVING count(c)=?4";
            query = entityManager.createQuery(buildSortingQuery(preparedQuery, filter), Certificate.class);
            query.setParameter(1, Objects.isNull(filter.getName()) ? "%%" : "%" + filter.getName() + "%");
            query.setParameter(2, Objects.isNull(filter.getDescription()) ? "%%" : "%" + filter.getDescription() + "%");
            query.setParameter(3, List.of(tagNames));
            query.setParameter(4, Long.valueOf(tagNames.length));
        } else {
            String preparedQuery = "select distinct c from Certificate c " +
                    "join c.tags t where c.name like ?1 and c.description like ?2";
            query = entityManager.createQuery(buildSortingQuery(preparedQuery, filter), Certificate.class);
            query.setParameter(1, Objects.nonNull(filter.getName()) ? "%" + filter.getName() + "%" : "%%");
            query.setParameter(2, Objects.nonNull(filter.getDescription()) ? "%" + filter.getDescription() + "%" : "%%");
        }
        query.setFirstResult(paginator.getStart());
        query.setMaxResults(paginator.getRecordsPerPage());
        return query.getResultList();
    }

    private String buildSortingQuery(String query, Filter filter) {
        if (filter.getSort() == SortingField.DATE) {
            query = query + " order by c.createDate";
        }

        if (filter.getSort() == SortingField.NAME) {
            query = query + " order by c.name";
        }

        if (filter.getOrder() == Sorting.ASC) {
            query = query + " ASC";
        }

        if (filter.getOrder() == Sorting.DESC) {
            query = query + " DESC";
        }

        return query;
    }
}
