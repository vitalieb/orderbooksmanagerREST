package blog.codingideas.orderbooksmanager.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN, reason = "OrderBook is already opened")
public class OrderBookOpenedException extends RuntimeException {
}
