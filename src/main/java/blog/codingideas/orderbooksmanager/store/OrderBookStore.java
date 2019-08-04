package blog.codingideas.orderbooksmanager.store;

import blog.codingideas.orderbooksmanager.model.OrderBook;
import blog.codingideas.orderbooksmanager.model.OrderWrapper;

import java.util.concurrent.ConcurrentHashMap;

public interface OrderBookStore {

    OrderBook createOrderBook(OrderBook orderBook);

    boolean isOrderBookAvailableForOrders(String instrumentId);

    void addOrderToOrderBook(OrderWrapper orderWrapper);

    ConcurrentHashMap<String, OrderBook> getOrderBooks();
}
