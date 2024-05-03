package dev.morenomjc.gleifdatapipeline.jobs.service;

import dev.morenomjc.gleif.model.LeiGoldenCopyRecord;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LeiGoldenCopyRecordPublisher {

  private final KafkaTemplate<String, LeiGoldenCopyRecord> kafkaTemplate;

  public LeiGoldenCopyRecordPublisher(
      @Qualifier("kafkaTemplate") KafkaTemplate<String, LeiGoldenCopyRecord> kafkaTemplate) {
    this.kafkaTemplate = kafkaTemplate;
  }

  public void publish(LeiGoldenCopyRecord leiRecord) {
    kafkaTemplate.send("lei-records", leiRecord);
    log.info("Published LEI {} record", leiRecord.getLei().getValue());
  }
}
