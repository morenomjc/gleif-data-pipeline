package dev.morenomjc.gleif.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class LeiGoldenCopyRecord {

  @JsonProperty("LEI")
  private NestedString lei;

  @JsonProperty("Entity")
  private Entity entity;

  @Setter
  @Getter
  public static class NestedString {

    @JsonProperty("$")
    private String value;

  }

  @Data
  public static class Entity {

    @JsonProperty("LegalName")
    private Name legalName;
    @JsonProperty("OtherEntityNames")
    private OtherEntityNames otherEntityNames;
    @JsonProperty("LegalAddress")
    private Address legalAddress;
    @JsonProperty("HeadquartersAddress")
    private Address headquartersAddress;
    @JsonProperty("LegalJurisdiction")
    private NestedString legalJurisdiction;
    @JsonProperty("EntityCategory")
    private NestedString entityCategory;
    @JsonProperty("EntityStatus")
    private NestedString entityStatus;

  }

  @Data
  public static class Name {

    @JsonProperty("@xml:lang")
    private String language;

    @JsonProperty("$")
    private String value;

  }

  @Data
  public static class TypedName {

    @JsonProperty("@xml:lang")
    private String language;

    @JsonProperty("$")
    private String value;

    @JsonProperty("@type")
    private String type;

  }

  @Data
  public static class OtherEntityNames {

    @JsonProperty("OtherEntityName")
    private List<TypedName> otherEntityNames;

  }

  @Data
  public static class Address {

    @JsonProperty("@xml:lang")
    private String language;

    @JsonProperty("FirstAddressLine")
    private NestedString firstAddressLine;
    @JsonProperty("AdditionalAddressLine")
    private List<NestedString> additionalAddressLines;
    @JsonProperty("City")
    private NestedString city;
    @JsonProperty("Country")
    private NestedString country;
    @JsonProperty("Region")
    private NestedString region;
    @JsonProperty("PostalCode")
    private NestedString postalCode;

  }
}

