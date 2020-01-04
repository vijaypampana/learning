package learning.exceptions


abstract class BaseException extends Exception {

    private String message;

    BaseException(String msg) {
        this.message = msg;
    }

    String getMessage() {
        return message;
    }
}
