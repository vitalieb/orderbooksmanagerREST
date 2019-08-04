package blog.codingideas.orderbooksmanager.service;

import blog.codingideas.orderbooksmanager.model.Execution;
import blog.codingideas.orderbooksmanager.model.Order;
import blog.codingideas.orderbooksmanager.model.OrderBook;
import blog.codingideas.orderbooksmanager.model.OrderWrapper;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

public interface OrderBooksManager {

    OrderBook createOrderBook(String instrumentId);
    Order addOrderToOrderBook(OrderWrapper orderWrapper);
    Execution addExecutionToOrderBook(Execution execution);

    void openOrderBook(String instrumentId);
    void closeOrderBook(String instrumentId);
    int amountOfOrdersInOrderBook(String instrumentId);
    float demandPerOrderBook(String instrumentId);
    Order orderWithBiggestDemandFromOrderBook(String instrumentId);
    Order orderWithSmallestDemandFromOrderBook(String instrumentId);
    Order earliestOrderFromOrderBook(String instrumentId);
    Order latestOrderFromOrderBook(String instrumentId);
    Map<BigDecimal, Double> limitBreakDownForOrderBook(String instrumentId);

    int amountOfValidOrdersFromOrderBook(String instrumentId);
    int amountOfInvalidOrdersFromOrderBook(String instrumentId);
    Float validDemandPerOrderBook(String instrumentId);
    Float invalidDemandPerOrderBook(String instrumentId);
    Float accumulatedExecutionQuantityPerOrderBook(String instrumentId);
    float executionPricePerOrderBook(String instrumentId);

    boolean isOrderValid(UUID orderId);
    float executionQuantityPerOrder(UUID orderId);
    BigDecimal getOrderPrice(UUID orderId);
    BigDecimal getExecutionPrice(UUID orderId);

}
