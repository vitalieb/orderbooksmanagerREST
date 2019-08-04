package blog.codingideas.orderbooksmanager.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN, reason = "OrderBook is executed and accepts no new executions")
public class OrderBookExecutedException extends RuntimeException {
}
