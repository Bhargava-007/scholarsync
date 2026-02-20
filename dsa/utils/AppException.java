package utils;

public class AppException extends Exception {
    private final int status;

    public AppException(String message, int status) {
        super(message);
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}
