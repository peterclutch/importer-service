@cleanFeature
Feature: Import feature - clean

  Background:
    Given a brand is logged - clean

  Scenario: As a brand I want to import factories
    Then I get FACTORY configuration
    And The FACTORY configuration is correct
    Then I try a FACTORY import with dry run option "true"
    And The file contains "3" errors
    And The line "2" is valid
    And The line "3" is in error with code "ADMINISTRATIVE_AREA_LABEL_ERROR" on field "ADMINISTRATIVE_AREA"
    And The line "3" is in error with code "CUSTOM_ID" on field "CUSTOM_ID"
    And The line "4" is in error with code "FACTORY_NAME" on field "FACTORY_NAME"
    And The line "4" is in error with code "CUSTOM_ID" on field "CUSTOM_ID"
    And The line "5" is in error with code "NOT_BLANK" on field "FACTORY_NAME"
    And The line "5" is in error with code "NOT_BLANK" on field "EMAIL"
    And The line "5" is in error with code "COUNTRY_LABEL_ERROR" on field "COUNTRY"
    And The line "5" is in error with code "NOT_BLANK" on field "ADDRESS_DETAILS"
    And The line "5" is in error with code "NOT_BLANK" on field "CONTACT_LAST_NAME"
    And The line "5" is in error with code "NOT_BLANK" on field "CONTACT_FIRST_NAME"
    And The line "5" is in error with code "CUSTOM_ID" on field "CUSTOM_ID"
    Then I try a FACTORY import with dry run option "false"
    And The FACTORY in line "2" is saved

  Scenario: As a brand I want to import purchase order
#    Given an address is filled - clean
#    When I create a factory named "nakamura" - clean
#    Then the factory is created - clean
#    And I create a product UNCATEGORIZED named "djadja" referenced "djadja" - clean
#    And I create a product UNCATEGORIZED named "les copines" referenced "les copines" - clean
#    And I create a product UNCATEGORIZED named "Pookie" referenced "Pookie" - clean
#    And I create a product UNCATEGORIZED named "La Dot" referenced "La Dot" - clean
    Then I get PURCHASE_ORDER configuration
    And The PURCHASE_ORDER configuration is correct
    Then I try a PURCHASE_ORDER import with dry run option "true"
    And The PURCHASE_ORDER in line "2" is not saved
    And The PURCHASE_ORDER in line "7" is not saved
    And The line "2" is valid
    And The line "3" is valid
    And The line "4" is valid
    And The line "5" is valid
    And The line "6" is in error with code "NOT_FOUND" on field "PRODUCT_IDENTIFIER"
    And The line "6" is in error with code "TIMELINE_ERROR" on field "ORDER_DATE"
    And The line "6" is in error with code "INCONSISTENT_SHIPMENT_DATE" on field "SHIPMENT_DATE"
    And The line "6" is in error with code "INCONSISTENT_ORDER_DATE" on field "ORDER_DATE"
    And The line "6" is in error with code "INCONSISTENT_FACTORY_NAME" on field "FACTORY_NAME"
    And The line "6" is in error with code "TIMELINE_ERROR" on field "ORDER_DATE"
    And The line "7" is in error with code "NOT_FOUND" on field "FACTORY_NAME"
    And The line "7" is in error with code "INCONSISTENT_SHIPMENT_DATE" on field "SHIPMENT_DATE"
    And The line "7" is in error with code "INCONSISTENT_ORDER_DATE" on field "ORDER_DATE"
    And The line "7" is in error with code "NOT_BLANK" on field "UNIT"
    And The line "7" is in error with code "INCONSISTENT_FACTORY_NAME" on field "FACTORY_NAME"
    And The line "8" is in error with code "BAD_FORMAT" on field "ORDER_DATE"
    And The line "8" is in error with code "BAD_FORMAT" on field "SHIPMENT_DATE"
    And The line "8" is in error with code "INCONSISTENT_SHIPMENT_DATE" on field "SHIPMENT_DATE"
    And The line "8" is in error with code "INCONSISTENT_ORDER_DATE" on field "ORDER_DATE"
