package com.peter.importerservice.service.importer.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ImportSummaryDTO {

  private Long error = 0L;
  private Long success = 0L;

  public void incError() {
    error++;
  }

  public void incSuccess() {
    success++;
  }
}
