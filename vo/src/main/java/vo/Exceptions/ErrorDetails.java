package vo.Exceptions;

import org.springframework.http.HttpStatus;

import java.util.Date;

public class ErrorDetails {
    private Date timestamp;
    private String message;
    private String details;
    private HttpStatus status;

    public ErrorDetails(Date timestamp, String message, String details, HttpStatus status) {
        super();
        this.timestamp = timestamp;
        this.message = message;
        this.details = details;
        this.status = status;
    }
}
