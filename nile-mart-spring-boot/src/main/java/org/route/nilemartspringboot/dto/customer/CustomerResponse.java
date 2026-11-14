package org.route.nilemartspringboot.dto.customer;

import lombok.Data;

@Data
public class CustomerResponse {
    private String id;
    private String name;
    private String email;
    private String phone;
}
