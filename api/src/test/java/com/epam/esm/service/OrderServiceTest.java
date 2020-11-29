package com.epam.esm.service;

import com.epam.esm.domain.Certificate;
import com.epam.esm.domain.Order;
import com.epam.esm.dto.CertificateDto;
import com.epam.esm.dto.OrderDto;
import com.epam.esm.dto.mapper.OrderMapper;
import com.epam.esm.repository.OrderDao;
import com.epam.esm.service.impl.OrderServiceImpl;
import com.epam.esm.util.Paginator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    private static Paginator paginator;
    private static Order order;
    private static OrderDto orderDto;
    private static Certificate certificate;
    private static CertificateDto certificateDto;
    @Mock
    private OrderDao orderDao;
    @Mock
    private OrderMapper orderMapper;

    @InjectMocks
    private OrderServiceImpl orderService;

    @BeforeAll
    static void initData() {
        certificate = Certificate.builder().id(1L).name("name").description("description")
                .createDate(Instant.now()).lastUpdateDate(Instant.now()).duration(5).build();
        certificateDto = CertificateDto.builder().id(1L).name("name").description("description")
                .createDate(Instant.now()).lastUpdateDate(Instant.now()).duration(5).build();
        order = Order.builder().id(1L).cost(new BigDecimal(15.5)).purchaseDate(LocalDate.now())
                .certificate(certificate).build();
        orderDto = OrderDto.builder().id(1L).cost(new BigDecimal(15.5)).purchaseDate(LocalDate.now())
                .certificate(certificateDto).build();
        paginator = new Paginator(1, 10);

    }

    @Test
    void getUsersOrders() {
        List<Order> orders = Collections.singletonList(order);
        List<OrderDto> ordersDto = Collections.singletonList(orderDto);

        Mockito.when(orderDao.findAllByUserId(paginator, 1L)).thenReturn(orders);
        Mockito.when(orderMapper.mapFromDto(order)).thenReturn(orderDto);

        Assertions.assertEquals(ordersDto, orderService.getUsersOrders(paginator, 1L));
    }

}
