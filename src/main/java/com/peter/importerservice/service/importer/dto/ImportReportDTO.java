package com.peter.importerservice.service.importer.dto;

import com.peter.importerservice.exception.technical.ImportDryRunException;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ImportReportDTO {

  private ImportSummaryDTO summary;
  private List<ImportDryRunException.LineError> errors;

  public ImportReportDTO summary(ImportSummaryDTO summary) {
    this.summary = summary;
    return this;
  }

  public ImportReportDTO errors(List<ImportDryRunException.LineError> errors) {
    this.errors = errors;
    return this;
  }
}
