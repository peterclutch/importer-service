package com.peter.importerservice.service.bo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContactBO {

  private Long id;

  private String firstName;

  private String lastName;

  private String localName;

  private String position;

  private String phone;

  private String mobile;

  private String email;
}
