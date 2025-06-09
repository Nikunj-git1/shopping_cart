package com.example.shopping_cart.advice;


import lombok.Data;

import java.util.List;

// Ask. to sir Global ece. handler is good for ExcelImport

@Data
public class ExcelValidationException extends RuntimeException {
    private List<String> errors;

    public ExcelValidationException(String message, List<String> errors) {
        super(message);
        this.errors = errors;
    }

    public List<String> getErrors() {
        return errors;
    }
}