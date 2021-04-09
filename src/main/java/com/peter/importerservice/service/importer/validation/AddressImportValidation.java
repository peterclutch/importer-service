package com.peter.importerservice.service.importer.validation;

import com.peter.importerservice.repository.AdministrativeAreaRepository;
import com.peter.importerservice.repository.CountryRepository;
import com.peter.importerservice.service.importer.dto.AddressImportBO;
import com.peter.importerservice.util.StringUtils;
import com.peter.importerservice.validator.ValidationErrorCode;
import lombok.AllArgsConstructor;

import javax.validation.ConstraintValidatorContext;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * <strong>Note</strong>: The validator needs to be executed in a new transaction to avoid
 * concurrent modification exception during entity validation by JPA framework.
 */
@AllArgsConstructor
public class AddressImportValidation
    extends AbstractConstraintValidator<AddressImport, AddressImportBO> {

  private final CountryRepository countryRepository;

  private final AdministrativeAreaRepository administrativeAreaRepository;

  @Override
  public boolean isValid(AddressImportBO address, ConstraintValidatorContext context) {
    if (address == null) {
      return true;
    }

    var countryValid = true;
    var administrativeAreaValid = true;

    context.disableDefaultConstraintViolation();
    countryValid = checkCountryLabel(address.getCountry(), context);

    if (StringUtils.isNotBlank(address.getCountry()) && countryValid) {
      administrativeAreaValid =
          checkAdministrativeAreaByCountry(
              address.getCountry(), address.getAdministrativeArea(), context);
    }

    return countryValid && administrativeAreaValid;
  }

  private boolean checkCountryLabel(String country, ConstraintValidatorContext context) {
    if (StringUtils.isBlank(country)) {
      return true;
    }

    boolean valid = countryRepository.existsByLabel(country);
    if (!valid) {
      context.disableDefaultConstraintViolation();
      context
          .buildConstraintViolationWithTemplate(
              ValidationErrorCode.Address.COUNTRY_LABEL_ERROR.name())
          .addPropertyNode("country")
          .addConstraintViolation();
    }
    return valid;
  }

  private boolean checkAdministrativeAreaByCountry(
      String countryLabel, String administrativeAreaLabel, ConstraintValidatorContext context) {
    var country = countryRepository.getByLabel(countryLabel);
    var countryCode = country.getIsoCode();

    final var valid = new AtomicBoolean(true);

    if (StringUtils.isNotBlank(administrativeAreaLabel)) {
      var areaOpt =
          administrativeAreaRepository.findByCountryCodeAndAdministrativeAreaLabel(
              countryCode, administrativeAreaLabel);

      areaOpt.ifPresentOrElse(
          area -> {
            var administrativeAreaCode = area.getIsoCode();
            if (!(isCountryWithoutArea(countryCode, administrativeAreaCode)
                || isCountryWithArea(countryCode, administrativeAreaCode))) {
              valid.set(false);
            }
          },
          () -> {
            valid.set(false);
          });
    }

    if (!valid.get()) {
      context.disableDefaultConstraintViolation();
      context
          .buildConstraintViolationWithTemplate(
              ValidationErrorCode.Address.ADMINISTRATIVE_AREA_LABEL_ERROR.name())
          .addPropertyNode("administrativeArea")
          .addConstraintViolation();
    }
    return valid.get();
  }

  private boolean isCountryWithArea(String countryCode, String administrativeAreaCode) {
    return nullOrEmpty(administrativeAreaCode)
        || (administrativeAreaRepository.hasAreaCodes(countryCode)
            && administrativeAreaRepository.existsAdministrativeAreaByCountryIsoCodeAndIsoCode(
                countryCode, administrativeAreaCode));
  }

  private boolean isCountryWithoutArea(String countryCode, String administrativeAreaCode) {
    return nullOrEmpty(administrativeAreaCode)
        && !administrativeAreaRepository.hasAreaCodes(countryCode);
  }
}
