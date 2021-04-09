package com.peter.importerservice.service.importer.dto;

import com.peter.importerservice.service.importer.validation.AddressImport;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AddressImport
public class AddressImportBO {

    @NotBlank
    private String country;

    private String administrativeArea;

    @NotBlank private String street;

    @NotBlank private String city;

    private String postCode;
}
