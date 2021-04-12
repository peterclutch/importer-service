package com.peter.importerservice.validator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.validation.ConstraintValidatorContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GtinValidationTest {

  @Mock private ConstraintValidatorContext context;

  @Mock private ConstraintValidatorContext.ConstraintViolationBuilder builder;

  @Mock
  private ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext node;

  @Mock private ConstraintValidatorContext.ConstraintViolationBuilder.NodeContextBuilder iterable;

  @Mock private ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderDefinedContext key;

  private GtinValidation validator;

  private static final String GOOD_GTIN = "6291041500213";
  private static final String BAD_GTIN = "6291041500215";

  private static final String GOOD_GTIN_12 = "614141987658";
  private static final String BAD_GTIN_12 = "614141987653";

  private static final String WRONG_CHAR_GTIN = "47348…324723";

  @BeforeEach
  public void setup() {
    validator = new GtinValidation();
  }

  private void buildContextMock() {
    when(context.buildConstraintViolationWithTemplate(anyString())).thenReturn(builder);
    when(builder.addPropertyNode(null)).thenReturn(node);
    when(node.inIterable()).thenReturn(iterable);
    when(iterable.atKey(anyString())).thenReturn(key);
  }

  @Test
  public void must_validate_if_field_is_null() {
    assertThat(validator.isValid(null, context)).isTrue();
  }

  @Test
  public void must_validate_if_field_is_empty() {
    assertThat(validator.isValid("", context)).isTrue();
  }

  @Test
  public void must_not_validate_if_field_is_not_string_type() {
    buildContextMock();
    assertThat(validator.isValid(50D, context)).isFalse();
  }

  @Test
  public void must_validate_if_gtin_is_correct() {
    assertThat(validator.isValid(GOOD_GTIN, context)).isTrue();
  }

  @Test
  public void must_not_validate_if_gtin_is_incorrect() {
    buildContextMock();
    assertThat(validator.isValid(BAD_GTIN, context)).isFalse();
  }

  @Test
  public void must_not_validate_if_gtin_is_contains_non_alphanmerical_character() {
    buildContextMock();
    assertThat(validator.isValid(WRONG_CHAR_GTIN, context)).isFalse();
  }

  @Test
  public void must_validate_if_gtin_12_is_correct() {
    assertThat(validator.isValid(GOOD_GTIN_12, context)).isTrue();
  }

  @Test
  public void must_not_validate_if_gtin_12_is_incorrect() {
    buildContextMock();
    assertThat(validator.isValid(BAD_GTIN_12, context)).isFalse();
  }
}
