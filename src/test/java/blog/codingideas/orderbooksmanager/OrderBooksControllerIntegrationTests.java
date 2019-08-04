package blog.codingideas.orderbooksmanager;

import blog.codingideas.orderbooksmanager.model.Execution;
import blog.codingideas.orderbooksmanager.testmodel.Order;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class OrderBooksControllerIntegrationTests {

    @Rule
    public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation("target/snippets");

    @Autowired
    private WebApplicationContext context;

    @Autowired
    ObjectMapper objectMapper;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
                .apply(documentationConfiguration(this.restDocumentation))
                .alwaysDo(document("{method-name}",
                        preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint())))
                .build();
    }

    @Test
    public void createOrderBook() throws Exception {
        String instrumentId = "instrument1";
        this.mockMvc.perform(post("/orderbook/" + instrumentId))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.instrumentId").value(instrumentId));
    }

    @Test
    public void openOrderBook() throws Exception {
        String instrumentId = "instrument1";
        this.mockMvc.perform(post("/orderbook/" + instrumentId));
        this.mockMvc.perform(put("/orderbook/" + instrumentId + "/open")).andExpect(status().isOk());
    }

    @Test
    public void openOrderBookSecondTimeReturnsUnauthorized() throws Exception {
        String instrumentId = "instrument1";
        this.mockMvc.perform(post("/orderbook/" + instrumentId));
        this.mockMvc.perform(put("/orderbook/" + instrumentId + "/open")).andExpect(status().isOk());
        this.mockMvc.perform(put("/orderbook/" + instrumentId + "/open")).andExpect(status().isForbidden());
    }

    @Test
    public void closeOrderBook() throws Exception {
        String instrumentId = "instrument1";
        this.mockMvc.perform(post("/orderbook/" + instrumentId));
        this.mockMvc.perform(put("/orderbook/" + instrumentId + "/open"));
        this.mockMvc.perform(put("/orderbook/" + instrumentId + "/close")).andExpect(status().isOk());
    }

    @Test
    public void amountOfOrdersInOrderBook() throws Exception {
        String instrumentId = "instrument1";
        Order order = new Order(15f, instrumentId, new BigDecimal(45));
        String orderAsString = objectMapper.writeValueAsString(order);
        this.mockMvc.perform(post("/orderbook/" + instrumentId));
        this.mockMvc.perform(put("/orderbook/" + instrumentId + "/open"));
        this.mockMvc.perform(post("/order").contentType(MediaType.APPLICATION_JSON).content(orderAsString));
        this.mockMvc.perform(get("/orderbook/" + instrumentId + "/orders-number")).andExpect(status().isOk())
                .andExpect(jsonPath("$.amountOfOrdersInOrderBook").value(1));
    }

    @Test
    public void demandPerOrderBook() throws Exception {
        String instrumentId = "instrument1";
        Order order = new Order(15f, instrumentId, new BigDecimal(45));
        Order order2 = new Order(66f, instrumentId, new BigDecimal(66));
        String orderAsString = objectMapper.writeValueAsString(order);
        String orderAsString2 = objectMapper.writeValueAsString(order2);
        this.mockMvc.perform(post("/orderbook/" + instrumentId));
        this.mockMvc.perform(put("/orderbook/" + instrumentId + "/open"));
        this.mockMvc.perform(post("/order").contentType(MediaType.APPLICATION_JSON).content(orderAsString));
        this.mockMvc.perform(post("/order").contentType(MediaType.APPLICATION_JSON).content(orderAsString2));
        this.mockMvc.perform(get("/orderbook/" + instrumentId + "/demand")).andExpect(status().isOk())
                .andExpect(jsonPath("$.demandPerOrderBook").value(81));
    }

    @Test
    public void limitBreakDownForOrderBook() throws Exception {
        String instrumentId = "instrument1";
        Order order = new Order(15f, instrumentId, new BigDecimal(45));
        Order order2 = new Order(66f, instrumentId, new BigDecimal(66));
        Order order3 = new Order(20f, instrumentId, new BigDecimal(66));
        String orderAsString = objectMapper.writeValueAsString(order);
        String orderAsString2 = objectMapper.writeValueAsString(order2);
        String orderAsString3 = objectMapper.writeValueAsString(order3);
        this.mockMvc.perform(post("/orderbook/" + instrumentId));
        this.mockMvc.perform(put("/orderbook/" + instrumentId + "/open"));
        this.mockMvc.perform(post("/order").contentType(MediaType.APPLICATION_JSON).content(orderAsString));
        this.mockMvc.perform(post("/order").contentType(MediaType.APPLICATION_JSON).content(orderAsString2));
        this.mockMvc.perform(post("/order").contentType(MediaType.APPLICATION_JSON).content(orderAsString3));
        this.mockMvc.perform(get("/orderbook/" + instrumentId + "/limit-break-down")).andExpect(status().isOk())
                .andExpect(jsonPath("$.45").value(15))
                .andExpect(jsonPath("$.66").value(86));
    }

    @Test
    public void amountOfValidOrdersFromOrderBook() throws Exception {
        String instrumentId = "instrument1";
        Order order = new Order(15f, instrumentId, new BigDecimal(45));
        Order order2 = new Order(66f, instrumentId, new BigDecimal(66));
        String orderAsString = objectMapper.writeValueAsString(order);
        String orderAsString2 = objectMapper.writeValueAsString(order2);
        this.mockMvc.perform(post("/orderbook/" + instrumentId));
        this.mockMvc.perform(put("/orderbook/" + instrumentId + "/open"));
        this.mockMvc.perform(post("/order").contentType(MediaType.APPLICATION_JSON).content(orderAsString));
        this.mockMvc.perform(post("/order").contentType(MediaType.APPLICATION_JSON).content(orderAsString2));
        this.mockMvc.perform(get("/orderbook/" + instrumentId + "/valid-orders")).andExpect(status().isOk())
                .andExpect(jsonPath("$.validOrdersCount").value(2));
    }

    @Test
    public void amountOfInvalidOrdersFromOrderBook() throws Exception {
        String instrumentId = "instrument1";
        Order order = new Order(15f, instrumentId, new BigDecimal(45));
        Order order2 = new Order(66f, instrumentId, new BigDecimal(66));
        String orderAsString = objectMapper.writeValueAsString(order);
        String orderAsString2 = objectMapper.writeValueAsString(order2);
        this.mockMvc.perform(post("/orderbook/" + instrumentId));
        this.mockMvc.perform(put("/orderbook/" + instrumentId + "/open"));
        this.mockMvc.perform(post("/order").contentType(MediaType.APPLICATION_JSON).content(orderAsString));
        this.mockMvc.perform(post("/order").contentType(MediaType.APPLICATION_JSON).content(orderAsString2));
        this.mockMvc.perform(get("/orderbook/" + instrumentId + "/invalid-orders")).andExpect(status().isOk())
                .andExpect(jsonPath("$.invalidOrdersCount").value(0));
    }

    @Test
    public void validDemandPerOrderBook() throws Exception {
        String instrumentId = "instrument1";
        Order order = new Order(15f, instrumentId, new BigDecimal(45));
        Order order2 = new Order(66f, instrumentId, new BigDecimal(66));
        Execution execution = new Execution(instrumentId, 50f, new BigDecimal(55));
        String orderAsString = objectMapper.writeValueAsString(order);
        String orderAsString2 = objectMapper.writeValueAsString(order2);
        String executionAsString = objectMapper.writeValueAsString(execution);
        this.mockMvc.perform(post("/orderbook/" + instrumentId));
        this.mockMvc.perform(put("/orderbook/" + instrumentId + "/open"));
        this.mockMvc.perform(post("/order").contentType(MediaType.APPLICATION_JSON).content(orderAsString));
        this.mockMvc.perform(post("/order").contentType(MediaType.APPLICATION_JSON).content(orderAsString2));
        this.mockMvc.perform(put("/orderbook/" + instrumentId + "/close"));
        this.mockMvc.perform(post("/execution").contentType(MediaType.APPLICATION_JSON).content(executionAsString));
        this.mockMvc.perform(get("/orderbook/" + instrumentId + "/valid-demand")).andExpect(status().isOk())
                .andExpect(jsonPath("$.validDemand").value(66));
    }

    @Test
    public void invalidDemandPerOrderBook() throws Exception {
        String instrumentId = "instrument1";
        Order order = new Order(15f, instrumentId, new BigDecimal(45));
        Order order2 = new Order(66f, instrumentId, new BigDecimal(66));
        Execution execution = new Execution(instrumentId, 50f, new BigDecimal(55));
        String orderAsString = objectMapper.writeValueAsString(order);
        String orderAsString2 = objectMapper.writeValueAsString(order2);
        String executionAsString = objectMapper.writeValueAsString(execution);
        this.mockMvc.perform(post("/orderbook/" + instrumentId));
        this.mockMvc.perform(put("/orderbook/" + instrumentId + "/open"));
        this.mockMvc.perform(post("/order").contentType(MediaType.APPLICATION_JSON).content(orderAsString));
        this.mockMvc.perform(post("/order").contentType(MediaType.APPLICATION_JSON).content(orderAsString2));
        this.mockMvc.perform(put("/orderbook/" + instrumentId + "/close"));
        this.mockMvc.perform(post("/execution").contentType(MediaType.APPLICATION_JSON).content(executionAsString));
        this.mockMvc.perform(get("/orderbook/" + instrumentId + "/invalid-demand")).andExpect(status().isOk())
                .andExpect(jsonPath("$.invalidDemand").value(15));
    }

    @Test
    public void accumulatedExecutionQuantityPerOrderBook() throws Exception {
        String instrumentId = "instrument1";
        Order order = new Order(15f, instrumentId, new BigDecimal(45));
        Order order2 = new Order(66f, instrumentId, new BigDecimal(66));
        Execution execution = new Execution(instrumentId, 50f, new BigDecimal(55));
        String orderAsString = objectMapper.writeValueAsString(order);
        String orderAsString2 = objectMapper.writeValueAsString(order2);
        String executionAsString = objectMapper.writeValueAsString(execution);
        this.mockMvc.perform(post("/orderbook/" + instrumentId));
        this.mockMvc.perform(put("/orderbook/" + instrumentId + "/open"));
        this.mockMvc.perform(post("/order").contentType(MediaType.APPLICATION_JSON).content(orderAsString));
        this.mockMvc.perform(post("/order").contentType(MediaType.APPLICATION_JSON).content(orderAsString2));
        this.mockMvc.perform(put("/orderbook/" + instrumentId + "/close"));
        this.mockMvc.perform(post("/execution").contentType(MediaType.APPLICATION_JSON).content(executionAsString));
        this.mockMvc.perform(get("/orderbook/" + instrumentId + "/execution-quantity")).andExpect(status().isOk())
                .andExpect(jsonPath("$.executionQuantity").value(50));
    }

    @Test
    public void executionPricePerOrderBook() throws Exception {
        String instrumentId = "instrument1";
        Order order = new Order(15f, instrumentId, new BigDecimal(45));
        Order order2 = new Order(66f, instrumentId, new BigDecimal(66));
        Execution execution = new Execution(instrumentId, 50f, new BigDecimal(55));
        String orderAsString = objectMapper.writeValueAsString(order);
        String orderAsString2 = objectMapper.writeValueAsString(order2);
        String executionAsString = objectMapper.writeValueAsString(execution);
        this.mockMvc.perform(post("/orderbook/" + instrumentId));
        this.mockMvc.perform(put("/orderbook/" + instrumentId + "/open"));
        this.mockMvc.perform(post("/order").contentType(MediaType.APPLICATION_JSON).content(orderAsString));
        this.mockMvc.perform(post("/order").contentType(MediaType.APPLICATION_JSON).content(orderAsString2));
        this.mockMvc.perform(put("/orderbook/" + instrumentId + "/close"));
        this.mockMvc.perform(post("/execution").contentType(MediaType.APPLICATION_JSON).content(executionAsString));
        this.mockMvc.perform(get("/orderbook/" + instrumentId + "/execution-price")).andExpect(status().isOk())
                .andExpect(jsonPath("$.executionPrice").value(55));
    }

    @Test
    public void orderWithBiggestDemandFromOrderBook() throws Exception {
        String instrumentId = "instrument1";
        Order order = new Order(15f, instrumentId, new BigDecimal(45));
        Order order2 = new Order(66f, instrumentId, new BigDecimal(66));
        Order order3 = new Order(20f, instrumentId, new BigDecimal(66));
        String orderAsString = objectMapper.writeValueAsString(order);
        String orderAsString2 = objectMapper.writeValueAsString(order2);
        String orderAsString3 = objectMapper.writeValueAsString(order3);
        this.mockMvc.perform(post("/orderbook/" + instrumentId));
        this.mockMvc.perform(put("/orderbook/" + instrumentId + "/open"));
        this.mockMvc.perform(post("/order").contentType(MediaType.APPLICATION_JSON).content(orderAsString));
        this.mockMvc.perform(post("/order").contentType(MediaType.APPLICATION_JSON).content(orderAsString2));
        this.mockMvc.perform(post("/order").contentType(MediaType.APPLICATION_JSON).content(orderAsString3));
        this.mockMvc.perform(get("/orderbook/" + instrumentId + "/biggest-order")).andExpect(status().isOk())
                .andExpect(jsonPath("$.quantity").value(66));
    }

    @Test
    public void orderWithSmallestDemandFromOrderBook() throws Exception {
        String instrumentId = "instrument1";
        Order order = new Order(15f, instrumentId, new BigDecimal(45));
        Order order2 = new Order(66f, instrumentId, new BigDecimal(66));
        Order order3 = new Order(20f, instrumentId, new BigDecimal(66));
        String orderAsString = objectMapper.writeValueAsString(order);
        String orderAsString2 = objectMapper.writeValueAsString(order2);
        String orderAsString3 = objectMapper.writeValueAsString(order3);
        this.mockMvc.perform(post("/orderbook/" + instrumentId));
        this.mockMvc.perform(put("/orderbook/" + instrumentId + "/open"));
        this.mockMvc.perform(post("/order").contentType(MediaType.APPLICATION_JSON).content(orderAsString));
        this.mockMvc.perform(post("/order").contentType(MediaType.APPLICATION_JSON).content(orderAsString2));
        this.mockMvc.perform(post("/order").contentType(MediaType.APPLICATION_JSON).content(orderAsString3));
        this.mockMvc.perform(get("/orderbook/" + instrumentId + "/smallest-order")).andExpect(status().isOk())
                .andExpect(jsonPath("$.quantity").value(15));
    }

    @Test
    public void earliestOrderFromOrderBook() throws Exception {
        String instrumentId = "instrument1";
        Order order = new Order(15f, instrumentId, new BigDecimal(45));
        Order order2 = new Order(66f, instrumentId, new BigDecimal(66));
        Order order3 = new Order(20f, instrumentId, new BigDecimal(66));
        String orderAsString = objectMapper.writeValueAsString(order);
        String orderAsString2 = objectMapper.writeValueAsString(order2);
        String orderAsString3 = objectMapper.writeValueAsString(order3);
        this.mockMvc.perform(post("/orderbook/" + instrumentId));
        this.mockMvc.perform(put("/orderbook/" + instrumentId + "/open"));
        this.mockMvc.perform(post("/order").contentType(MediaType.APPLICATION_JSON).content(orderAsString));
        this.mockMvc.perform(post("/order").contentType(MediaType.APPLICATION_JSON).content(orderAsString2));
        this.mockMvc.perform(post("/order").contentType(MediaType.APPLICATION_JSON).content(orderAsString3));
        this.mockMvc.perform(get("/orderbook/" + instrumentId + "/earliest-order")).andExpect(status().isOk())
                .andExpect(jsonPath("$.quantity").value(15));
    }

    @Test
    public void latestOrderFromOrderBook() throws Exception {
        String instrumentId = "instrument1";
        Order order = new Order(15f, instrumentId, new BigDecimal(45));
        Order order2 = new Order(66f, instrumentId, new BigDecimal(66));
        Order order3 = new Order(20f, instrumentId, new BigDecimal(66));
        String orderAsString = objectMapper.writeValueAsString(order);
        String orderAsString2 = objectMapper.writeValueAsString(order2);
        String orderAsString3 = objectMapper.writeValueAsString(order3);
        this.mockMvc.perform(post("/orderbook/" + instrumentId));
        this.mockMvc.perform(put("/orderbook/" + instrumentId + "/open"));
        this.mockMvc.perform(post("/order").contentType(MediaType.APPLICATION_JSON).content(orderAsString));
        this.mockMvc.perform(post("/order").contentType(MediaType.APPLICATION_JSON).content(orderAsString2));
        this.mockMvc.perform(post("/order").contentType(MediaType.APPLICATION_JSON).content(orderAsString3));
        this.mockMvc.perform(get("/orderbook/" + instrumentId + "/latest-order")).andExpect(status().isOk())
                .andExpect(jsonPath("$.quantity").value(20));
    }
}
