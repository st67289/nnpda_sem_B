// UnprocessableEntityException.java  (HTTP 422)
package nnpda.nnpda.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class UnprocessableEntityException extends RuntimeException {
    public UnprocessableEntityException(String msg) { super(msg); }
}
