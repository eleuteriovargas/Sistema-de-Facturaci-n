package com.vargas.facturacion.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.env.Profiles;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.core.env.Environment;
import java.time.LocalDateTime;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    private final Environment environment;

    public GlobalExceptionHandler(Environment environment) {
        this.environment = environment;
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public String handleResourceNotFound(ResourceNotFoundException ex, Model model) {
        model.addAttribute("error", ex.getMessage());
        return "error/404";
    }

    @ExceptionHandler(BusinessException.class)
    public String handleBusinessException(BusinessException ex, Model model) {
        model.addAttribute("error", ex.getMessage());
        return "error/business";
    }

    @ExceptionHandler(Exception.class)
    public String handleGeneralException(Exception ex, Model model, HttpServletRequest request) {
        model.addAttribute("error", "Ocurrió un error inesperado");
        model.addAttribute("message", ex.getMessage());
        model.addAttribute("timestamp", LocalDateTime.now());
        model.addAttribute("path", request.getRequestURI());
        model.addAttribute("status", HttpStatus.INTERNAL_SERVER_ERROR.value());


        if (environment.acceptsProfiles(Profiles.of("dev"))) {
            model.addAttribute("exception", ex.getClass().getName());
            model.addAttribute("trace", ExceptionUtils.getStackTrace(ex));
            model.addAttribute("development", true);
        } else {
            model.addAttribute("development", false);
        }


        return "error/general";
    }
}