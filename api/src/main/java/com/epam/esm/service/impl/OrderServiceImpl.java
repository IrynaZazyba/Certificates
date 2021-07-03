package com.epam.esm.service.impl;

import com.epam.esm.domain.Order;
import com.epam.esm.domain.User;
import com.epam.esm.dto.CertificateDto;
import com.epam.esm.dto.OrderDto;
import com.epam.esm.dto.mapper.CertificateMapper;
import com.epam.esm.dto.mapper.OrderMapper;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.repository.OrderDao;
import com.epam.esm.service.CertificateService;
import com.epam.esm.service.OrderService;
import com.epam.esm.util.Paginator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderDao orderdao;
    private final OrderMapper orderMapper;
    private final CertificateMapper certificateMapper;
    private final CertificateService certificateService;

    @Override
    public List<OrderDto> getUsersOrders(Paginator paginator, Long userId) {
        return orderdao.findAllByUserId(paginator, userId)
                .stream()
                .map(orderMapper::mapFromDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public OrderDto makeOrder(Long userId, CertificateDto certificateDto) {
        CertificateDto certificate = certificateService.getOne(certificateDto.getId());
        Order order = Order.builder()
                .purchaseDate(LocalDate.now())
                .cost(certificate.getPrice())
                .user(User.builder().id(userId).build())
                .certificate(certificateMapper.toModel(certificate))
                .build();
        return orderMapper.mapFromDto(orderdao.save(order));
    }

    @Override
    public OrderDto getOrderInfo(Long userId, Long orderId) {
        Order byIdAndUserId = orderdao.findByIdAndUserId(orderId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("No order with id=" + userId + " by current user"));
        return orderMapper.mapOrderInfo(byIdAndUserId);
    }

}
