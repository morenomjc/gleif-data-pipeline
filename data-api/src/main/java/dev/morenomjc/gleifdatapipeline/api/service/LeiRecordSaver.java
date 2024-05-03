package dev.morenomjc.gleifdatapipeline.api.service;

import dev.morenomjc.gleif.model.LeiGoldenCopyRecord;
import dev.morenomjc.gleifdatapipeline.api.mappers.LeiRecordMapper;
import dev.morenomjc.gleifdatapipeline.api.model.mongodb.LeiMongoDocument;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class LeiRecordSaver {

  private final MongoTemplate mongoTemplate;
  private final LeiRecordMapper leiRecordMapper;

  @KafkaListener(topics = "lei-records", groupId = "lei-saver")
  public void upsert(LeiGoldenCopyRecord leiGoldenCopyRecord) {
    log.info("Saving: {}", leiGoldenCopyRecord.getLei().getValue());

    LeiMongoDocument leiMongoDocument = leiRecordMapper.toMongoDocument(leiGoldenCopyRecord);

    Query findByLei = new Query(Criteria.where("lei").is(leiMongoDocument.getLei()));
    boolean exists = mongoTemplate.exists(findByLei, LeiMongoDocument.class);

    if (!exists) {
      log.info("LEI: {} not found. Inserting.", leiMongoDocument.getLei());
      mongoTemplate.save(leiMongoDocument);
    } else {
      log.info("LEI: {} found. Replacing.", leiMongoDocument.getLei());
      mongoTemplate.replace(findByLei, leiMongoDocument);
    }
  }
}
