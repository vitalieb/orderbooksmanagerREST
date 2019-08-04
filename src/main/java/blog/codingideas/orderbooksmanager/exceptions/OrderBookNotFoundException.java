package blog.codingideas.orderbooksmanager.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "OrderBook not present in the system")
public class OrderBookNotFoundException extends RuntimeException {
}
