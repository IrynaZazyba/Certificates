package com.epam.esm.repository.impl;

import com.epam.esm.domain.Order;
import com.epam.esm.repository.OrderDao;
import com.epam.esm.util.Paginator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class OrderDaoImpl implements OrderDao {

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public List<Order> findAllByUserId(Paginator paginator, Long id) {
        TypedQuery<Order> query = entityManager.createQuery("select o from Order o where o.user.id=?1", Order.class);
        query.setParameter(1, id);
        query.setFirstResult(paginator.getStart());
        query.setMaxResults(paginator.getRecordsPerPage());
        return query.getResultList();
    }

    @Override
    public Optional<Order> findByIdAndUserId(Long id, Long userId) {
        try {
            TypedQuery<Order> query = entityManager
                    .createQuery("select o from Order o where o.user.id=?1 and o.id=?2", Order.class);
            query.setParameter(1, userId);
            query.setParameter(2, id);
            return Optional.of(query.getSingleResult());
        } catch (NoResultException ex) {
            return Optional.empty();
        }
    }

    @Override
    public Order save(Order order) {
        entityManager.persist(order);
        return order;
    }
}



