package com.clusus.bloombergfxdeals.entity;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "fx_deals")
public class FxDeal {

  @Id
  @NotEmpty(message = "Deal Unique Id cannot be empty or null")
  @Column(name = "deal_unique_id", unique = true)
  private String dealUniqueId;

  @NotBlank(message = "Ordering currency code cannot be blank")
  @Pattern(regexp = "[A-Z]{3}", message = "Invalid ordering currency ISO code")
  @Column(name = "ordering_currency_iso_code")
  private String orderingCurrencyIsoCode;

  @NotBlank(message = "Target currency code cannot be blank")
  @Pattern(regexp = "[A-Z]{3}", message = "Invalid target currency ISO code")
  @Column(name = "to_currency_iso_code")
  private String targetCurrencyIsoCode;

  @NotNull(message = "Deal timestamp cannot be null")
  @Column(name = "deal_timestamp")
  private LocalDateTime dealTimestamp;

  @NotNull(message = "Deal amount cannot be null")
  @DecimalMin(value = "0.0", inclusive = false, message = "Deal amount must be greater than zero")
  @Column(name = "deal_amount")
  private BigDecimal dealAmount;

  public FxDeal() {}

  public FxDeal(
      String dealUniqueId,
      String orderingCurrencyIsoCode,
      String targetCurrencyIsoCode,
      LocalDateTime dealTimestamp,
      BigDecimal dealAmount) {
    this.dealUniqueId = dealUniqueId;
    this.orderingCurrencyIsoCode = orderingCurrencyIsoCode;
    this.targetCurrencyIsoCode = targetCurrencyIsoCode;
    this.dealTimestamp = dealTimestamp;
    this.dealAmount = dealAmount;
  }

  public String getDealUniqueId() {
    return dealUniqueId;
  }

  public void setDealUniqueId(String dealUniqueId) {
    this.dealUniqueId = dealUniqueId;
  }

  public String getOrderingCurrencyIsoCode() {
    return orderingCurrencyIsoCode;
  }

  public void setOrderingCurrencyIsoCode(String orderingCurrencyIsoCode) {
    this.orderingCurrencyIsoCode = orderingCurrencyIsoCode;
  }

  public String getTargetCurrencyIsoCode() {
    return targetCurrencyIsoCode;
  }

  public void setTargetCurrencyIsoCode(String targetCurrencyIsoCode) {
    this.targetCurrencyIsoCode = targetCurrencyIsoCode;
  }

  public LocalDateTime getDealTimestamp() {
    return dealTimestamp;
  }

  public void setDealTimestamp(LocalDateTime dealTimestamp) {
    this.dealTimestamp = dealTimestamp;
  }

  public BigDecimal getDealAmount() {
    return dealAmount;
  }

  public void setDealAmount(BigDecimal dealAmount) {
    this.dealAmount = dealAmount;
  }

  @Override
  public String toString() {
    return "FxDeal{"
        + ", dealUniqueId='"
        + dealUniqueId
        + '\''
        + ", orderingCurrencyIsoCode='"
        + orderingCurrencyIsoCode
        + '\''
        + ", targetCurrencyIsoCode='"
        + targetCurrencyIsoCode
        + '\''
        + ", dealTimestamp="
        + dealTimestamp
        + ", dealAmount="
        + dealAmount
        + '}';
  }
}
