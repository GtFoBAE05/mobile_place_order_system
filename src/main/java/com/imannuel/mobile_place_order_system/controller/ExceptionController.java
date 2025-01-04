package com.imannuel.mobile_place_order_system.controller;

import com.imannuel.mobile_place_order_system.dto.mapper.ResponseMapper;
import jakarta.validation.ConstraintViolationException;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class ExceptionController {
    @ExceptionHandler({ResponseStatusException.class})
    public ResponseEntity<?> handlingResponseStatusException(ResponseStatusException e) {
        return ResponseMapper.toCommonResponse(false,
                HttpStatus.valueOf(e.getStatusCode().value()), e.getReason(), null
        );
    }

    @ExceptionHandler({PropertyReferenceException.class})
    public ResponseEntity<?> handlingPropertyReferenceException(PropertyReferenceException e) {
        return ResponseMapper.toCommonResponse(false,
                HttpStatus.BAD_REQUEST, String.format("%s is invalid field to sort", e.getPropertyName()),
                null
        );
    }

    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<?> handlingConstraintViolationException(ConstraintViolationException e) {
        return ResponseMapper.toCommonResponse(false,
                HttpStatus.BAD_REQUEST, e.getMessage(), null
        );
    }
}
