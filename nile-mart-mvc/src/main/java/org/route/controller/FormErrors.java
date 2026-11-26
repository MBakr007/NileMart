package org.route.controller;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

final class FormErrors {

    private FormErrors() {
    }

    static Set<String> fieldsWithErrors(BindingResult result) {
        return result.getFieldErrors().stream()
                .map(FieldError::getField)
                .collect(Collectors.toSet());
    }

    static void rejectAll(BindingResult result, Map<String, String> errors) {
        errors.forEach((field, message) ->
                result.rejectValue(field, "serviceValidation." + field, message));
    }
}
