package org.ta.store.aspect;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.ta.store.dto.ResponseDto;

@RestControllerAdvice
public class ControllerExceptionHandler {

    private static Logger log = LoggerFactory.getLogger(ControllerExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ResponseDto serviceException(Exception ex, WebRequest request)
    {
        log.error("Exception#serviceException#"+ ExceptionUtils.getStackTrace(ex));
        ResponseDto dto = new ResponseDto(false, HttpStatus.BAD_REQUEST.value(), ex.getMessage());
        return dto;
    }
}
