package com.clusus.bloombergfxdeals.config;

import com.clusus.bloombergfxdeals.entity.FxDeal;
import com.clusus.bloombergfxdeals.repository.FxDealRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FxDealWriter implements ItemWriter<FxDeal> {

  private final Logger logger = LoggerFactory.getLogger(FxDealWriter.class);

  @Autowired private FxDealRepository fxDealRepository;

  @Override
  public void write(List<? extends FxDeal> fxDeal) {
    logger.info("inside FxDealWriter");
    fxDealRepository.saveAll(fxDeal);
  }
}
