package com.adelok.marketplace_auth.exceptions;

import com.adelok.marketplace_auth.dto.error.ErrorCode;
import com.adelok.marketplace_auth.dto.error.ErrorResponse;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


import java.time.LocalDateTime;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handle(IllegalArgumentException exception, HttpServletRequest request) {
        log.warn("STEAMID CANT BE NULL");
        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                ErrorCode.BAD_REQUEST,
                HttpStatus.BAD_REQUEST,
                "PLEASE, ENTER STEAM ID",
                request.getRequestURI(),
                null);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorResponse> handle(UsernameNotFoundException exception, HttpServletRequest request) {
        log.warn("THERES NO USER WITH THIS USERNAME");
        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                ErrorCode.UNAUTHORIZED,
                HttpStatus.UNAUTHORIZED,
                "USER NOT FOUND WITH THIS USERNAME",
                request.getRequestURI(),
                null);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ErrorResponse> handle(JwtException exception, HttpServletRequest request) {
        log.warn("INVALID TOKEN");
        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                ErrorCode.UNAUTHORIZED,
                HttpStatus.UNAUTHORIZED,
                "INVALID TOKEN, MAYBE YOUR TOKEN IS EXPIRED",
                request.getRequestURI(),
                null);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handle(MethodArgumentNotValidException exception, HttpServletRequest request) {
        log.warn("STEAM ID IS NOT VALID");
        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                ErrorCode.BAD_REQUEST,
                HttpStatus.BAD_REQUEST,
                "ARGUMENT NOT VALID, TOKEN COULD NOT BE EMPTY",
                request.getRequestURI(),
                null);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<ErrorResponse> handle(InvalidTokenException exception, HttpServletRequest request) {
        log.warn("INVALID REFRESH TOKEN {} {}", request.getRequestURI(), exception.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                ErrorCode.UNAUTHORIZED,
                HttpStatus.UNAUTHORIZED,
                "INVALID REFRESH TOKEN",
                request.getRequestURI(),
                null);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handle(Exception exception, HttpServletRequest request) {
        log.error("UNEXPECTED ERROR", exception);
        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                ErrorCode.INTERNAL_ERROR,
                HttpStatus.INTERNAL_SERVER_ERROR,
                "SOMETHING WENT WRONG",
                request.getRequestURI(),
                null);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

}
