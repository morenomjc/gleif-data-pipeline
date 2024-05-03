package dev.morenomjc.gleifdatapipeline.api.repository;

import dev.morenomjc.gleifdatapipeline.api.model.mongodb.LeiMongoDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LeiMongoDocumentRepository extends MongoRepository<LeiMongoDocument, String> {

}
