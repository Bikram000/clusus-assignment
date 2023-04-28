package com.clusus.bloombergfxdeals.config;

import com.clusus.bloombergfxdeals.controller.FxDealController;
import com.clusus.bloombergfxdeals.entity.FxDeal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class FxDealProcessor implements ItemProcessor<FxDeal, FxDeal> {

  private final Logger logger = LoggerFactory.getLogger(FxDealProcessor.class);

  @Override
  public FxDeal process(FxDeal fxDeal) throws Exception {
    // process and validate fxFields...
    logger.info("inside fxDealProcessor");
    return fxDeal;
  }
}
