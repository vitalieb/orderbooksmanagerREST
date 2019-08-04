package blog.codingideas.orderbooksmanager.service;

import blog.codingideas.orderbooksmanager.model.Order;

import java.math.BigDecimal;

public interface OrdersManager {

    Order createAndSaveOrder(float quantity, String instrumentId, BigDecimal price);

}
