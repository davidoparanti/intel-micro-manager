package com.example.intelmicromanager.exception;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.example.intelmicromanager.exception.domain.UserNotFoundExecution;
import com.example.intelmicromanager.model.HttpResponse;
import com.example.intelmicromanager.exception.domain.EmailExitException;
import com.example.intelmicromanager.exception.domain.EmailNotFoundException;
import com.example.intelmicromanager.exception.domain.UsernameExitException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.expression.AccessException;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.persistence.NoResultException;
import javax.security.auth.login.AccountLockedException;
import java.io.IOException;
import java.util.Objects;

@RestControllerAdvice
public class ExceptionHandling implements ErrorController {
    private final static Logger LOGGER = LoggerFactory.getLogger(ExceptionHandling.class);

    private final static String ACCOUNT_LOCKED = "Your account has been locked. Please contact administration";
    private static final String METHOD_IS_NOT_ALLOWED = "This request method is not allowed on this endpoint. Please send send a '%s' request";
    private static final String INTERNAL_SERVER_ERROR_MSG = "An error occurred while processing the request";
    private static final String INCORRECT_CREDENTIALS = "Username / password incorrect. Please try again";
    private static final String ACCOUNT_DISABLED = "Your account has been locked. If this is an error, please contact administration";
    private static final String ERROR_PROCESSING_FILE = "Error occurred while processing file";
    private static final String NOT_ENOUGH_PERMISSION = "You do not have enough permission";
    private static final String NO_URL_MAPPING = "There is no mapping for this url";
    private static final String ERROR_PATH = "/error";


    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<HttpResponse> accountDisabledExecution() {
        return createHttpResponse(HttpStatus.FORBIDDEN, ACCOUNT_DISABLED);
    }

    @ExceptionHandler(AccountLockedException.class)
    public ResponseEntity<HttpResponse> accountLockedExecution() {
        return createHttpResponse(HttpStatus.FORBIDDEN, ACCOUNT_LOCKED);
    }

    @ExceptionHandler(AccessException.class)
    public ResponseEntity<HttpResponse> notEnoughPermissionException() {
        return createHttpResponse(HttpStatus.FORBIDDEN, NOT_ENOUGH_PERMISSION);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<HttpResponse> badCredentialException() {
        return createHttpResponse(HttpStatus.BAD_REQUEST, INCORRECT_CREDENTIALS);
    }

    @ExceptionHandler(LockedException.class)
    public ResponseEntity<HttpResponse> lockedException() {
        return createHttpResponse(HttpStatus.UNAUTHORIZED, ACCOUNT_DISABLED);
    }

    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<HttpResponse> tokenExpiredException(TokenExpiredException exception) {
        return createHttpResponse(HttpStatus.UNAUTHORIZED, exception.getMessage().toUpperCase());
    }

    @ExceptionHandler(EmailExitException.class)
    public ResponseEntity<HttpResponse> emailExitException(EmailExitException exception) {
        return createHttpResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(UsernameExitException.class)
    public ResponseEntity<HttpResponse> usernameExitException(UsernameExitException exception) {
        return createHttpResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(UserNotFoundExecution.class)
    public ResponseEntity<HttpResponse> usernameExitException(UserNotFoundExecution exception) {
        return createHttpResponse(HttpStatus.NOT_FOUND, exception.getMessage());
    }

    @ExceptionHandler(EmailNotFoundException.class)
    public ResponseEntity<HttpResponse> emailNotFoundException(EmailNotFoundException exception) {
        return createHttpResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<HttpResponse> emailNotFoundException(UsernameNotFoundException exception) {
        return createHttpResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<HttpResponse> methodNotSupportedExaction(HttpRequestMethodNotSupportedException exception) {
        HttpMethod supportedMethod = Objects.requireNonNull(exception.getSupportedHttpMethods()).iterator().next();
        return createHttpResponse(HttpStatus.METHOD_NOT_ALLOWED, String.format(METHOD_IS_NOT_ALLOWED, supportedMethod));
    }

//    @ExceptionHandler(NoHandlerFoundException.class)
//    public ResponseEntity<HttpResponse> methodNotSupportedExaction(NoHandlerFoundException exception) {
//        return createHttpResponse(HttpStatus.BAD_REQUEST, "This page was not found");
//    }

    @ExceptionHandler(NoResultException.class)
    public ResponseEntity<HttpResponse> notFoundException(NoResultException exception) {
        return createHttpResponse(HttpStatus.NOT_FOUND, exception.getMessage());
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<HttpResponse> iOException(IOException exception) {
        LOGGER.error(exception.getMessage());
        return createHttpResponse(HttpStatus.INTERNAL_SERVER_ERROR, ERROR_PROCESSING_FILE);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<HttpResponse> internalServerErrorException(Exception exception) {
        LOGGER.error(exception.getMessage());
        return createHttpResponse(HttpStatus.INTERNAL_SERVER_ERROR, INTERNAL_SERVER_ERROR_MSG);
    }

    private ResponseEntity<HttpResponse> createHttpResponse(HttpStatus httpStatus, String message) {
        HttpResponse httpResponse = new HttpResponse(httpStatus.value(),
                httpStatus, httpStatus.getReasonPhrase().toUpperCase(), message.toUpperCase());
        return new ResponseEntity<>(httpResponse, httpStatus);
    }

    @RequestMapping(ERROR_PATH)
    public ResponseEntity<HttpResponse> notFound404() {
        return createHttpResponse(HttpStatus.NOT_FOUND, NO_URL_MAPPING);
    }

}
