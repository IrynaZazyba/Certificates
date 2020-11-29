package com.epam.esm.repository;

import com.epam.esm.domain.Order;
import com.epam.esm.util.Paginator;

import java.util.List;
import java.util.Optional;

public interface OrderDao {

    List<Order> findAllByUserId(Paginator paginator, Long id);

    Optional<Order> findByIdAndUserId(Long id, Long userId);

    Order save(Order order);

}
