package com.clusus.bloombergfxdeals.config;

import com.clusus.bloombergfxdeals.dto.ErrorResponse;
import com.clusus.bloombergfxdeals.entity.FxDeal;
import com.clusus.bloombergfxdeals.exception.BusinessException;
import com.clusus.bloombergfxdeals.repository.FxDealRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class FxDealWriter implements ItemWriter<FxDeal> {

  private final Logger logger = LoggerFactory.getLogger(FxDealWriter.class);

  @Autowired private FxDealRepository fxDealRepository;

  @Override
  @Transactional
  public void write(List<? extends FxDeal> fxDealsList) {
    logger.info("Inside FxDealWriter to save fxDeals");

    for (FxDeal fxDeal : fxDealsList) {
      try {
        if (!fxDealRepository.existsByUniqueDealId(fxDeal.getDealUniqueId())) {
          fxDealRepository.save(fxDeal);
          logger.info("FX Deal saved successfully with unique ID: {}", fxDeal.getDealUniqueId());
        } else {
          String errorMessage =
              String.format("FX Deal with unique ID %s already exists.", fxDeal.getDealUniqueId());
          logger.error(errorMessage);
          throw new BusinessException(new ErrorResponse("DUPLICATE", errorMessage));
        }
      } catch (RuntimeException e) {
        String errorDescription =
            String.format(
                "Exception occurred while saving fxDeal with id: %s", fxDeal.getDealUniqueId());
        logger.error(errorDescription);
        throw new BusinessException(new ErrorResponse("DEAL_ERROR", errorDescription));
      }
    }
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void saveFxDeal(FxDeal fxDeal) {
    fxDealRepository.save(fxDeal);
  }
}
