package com.epam.esm.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto extends RepresentationModel<UserDto> {

    private Long id;
    @NotBlank(message = "{error.name.mandatory}")
    @Size(min = 1, max = 50, message = "{error.username.size}")
    private String username;
    @NotBlank(message = "{error.name.mandatory}")
    @Size(min = 1, max = 50, message = "{error.surname.size}")
    private String surname;
    private List<OrderDto> orders;

}
