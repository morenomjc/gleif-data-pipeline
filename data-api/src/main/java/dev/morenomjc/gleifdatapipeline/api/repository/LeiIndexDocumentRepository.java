package dev.morenomjc.gleifdatapipeline.api.repository;

import dev.morenomjc.gleifdatapipeline.api.model.elasticsearch.LeiIndexDocument;
import java.util.List;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LeiIndexDocumentRepository extends
    ElasticsearchRepository<LeiIndexDocument, String> {

  List<LeiIndexDocument> findByNameValueOrOtherNamesValue(String text1, String text2);
}
