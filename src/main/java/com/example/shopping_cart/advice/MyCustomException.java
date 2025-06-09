package com.example.shopping_cart.advice;

public class MyCustomException extends RuntimeException {


    public MyCustomException(String message) {

        super(message);
    }

    @Override
    public String toString() {

        return "EmptyDataException{message='" + super.getMessage() + '\'' + '}';
    }

}