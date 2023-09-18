package com.github.register.infrastructure.server;


import com.github.register.domain.payload.response.CodeMessage;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.validation.FieldError;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author sniper
 * @date 18 Sep 2023
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CodeMessage> handleValidationErrors(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage).collect(Collectors.toList());
        return new ResponseEntity<>(getErrors(errors), new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }


    private CodeMessage getErrors(List<String> errors) {
        CodeMessage codeMessage = new CodeMessage(-1, "bad request");
        codeMessage.setData(errors);
        return codeMessage;
    }

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<CodeMessage> handleGeneralExceptions(Exception ex) {
        List<String> errors = Collections.singletonList(ex.getMessage());
        return new ResponseEntity<>(getErrors(errors), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({RuntimeException.class})
    public ResponseEntity<CodeMessage> handleRuntimeException(RuntimeException exception) {
        return CommonResponse.failure(exception.getMessage());
    }

}
