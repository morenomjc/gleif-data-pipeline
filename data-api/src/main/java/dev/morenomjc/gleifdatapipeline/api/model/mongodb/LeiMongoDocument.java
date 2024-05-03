package dev.morenomjc.gleifdatapipeline.api.model.mongodb;

import java.util.List;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@ToString
@Document(collection = "lei-documents")
public class LeiMongoDocument {

  @Id
  private String id;

  @Indexed(unique = true)
  private String lei;
  private Entity entity;

  @Data
  public static class Entity {

    private Name legalName;
    private List<TypedName> otherEntityNames;
    private Address legalAddress;
    private Address headquartersAddress;
    private String legalJurisdiction;
    private String entityCategory;
    private String entityStatus;

  }

  @Data
  public static class Name {

    private String language;
    private String value;

  }

  @Data
  public static class TypedName {

    private String language;
    private String value;
    private String type;

  }

  @Data
  public static class Address {

    private String language;

    private List<String> addressLines;
    private String city;
    private String country;
    private String region;
    private String postalCode;

  }

}

