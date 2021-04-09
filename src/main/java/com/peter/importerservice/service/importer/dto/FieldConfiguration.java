package com.peter.importerservice.service.importer.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
public class FieldConfiguration implements Comparable<FieldConfiguration> {

    @NotNull
    @NotBlank
    private String fieldName;

    @Min(1)
    @NotNull
    private Integer position;

    private Boolean isRequired;

    public FieldConfiguration(
            @NotNull @NotBlank String fieldName, @Min(1) @NotNull Integer position) {
        this.fieldName = fieldName;
        this.position = position;
    }

    @Override
    public int compareTo(FieldConfiguration o) {
        return Objects.compare(this.getPosition(), o.getPosition(), Integer::compareTo);
    }
}
