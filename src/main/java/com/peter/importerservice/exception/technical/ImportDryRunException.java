package com.peter.importerservice.exception.technical;

import com.peter.importerservice.service.importer.dto.ImportSummaryDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.util.Pair;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ImportDryRunException extends RuntimeException {

    private final ImportSummaryDTO importSummary;
    private final Map<Long, Set<Pair<String, String>>> errors;

    public ImportDryRunException(
            final ImportSummaryDTO importSummary, final Map<Long, Set<Pair<String, String>>> errors) {
        super(String.format("%d errors found on the current import", errors.size()));
        this.importSummary = importSummary;
        this.errors = errors;
    }

    public ImportSummaryDTO getImportSummary() {
        return importSummary;
    }

    public List<LineError> getErrors() {
        return errors.entrySet().stream()
                .map(
                        e -> {
                            var errors =
                                    e.getValue().stream()
                                            .map(pair -> new FieldError(pair.getFirst(), pair.getSecond()))
                                            .collect(Collectors.toList());

                            return new LineError(e.getKey(), errors);
                        })
                .collect(Collectors.toList());
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class LineError {
        private Long lineNumber;
        private List<FieldError> errors;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class FieldError {
        private String fieldName;
        private String error;
    }
}
