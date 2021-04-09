package com.peter.importerservice.service.importer.dto;

import com.peter.importerservice.service.dto.ContactCreationDTO;
import com.peter.importerservice.service.importer.configuration.Constants;
import com.peter.importerservice.service.importer.configuration.LineElement;
import com.peter.importerservice.service.importer.validation.CustomId;
import com.peter.importerservice.service.importer.validation.CustomIdConstraint;
import com.peter.importerservice.service.importer.validation.FactoryName;
import com.peter.importerservice.service.importer.validation.FactoryNameConstraint;
import com.peter.importerservice.validator.StringLength;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@FactoryName
@CustomId
public class FactoryImportBO implements
        LineElement<FactoryImportBO>, FactoryNameConstraint, CustomIdConstraint {

  private Long lineNumber;

  @NotBlank
  @StringLength(min = 1, max = 255)
  private String name;

  @Valid @NotNull private ContactCreationDTO contact = new ContactCreationDTO();

  @Pattern(regexp = Constants.NOT_BLANK_REGEXP)
  @StringLength(min = 1, max = 255)
  private String customId;

  @Pattern(regexp = Constants.NOT_BLANK_REGEXP)
  @StringLength(min = 1, max = 255)
  private String localName;

  @Valid @NotNull private AddressImportBO address = new AddressImportBO();

  @Override
  public Long getEntityId() {
    return null;
  }

  @Override
  public Long getId() {
    return getEntityId();
  }
}
