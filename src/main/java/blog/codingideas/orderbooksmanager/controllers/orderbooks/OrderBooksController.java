package blog.codingideas.orderbooksmanager.controllers.orderbooks;

import blog.codingideas.orderbooksmanager.model.Order;
import blog.codingideas.orderbooksmanager.model.OrderBook;
import blog.codingideas.orderbooksmanager.service.OrderBooksManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping("/orderbook")
public class OrderBooksController implements OrderBooksControllerInterface {

    @Autowired
    OrderBooksManager orderBooksManager;

    @Override
    @PostMapping("/{instrumentId}")
    public ResponseEntity<OrderBook> createOrderBook(@PathVariable String instrumentId) {
        OrderBook createdOrderBook = orderBooksManager.createOrderBook(instrumentId);
        return new ResponseEntity<>(createdOrderBook, HttpStatus.CREATED);
    }

    @Override
    @PutMapping("/{instrumentId}/open")
    public ResponseEntity<Object> openOrderBook(@PathVariable String instrumentId) {
        orderBooksManager.openOrderBook(instrumentId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    @PutMapping("/{instrumentId}/close")
    public ResponseEntity<Object> closeOrderBook(@PathVariable String instrumentId) {
        orderBooksManager.closeOrderBook(instrumentId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    @GetMapping("/{instrumentId}/orders-number")
    public ResponseEntity<Object> amountOfOrdersInOrderBook(@PathVariable String instrumentId) {
        int amountOfOrdersInOrderBook = orderBooksManager.amountOfOrdersInOrderBook(instrumentId);
        return new ResponseEntity<>(Collections.singletonMap("amountOfOrdersInOrderBook", amountOfOrdersInOrderBook), HttpStatus.OK);
    }

    @Override
    @GetMapping("/{instrumentId}/demand")
    public ResponseEntity<Object> demandPerOrderBook(@PathVariable String instrumentId) {
        float demandPerOrderBook = orderBooksManager.demandPerOrderBook(instrumentId);
        return new ResponseEntity<>(Collections.singletonMap("demandPerOrderBook", demandPerOrderBook), HttpStatus.OK);
    }

    @Override
    @GetMapping("/{instrumentId}/limit-break-down")
    public ResponseEntity<Object> limitBreakDownForOrderBook(@PathVariable String instrumentId) {
        return new ResponseEntity<>(orderBooksManager.limitBreakDownForOrderBook(instrumentId), HttpStatus.OK);
    }

    @Override
    @GetMapping("/{instrumentId}/valid-orders")
    public ResponseEntity<Object> amountOfValidOrdersFromOrderBook(@PathVariable String instrumentId) {
        int amountOfValidOrdersFromOrderBook = orderBooksManager.amountOfValidOrdersFromOrderBook(instrumentId);
        return new ResponseEntity<>(Collections.singletonMap("validOrdersCount", amountOfValidOrdersFromOrderBook), HttpStatus.OK);
    }

    @Override
    @GetMapping("/{instrumentId}/invalid-orders")
    public ResponseEntity<Object> amountOfInvalidOrdersFromOrderBook(@PathVariable String instrumentId) {
        int amountOfInvalidOrdersFromOrderBook = orderBooksManager.amountOfInvalidOrdersFromOrderBook(instrumentId);
        return new ResponseEntity<>(Collections.singletonMap("invalidOrdersCount", amountOfInvalidOrdersFromOrderBook), HttpStatus.OK);
    }

    @Override
    @GetMapping("/{instrumentId}/valid-demand")
    public ResponseEntity<Object> validDemandPerOrderBook(@PathVariable String instrumentId) {
        Float validDemandPerOrderBook = orderBooksManager.validDemandPerOrderBook(instrumentId);
        return new ResponseEntity<>(Collections.singletonMap("validDemand", validDemandPerOrderBook), HttpStatus.OK);
    }

    @Override
    @GetMapping("/{instrumentId}/invalid-demand")
    public ResponseEntity<Object> invalidDemandPerOrderBook(@PathVariable String instrumentId) {
        Float invalidDemandPerOrderBook = orderBooksManager.invalidDemandPerOrderBook(instrumentId);
        return new ResponseEntity<>(Collections.singletonMap("invalidDemand", invalidDemandPerOrderBook), HttpStatus.OK);
    }

    @Override
    @GetMapping("/{instrumentId}/execution-quantity")
    public ResponseEntity<Object> accumulatedExecutionQuantityPerOrderBook(@PathVariable String instrumentId) {
        Float accumulatedExecutionQuantityPerOrderBook = orderBooksManager.accumulatedExecutionQuantityPerOrderBook(instrumentId);
        return new ResponseEntity<>(Collections.singletonMap("executionQuantity", accumulatedExecutionQuantityPerOrderBook), HttpStatus.OK);
    }

    @Override
    @GetMapping("/{instrumentId}/execution-price")
    public ResponseEntity<Object> executionPricePerOrderBook(@PathVariable String instrumentId) {
        float executionPricePerOrderBook = orderBooksManager.executionPricePerOrderBook(instrumentId);
        return new ResponseEntity<>(Collections.singletonMap("executionPrice", executionPricePerOrderBook), HttpStatus.OK);
    }

    @Override
    @GetMapping("/{instrumentId}/biggest-order")
    public ResponseEntity<Order> orderWithBiggestDemandFromOrderBook(@PathVariable String instrumentId) {
        return new ResponseEntity<>(orderBooksManager.orderWithBiggestDemandFromOrderBook(instrumentId), HttpStatus.OK);
    }

    @Override
    @GetMapping("/{instrumentId}/smallest-order")
    public ResponseEntity<Order> orderWithSmallestDemandFromOrderBook(@PathVariable String instrumentId) {
        return new ResponseEntity<>(orderBooksManager.orderWithSmallestDemandFromOrderBook(instrumentId), HttpStatus.OK);
    }

    @Override
    @GetMapping("/{instrumentId}/earliest-order")
    public ResponseEntity<Order> earliestOrderFromOrderBook(@PathVariable String instrumentId) {
        return new ResponseEntity<>(orderBooksManager.earliestOrderFromOrderBook(instrumentId), HttpStatus.OK);
    }

    @Override
    @GetMapping("/{instrumentId}/latest-order")
    public ResponseEntity<Order> latestOrderFromOrderBook(@PathVariable String instrumentId) {
        return new ResponseEntity<>(orderBooksManager.latestOrderFromOrderBook(instrumentId), HttpStatus.OK);
    }


}
