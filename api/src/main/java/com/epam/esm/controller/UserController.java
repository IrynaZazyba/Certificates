package com.epam.esm.controller;

import com.epam.esm.dto.CertificateDto;
import com.epam.esm.dto.OrderDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.dto.UserDto;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.UserService;
import com.epam.esm.util.Paginator;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/users")
public class UserController {

    private final UserService userService;
    private final OrderService orderService;

    /**
     * Get user by identifier
     *
     * @param id requested user identifier
     * @return requested user
     */
    @RequestMapping(value = "/{id}", method = GET, produces = APPLICATION_JSON_VALUE)
    public UserDto getOne(@PathVariable("id") Long id) {
        UserDto user = userService.getOne(id);
        user.add(linkTo(UserController.class).slash(user.getId()).withSelfRel());
        return user;
    }

    /**
     * Show list of users
     *
     * @param page current page
     * @param size number records per page
     * @return list of users
     */
    @RequestMapping(method = GET)
    @ResponseBody
    public CollectionModel<UserDto> getAll(@RequestParam(required = false) Integer page,
                                           @RequestParam(required = false) Integer size) {
        Paginator paginator = new Paginator(size, page);
        List<UserDto> users = userService.getAll(paginator);
        users.forEach(user -> user.add(linkTo(UserController.class).slash(user.getId()).withSelfRel()));
        Link link = linkTo(UserController.class).withSelfRel();
        return CollectionModel.of(users).add(link);
    }

    /**
     * Show users orders
     *
     * @param page current page
     * @param size number records per page
     * @param id   requested user identifier
     * @return list of orders
     */
    @RequestMapping(method = GET, value = "/{id}/orders")
    public CollectionModel<OrderDto> getUsersOrders(@RequestParam(required = false) Integer page,
                                                    @RequestParam(required = false) Integer size,
                                                    @PathVariable Long id) {
        Paginator paginator = new Paginator(size, page);
        List<OrderDto> usersOrders = orderService.getUsersOrders(paginator, id);
        usersOrders.forEach(order -> {
            order.add(linkTo(UserController.class).slash(order.getId()).withSelfRel());
            addLinkToCertificate(order.getCertificate());
        });
        Link link = linkTo(methodOn(UserController.class).getUsersOrders(page, size, id)).withSelfRel();
        return CollectionModel.of(usersOrders).add(link);
    }

    /**
     * Allows users to place an order
     *
     * @param id requested user identifier
     * @return order
     */
    @RequestMapping(method = POST, value = "/{id}/orders")
    public OrderDto makeOrder(@PathVariable Long id, @RequestBody CertificateDto certificateDto) {
        OrderDto orderDto = orderService.makeOrder(id, certificateDto);
        CertificateDto certificate = orderDto.getCertificate();
        addLinkToCertificate(certificate);
        orderDto.add(linkTo(methodOn(UserController.class).getOrderInfo(id, orderDto.getId())).withSelfRel());
        return orderDto;
    }

    /**
     * Show order info
     *
     * @param id requested user identifier
     * @return order
     */
    @RequestMapping(method = GET, value = "/{id}/orders/{orderId}")
    public OrderDto getOrderInfo(@PathVariable Long id,
                                 @PathVariable Long orderId) {
        OrderDto orderInfo = orderService.getOrderInfo(id, orderId);
        orderInfo.add(linkTo(methodOn(UserController.class).getOrderInfo(id, orderId)).withSelfRel());
        return orderInfo;
    }

    /**
     * Get the most widely used tag of a user with the
     * highest cost of all orders
     *
     * @param id requested user identifier
     * @return tag
     */
    @RequestMapping(value = "/{id}/tags/popular", method = GET)
    public TagDto getMostPopular(@PathVariable Long id) {
        TagDto tag = userService.getMostPopularUserTagByOrderSum(id);
        tag.add(linkTo(methodOn(UserController.class).getMostPopular(id)).withSelfRel());
        return tag;
    }

    private void addLinkToCertificate(CertificateDto certificate) {
        certificate.add(linkTo(methodOn(CertificateController.class).getOne(certificate.getId())).withSelfRel());
        List<TagDto> tags = certificate.getTags();
        if (tags.size() != 0) {
            tags.forEach(tag -> tag.add(linkTo(TagController.class).slash(tag.getId()).withSelfRel()));
        }
    }

}
