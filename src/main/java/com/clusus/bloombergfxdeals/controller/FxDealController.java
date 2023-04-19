package com.clusus.bloombergfxdeals.controller;

import com.clusus.bloombergfxdeals.dto.FinalResponse;
import com.clusus.bloombergfxdeals.entity.FxDeal;
import com.clusus.bloombergfxdeals.service.FxDealService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/fx-deal")
public class FxDealController {

  private final Logger logger = LoggerFactory.getLogger(FxDealController.class);
  @Autowired private FxDealService fxDealService;

  /**
   * Endpoint to save FX deal details.
   *
   * <p>This API endpoint accepts a JSON payload representing an FxDeal object and saves it to the
   * database. The FxDeal object is validated against the specified constraints using the @Valid
   * annotation, and any validation errors are returned to the client as a Bad Request response with
   * a map of error fields as key and error messages as values. If the FxDeal is successfully saved
   * to the database, a success message is returned to the client in the response body.
   *
   * @param fxDeal The FxDeal object to save. Must adhere to the specified validation constraints.
   * @return ResponseEntity containing a FinalResponse object with a success message, or a Bad
   *     Request response with a map of error fields and validation error messages.
   */
  @PostMapping(
      value = "/saveDealDetails",
      produces = MediaType.APPLICATION_JSON_VALUE,
      consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<FinalResponse> saveDealDetails(@Valid @RequestBody FxDeal fxDeal) {
    logger.info("Received request to save deal details: {}", fxDeal);
    fxDealService.saveDealDetails(fxDeal);
    logger.info("Deal details saved successfully with unique ID: {}", fxDeal.getDealUniqueId());
    String message =
        String.format(
            "Deal details with unique ID: %s has been accepted", fxDeal.getDealUniqueId());

    return new ResponseEntity<>(new FinalResponse(message), HttpStatus.CREATED);
  }
}
