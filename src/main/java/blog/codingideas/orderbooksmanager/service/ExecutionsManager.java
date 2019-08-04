package blog.codingideas.orderbooksmanager.service;

import blog.codingideas.orderbooksmanager.model.Execution;

import java.math.BigDecimal;

public interface ExecutionsManager {

    Execution createAndAddExecution(String instrumentId, float quantity, BigDecimal price);
}
