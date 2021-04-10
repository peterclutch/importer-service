package com.peter.importerservice.validator;

import com.peter.importerservice.repository.FactoryRepository;
import com.peter.importerservice.service.importer.dto.bo.FactoryImportBO;
import com.peter.importerservice.service.importer.validation.FactoryNameResourceValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.validation.ConstraintValidatorContext;
import javax.validation.ConstraintValidatorContext.ConstraintViolationBuilder;
import javax.validation.ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class FactoryNameResourceValidatorTest {

  @Mock ConstraintValidatorContext context;

  @Mock ConstraintViolationBuilder builder;

  @Mock NodeBuilderCustomizableContext nodeBuilder;

  @Mock FactoryRepository entityRepository;

  @InjectMocks private FactoryNameResourceValidator underTest;

  @DisplayName("Should thow an exception with less than 2 parameters")
  @Test
  void testWithLessParameters() {
    assertThrows(
        RuntimeException.class, () -> underTest.isValid(new Object[] {1L}, context));
  }

  @DisplayName("Should fail with a existing name for existing Factory")
  @Test
  void testWithExistingName() {
//    QimaTestUtil.setTestBrandUserSecurityContext();
    setContextBehaviour();
    final var factory = new FactoryImportBO().name("new");
    when(entityRepository.getName(anyLong())).thenReturn("old");
    when(entityRepository.existsByNameAndBrandId(anyString(), anyLong())).thenReturn(Boolean.TRUE);
    final var expectedValidation = underTest.isValid(new Object[] {3L, factory}, context);
    assertThat(expectedValidation).isFalse();
  }

  @DisplayName("Should valid with an identical name for existing Factory")
  @Test
  void testWithSameNameOfExistingFactory() {
//    QimaTestUtil.setTestBrandUserSecurityContext();
    final var factory = new FactoryImportBO().name("old");
    when(entityRepository.getName(anyLong())).thenReturn("old");
    final var expectedValidation = underTest.isValid(new Object[] {3L, factory}, context);
    assertThat(expectedValidation).isTrue();
  }

  private void setContextBehaviour() {
    when(context.buildConstraintViolationWithTemplate(anyString())).thenReturn(builder);
    when(builder.addPropertyNode(anyString())).thenReturn(nodeBuilder);
  }
}
