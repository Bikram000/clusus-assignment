package com.clusus.bloombergfxdeals;

import com.clusus.bloombergfxdeals.entity.FxDeal;
import com.clusus.bloombergfxdeals.exception.BusinessException;
import com.clusus.bloombergfxdeals.repository.FxDealRepository;
import com.clusus.bloombergfxdeals.service.FxDealServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@SpringBootTest
public class FxDealServiceTest {
  @Mock FxDealRepository fxDealRepository;

  @InjectMocks FxDealServiceImpl fxDealService;

  @Test
  public void testSaveDetailsForValidFxDeal() {
    FxDeal fxDeal1 =
        new FxDeal("test1", "USD", "JPY", LocalDateTime.now(), new BigDecimal("50.05"));
    when(fxDealRepository.existsByUniqueDealId(anyString())).thenReturn(false);
    when(fxDealRepository.save(fxDeal1)).thenReturn(fxDeal1);
    fxDealService.saveDealDetails(fxDeal1);
    verify(fxDealRepository, times(1)).existsByUniqueDealId("test1");
    verify(fxDealRepository, times(1)).save(fxDeal1);
    verifyNoMoreInteractions(fxDealRepository);
  }

  @Test
  public void testSaveDetailsForExistingFxDeal() {
    FxDeal fxDeal1 =
        new FxDeal("test1", "USD", "JPY", LocalDateTime.now(), new BigDecimal("50.05"));
    when(fxDealRepository.existsByUniqueDealId(anyString())).thenReturn(true);
    when(fxDealRepository.existsByUniqueDealId(anyString())).thenReturn(true);

    BusinessException expectedException =
        assertThrows(
            BusinessException.class,
            () -> {
              fxDealService.saveDealDetails(fxDeal1);
            });

    assertEquals("DUPLICATE", expectedException.getErrorResponse().getErrorCode());
    assertEquals(
        "FX Deal with unique ID test1 already exists.",
        expectedException.getErrorResponse().getErrorDescription());

    verify(fxDealRepository, times(1)).existsByUniqueDealId("test1");
    verifyNoMoreInteractions(fxDealRepository);
  }

  @Test
  public void testSaveDealDetailsForNullFxDeal() {
    FxDeal fxDeal = null;

    IllegalArgumentException expectedException =
        assertThrows(
            IllegalArgumentException.class,
            () -> {
              fxDealService.saveDealDetails(fxDeal);
            });

    assertEquals("Input FxDeal object is null", expectedException.getMessage());

    verifyNoInteractions(fxDealRepository);
  }

  @Test
  public void testSaveDealDetails_Exception() {
    FxDeal fxDeal1 =
        new FxDeal("test1", "USD", "JPY", LocalDateTime.now(), new BigDecimal("50.05"));
    when(fxDealRepository.existsByUniqueDealId(anyString())).thenReturn(false);
    doThrow(new RuntimeException("Database connection failed"))
        .when(fxDealRepository)
        .save(fxDeal1);

    RuntimeException expectedException =
        assertThrows(
            RuntimeException.class,
            () -> {
              fxDealService.saveDealDetails(fxDeal1);
            });
    verify(fxDealRepository, times(1)).existsByUniqueDealId("test1");
    verify(fxDealRepository, times(1)).save(fxDeal1);
    verifyNoMoreInteractions(fxDealRepository);
  }
}
