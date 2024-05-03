package dev.morenomjc.gleifdatapipeline.api.service;

import dev.morenomjc.gleifdatapipeline.api.mappers.LeiRecordMapper;
import dev.morenomjc.gleifdatapipeline.api.model.LeiResource;
import dev.morenomjc.gleifdatapipeline.api.model.elasticsearch.LeiIndexDocument;
import dev.morenomjc.gleifdatapipeline.api.model.mongodb.LeiMongoDocument;
import dev.morenomjc.gleifdatapipeline.api.repository.LeiIndexDocumentRepository;
import dev.morenomjc.gleifdatapipeline.api.repository.LeiMongoDocumentRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class LeiRetrievalService {

  private final MongoTemplate mongoTemplate;
  private final LeiMongoDocumentRepository leiMongoDocumentRepository;
  private final LeiIndexDocumentRepository leiDocumentRepository;
  private final LeiRecordMapper leiRecordMapper;

  public Page<LeiResource> retrievePage(Pageable pageRequest) {
    Page<LeiMongoDocument> page = leiMongoDocumentRepository.findAll(pageRequest);
    List<LeiResource> leiResources = page.getContent().stream().map(leiRecordMapper::fromDocument)
        .toList();
    return new PageImpl<>(leiResources, pageRequest, page.getTotalElements());
  }

  public Optional<LeiResource> findByLei(String lei) {

    Query findByLei = new Query(Criteria.where("lei").is(lei));
    boolean exists = mongoTemplate.exists(findByLei, LeiMongoDocument.class);

    if (exists) {
      log.info("Find By LEI: {}. Found", lei);
      LeiMongoDocument leiMongoDocument = mongoTemplate.findOne(findByLei, LeiMongoDocument.class);
      return Optional.ofNullable(leiRecordMapper.fromDocument(leiMongoDocument));
    }
    log.info("Find By LEI: {}. Not Found", lei);
    return Optional.empty();
  }

  public List<LeiResource> findByName(String text) {
    List<LeiIndexDocument> leiIndexDocuments = leiDocumentRepository
        .findByNameValueOrOtherNamesValue(text, text);
    List<String> leis = leiIndexDocuments.stream().map(LeiIndexDocument::getLei).toList();
    log.info("Found {} matches name={}", leis.size(), text);

    Query findByLeis = new Query(Criteria.where("lei").in(leis));
    return mongoTemplate.find(findByLeis, LeiMongoDocument.class).stream()
        .map(leiRecordMapper::fromDocument).toList();
  }
}
