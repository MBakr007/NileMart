package org.route.model;

/**
 * Customer contact details. {@code email} is optional and may be {@code null}.
 */
public record Customer(String fullName, String phone, String email) {
}
