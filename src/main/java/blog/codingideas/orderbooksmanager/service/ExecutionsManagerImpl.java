package blog.codingideas.orderbooksmanager.service;

import blog.codingideas.orderbooksmanager.model.Execution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class ExecutionsManagerImpl implements ExecutionsManager {

    @Autowired
    OrderBooksManager orderBooksManager;

    @Override
    public Execution createAndAddExecution(String instrumentId, float quantity, BigDecimal price) {

        Execution execution = new Execution(instrumentId, quantity, price);
        return orderBooksManager.addExecutionToOrderBook(execution);


    }


}
