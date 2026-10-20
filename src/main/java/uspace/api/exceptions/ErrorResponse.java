package uspace.api.exceptions;

public class ErrorResponse {

    public final String error;
    public final String description;

    public ErrorResponse(String error, String description) {
        this.error = error;
        this.description = description;
    }
}
