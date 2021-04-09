package com.peter.importerservice.service.bo;

import com.peter.importerservice.service.dto.AdministrativeAreaDTO;
import com.peter.importerservice.service.dto.CountryDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressBO {

    private Long id;

    private CountryDTO country;

    private AdministrativeAreaDTO administrativeArea;

    private String street;

    private String city;

    private String postCode;

}
