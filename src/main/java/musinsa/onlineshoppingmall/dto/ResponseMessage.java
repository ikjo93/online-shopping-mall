package musinsa.onlineshoppingmall.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ResponseMessage {

    private HttpStatus status;
    private String message;

    public static ResponseMessage create(HttpStatus status, String message) {
        return new ResponseMessage(status, message);
    }

}
