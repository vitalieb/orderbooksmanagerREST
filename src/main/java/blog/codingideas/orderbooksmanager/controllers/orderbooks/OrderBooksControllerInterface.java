package blog.codingideas.orderbooksmanager.controllers.orderbooks;

import blog.codingideas.orderbooksmanager.model.Order;
import blog.codingideas.orderbooksmanager.model.OrderBook;
import org.springframework.http.ResponseEntity;

public interface OrderBooksControllerInterface {
    ResponseEntity<OrderBook> createOrderBook(String instrumentId);

    ResponseEntity<Object> openOrderBook(String instrumentId);

    ResponseEntity<Object> closeOrderBook(String instrumentId);

    ResponseEntity<Object> amountOfOrdersInOrderBook(String instrumentId);

    ResponseEntity<Object> demandPerOrderBook(String instrumentId);

    ResponseEntity<Object> limitBreakDownForOrderBook(String instrumentId);

    ResponseEntity<Object> amountOfValidOrdersFromOrderBook(String instrumentId);

    ResponseEntity<Object> amountOfInvalidOrdersFromOrderBook(String instrumentId);

    ResponseEntity<Object> validDemandPerOrderBook(String instrumentId);

    ResponseEntity<Object> invalidDemandPerOrderBook(String instrumentId);

    ResponseEntity<Object> accumulatedExecutionQuantityPerOrderBook(String instrumentId);

    ResponseEntity<Object> executionPricePerOrderBook(String instrumentId);

    ResponseEntity<Order> orderWithBiggestDemandFromOrderBook(String instrumentId);

    ResponseEntity<Order> orderWithSmallestDemandFromOrderBook(String instrumentId);

    ResponseEntity<Order> earliestOrderFromOrderBook(String instrumentId);

    ResponseEntity<Order> latestOrderFromOrderBook(String instrumentId);
}
