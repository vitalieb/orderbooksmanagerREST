package blog.codingideas.orderbooksmanager.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "No executions found")
public class NoExecutionsNotFoundException extends RuntimeException {
}
