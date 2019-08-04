package blog.codingideas.orderbooksmanager.controllers.orders;

import blog.codingideas.orderbooksmanager.model.Order;
import blog.codingideas.orderbooksmanager.service.OrderBooksManager;
import blog.codingideas.orderbooksmanager.service.OrdersManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.UUID;

@RestController
@RequestMapping("/order")
public class OrdersController implements OrdersControllerInterface {

    @Autowired
    OrdersManager ordersManager;

    @Autowired
    OrderBooksManager orderBooksManager;

    @Override
    @PostMapping(value = "", consumes = "application/json")
    public ResponseEntity<Order> createAndSaveOrder(@RequestBody Order order) {
        Order createdAndSavedOrder = ordersManager.createAndSaveOrder(order.getQuantity(), order.getInstrumentId(), order.getPrice());
        return new ResponseEntity<>(createdAndSavedOrder, HttpStatus.CREATED);
    }

    @Override
    @GetMapping("/{orderId}/valid")
    public ResponseEntity<Object> isOrderValid(@PathVariable String orderId) {
        boolean orderValid = orderBooksManager.isOrderValid(UUID.fromString(orderId));
        return new ResponseEntity<>(Collections.singletonMap("orderValid", orderValid), HttpStatus.OK);
    }

    @Override
    @GetMapping("/{orderId}/execution-quantity")
    public ResponseEntity<Object> executionQuantityPerOrder(@PathVariable String orderId) {
        float executionQuantityPerOrder = orderBooksManager.executionQuantityPerOrder(UUID.fromString(orderId));
        return new ResponseEntity<>(Collections.singletonMap("executionQuantity", executionQuantityPerOrder), HttpStatus.OK);
    }

    @Override
    @GetMapping("/{orderId}/execution-price")
    public ResponseEntity<Object> getExecutionPrice(@PathVariable String orderId) {
        BigDecimal executionPrice = orderBooksManager.getExecutionPrice(UUID.fromString(orderId));
        return new ResponseEntity<>(Collections.singletonMap("executionPrice", executionPrice), HttpStatus.OK);
    }

    @Override
    @GetMapping("/{orderId}/price")
    public ResponseEntity<Object> getOrderPrice(@PathVariable String orderId) {
        BigDecimal orderPrice = orderBooksManager.getOrderPrice(UUID.fromString(orderId));
        return new ResponseEntity<>(Collections.singletonMap("price", orderPrice), HttpStatus.OK);
    }
}
