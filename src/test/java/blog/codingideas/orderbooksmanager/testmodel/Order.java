package blog.codingideas.orderbooksmanager.testmodel;

import java.math.BigDecimal;

public final class Order {

    private final float quantity;
    private final String instrumentId;
    private final BigDecimal price;

    public Order(float quantity, String instrumentId, BigDecimal price) {
        this.quantity = quantity;
        this.instrumentId = instrumentId;
        this.price = price;
    }

    public Order(float quantity, String instrumentId) {
        this.quantity = quantity;
        this.instrumentId = instrumentId;
        price = null;
    }

    public float getQuantity() {
        return quantity;
    }

    public String getInstrumentId() {
        return instrumentId;
    }

    public BigDecimal getPrice() {
        return price;
    }

}
