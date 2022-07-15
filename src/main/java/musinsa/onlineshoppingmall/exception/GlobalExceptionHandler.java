package musinsa.onlineshoppingmall.exception;

import musinsa.onlineshoppingmall.dto.ResponseMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {

    @ExceptionHandler({MethodArgumentTypeMismatchException.class, HttpRequestMethodNotSupportedException.class})
    public ResponseEntity<ResponseMessage> handleMethodArgumentTypeMismatchException() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(ResponseMessage.create(HttpStatus.NOT_FOUND, "잘못된 요청입니다."));
    }

    @ExceptionHandler({MethodArgumentNotValidException.class, HttpMessageNotReadableException.class})
    public ResponseEntity<ResponseMessage> handleMethodArgumentNotValidException() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ResponseMessage.create(HttpStatus.BAD_REQUEST, "잘못된 형식의 데이터입니다."));
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ResponseMessage> handleIllegalStateException(IllegalStateException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
            .body(ResponseMessage.create(HttpStatus.CONFLICT, exception.getMessage()));
    }

}
