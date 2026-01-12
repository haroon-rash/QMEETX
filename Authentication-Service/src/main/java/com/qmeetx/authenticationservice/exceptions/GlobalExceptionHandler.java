package com.qmeetx.authenticationservice.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
/* @ Used because there is global exception present
at QMEETX-Shared which execute At last if no EXCEPTION handler found
in this Module
* */
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalExceptionHandler {

    //Handle Email Already Exist Exception

    @ExceptionHandler(EmailAlreadyExistException.class)
 public ResponseEntity<?> handleEmailAlreadyExistException(EmailAlreadyExistException ex,HttpServletRequest request) {
        Map<String,Object> errors = Map.of("message", ex.getMessage(),
                "timestamp", LocalDateTime.now()
                ,"status",HttpStatus.CONFLICT.value(),
                "error","conflict",
                "path",request.getRequestURI()

        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(errors);
    }

//Handle DTO Validations (@NOT BLANK(),@SIZE() etc)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        Map<String,String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach((fielderror) -> {
            errors.put(fielderror.getField(),fielderror.getDefaultMessage());
        });

        return ResponseEntity.badRequest().body(errors);
    }


    @ExceptionHandler(UserNotFoundException.class)
public ResponseEntity<?> handleUserNotFoundException(UserNotFoundException ex,HttpServletRequest request) {
        Map<String,Object> errors = Map.of("message", ex.getMessage()

                ,"timestamp", LocalDateTime.now()
                ,"status",HttpStatus.NOT_FOUND.value(),
                "error","NOT FOUND",
                "path",request.getRequestURI()

        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errors);
    }

    @ExceptionHandler(PasswordNotMatchException.class)
    public ResponseEntity<?> handlePasswordNotMatchException(PasswordNotMatchException ex,HttpServletRequest request) {
        Map<String,Object> errors = Map.of("message", ex.getMessage()

                ,"timestamp", LocalDateTime.now()
                ,"status",HttpStatus.UNAUTHORIZED.value(),
                "error","Unauthorized",
                "path",request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errors);
    }

//Handle OAuth Exception Email processing in OAuth
    @ExceptionHandler(OAuthProcessingException.class)
    public ResponseEntity<?> handleOAuthProcessingException(OAuthProcessingException ex, HttpServletRequest request) {
        Map<String,Object> errors = Map.of("message", ex.getMessage(),
                "timestamp", LocalDateTime.now()
                ,"status",HttpStatus.BAD_REQUEST.value(),
                "error","Bad Request",
                "path",request.getRequestURI()
        );
        return ResponseEntity.badRequest().body(errors);
    }






}
