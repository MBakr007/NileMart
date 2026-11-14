package org.route.nilemartspringboot.mapper;

import org.mapstruct.Mapper;
import org.route.nilemartspringboot.dto.cart.CartResponse;
import org.route.nilemartspringboot.model.Cart;

@Mapper(componentModel = "spring")
public interface CartMapper {

    CartResponse toCartResponse(Cart cart);
}
