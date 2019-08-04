package blog.codingideas.orderbooksmanager.service;

import blog.codingideas.orderbooksmanager.exceptions.NoExecutionsNotFoundException;
import blog.codingideas.orderbooksmanager.exceptions.OrderBookClosedException;
import blog.codingideas.orderbooksmanager.exceptions.OrderBookNotFoundException;
import blog.codingideas.orderbooksmanager.exceptions.OrderNotFoundException;
import blog.codingideas.orderbooksmanager.model.*;
import blog.codingideas.orderbooksmanager.store.OrderBookStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.summingDouble;

@Service
public class OrderBooksManagerImpl implements OrderBooksManager {

    @Autowired
    OrderBookStore orderBookStore;

    @Override
    public OrderBook createOrderBook(String instrumentId) {
        OrderBook orderBook = new OrderBook(instrumentId);
        return orderBookStore.createOrderBook(orderBook);
    }

    @Override
    public Order addOrderToOrderBook(OrderWrapper orderWrapper) {
        Order order = orderWrapper.getOrder();
        if (orderBookStore.isOrderBookAvailableForOrders(order.getInstrumentId())) {
            orderBookStore.addOrderToOrderBook(orderWrapper);
            return orderBookStore.getOrderBooks().get(order.getInstrumentId()).getOrderWrappers().get(order.getOrderId()).getOrder();
        } else {
            throw new OrderBookClosedException();
        }
    }

    @Override
    public Execution addExecutionToOrderBook(Execution execution) {
        getOrderBook(execution.getInstrumentId()).addExecutionToOrderBook(execution);
        return execution;
    }

    @Override
    public void openOrderBook(String instrumentId) {
        getOrderBook(instrumentId).setOpened(true);
    }

    @Override
    public void closeOrderBook(String instrumentId) {
        getOrderBook(instrumentId).setOpened(false);
    }

    @Override
    public int amountOfOrdersInOrderBook(String instrumentId) {
        return getOrderBook(instrumentId).getOrderWrappers().size();
    }

    private OrderBook getOrderBook(String instrumentId) {
        OrderBook orderBook = orderBookStore.getOrderBooks().get(instrumentId);
        if (orderBook != null) {
            return orderBook;
        } else {
            throw new OrderBookNotFoundException();
        }
    }

    @Override
    public float demandPerOrderBook(String instrumentId) {
        return getOrderBook(instrumentId).getOrderWrappers().values().stream().map(OrderWrapper::getOrder).map(Order::getQuantity).reduce(0f, Float::sum);
    }

    @Override
    public Order orderWithBiggestDemandFromOrderBook(String instrumentId) {
        return getOrderBook(instrumentId).getOrderWrappers().entrySet().stream()
                .max(Comparator.comparingDouble(e -> e.getValue().getOrder().getQuantity()))
                .orElseThrow(OrderNotFoundException::new).getValue().getOrder();
    }

    @Override
    public Order orderWithSmallestDemandFromOrderBook(String instrumentId) {
        return getOrderBook(instrumentId).getOrderWrappers().entrySet().stream()
                .min(Comparator.comparingDouble(e -> e.getValue().getOrder().getQuantity()))
                .orElseThrow(OrderNotFoundException::new).getValue().getOrder();
    }

    @Override
    public Order earliestOrderFromOrderBook(String instrumentId) {
        return getOrderBook(instrumentId).getOrderWrappers().entrySet().stream()
                .min(Comparator.comparingInt(e -> e.getValue().getOrderNumber()))
                .orElseThrow(OrderNotFoundException::new).getValue().getOrder();
    }

    @Override
    public Order latestOrderFromOrderBook(String instrumentId) {
        return getOrderBook(instrumentId).getOrderWrappers().entrySet().stream()
                .max(Comparator.comparingInt(e -> e.getValue().getOrderNumber()))
                .orElseThrow(OrderNotFoundException::new).getValue().getOrder();
    }

    @Override
    public Map<BigDecimal, Double> limitBreakDownForOrderBook(String instrumentId) {
        return getOrderBook(instrumentId).getOrderWrappers().values().stream().map(OrderWrapper::getOrder).filter(order -> order.getPrice() != null && order.getOrderType().equals(OrderType.LIMIT)).collect(groupingBy(Order::getPrice, summingDouble(Order::getQuantity)));
    }

    @Override
    public int amountOfValidOrdersFromOrderBook(String instrumentId) {
        return (int) getOrderBook(instrumentId).getOrderWrappers().values().stream().filter(OrderWrapper::isValid).count();
    }

    @Override
    public int amountOfInvalidOrdersFromOrderBook(String instrumentId) {
        return (int) getOrderBook(instrumentId).getOrderWrappers().values().stream().filter(orderWrapper -> !orderWrapper.isValid()).count();
    }

    @Override
    public Float validDemandPerOrderBook(String instrumentId) {
        return getOrderBook(instrumentId).getOrderWrappers().values().stream().filter(OrderWrapper::isValid).map(OrderWrapper::getOrder).map(Order::getQuantity).reduce(0f, Float::sum);
    }

    @Override
    public Float invalidDemandPerOrderBook(String instrumentId) {
        return getOrderBook(instrumentId).getOrderWrappers().values().stream().filter(orderWrapper -> !orderWrapper.isValid()).map(OrderWrapper::getOrder).map(Order::getQuantity).reduce(0f, Float::sum);
    }

    @Override
    public Float accumulatedExecutionQuantityPerOrderBook(String instrumentId) {
        return getOrderBook(instrumentId).getOrderWrappers().values().stream().map(OrderWrapper::getExecutionQuantity).reduce(0f, Float::sum);
    }

    @Override
    public float executionPricePerOrderBook(String instrumentId) {
        return getOrderBook(instrumentId).getOrderWrappers().values().stream().filter(OrderWrapper::isValid).map(OrderWrapper::getExecutionPrice).findFirst().orElseThrow(NoExecutionsNotFoundException::new).floatValue();
    }

    @Override
    public boolean isOrderValid(UUID orderId) {
        OrderWrapper orderWrapper = getOrderWrapperByOrderId(orderId);
        return orderWrapper.isValid();
    }

    @Override
    public float executionQuantityPerOrder(UUID orderId) {
        return getOrderWrapperByOrderId(orderId).getExecutionQuantity();
    }

    @Override
    public BigDecimal getOrderPrice(UUID orderId) {
        return getOrderWrapperByOrderId(orderId).getOrder().getPrice();
    }

    @Override
    public BigDecimal getExecutionPrice(UUID orderId) {
        return getOrderWrapperByOrderId(orderId).getExecutionPrice();
    }

    private OrderWrapper getOrderWrapperByOrderId(UUID orderId) {
        Optional<ConcurrentHashMap<UUID, OrderWrapper>> mapContainingOrderOptional = orderBookStore.getOrderBooks().values().stream().map(OrderBook::getOrderWrappers).filter(orderWrappersMap -> orderWrappersMap.containsKey(orderId)).findFirst();
        ConcurrentHashMap<UUID, OrderWrapper> mapContainingOrder = mapContainingOrderOptional.orElseThrow(OrderNotFoundException::new);
        return mapContainingOrder.get(orderId);
    }
}
