package com.epam.esm.dto.mapper;

import com.epam.esm.domain.Order;
import com.epam.esm.dto.OrderDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Data
@AllArgsConstructor
public class OrderMapper {

    private final CertificateMapper certificateMapper;

    public OrderDto mapFromDto(Order order) {
        return OrderDto.builder()
                .id(order.getId())
                .cost(order.getCost())
                .certificate(certificateMapper.fromModel(order.getCertificate()))
                .purchaseDate(order.getPurchaseDate())
                .build();
    }

    public Order mapToModel(OrderDto orderDto) {
        return Order.builder()
                .id(orderDto.getId())
                .certificate(certificateMapper.toModel(orderDto.getCertificate()))
                .purchaseDate(orderDto.getPurchaseDate())
                .cost(orderDto.getCost())
                .build();
    }

    public List<OrderDto> toDtoList(List<Order> orders) {
        return orders.stream().map(this::mapFromDto).collect(Collectors.toList());
    }

    public List<Order> toModelList(List<OrderDto> orders) {
        return orders.stream().map(this::mapToModel).collect(Collectors.toList());
    }

    public OrderDto mapOrderInfo(Order order) {
        return OrderDto.builder().cost(order.getCost()).purchaseDate(order.getPurchaseDate()).build();
    }

}
