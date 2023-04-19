package com.clusus.bloombergfxdeals.repository;

import com.clusus.bloombergfxdeals.entity.FxDeal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FxDealRepository extends JpaRepository<FxDeal, Long> {

  @Query(
      value =
          "SELECT IF(COUNT(*) > 0, 'true', 'false') FROM fx_deals fd WHERE fd.deal_unique_id = :uniqueDealId",
      nativeQuery = true)
  boolean existsByUniqueDealId(@Param("uniqueDealId") String uniqueDealId);
}
