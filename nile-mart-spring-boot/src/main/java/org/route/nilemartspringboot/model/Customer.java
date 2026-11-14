package org.route.nilemartspringboot.model;

import lombok.Builder;
import lombok.Data;
import org.route.nilemartspringboot.enums.AccountStatus;
import org.route.nilemartspringboot.enums.Role;



@Data
@Builder
public class Customer {
    private String id;
    private String name;
    private String hashPassword;
    private Role role;
    private AccountStatus accountStatus;
    private String email;
    private String phone;
}
