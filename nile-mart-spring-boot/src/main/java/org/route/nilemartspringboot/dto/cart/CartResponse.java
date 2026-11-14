package org.route.nilemartspringboot.dto.cart;

import lombok.Builder;
import lombok.Data;
import org.route.nilemartspringboot.dto.customer.CustomerResponse;

import java.util.List;

@Data
@Builder
public class CartResponse {
    private String id;
    private CustomerResponse customer;
    private List<CartItemResponse> cartItems;
}
