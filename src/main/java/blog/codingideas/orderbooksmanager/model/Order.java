package blog.codingideas.orderbooksmanager.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public final class Order {

    private final UUID orderId;
    private final float quantity;
    private final Instant entryDate;
    private final String instrumentId;
    private final BigDecimal price;
    private final OrderType orderType;

    public Order(float quantity, Instant entryDate, String instrumentId, BigDecimal price, OrderType orderType) {
        this.orderId = UUID.randomUUID();
        this.quantity = quantity;
        this.entryDate = entryDate;
        this.instrumentId = instrumentId;
        this.price = price;
        this.orderType = orderType;
    }

    public UUID getOrderId() {
        return orderId;
    }

    public float getQuantity() {
        return quantity;
    }

    public Instant getEntryDate() {
        return entryDate;
    }

    public String getInstrumentId() {
        return instrumentId;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public OrderType getOrderType() {
        return orderType;
    }

}
