package dev.morenomjc.gleifdatapipeline.jobs.batch;

import dev.morenomjc.gleif.model.LeiGoldenCopyRecord;
import dev.morenomjc.gleifdatapipeline.jobs.service.LeiGoldenCopyRecordPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class LeiRecordWriter implements ItemWriter<LeiGoldenCopyRecord> {

  private final LeiGoldenCopyRecordPublisher publisher;

  @Override
  public void write(Chunk<? extends LeiGoldenCopyRecord> chunk) throws Exception {
    log.info("Writing: {}", chunk.size());
    chunk.forEach(publisher::publish);
  }
}
