package blog.codingideas.orderbooksmanager.model;

import blog.codingideas.orderbooksmanager.exceptions.OrderBookClosedException;
import blog.codingideas.orderbooksmanager.exceptions.OrderBookExecutedException;
import blog.codingideas.orderbooksmanager.exceptions.OrderBookOpenedException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class OrderBook {

    private final String instrumentId;
    private boolean opened = false;
    private ConcurrentHashMap<UUID, OrderWrapper> orderWrappers = new ConcurrentHashMap<>();
    private List<Execution> executions = new ArrayList<>();
    private boolean executed = false;

    public OrderBook(String instrumentId) {
        this.instrumentId = instrumentId;
    }

    public String getInstrumentId() {
        return instrumentId;
    }

    public boolean isOpened() {
        return opened;
    }

    public void setOpened(boolean opened) {
        if (opened && this.opened) {
            throw new OrderBookOpenedException();
        } else if (!opened && !this.opened) {
            throw new OrderBookClosedException();
        }
        this.opened = opened;
    }

    public ConcurrentHashMap<UUID, OrderWrapper> getOrderWrappers() {
        return orderWrappers;
    }

    public List<Execution> getExecutions() {
        return executions;
    }

    public boolean isExecuted() {
        return executed;
    }

    public void addExecutionToOrderBook(Execution execution) {
        if (!executed && !opened) {
            executions.add(execution);
            orderWrappers.values().forEach(orderWrapper -> {
                orderWrapper.markAsValidIfOk(execution.getPrice());
            });
            Float accumulatedValidOrderQuantity = orderWrappers.values().stream().filter(OrderWrapper::isValid).map(OrderWrapper::getOrder).map(Order::getQuantity).reduce(0f, Float::sum);
            int numberOfValidOrders = (int) orderWrappers.values().stream().filter(OrderWrapper::isValid).count();
            float quantityToBeExecutedPerValidOrder = execution.getQuantity() / numberOfValidOrders;

            orderWrappers.values().forEach(orderWrapper -> {
                orderWrapper.executeIfValid(execution.getPrice(), quantityToBeExecutedPerValidOrder);
            });

            Float existingExecutionsQuantity = executions.stream().map(Execution::getQuantity).reduce(0f, Float::sum);
            if (existingExecutionsQuantity >= accumulatedValidOrderQuantity) {
                executed = true;
            }
        } else {
            throw new OrderBookExecutedException();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OrderBook orderBook = (OrderBook) o;

        return instrumentId.equals(orderBook.instrumentId);
    }

    @Override
    public int hashCode() {
        return instrumentId.hashCode();
    }
}
