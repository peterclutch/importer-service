package com.peter.importerservice.service.bo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FactoryBO {

    private Long id;

    private String name;

    private AddressBO address;

    private ContactBO contact;

    private String customId;

    private String localName;
}
