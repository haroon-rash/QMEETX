package com.qmeetx.qmeetxshared.Exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
@Slf4j
@ControllerAdvice
@Order(Ordered.LOWEST_PRECEDENCE)
public class GlobalExceptionHandler {

    //Handle DTO Validations (@NOT BLANK(),@SIZE() etc)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach((fielderror) -> {
            errors.put(fielderror.getField(), fielderror.getDefaultMessage());
        });

        return ResponseEntity.badRequest().body(errors);

    }


    //Handle all Other Exceptions

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleOtherException(Exception ex, HttpServletRequest request) {
        Map<String, Object> errors = Map.of("message", "An unexpected error occurred. Please try again later"

                , "timestamp", LocalDateTime.now()
                , "status", HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "error", "Unauthorized",
                "path", request.getRequestURI()
        );
        log.error("Unhandled exception: ", ex);
        return ResponseEntity.internalServerError().body(errors);
    }

//to catch database-related constraint errors at runtime
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<?> handleDataIntegrityViolationException(DataIntegrityViolationException ex, HttpServletRequest request) {
        Map<String,Object> error = Map.of(
                "timestamp", LocalDateTime.now(),
                "status", HttpStatus.CONFLICT.value(),
                "error", "Conflict",
                "message", "Email or provider already exists",
                "path", request.getRequestURI()
        );
        log.error("Data integrity violation: {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    }
