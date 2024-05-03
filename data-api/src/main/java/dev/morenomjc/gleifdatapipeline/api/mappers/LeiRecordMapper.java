package dev.morenomjc.gleifdatapipeline.api.mappers;

import dev.morenomjc.gleif.model.LeiGoldenCopyRecord;
import dev.morenomjc.gleif.model.LeiGoldenCopyRecord.Address;
import dev.morenomjc.gleif.model.LeiGoldenCopyRecord.NestedString;
import dev.morenomjc.gleifdatapipeline.api.model.LeiResource;
import dev.morenomjc.gleifdatapipeline.api.model.elasticsearch.LeiIndexDocument;
import dev.morenomjc.gleifdatapipeline.api.model.mongodb.LeiMongoDocument;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = ComponentModel.SPRING,
    unmappedSourcePolicy = ReportingPolicy.IGNORE,
    unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LeiRecordMapper {

  @Mapping(target = "name", source = "entity.legalName.value")
  @Mapping(target = "nameLang", source = "entity.legalName.language")
  @Mapping(target = "otherNames", source = "entity.otherEntityNames")
  @Mapping(target = ".", source = "entity")
  LeiResource fromDocument(LeiMongoDocument document);

  @Mapping(target = "entity.otherEntityNames", source = "entity.otherEntityNames.otherEntityNames")
  @Mapping(target = "entity.legalAddress.addressLines", expression = "java(mapAddressLine(address))")
  @Mapping(target = "entity.headquartersAddress.addressLines", expression = "java(mapAddressLine(address))")
  LeiMongoDocument toMongoDocument(LeiGoldenCopyRecord record);

  @Mapping(target = "name", source = "entity.legalName")
  @Mapping(target = "otherNames", source = "entity.otherEntityNames.otherEntityNames")
  LeiIndexDocument toIndexDocument(LeiGoldenCopyRecord record);

  default String getValue(NestedString nestedString) {
    if (nestedString == null) {
      return null;
    }
    return nestedString.getValue();
  }

  default List<String> mapAddressLine(Address address) {
    List<String> addressLines = new ArrayList<>();
    if (address != null) {
      String addr1 = getValue(address.getFirstAddressLine());
      if (addr1 != null) {
        addressLines.add(addr1);
      }

      if (address.getAdditionalAddressLines() != null) {
        address.getAdditionalAddressLines().forEach(nestedString -> {
          String addr = getValue(nestedString);
          if (addr != null) {
            addressLines.add(addr);
          }
        });
      }

    }
    return addressLines;
  }

  @AfterMapping
  default void mapCountryName(@MappingTarget LeiResource leiResource) {
    String country = Locale.of(Locale.getDefault().getLanguage(),
        leiResource.getLegalJurisdiction()).getDisplayCountry();
    leiResource.setCountryName(country);

    if (leiResource.getLegalAddress() != null) {
      String ctry = Locale.of(Locale.getDefault().getLanguage(),
          leiResource.getLegalAddress().getCountry()).getDisplayCountry();
      leiResource.getLegalAddress().setCountryName(ctry);
    }

    if (leiResource.getHeadquartersAddress() != null) {
      String ctry = Locale.of(Locale.getDefault().getLanguage(),
          leiResource.getHeadquartersAddress().getCountry()).getDisplayCountry();
      leiResource.getHeadquartersAddress().setCountryName(ctry);
    }
  }

}
