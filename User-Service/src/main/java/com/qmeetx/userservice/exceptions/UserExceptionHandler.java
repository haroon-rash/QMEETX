package com.qmeetx.userservice.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.Map;

@Slf4j
@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class UserExceptionHandler {

    @ExceptionHandler(WrongInputFormatException.class)
    public ResponseEntity<?> handleWrongInputFormatException(WrongInputFormatException ex) {

        Map<String,Object> error = Map.of(
                "timestamp", LocalDateTime.now(),
                "status", HttpStatus.BAD_REQUEST.value(),
                "error", "Wrong Input Format",
                "message", "Input Format is Incorrect { "+ ex.getMessage()+" }"

        );

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<?> handleUserNotFoundException(UserNotFoundException ex, HttpServletRequest request) {
        Map<String,Object> errors = Map.of("message", ex.getMessage()

                ,"timestamp", LocalDateTime.now()
                ,"status",HttpStatus.NOT_FOUND.value(),
                "error","NOT FOUND",
                "path",request.getRequestURI()

        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errors);
    }



}
