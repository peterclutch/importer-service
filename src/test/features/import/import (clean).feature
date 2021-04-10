@cleanFeature
Feature: Import feature - clean

  Background:
    Given a brand is logged - clean

#  Scenario: As a brand I want to import products
#    Then I get PRODUCT configuration
#    And The PRODUCT configuration is correct
#    Then I try a PRODUCT import with dry run option "true"
#    And The PRODUCT in line "2" is not saved
#    And The PRODUCT in line "4" is not saved
#    And The file contains "2" errors
#    And The line "2" is valid
#    And The line "3" is in error with code "INVALID_GTIN" on field "GTIN"
#    And The line "5" is in error with code "PRODUCT_IDENTIFIER_ALREADY_USED" on field "NAME"
#    Then I try a PRODUCT import with dry run option "false"
#    And The PRODUCT in line "2" is saved
#    And The PRODUCT in line "4" is saved
#
#  Scenario: As a brand I want to import users
#    Then I get USER configuration
#    And The USER configuration is correct
#    Then I try a USER import with dry run option "true"
#    And The USER in line "2" is not saved
#    And The USER in line "3" is not saved
#    And The file contains "3" errors
#    And The line "2" is valid
#    And The line "3" is in error with code "ROLE_NOT_FOUND" on field "ROLE"
#    And The line "4" is in error with code "CANNOT_SET_ROLE" on field "ROLE"
#    And The line "5" is valid
#    And The line "6" is in error with code "EMAIL_ALREADY_USED" on field "EMAIL"
#    And The line "7" is valid
#    Then I try a USER import with dry run option "false"
#    And The USER in line "2" is saved
#    And The USER in line "5" is saved
#    And The USER in line "7" is saved
#
#  Scenario: As a brand I want to import purchase order
#    Given an address is filled - clean
#    When I create a factory named "nakamura" - clean
#    Then the factory is created - clean
#    And I create a product UNCATEGORIZED named "djadja" referenced "djadja" - clean
#    And I create a product UNCATEGORIZED named "les copines" referenced "les copines" - clean
#    And I create a product UNCATEGORIZED named "Pookie" referenced "Pookie" - clean
#    And I create a product UNCATEGORIZED named "La Dot" referenced "La Dot" - clean
#    Then I get PURCHASE_ORDER configuration
#    And The PURCHASE_ORDER configuration is correct
#    Then I try a PURCHASE_ORDER import with dry run option "true"
#    And The PURCHASE_ORDER in line "2" is not saved
#    And The PURCHASE_ORDER in line "7" is not saved
#    And The line "2" is valid
#    And The line "3" is valid
#    And The line "4" is valid
#    And The line "5" is valid
#    And The line "6" is in error with code "NOT_FOUND" on field "PRODUCT_IDENTIFIER"
#    And The line "6" is in error with code "TIMELINE_ERROR" on field "ORDER_DATE"
#    And The line "6" is in error with code "INCONSISTENT_SHIPMENT_DATE" on field "SHIPMENT_DATE"
#    And The line "6" is in error with code "INCONSISTENT_ORDER_DATE" on field "ORDER_DATE"
#    And The line "6" is in error with code "INCONSISTENT_FACTORY_NAME" on field "FACTORY_NAME"
#    And The line "6" is in error with code "TIMELINE_ERROR" on field "ORDER_DATE"
#    And The line "7" is in error with code "NOT_FOUND" on field "FACTORY_NAME"
#    And The line "7" is in error with code "INCONSISTENT_SHIPMENT_DATE" on field "SHIPMENT_DATE"
#    And The line "7" is in error with code "INCONSISTENT_ORDER_DATE" on field "ORDER_DATE"
#    And The line "7" is in error with code "NOT_BLANK" on field "UNIT"
#    And The line "7" is in error with code "INCONSISTENT_FACTORY_NAME" on field "FACTORY_NAME"
#    And The line "8" is in error with code "BAD_FORMAT" on field "ORDER_DATE"
#    And The line "8" is in error with code "BAD_FORMAT" on field "SHIPMENT_DATE"
#    And The line "8" is in error with code "INCONSISTENT_SHIPMENT_DATE" on field "SHIPMENT_DATE"
#    And The line "8" is in error with code "INCONSISTENT_ORDER_DATE" on field "ORDER_DATE"
#
#  Scenario: As a brand I want to import defect checklist
#    Then I get DEFECT_CHECKLIST configuration
#    And The DEFECT_CHECKLIST configuration is correct
#    Then I try a DEFECT_CHECKLIST import with dry run option "true"
#    And The DEFECT_CHECKLIST in line "2" is not saved
#    And The DEFECT_CHECKLIST in line "3" is not saved
#    And The line "2" is valid
#    And The line "3" is valid
#    And The line "4" is valid
#    And The line "5" is in error with code "INVALID_CLASSIFICATION" on field "DEFECT_CLASSIFICATION"
#    And The line "6" is in error with code "ALREADY_EXIST" on field "DEFECT_CHECKLIST_NAME"
#
#  Scenario: As a brand I want to import test checklist
#    Then I get TEST_CHECKLIST configuration
#    And The TEST_CHECKLIST configuration is correct
#    Then I try a TEST_CHECKLIST import with dry run option "true"
#    And The TEST_CHECKLIST in line "2" is not saved
#    And The TEST_CHECKLIST in line "3" is not saved
#    And The line "2" is valid
#    And The line "3" is valid
#    And The line "4" is valid
#    And The line "5" is valid
#    And The line "6" is in error with code "SAMPLING_SIZE_NOT_EQUAL_INSPECTION_LEVEL" on field "SAMPLING_SIZE"
#    And The line "7" is in error with code "SAMPLING_SIZE_NOT_POSITIVE_INTEGER" on field "SAMPLING_SIZE"
#    And The line "8" is in error with code "SAMPLING_SIZE_NOT_POSITIVE_INTEGER" on field "SAMPLING_SIZE"
#    And The line "9" is in error with code "SAMPLING_SIZE_NOT_POSITIVE_INTEGER" on field "SAMPLING_SIZE"
#    And The line "10" is in error with code "SAMPLING_SIZE_NOT_PERCENTAGE" on field "SAMPLING_SIZE"
#    And The line "11" is valid
#    And The line "12" is in error with code "SAMPLING_SIZE_NOT_EMPTY" on field "SAMPLING_SIZE"
#    And The line "13" is in error with code "NOT_BLANK" on field "SAMPLING_SIZE_UNIT"
#    And The line "14" is in error with code "INVALID_RESPONSE_TYPE" on field "RESPONSE_TYPE"
#    And The line "15" is in error with code "INVALID_SAMPLING_TYPE" on field "SAMPLING_TYPE"
#    And The line "16" is in error with code "SAMPLING_SIZE_UNIT_NOT_EMPTY" on field "SAMPLING_SIZE_UNIT"
#    Then I try a TEST_CHECKLIST import with dry run option "false"
#    And The TEST_CHECKLIST in line "2" is saved
#    And The TEST_CHECKLIST in line "11" is saved

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
