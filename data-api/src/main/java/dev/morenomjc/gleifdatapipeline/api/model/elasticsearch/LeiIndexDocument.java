package dev.morenomjc.gleifdatapipeline.api.model.elasticsearch;

import java.util.List;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Data
@ToString
@Document(indexName = "lei_records")
public class LeiIndexDocument {

  @Id
  private String id;

  @Field(type = FieldType.Text)
  private String lei;
  @Field(type = FieldType.Object)
  private Name name;
  @Field(type = FieldType.Nested)
  private List<TypedName> otherNames;

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

}

