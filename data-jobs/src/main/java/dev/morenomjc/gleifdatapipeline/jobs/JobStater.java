package dev.morenomjc.gleifdatapipeline.jobs;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JobStater implements CommandLineRunner {

  private final JobRegistry jobRegistry;
  private final JobLauncher jobLauncher;

  @Override
  public void run(String... args) throws Exception {
    Job job = jobRegistry.getJob("parseGoldenCopyJob");

    Resource jsonRawFile = new ClassPathResource(
        "gleif/json/20240505-0000-gleif-goldencopy-lei2-intra-day.json");

    JobParameters jobParameters = new JobParametersBuilder()
        .addString("jobID", String.valueOf(System.currentTimeMillis()))
        .addString("jsonRawFilePath", jsonRawFile.getFile().getAbsolutePath())
        .toJobParameters();

    jobLauncher.run(job, jobParameters);
  }
}
