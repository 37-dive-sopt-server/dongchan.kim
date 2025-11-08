package org.sopt.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /** 비즈니스 예외 */
    @ExceptionHandler(DomainException.class)
    public ApiError handleDomain(DomainException e, HttpServletRequest req) {
        ErrorCode code = e.getErrorCode();
        log.warn("[DOMAIN] {} - {}", code.name(), e.getMessage());
        return build(code.getStatus(), e.getMessage(), req.getRequestURI(), null);
    }

    /** Validation 예외 */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleValidation(MethodArgumentNotValidException e, HttpServletRequest req) {
        List<ApiError.FieldError> fields = e.getBindingResult().getFieldErrors().stream()
                .map(fe -> ApiError.FieldError.builder()
                        .field(fe.getField())
                        .rejectedValue(fe.getRejectedValue())
                        .reason(fe.getDefaultMessage())
                        .build())
                .toList();
        return build(HttpStatus.BAD_REQUEST, "요청 값 검증에 실패했습니다", req.getRequestURI(), fields);
    }

    /** 잘못된 요청 형식 (파라미터/바디 오류 등) */
    @ExceptionHandler({
            MethodArgumentTypeMismatchException.class,
            HttpMessageNotReadableException.class,
            MissingServletRequestParameterException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleBadRequest(Exception e, HttpServletRequest req) {
        log.warn("[BAD_REQUEST] {}", e.getMessage());
        return build(HttpStatus.BAD_REQUEST, "잘못된 요청입니다", req.getRequestURI(), null);
    }

    /** DB 제약 조건 위반 */
    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleIntegrity(DataIntegrityViolationException e, HttpServletRequest req) {
        Throwable cause = e.getMostSpecificCause();
        if (cause instanceof ConstraintViolationException cve) {
            String constraint = cve.getConstraintName();
            if (constraint != null && constraint.toLowerCase().contains("article") &&
                    constraint.toLowerCase().contains("title")) {
                return build(HttpStatus.CONFLICT, "이미 존재하는 제목입니다", req.getRequestURI(), null);
            }
        }
        return build(HttpStatus.CONFLICT, "데이터 제약 조건 위반", req.getRequestURI(), null);
    }

    /** 500에러 */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError handleUnknown(Exception e, HttpServletRequest req) {
        log.error("[INTERNAL_ERROR]", e);
        return build(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류", req.getRequestURI(), null);
    }

    
    private ApiError build(HttpStatus status, String message, String path, List<ApiError.FieldError> fields) {
        return ApiError.builder()
                .timestamp(LocalDateTime.now())
                .status(status.value())
                .error(status.name())
                .message(message)
                .path(path)
                .errors(fields)
                .build();
    }
}
