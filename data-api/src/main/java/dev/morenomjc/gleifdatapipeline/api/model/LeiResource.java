package dev.morenomjc.gleifdatapipeline.api.model;

import java.util.List;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class LeiResource {

  private String lei;
  private String name;
  private String nameLang;

  private List<TypedName> otherNames;

  private Address legalAddress;
  private Address headquartersAddress;

  private String legalJurisdiction;
  private String countryName;
  private String entityCategory;
  private String entityStatus;

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
    private String countryName;
    private String region;
    private String postalCode;

  }
}
