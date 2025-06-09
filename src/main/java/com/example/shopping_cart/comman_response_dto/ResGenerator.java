package com.example.shopping_cart.comman_response_dto;

import jakarta.persistence.Table;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.NoSuchElementException;

public class ResGenerator {
    public static ResponseEntity<CommonResponse> success(String message, Object data) {
        return new ResponseEntity<>(new CommonResponse(true, message, data), HttpStatus.OK);
    }

    public static ResponseEntity<CommonResponse> create(String message, Object data) {
        return new ResponseEntity<>(new CommonResponse(true, message, data), HttpStatus.CREATED);
    }

    public static ResponseEntity<CommonResponse> orderPlaced(String message, Integer orderId) {
        return new ResponseEntity<>(new CommonResponse(true, message, orderId), HttpStatus.CREATED);
    }

    public static ResponseEntity<CommonResponse> pathNotFound(String message) {
        return new ResponseEntity<>(new CommonResponse(true, message, null), HttpStatus.INTERNAL_SERVER_ERROR);
    }

//    public static ResponseEntity<CommonResponse> invalidField(String message, Object data) {
//        return new ResponseEntity<>(new CommonResponse(false, message, data), HttpStatus.BAD_REQUEST);
//    }




    public static void throwFound(Class<?> entityClass) {
        String tableName = extractTableName(entityClass);
        throw new DataIntegrityViolationException(tableName + " already exists");
    }

    public static Boolean throwNotFound(Class<?> entityClass) {
        String tableName = extractTableName(entityClass);
        throw new NoSuchElementException(tableName + " not found");
    }

//Type <Wildcard> Varable name
    private static String extractTableName(Class<?> clazz) {
//देखता है कि @Table annotation है या नहीं
        if (clazz.isAnnotationPresent(Table.class)) {
//annotation से डेटा निकालता है
            Table table = clazz.getAnnotation(Table.class);
            return table.name();
        }
//अगर annotation ना हो, तो क्लास का नाम return करता है
        return clazz.getSimpleName();
    }
}
