package blog.codingideas.orderbooksmanager.service;

import blog.codingideas.orderbooksmanager.model.Execution;
import blog.codingideas.orderbooksmanager.model.OrderWrapper;
import blog.codingideas.orderbooksmanager.store.OrderBookStore;
import blog.codingideas.orderbooksmanager.store.OrderBookStoreImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


@RunWith(MockitoJUnitRunner.class)
public class OrderBooksManagerImplTest {

    @Spy
    OrderBookStore orderBookStore = new OrderBookStoreImpl();
    @InjectMocks
    @Spy
    OrderBooksManagerImpl orderBooksManager = new OrderBooksManagerImpl();

    @Test
    public void test_addOrderToOrderBookAndCheckIfItIsValid() {

        String instrumentId = "instrument1";
        OrderWrapper orderWrapper = OrdersManagerImpl.generateOrderWrapper(23f, instrumentId, new BigDecimal(40));
        orderBooksManager.createOrderBook(instrumentId);
        orderBooksManager.openOrderBook(instrumentId);
        orderBooksManager.addOrderToOrderBook(orderWrapper);
        assertTrue(orderBooksManager.isOrderValid(orderWrapper.getOrder().getOrderId()));

    }

    @Test
    public void test_addExecutionToOrderBook() {

        String instrumentId = "instrument1";
        OrderWrapper orderWrapper = OrdersManagerImpl.generateOrderWrapper(23f, instrumentId, new BigDecimal(40));
        orderBooksManager.createOrderBook(instrumentId);
        orderBooksManager.openOrderBook(instrumentId);
        orderBooksManager.addOrderToOrderBook(orderWrapper);
        orderBooksManager.closeOrderBook(instrumentId);
        orderBooksManager.addExecutionToOrderBook(new Execution(instrumentId, 14f, new BigDecimal(40)));
        orderBooksManager.addExecutionToOrderBook(new Execution(instrumentId, 42f, new BigDecimal(40)));
        UUID orderId = orderWrapper.getOrder().getOrderId();
        assertTrue(orderBooksManager.isOrderValid(orderId));
        assertEquals(23, orderBooksManager.executionQuantityPerOrder(orderId),1);
    }

    @Test
    public void test_amountOfOrdersInOrderBook_and_demandPerOrderBook() {
        String instrumentId = "instrument1";
        float quantity1 = 23f;
        float quantity2 = 44f;
        float quantity3 = 37f;
        float quantity4 = 38f;
        float totalDemand = quantity1 + quantity2 + quantity3 + quantity4;
        float validDemand = quantity2 + quantity3 + quantity4;
        float invalidDemand = quantity1;
        OrderWrapper orderWrapper = OrdersManagerImpl.generateOrderWrapper(quantity1, instrumentId, new BigDecimal(30));
        OrderWrapper orderWrapper2 = OrdersManagerImpl.generateOrderWrapper(quantity2, instrumentId, new BigDecimal(50));
        OrderWrapper orderWrapper3 = OrdersManagerImpl.generateOrderWrapper(quantity3, instrumentId, new BigDecimal(47));
        OrderWrapper orderWrapper4 = OrdersManagerImpl.generateOrderWrapper(quantity4, instrumentId, null);
        orderBooksManager.createOrderBook(instrumentId);
        orderBooksManager.openOrderBook(instrumentId);
        orderBooksManager.addOrderToOrderBook(orderWrapper);
        orderBooksManager.addOrderToOrderBook(orderWrapper2);
        orderBooksManager.addOrderToOrderBook(orderWrapper3);
        orderBooksManager.addOrderToOrderBook(orderWrapper4);
        orderBooksManager.closeOrderBook(instrumentId);
        orderBooksManager.addExecutionToOrderBook(new Execution(instrumentId, 14, new BigDecimal(40)));
        assertEquals(4, orderBooksManager.amountOfOrdersInOrderBook(instrumentId));
        assertEquals(totalDemand, orderBooksManager.demandPerOrderBook(instrumentId), 1);
        assertEquals(orderWrapper2.getOrder(), orderBooksManager.orderWithBiggestDemandFromOrderBook(instrumentId));
        assertEquals(orderWrapper.getOrder(), orderBooksManager.orderWithSmallestDemandFromOrderBook(instrumentId));
        assertEquals(orderWrapper.getOrder(), orderBooksManager.earliestOrderFromOrderBook(instrumentId));
        assertEquals(orderWrapper4.getOrder(), orderBooksManager.latestOrderFromOrderBook(instrumentId));
        assertEquals(3, orderBooksManager.amountOfValidOrdersFromOrderBook(instrumentId));
        assertEquals(1, orderBooksManager.amountOfInvalidOrdersFromOrderBook(instrumentId));
        assertEquals(validDemand, orderBooksManager.validDemandPerOrderBook(instrumentId), 1);
        assertEquals(invalidDemand, orderBooksManager.invalidDemandPerOrderBook(instrumentId), 1);
        assertEquals(new BigDecimal(50), orderBooksManager.getOrderPrice(orderWrapper2.getOrder().getOrderId()));
        assertEquals(new BigDecimal(40), orderBooksManager.getExecutionPrice(orderWrapper2.getOrder().getOrderId()));
        assertEquals(14, orderBooksManager.accumulatedExecutionQuantityPerOrderBook(instrumentId), 1);
        assertEquals(40, orderBooksManager.executionPricePerOrderBook(instrumentId), 1);
        HashMap<BigDecimal, Double> limitBreakDown = new HashMap<>();
        limitBreakDown.put(new BigDecimal(47), 37d);
        limitBreakDown.put(new BigDecimal(30), 23d);
        limitBreakDown.put(new BigDecimal(50), 44d);
        assertEquals(limitBreakDown, orderBooksManager.limitBreakDownForOrderBook(instrumentId));

    }

}
