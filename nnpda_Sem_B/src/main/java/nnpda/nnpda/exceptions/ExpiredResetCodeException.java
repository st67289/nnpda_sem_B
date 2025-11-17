package nnpda.nnpda.exceptions;

public class ExpiredResetCodeException extends RuntimeException {
    public ExpiredResetCodeException(String message) { super(message); }
}