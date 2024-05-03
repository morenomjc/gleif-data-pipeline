package dev.morenomjc.gleifdatapipeline.jobs.batch;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.morenomjc.gleif.model.LeiGoldenCopyRecord;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.json.JacksonJsonObjectReader;
import org.springframework.batch.item.json.JsonItemReader;
import org.springframework.batch.item.json.builder.JsonItemReaderBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class GoldenCopyJobConfiguration {

  private final PlatformTransactionManager platformTransactionManager;
  private final JobRepository jobRepository;
  private final ObjectMapper objectMapper;

  @Bean(name = "parseGoldenCopyJob")
  public Job parseGoldenCopyJob(JobRepository jobRepository,
      @Qualifier("prepareJsonFileStep") Step step1,
      @Qualifier("parseJsonFileStep") Step step2) {
    return new JobBuilder("parseGoldenCopyJob", jobRepository)
        .start(step1)
        .next(step2)
        .build();
  }

  @Bean
  public Step prepareJsonFileStep(RemoveNestedJsonTasklet removeNestedJsonTasklet) {
    return new StepBuilder("prepareJsonFileStep", jobRepository)
        .tasklet(removeNestedJsonTasklet, platformTransactionManager)
        .build();
  }

  @Bean
  @JobScope
  public Step parseJsonFileStep(JobRepository jobRepository,
      @Qualifier("jsonItemReader") ItemReader<LeiGoldenCopyRecord> jsonItemReader,
      @Qualifier("leiRecordWriter") ItemWriter<LeiGoldenCopyRecord> leiRecordWriter
  ) {
    return new StepBuilder("parseJsonFileStep", jobRepository)
        .<LeiGoldenCopyRecord, LeiGoldenCopyRecord>chunk(100, platformTransactionManager)
        .reader(jsonItemReader)
        .writer(leiRecordWriter)
        .build();
  }

  @StepScope
  @Bean(name = "jsonItemReader")
  public JsonItemReader<LeiGoldenCopyRecord> jsonItemReader(
      @Value("#{jobParameters['jsonRawFilePath']}") String jsonRawFilePath) {
    return new JsonItemReaderBuilder<LeiGoldenCopyRecord>()
        .jsonObjectReader(new JacksonJsonObjectReader<>(objectMapper, LeiGoldenCopyRecord.class))
        .resource(new FileSystemResource(jsonRawFilePath))
        .name("jsonItemReader")
        .build();
  }
}