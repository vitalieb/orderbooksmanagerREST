package blog.codingideas.orderbooksmanager.model;

import java.math.BigDecimal;

public class Execution {

    private final String instrumentId;
    private final float quantity;
    private final BigDecimal price;

    public Execution(String instrumentId, float quantity, BigDecimal price) {
        this.instrumentId = instrumentId;
        this.quantity = quantity;
        this.price = price;
    }

    public String getInstrumentId() {
        return instrumentId;
    }

    public float getQuantity() {
        return quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
