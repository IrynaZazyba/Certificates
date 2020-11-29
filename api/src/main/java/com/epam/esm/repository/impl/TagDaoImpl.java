package com.epam.esm.repository.impl;

import com.epam.esm.domain.Tag;
import com.epam.esm.repository.TagDao;
import com.epam.esm.util.Paginator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TagDaoImpl implements TagDao {

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public Long filterTags(Long userId) {
        Object singleResult = entityManager
                .createNativeQuery(
                        "SELECT temp.tag_id FROM (" +
                                "SELECT tag.id as tag_id, count(tag.id)as count, user_id, sum(cost)as s FROM `tag` " +
                                "INNER JOIN certificate_has_tag on certificate_has_tag.tag_id=tag.id " +
                                "INNER JOIN certificate ON certificate.id=certificate_has_tag.certificate_id " +
                                "INNER JOIN orders o ON o.id=certificate.id WHERE user_id=?1 " +
                                "GROUP BY tag_id) as temp HAVING MAX(temp.count) and MAX(temp.s)")
                .setParameter(1, userId)
                .getSingleResult();
        return ((BigInteger) singleResult).longValue();
    }

    @Override
    public Optional<Tag> getOne(Long id) {
        Tag tag = entityManager.find(Tag.class, id);
        return Optional.of(tag);
    }

    @Override
    public List<Tag> getAll(Paginator paginator) {
        TypedQuery<Tag> query = entityManager.createQuery("select t from Tag t", Tag.class);
        query.setFirstResult(paginator.getStart());
        query.setMaxResults(paginator.getRecordsPerPage());
        return query.getResultList();
    }

    @Override
    public Optional<Tag> findByName(String name) {
        try {
            TypedQuery<Tag> query = entityManager.createQuery("select t from Tag t where name=?1", Tag.class);
            query.setParameter(1, name);
            return Optional.of(query.getSingleResult());
        } catch (NoResultException ex) {
            return Optional.empty();
        }
    }

    @Override
    public Tag save(Tag tag) {
        entityManager.persist(tag);
        return tag;
    }

    @Override
    public void deleteById(Long id) {
        getOne(id).ifPresent(entityManager::remove);
    }

}
