package com.peter.importerservice.service.importer.configuration.factory;

import com.peter.importerservice.service.importer.configuration.ImportConfigurationFields;
import com.peter.importerservice.service.importer.dto.bo.FactoryImportBO;

import java.util.function.BiConsumer;

public enum ImportFactoryFields implements ImportConfigurationFields<FactoryImportBO> {
  FACTORY_NAME(FactoryImportBO::setName),
  CUSTOM_ID(FactoryImportBO::setCustomId) {
    @Override
    public boolean isRequired() {
      return false;
    }
  },
  FACTORY_LOCAL_NAME(FactoryImportBO::setLocalName) {
    @Override
    public boolean isRequired() {
      return false;
    }
  },
  COUNTRY(ImportFactoryFields::setCountry),
  ADMINISTRATIVE_AREA(ImportFactoryFields::setAdministrativeArea) {
    @Override
    public boolean isRequired() {
      return false;
    }
  },
  CITY(ImportFactoryFields::setCity),
  POSTAL_CODE(ImportFactoryFields::setPostalCode) {
    @Override
    public boolean isRequired() {
      return false;
    }
  },
  ADDRESS_DETAILS(ImportFactoryFields::setAddressDetail),
  CONTACT_FIRST_NAME(ImportFactoryFields::setContactFirstName),
  CONTACT_LAST_NAME(ImportFactoryFields::setContactLastName),
  CONTACT_LOCAL_NAME(ImportFactoryFields::setContactLocalName) {
    @Override
    public boolean isRequired() {
      return false;
    }
  },
  POSITION(ImportFactoryFields::setPosition) {
    @Override
    public boolean isRequired() {
      return false;
    }
  },
  WORK_PHONE(ImportFactoryFields::setWorkPhone),
  MOBILE_PHONE(ImportFactoryFields::setMobilePhone) {
    @Override
    public boolean isRequired() {
      return false;
    }
  },
  EMAIL(ImportFactoryFields::setEmail);

  private final BiConsumer<FactoryImportBO, String> consumer;

  ImportFactoryFields(BiConsumer<FactoryImportBO, String> consumer) {
    this.consumer = consumer;
  }

  @Override
  public void setValue(FactoryImportBO bo, String value) {
    this.consumer.accept(bo, value);
  }

  private static void setCountry(FactoryImportBO bo, String value) {
    bo.getAddress().setCountry(value);
  }

  private static void setAdministrativeArea(FactoryImportBO bo, String value) {
    bo.getAddress().setAdministrativeArea(value);
  }

  private static void setCity(FactoryImportBO bo, String value) {
    bo.getAddress().setCity(value);
  }

  private static void setPostalCode(FactoryImportBO bo, String value) {
    bo.getAddress().setPostCode(value);
  }

  private static void setAddressDetail(FactoryImportBO bo, String value) {
    bo.getAddress().setStreet(value);
  }

  private static void setContactFirstName(FactoryImportBO bo, String value) {
    bo.getContact().setFirstName(value);
  }

  private static void setContactLastName(FactoryImportBO bo, String value) {
    bo.getContact().setLastName(value);
  }

  private static void setContactLocalName(FactoryImportBO bo, String value) {
    bo.getContact().setLocalName(value);
  }

  private static void setPosition(FactoryImportBO bo, String value) {
    bo.getContact().setPosition(value);
  }

  private static void setWorkPhone(FactoryImportBO bo, String value) {
    bo.getContact().setPhone(value);
  }

  private static void setMobilePhone(FactoryImportBO bo, String value) {
    bo.getContact().setMobile(value);
  }

  private static void setEmail(FactoryImportBO bo, String value) {
    bo.getContact().setEmail(value);
  }
}
