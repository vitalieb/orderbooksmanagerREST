package blog.codingideas.orderbooksmanager.controllers.orders;

import blog.codingideas.orderbooksmanager.model.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

public interface OrdersControllerInterface {

    ResponseEntity<Order> createAndSaveOrder(Order order);

    ResponseEntity<Object> isOrderValid(String orderId);

    ResponseEntity<Object> executionQuantityPerOrder(@PathVariable String orderId);

    ResponseEntity<Object> getOrderPrice(@PathVariable String orderId);

    ResponseEntity<Object> getExecutionPrice(@PathVariable String orderId);
}
