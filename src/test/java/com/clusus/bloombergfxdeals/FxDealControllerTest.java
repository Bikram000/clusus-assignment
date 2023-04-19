package com.clusus.bloombergfxdeals;

import com.clusus.bloombergfxdeals.controller.FxDealController;
import com.clusus.bloombergfxdeals.entity.FxDeal;
import com.clusus.bloombergfxdeals.service.FxDealService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FxDealController.class)
public class FxDealControllerTest {
  @Autowired private MockMvc mockMvc;

  @MockBean private FxDealService fxDealService;

  @Autowired ObjectMapper objectMapper;

  @Test
  public void testSaveDealDetails() throws Exception {

    FxDeal fxDeal1 =
        new FxDeal("test1", "USD", "JPY", LocalDateTime.now(), new BigDecimal("50.05"));
    doNothing().when(fxDealService).saveDealDetails(fxDeal1);
    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/fx-deal/saveDealDetails")
                .content(objectMapper.writeValueAsString(fxDeal1))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isCreated());
  }
}
