package com.peter.importerservice.validator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidatorContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AllowedMimeTypeValidatorTest {

    @Mock
    private AllowedMimeType allowedMimeType;
    private AllowedMimeTypeValidator cut;
    @Mock private ConstraintValidatorContext context;

    @Mock
    ConstraintValidatorContext.ConstraintViolationBuilder builder;

    @Mock
    ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext nodeBuilder;

    @BeforeEach
    void setup() {
        cut = new AllowedMimeTypeValidator();
        cut.initialize(allowedMimeType);
    }

    @DisplayName("When annotation is used without value, no file is allowed")
    @Test
    public void noAllowedMimeType() {
        when(allowedMimeType.value()).thenReturn(new String[] {});
        setContextBehaviour();
        assertThat(cut.isValid(pngFile(), context)).isFalse();
        assertThat(cut.isValid(jsonFile(), context)).isFalse();
        assertThat(cut.isValid(pdfFile(), context)).isFalse();
    }

    @DisplayName("When annotation is used with a value, only file with this type is allowed")
    @Test
    public void pdfAllowedMimeType() {
        when(allowedMimeType.value()).thenReturn(new String[] {"application/pdf"});
        setContextBehaviour();
        assertThat(cut.isValid(pngFile(), context)).isFalse();
        assertThat(cut.isValid(jsonFile(), context)).isFalse();
        assertThat(cut.isValid(pdfFile(), context)).isTrue();
    }

    @DisplayName(
            "When annotation is used with multiple values, only files with those types are allowed")
    @Test
    public void multipleAllowedMimeType() {
        when(allowedMimeType.value()).thenReturn(new String[] {"application/pdf", "image/png"});
        setContextBehaviour();
        assertThat(cut.isValid(pngFile(), context)).isTrue();
        assertThat(cut.isValid(jsonFile(), context)).isFalse();
        assertThat(cut.isValid(pdfFile(), context)).isTrue();
    }

    @DisplayName("When the file is null, then it is valid")
    @Test
    public void emptyMultipartFileIsValid() {
        assertThat(cut.isValid(null, context)).isTrue();
    }

    private MultipartFile pngFile() {
        MultipartFile pngFile = mock(MultipartFile.class);
        when(pngFile.getContentType()).thenReturn(MediaType.IMAGE_PNG_VALUE);
        return pngFile;
    }

    private MultipartFile jsonFile() {
        MultipartFile jsonFile = mock(MultipartFile.class);
        when(jsonFile.getContentType()).thenReturn(MediaType.APPLICATION_JSON_VALUE);
        return jsonFile;
    }

    private MultipartFile pdfFile() {
        MultipartFile jsonFile = mock(MultipartFile.class);
        when(jsonFile.getContentType()).thenReturn(MediaType.APPLICATION_PDF_VALUE);
        return jsonFile;
    }

    private void setContextBehaviour() {
        when(context.buildConstraintViolationWithTemplate(anyString())).thenReturn(builder);
        when(builder.addPropertyNode(anyString())).thenReturn(nodeBuilder);
    }
}
