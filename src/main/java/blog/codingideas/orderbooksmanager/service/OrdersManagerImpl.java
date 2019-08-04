package blog.codingideas.orderbooksmanager.service;

import blog.codingideas.orderbooksmanager.model.Order;
import blog.codingideas.orderbooksmanager.model.OrderType;
import blog.codingideas.orderbooksmanager.model.OrderWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class OrdersManagerImpl implements OrdersManager {

    @Autowired
    OrderBooksManager orderBooksManager;

    private static AtomicInteger orderCounter = new AtomicInteger();


    @Override
    public Order createAndSaveOrder(float quantity, String instrumentId, BigDecimal price) {
        return orderBooksManager.addOrderToOrderBook(generateOrderWrapper(quantity, instrumentId, price));
    }

    public static OrderWrapper generateOrderWrapper(float quantity, String instrumentId, BigDecimal price) {
        int orderNumber = orderCounter.incrementAndGet();
        Order order = new Order(quantity, Instant.now(), instrumentId, price, price != null ? OrderType.LIMIT : OrderType.MARKET);  //Todo: this null must be checked
        return new OrderWrapper(order, orderNumber);
    }
}
