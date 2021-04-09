package com.peter.importerservice.service.importer.configuration;

/** Application constants. */
public final class Constants {

  public static final String NOT_BLANK_REGEXP = "^\\S($|.*\\S$)";

  public static final class Import {
    public static final String LINE_ERROR = "GLOBAL";
    public static final String BACKOFFICE = "backoffice user";

    private Import() {}
  }
}
