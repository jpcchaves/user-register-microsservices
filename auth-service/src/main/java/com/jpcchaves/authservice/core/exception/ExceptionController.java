package com.jpcchaves.authservice.core.exception;

import com.jpcchaves.authservice.core.exception.dto.ExceptionResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class ExceptionController {

    private static final Logger log = LoggerFactory.getLogger(ExceptionController.class);

    @ExceptionHandler(PasswordsMissmatchException.class)
    public final ResponseEntity<ExceptionResponse> handlePasswordsMissmatchException(
            PasswordsMissmatchException ex, WebRequest request) {
        log.error(ex.getMessage());

        ExceptionResponse exceptionResponse =
                ExceptionResponse.builder()
                        .withMessage(ex.getMessage())
                        .withDetails(request.getDescription(false))
                        .withStatus(HttpStatus.BAD_REQUEST.value())
                        .build();

        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }
}
