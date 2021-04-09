package com.peter.importerservice.service.importer.configuration;

import java.util.List;

public interface MultiLineElement<E extends LineElement> extends LineElement<E> {

  void addLine(E object);

  List<E> getLines();
}
