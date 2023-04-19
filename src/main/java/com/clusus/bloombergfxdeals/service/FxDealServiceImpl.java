package com.clusus.bloombergfxdeals.service;

import com.clusus.bloombergfxdeals.dto.ErrorResponse;
import com.clusus.bloombergfxdeals.entity.FxDeal;
import com.clusus.bloombergfxdeals.exception.BusinessException;
import com.clusus.bloombergfxdeals.repository.FxDealRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FxDealServiceImpl implements FxDealService {

  private final Logger logger = LoggerFactory.getLogger(FxDealServiceImpl.class);
  @Autowired private FxDealRepository fxDealRepository;

  @Override
  public void saveDealDetails(FxDeal fxDeal) {
    try {
      if (fxDeal == null) {
        throw new IllegalArgumentException("Input FxDeal object is null");
      }
      if (!fxDealRepository.existsByUniqueDealId(fxDeal.getDealUniqueId())) {
        fxDealRepository.save(fxDeal);
        logger.info("FX Deal saved successfully with unique ID: {}", fxDeal.getDealUniqueId());
      } else {
        String errorMessage =
            String.format("FX Deal with unique ID %s already exists.", fxDeal.getDealUniqueId());
        logger.error(errorMessage);
        throw new BusinessException(new ErrorResponse("DUPLICATE", errorMessage));
      }
    } catch (Exception e) {
      String errorMessage = "Error occurred while saving FX Deal details";
      logger.error(errorMessage, e);
      throw e;
    }
  }
}
