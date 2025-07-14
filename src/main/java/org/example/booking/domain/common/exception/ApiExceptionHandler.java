package org.example.booking.domain.common.exception;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.booking.app.dto.response.ApiError;
import org.example.booking.app.dto.response.ResponseDTO;
import org.example.booking.app.service.ResponseFactory;
import org.example.booking.domain.common.constant.ResponseCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

import static org.example.booking.app.dto.response.ApiError.createFieldApiError;

@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class ApiExceptionHandler {

    private final ResponseFactory responseFactory;

    @ExceptionHandler(BusinessException.class)
    ResponseEntity<ResponseDTO> handleBusinessException(BusinessException e) {
        ResponseCode code = e.getResponseCode();
        if (code != null) {
            return ResponseEntity
                    .status(code.getCode())
                    .body(responseFactory.response(code));
        }

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(responseFactory.response(ResponseCode.INTERNAL_SERVER_ERROR));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({MethodArgumentNotValidException.class})
    ResponseDTO handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        List<ApiError> apiErrors = e.getFieldErrors().stream()
                .map(err -> createFieldApiError(err.getField(), err.getDefaultMessage(), err.getRejectedValue()))
                .toList();

        return responseFactory.invalidParams(apiErrors);
    }

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseDTO handleThrowable(Throwable e) {
        ApiError error = ApiError.createFieldApiError(
                "internal",
                e.getMessage(),
                e.getClass().getSimpleName()
        );

        return responseFactory.response(ResponseCode.INTERNAL_SERVER_ERROR, List.of(error));
    }
}
