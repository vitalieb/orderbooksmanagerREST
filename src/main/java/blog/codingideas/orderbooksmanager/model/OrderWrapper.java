package blog.codingideas.orderbooksmanager.model;

import java.math.BigDecimal;

public class OrderWrapper {

    private final Order order;
    private final int orderNumber;
    private boolean valid = true;
    private float executionQuantity;
    private BigDecimal executionPrice;

    public OrderWrapper(Order order, int orderNumber) {
        this.order = order;
        this.orderNumber = orderNumber;
    }

    public Order getOrder() {
        return order;
    }

    public int getOrderNumber(){
        return orderNumber;
    }

    public boolean isValid() {
        return valid;
    }

    public float getExecutionQuantity() {
        return executionQuantity;
    }

    public BigDecimal getExecutionPrice() {
        return executionPrice;
    }

    public void executeIfValid(BigDecimal executionPrice, float executionQuantity) {
        if (valid) {
            this.executionPrice = executionPrice;
            if (this.executionQuantity + executionQuantity < order.getQuantity()) {
                this.executionQuantity += executionQuantity;
            } else {
                this.executionQuantity = order.getQuantity();
            }
        } else {
            executionQuantity = 0;
        }
    }

    public void markAsValidIfOk(BigDecimal executionPrice) {
        if (order.getOrderType().equals(OrderType.LIMIT) && (order.getPrice().compareTo(executionPrice) < 0)) {
            valid = false;
        }
    }
}
