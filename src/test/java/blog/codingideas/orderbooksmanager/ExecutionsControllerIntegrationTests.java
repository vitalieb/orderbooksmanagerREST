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
import org.springframework.test.web.servlet.ResultActions;
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
public class ExecutionsControllerIntegrationTests {

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
    public void createAndAddExecution() throws Exception {
        String instrumentId = "instrument1";
        Order order = new Order(15f, instrumentId, new BigDecimal(67));
        Execution execution = new Execution(instrumentId, 50f, new BigDecimal(55));
        String orderAsString = objectMapper.writeValueAsString(order);
        String executionAsString = objectMapper.writeValueAsString(execution);
        this.mockMvc.perform(post("/orderbook/" + instrumentId));
        this.mockMvc.perform(put("/orderbook/" + instrumentId + "/open"));
        ResultActions resultActions = this.mockMvc.perform(post("/order").contentType(MediaType.APPLICATION_JSON).content(orderAsString));
        this.mockMvc.perform(put("/orderbook/" + instrumentId + "/close"));
        this.mockMvc.perform(post("/execution").contentType(MediaType.APPLICATION_JSON).content(executionAsString))
                .andDo(document("create-and-add-execution", requestFields(
                        fieldWithPath("quantity").description("Execution quantity"),
                        fieldWithPath("instrumentId").description("Id of the instrument"),
                        fieldWithPath("price").description("Price"))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.instrumentId").value(instrumentId))
                .andExpect(jsonPath("$.quantity").value(50))
                .andExpect(jsonPath("$.price").value(55));
    }

    @Test
    public void createAndAddExecution_failsBecauseOrderBookIsOpened() throws Exception {
        String instrumentId = "instrument1";
        Order order = new Order(15f, instrumentId, new BigDecimal(67));
        Execution execution = new Execution(instrumentId, 50f, new BigDecimal(55));
        String orderAsString = objectMapper.writeValueAsString(order);
        String executionAsString = objectMapper.writeValueAsString(execution);
        this.mockMvc.perform(post("/orderbook/" + instrumentId));
        this.mockMvc.perform(put("/orderbook/" + instrumentId + "/open"));
        ResultActions resultActions = this.mockMvc.perform(post("/order").contentType(MediaType.APPLICATION_JSON).content(orderAsString));
        this.mockMvc.perform(post("/execution").contentType(MediaType.APPLICATION_JSON).content(executionAsString))
                .andExpect(status().isForbidden());
    }

}
