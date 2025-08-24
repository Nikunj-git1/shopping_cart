package com.example.shopping_cart.advice;

import com.example.shopping_cart.comman_response_dto.CommonResponse;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.*;
import java.util.stream.Collectors;

@RestControllerAdvice

public class MyGlobalExceptionHandler extends RuntimeException {

    Logger logger = LoggerFactory.getLogger(MyGlobalExceptionHandler.class);

    //1 Default all in one
    @ExceptionHandler(Exception.class)
    public ResponseEntity<CommonResponse> handleMyCustomException(Exception ex) {
        logger.error("handleException : ", ex);
//        logger.error("handleException getCause : ", ex.getCause().getMessage());

// Ask to sir what is send msg. because this exc. use multi error
        return new ResponseEntity<>(new
                    CommonResponse(false, "ex.getMessage()", null), HttpStatus.INTERNAL_SERVER_ERROR);
    }


    //2 Variation of number of field or mismatch field name or mismatch of data type between code with JSON
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<CommonResponse> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {

        logger.error("handleHttpMessageNotReadableException : ", ex);

        String message = "Invalid request body";

        // यदि यह UnrecognizedPropertyException है, तो फील्ड का नाम निकालें
        Throwable cause = ex.getCause();
        if (cause instanceof UnrecognizedPropertyException unrecognizedEx) {
            String fieldName = unrecognizedEx.getPropertyName();
            message = "Invalid field: '" + fieldName + "'";
        } else if (cause instanceof MismatchedInputException mismatchEx) {
            List<JsonMappingException.Reference> path = mismatchEx.getPath();
            if (!path.isEmpty()) {
                message = "Invalid '" + path.get(0).getFieldName() + "' data type";
            }

        }
        CommonResponse commonResponse = new CommonResponse(false, message, null);

        return new ResponseEntity<>(commonResponse, HttpStatus.BAD_REQUEST);
    }


    //3 Validation field issue(null or blank)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CommonResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {

        logger.error("handleMethodArgumentNotValidException : ", ex);

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(err -> errors.put(err.getField(), err.getDefaultMessage()));

        CommonResponse commonResponse = new CommonResponse(false, "Validation failed", errors);

        return new ResponseEntity<>(commonResponse, HttpStatus.BAD_REQUEST);

    }

    //4 Duplicate Data (already exist)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<CommonResponse> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {

        logger.error("handleDataIntegrityViolationException : ", ex);

        CommonResponse commonResponse = new CommonResponse(false, ex.getMessage(), null);

        return new ResponseEntity<>(commonResponse, HttpStatus.CONFLICT);
    }

    //5 @RequestParam validation rule break
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<CommonResponse> handleConstraintViolationException(ConstraintViolationException ex) {

        logger.error("handleConstraintViolationException : ", ex);

        String message = ex.getConstraintViolations()
                .stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining("; "));

        CommonResponse commonResponse = new CommonResponse(false, message, null);


        return new ResponseEntity<>(commonResponse, HttpStatus.BAD_REQUEST);
    }

    //6 Integer value null or get to - optionalCustEntity.isEmpty()
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<CommonResponse> handleNoSuchElementException(NoSuchElementException ex) {

        logger.error("handleNoSuchElementException : ", ex);

        CommonResponse commonResponse = new CommonResponse(false, ex.getMessage(), null);

        return new ResponseEntity<>(commonResponse, HttpStatus.NOT_FOUND);
    }

    //7 @PathVariable > data not send (half(cut last) URL send)
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<CommonResponse> handleNoResourceFoundException(NoResourceFoundException ex) {

        logger.error("handleNoResourceFoundException : ", ex);

        CommonResponse commonResponse = new CommonResponse(false, ex.getMessage(), null);

        return new ResponseEntity<>(commonResponse, HttpStatus.NOT_FOUND);
    }

    //8 @PathVariable > data not send (but URL complete)
    @ExceptionHandler(MissingPathVariableException.class)
    public ResponseEntity<CommonResponse> handleMissingPathVariableException(MissingPathVariableException ex) {

        logger.error("handleMissingPathVariableException : ", ex);

        String missingParam = ex.getVariableName();
        String message = "Missing field : " + missingParam;

        CommonResponse commonResponse = new CommonResponse(false, message, null);

        return new ResponseEntity<>(commonResponse, HttpStatus.BAD_REQUEST);
    }

    //8 Cross data type send(exp. id : 20a6)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<CommonResponse> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {

        logger.error("handleMethodArgumentTypeMismatchException : ", ex);

        String missingParam = ex.getName();
        String message = " Invalid data type of data was passed to this field : " + missingParam;

        CommonResponse commonResponse = new CommonResponse(false, message, null);

        return new ResponseEntity<>(commonResponse, HttpStatus.BAD_REQUEST);
    }

    //9 Excel error handle
    @ExceptionHandler(ExcelValidationException.class)
    public ResponseEntity<Map<String, Object>> handleExcelValidation(ExcelValidationException ex) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("message", "Excel import errors:");
        response.put("errors", ex.getErrors());
        response.put("status", false);
        return ResponseEntity.badRequest().body(response);
    }
}