package com.peter.importerservice.service.dto;

import com.peter.importerservice.service.importer.configuration.Constants;
import com.peter.importerservice.validator.StringLength;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@Setter
public class ContactCreationDTO {

    private Long id;

    @NotBlank
    @StringLength(min = 1, max = 50)
    private String firstName;

    @NotBlank
    @StringLength(min = 1, max = 50)
    private String lastName;

    @Pattern(regexp = Constants.NOT_BLANK_REGEXP)
    @StringLength(min = 1, max = 50)
    private String localName;

    @Pattern(regexp = Constants.NOT_BLANK_REGEXP)
    private String position;

    @NotBlank
    private String phone;

    private String mobile;

    @NotBlank
    @StringLength(min = 5, max = 50)
    @Email
    private String email;

    public ContactCreationDTO id(final Long id) {
        this.id = id;
        return this;
    }

    public ContactCreationDTO firstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public ContactCreationDTO lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public ContactCreationDTO localName(String localName) {
        this.localName = localName;
        return this;
    }

    public ContactCreationDTO phone(String phone) {
        this.phone = phone;
        return this;
    }

    public ContactCreationDTO mobile(String mobile) {
        this.mobile = mobile;
        return this;
    }

    public ContactCreationDTO email(String email) {
        this.email = email;
        return this;
    }

    public ContactCreationDTO position(final String position) {
        this.position = position;
        return this;
    }
}
