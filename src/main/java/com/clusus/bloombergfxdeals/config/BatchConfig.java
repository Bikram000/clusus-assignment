package com.clusus.bloombergfxdeals.config;

import com.clusus.bloombergfxdeals.entity.FxDeal;
import com.clusus.bloombergfxdeals.repository.FxDealRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;

import javax.sql.DataSource;
import java.beans.PropertyEditorSupport;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;

@Configuration
@EnableBatchProcessing
public class BatchConfig {
  private final Logger logger = LoggerFactory.getLogger(BatchConfig.class);

  @Autowired private JobBuilderFactory jobBuilderFactory;
  @Autowired private StepBuilderFactory stepBuilderFactory;
  @Autowired private FxDealProcessor fxDealProcessor;
  @Autowired private FxDealWriter fxDealWriter;
  @Autowired private FxDealRepository fxDealRepository;
  @Autowired private DataSource dataSource;

  @Bean
  public Job fxFileJob(FlatFileItemReader<FxDeal> itemReader) {
    return jobBuilderFactory
        .get("fx-job")
        .incrementer(new RunIdIncrementer())
        .start(processFxFileStep(itemReader))
        .build();
  }

  @Bean
  //  @Transactional
  public Step processFxFileStep(FlatFileItemReader<FxDeal> fileItemReader) {
    return stepBuilderFactory
        .get("processFxFileStep")
        .<FxDeal, FxDeal>chunk(10)
        .reader(fileItemReader)
        .processor(fxDealProcessor)
        .writer(fxDealWriter)
        .taskExecutor(taskExecutor())
        .build();
  }

  @Bean
  @StepScope
  public FlatFileItemReader<FxDeal> fxDealItemReader(
      @Value("#{jobParameters['fullPathFileName']}") String filePath) {
    FlatFileItemReader<FxDeal> reader = new FlatFileItemReader<>();
    reader.setResource(new FileSystemResource(new File(filePath)));
    reader.setName("CSV-Reader");
    reader.setLinesToSkip(1);
    reader.setLineMapper(lineMapper());
    return reader;
  }

  private LineMapper<FxDeal> lineMapper() {
    DefaultLineMapper<FxDeal> lineMapper = new DefaultLineMapper<>();

    DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
    lineTokenizer.setDelimiter(",");
    lineTokenizer.setStrict(false);
    lineTokenizer.setNames(
        "dealUniqueId",
        "orderingCurrencyIsoCode",
        "targetCurrencyIsoCode",
        "dealTimestamp",
        "dealAmount");

    BeanWrapperFieldSetMapper<FxDeal> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
    fieldSetMapper.setTargetType(FxDeal.class);
    fieldSetMapper.setCustomEditors(
        Collections.singletonMap(
            LocalDateTime.class,
            new PropertyEditorSupport() {
              @Override
              public void setAsText(String text) throws IllegalArgumentException {
                setValue(
                    LocalDateTime.parse(text, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
              }
            }));
    lineMapper.setLineTokenizer(lineTokenizer);
    lineMapper.setFieldSetMapper(fieldSetMapper);

    return lineMapper;
  }

  @Bean
  public TaskExecutor taskExecutor() {
    SimpleAsyncTaskExecutor taskExecutor = new SimpleAsyncTaskExecutor();
    taskExecutor.setConcurrencyLimit(10);
    return taskExecutor;
  }
}
