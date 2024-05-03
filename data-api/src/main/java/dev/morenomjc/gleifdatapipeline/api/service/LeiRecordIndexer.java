package dev.morenomjc.gleifdatapipeline.api.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import dev.morenomjc.gleif.model.LeiGoldenCopyRecord;
import dev.morenomjc.gleifdatapipeline.api.mappers.LeiRecordMapper;
import dev.morenomjc.gleifdatapipeline.api.model.elasticsearch.LeiIndexDocument;
import dev.morenomjc.gleifdatapipeline.api.repository.LeiIndexDocumentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class LeiRecordIndexer {

  private final LeiIndexDocumentRepository repository;
  private final LeiRecordMapper leiRecordMapper;

  @KafkaListener(topics = "lei-records", groupId = "lei-indexer")
  public void index(LeiGoldenCopyRecord leiGoldenCopyRecord) throws JsonProcessingException {
    log.info("Indexing: {}", leiGoldenCopyRecord.getLei().getValue());

    LeiIndexDocument leiDocument = leiRecordMapper.toIndexDocument(leiGoldenCopyRecord);
    repository.save(leiDocument);
  }
}
