package blog.codingideas.orderbooksmanager.store;

import blog.codingideas.orderbooksmanager.exceptions.OrderBookAlreadyExistsException;
import blog.codingideas.orderbooksmanager.exceptions.OrderBookNotFoundException;
import blog.codingideas.orderbooksmanager.model.OrderBook;
import blog.codingideas.orderbooksmanager.model.OrderWrapper;
import org.springframework.stereotype.Repository;

import java.util.concurrent.ConcurrentHashMap;

@Repository
public class OrderBookStoreImpl implements OrderBookStore {

    private final ConcurrentHashMap<String, OrderBook> orderBooks = new ConcurrentHashMap<>();

    @Override
    public OrderBook createOrderBook(OrderBook orderBook) {
        OrderBook result = orderBooks.putIfAbsent(orderBook.getInstrumentId(), orderBook);
        if (result != null) {
            throw new OrderBookAlreadyExistsException();
        } else {
            return orderBook;
        }
    }

    @Override
    public boolean isOrderBookAvailableForOrders(String instrumentId) {
        OrderBook orderBook = orderBooks.get(instrumentId);
        if (orderBook == null) {
            throw new OrderBookNotFoundException();
        }
        return orderBook.isOpened();
    }

    @Override
    public void addOrderToOrderBook(OrderWrapper orderWrapper) {
        String instrumentId = orderWrapper.getOrder().getInstrumentId();
        if (isOrderBookAvailableForOrders(instrumentId)) {
            OrderBook orderBook = orderBooks.get(instrumentId);
            orderBook.getOrderWrappers().put(orderWrapper.getOrder().getOrderId(), orderWrapper);
        }
    }

    @Override
    public ConcurrentHashMap<String, OrderBook> getOrderBooks() {
        return orderBooks;
    }
}
