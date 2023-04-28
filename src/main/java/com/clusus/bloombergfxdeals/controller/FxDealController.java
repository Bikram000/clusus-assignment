package com.clusus.bloombergfxdeals.controller;

import com.clusus.bloombergfxdeals.dto.FinalResponse;
import com.clusus.bloombergfxdeals.entity.FxDeal;
import com.clusus.bloombergfxdeals.service.FxDealService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/fx-deal")
public class FxDealController {

  private final Logger logger = LoggerFactory.getLogger(FxDealController.class);
  @Autowired private FxDealService fxDealService;
  @Autowired private JobLauncher jobLauncher;
  @Autowired private Job job;

  @Value("${file-storage}")
  private String fileStorageDirectory;

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

  /**
   * End point to upload csv file containing fx-deals and persist data inside file to database.
   *
   * <p>Spring Batch is used to process large amount of data, for simplicity job is launched from
   * this endpoint call, but we can also use scheduler to schedule job to process files from storage
   * location automatically
   *
   * @param multipartFile
   */
  @PostMapping(path = "/uploadFxDeal")
  public void uploadFxDeal(@RequestParam("file") MultipartFile multipartFile) {
    try {
      String originalFileName = multipartFile.getOriginalFilename();
      File fileToImport = new File(fileStorageDirectory + originalFileName);
      multipartFile.transferTo(fileToImport);

      JobParameters jobParameters =
          new JobParametersBuilder()
              .addString("fullPathFileName", fileStorageDirectory + originalFileName)
              .addLong("startAt", System.currentTimeMillis())
              .toJobParameters();

      JobExecution execution = jobLauncher.run(job, jobParameters);

    } catch (JobExecutionAlreadyRunningException
        | JobRestartException
        | JobInstanceAlreadyCompleteException
        | JobParametersInvalidException
        | IOException e) {

      e.printStackTrace();
    }
  }
}
