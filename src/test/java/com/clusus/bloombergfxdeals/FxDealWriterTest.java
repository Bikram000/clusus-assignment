package com.clusus.bloombergfxdeals;

import com.clusus.bloombergfxdeals.config.FxDealWriter;
import com.clusus.bloombergfxdeals.entity.FxDeal;
import com.clusus.bloombergfxdeals.exception.BusinessException;
import com.clusus.bloombergfxdeals.repository.FxDealRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class FxDealWriterTest {
  @Mock private FxDealRepository fxDealRepository;

  @InjectMocks private FxDealWriter fxDealWriter;

  @Test
  public void testWrite_NewFxDeal_ShouldSaveFxDeal() {
    FxDeal fxDeal = new FxDeal("test1", "USD", "JPY", LocalDateTime.now(), new BigDecimal("50.05"));
    List<FxDeal> fxDealsList = new ArrayList<>();
    fxDealsList.add(fxDeal);

    Mockito.when(fxDealRepository.existsByUniqueDealId("test1")).thenReturn(false);
    fxDealWriter.write(fxDealsList);

    Mockito.verify(fxDealRepository, Mockito.times(1)).save(fxDeal);
  }

  @Test
  public void testWrite_DuplicateFxDeal_ShouldThrowException() {
    FxDeal fxDeal = new FxDeal();
    fxDeal.setDealUniqueId("deal1");
    List<FxDeal> fxDealsList = new ArrayList<>();
    fxDealsList.add(fxDeal);

    Mockito.when(fxDealRepository.existsByUniqueDealId(Mockito.anyString())).thenReturn(true);

    Assertions.assertThrows(
        BusinessException.class,
        () -> {
          fxDealWriter.write(fxDealsList);
        });
  }
}
