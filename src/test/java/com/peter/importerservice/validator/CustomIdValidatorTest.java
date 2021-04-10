package com.peter.importerservice.validator;

import com.peter.importerservice.repository.FactoryRepository;
import com.peter.importerservice.service.importer.validation.CustomIdConstraint;
import com.peter.importerservice.service.importer.validation.CustomIdValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.validation.ConstraintValidatorContext;
import javax.validation.ConstraintValidatorContext.ConstraintViolationBuilder;
import javax.validation.ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class CustomIdValidatorTest {

  @Mock ConstraintValidatorContext context;

  @Mock ConstraintViolationBuilder builder;

  @Mock NodeBuilderCustomizableContext nodeBuilder;

  @Mock private FactoryRepository factoryRepository;

  @InjectMocks private CustomIdValidator underTest;

  @DisplayName("Should valid with a null value")
  @Test
  void testWithUndefinedTarget() {
    assertThat(underTest.isValid(null, context)).isTrue();
  }

  @DisplayName("Should valid with a new Custom ID")
  @Test
  void testAddNewCustomId() {
//    QimaTestUtil.setTestBrandUserSecurityContext();
    var actualConstraint =
        new CustomIdConstraint() {

          @Override
          public Long getEntityId() {
            return 3L;
          }

          @Override
          public String getCustomId() {
            return "PO5441332";
          }
        };

    when(factoryRepository.getCustomId(anyLong(), anyLong())).thenReturn(null);
    var actualValidation = underTest.isValid(actualConstraint, context);
    assertThat(actualValidation).isTrue();
  }

  @DisplayName("Should valid updating Custom ID with unknown one")
  @Test
  void testUpdateWithUnknownCustomId() {
//    QimaTestUtil.setTestBrandUserSecurityContext();
    var actualConstraint =
        new CustomIdConstraint() {

          @Override
          public Long getEntityId() {
            return 3L;
          }

          @Override
          public String getCustomId() {
            return "PO5441332";
          }
        };

    when(factoryRepository.getCustomId(anyLong(), anyLong())).thenReturn("PJ5441332");
    when(factoryRepository.existsByCustomIdAndBrandId(anyString(), anyLong()))
        .thenReturn(Boolean.FALSE);
    var actualValidation = underTest.isValid(actualConstraint, context);
    assertThat(actualValidation).isTrue();
  }

  @DisplayName("Should fail updating Custum ID with known one")
  @Test
  void testUpdateWithKnownCustomId() {
//    QimaTestUtil.setTestBrandUserSecurityContext();
    setContextBehaviour();
    var actualConstraint =
        new CustomIdConstraint() {

          @Override
          public Long getEntityId() {
            return 3L;
          }

          @Override
          public String getCustomId() {
            return "PO5441332";
          }
        };

    when(factoryRepository.getCustomId(anyLong(), anyLong())).thenReturn("PJ5441332");
    when(factoryRepository.existsByCustomIdAndBrandId(anyString(), anyLong()))
        .thenReturn(Boolean.TRUE);
    var actualValidation = underTest.isValid(actualConstraint, context);
    assertThat(actualValidation).isFalse();
    assertContext();
  }

  @DisplayName("Should valid keeping Custom ID")
  @Test
  void testWithSameCustomId() {
//    QimaTestUtil.setTestBrandUserSecurityContext();
    var actualConstraint =
        new CustomIdConstraint() {

          @Override
          public Long getEntityId() {
            return 3L;
          }

          @Override
          public String getCustomId() {
            return "PO5441332";
          }
        };

    when(factoryRepository.getCustomId(anyLong(), anyLong())).thenReturn("PO5441332");
    var actualValidation = underTest.isValid(actualConstraint, context);
    assertThat(actualValidation).isTrue();
  }

  @DisplayName("Should valid removing Custom ID")
  @Test
  void testRemoveCustomId() {
//    QimaTestUtil.setTestBrandUserSecurityContext();
    var actualConstraint =
        new CustomIdConstraint() {

          @Override
          public Long getEntityId() {
            return 3L;
          }

          @Override
          public String getCustomId() {
            return null;
          }
        };

    var actualValidation = underTest.isValid(actualConstraint, context);
    assertThat(actualValidation).isTrue();
  }

  @DisplayName("Should valid with new Custom ID")
  @Test
  void testNewCustomId() {
//    QimaTestUtil.setTestBrandUserSecurityContext();
    var actualConstraint =
        new CustomIdConstraint() {

          @Override
          public Long getEntityId() {
            return null;
          }

          @Override
          public String getCustomId() {
            return "PO5441332";
          }
        };

    when(factoryRepository.existsByCustomIdAndBrandId(anyString(), anyLong()))
        .thenReturn(Boolean.FALSE);
    var actualValidation = underTest.isValid(actualConstraint, context);
    assertThat(actualValidation).isTrue();
  }

  @DisplayName("Should fail with new known Custom ID")
  @Test
  void testNewKnownCustomId() {
    setContextBehaviour();
    var actualConstraint =
        new CustomIdConstraint() {

          @Override
          public Long getEntityId() {
            return null;
          }

          @Override
          public String getCustomId() {
            return "PO5441332";
          }
        };

    when(factoryRepository.existsByCustomIdAndBrandId(anyString(), anyLong()))
        .thenReturn(Boolean.TRUE);
    var actualValidation = underTest.isValid(actualConstraint, context);
    assertThat(actualValidation).isFalse();
    assertContext();
  }

  private void setContextBehaviour() {
    when(context.buildConstraintViolationWithTemplate(anyString())).thenReturn(builder);
    when(builder.addPropertyNode(anyString())).thenReturn(nodeBuilder);
  }

  private void assertContext() {
    verify(context, times(1)).buildConstraintViolationWithTemplate(anyString());
    verify(builder, times(1)).addPropertyNode(anyString());
    verify(nodeBuilder, times(1)).addConstraintViolation();
  }
}
