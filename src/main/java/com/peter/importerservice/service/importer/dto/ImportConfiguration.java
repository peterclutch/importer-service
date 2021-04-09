package com.peter.importerservice.service.importer.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.peter.importerservice.service.importer.validation.FieldConfigurations;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ImportConfiguration {

  @Valid @NotNull @NotEmpty @FieldConfigurations
  private List<FieldConfiguration> fields = new ArrayList<>();

  @NotNull private Boolean dryRun;

  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  private char separator = ';';

  public ImportConfiguration addField(FieldConfiguration field) {
    this.fields.add(field);
    return this;
  }
}
