package com.epam.esm.repository.impl;

import com.epam.esm.domain.User;
import com.epam.esm.repository.UserDao;
import com.epam.esm.util.Paginator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserDaoImpl implements UserDao {

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public Optional<User> getOne(Long userId) {
        User user = entityManager.find(User.class, userId);
        return Optional.ofNullable(user);
    }

    @Override
    public List<User> getAll(Paginator paginator) {
        TypedQuery<User> query = entityManager.createQuery("select u from User u", User.class);
        query.setFirstResult(paginator.getStart());
        query.setMaxResults(paginator.getRecordsPerPage());
        return query.getResultList();
    }
}


