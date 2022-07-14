package musinsa.onlineshoppingmall.handler;

import musinsa.onlineshoppingmall.dto.ResponseMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@ResponseBody
public class DataProcessExceptionHandler {

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ResponseMessage> handleIllegalStateException(IllegalStateException exception) {
        ResponseMessage message = ResponseMessage.create(HttpStatus.CONFLICT, exception.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(message);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseMessage> handleValidationException(MethodArgumentNotValidException exception) {
        ResponseMessage message = ResponseMessage.create(HttpStatus.BAD_REQUEST, exception.getBindingResult().getFieldError().getDefaultMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ResponseMessage> handleHttpMessageNotReadableException() {
        ResponseMessage message = ResponseMessage.create(HttpStatus.BAD_REQUEST, "요청 바디 상 데이터 항목이 잘못된 형식으로 입력되었습니다.");
        return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
    }
}
