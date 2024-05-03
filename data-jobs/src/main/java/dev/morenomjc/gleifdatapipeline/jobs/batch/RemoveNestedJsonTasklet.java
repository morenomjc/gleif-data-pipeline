package dev.morenomjc.gleifdatapipeline.jobs.batch;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@StepScope
@Component
public class RemoveNestedJsonTasklet implements Tasklet {

  @Value("#{jobParameters['jsonRawFilePath']}")
  private String jsonRawFilePath;

  @Override
  public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext)
      throws IOException, InterruptedException {

    ProcessBuilder mktempProcessBuilder = new ProcessBuilder("mktemp", "tmp.XXXXX");
    Process mktempProcess = mktempProcessBuilder.start();
    mktempProcess.waitFor();

    BufferedReader mktempOutputReader = new BufferedReader(
        new InputStreamReader(mktempProcess.getInputStream()));
    String tmpFilePath = mktempOutputReader.readLine();
    mktempOutputReader.close();

    // Command 2: Modify the content of $FILE and write it to the temporary file
    ProcessBuilder sedProcessBuilder = new ProcessBuilder("sed", "s/^{\"records\"://",
        jsonRawFilePath);
    sedProcessBuilder.redirectOutput(new File(tmpFilePath));
    Process sedProcess = sedProcessBuilder.start();
    sedProcess.waitFor();

    // Command 3: Move the temporary file to $FILE
    ProcessBuilder mvProcessBuilder = new ProcessBuilder("mv", tmpFilePath, jsonRawFilePath);
    Process mvProcess = mvProcessBuilder.start();
    mvProcess.waitFor();

    log.info("Raw JSON file pre-processed: {}", jsonRawFilePath);
    return RepeatStatus.FINISHED;
  }

}