package com.epam.esm.service;

import com.epam.esm.dto.CertificateDto;
import com.epam.esm.dto.OrderDto;
import com.epam.esm.util.Paginator;

import java.util.List;

public interface OrderService {

    List<OrderDto> getUsersOrders(Paginator paginator, Long userId);

    OrderDto makeOrder(Long userId, CertificateDto certificateDto);

    OrderDto getOrderInfo(Long userId, Long orderId);
}
