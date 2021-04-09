package com.peter.importerservice.service.importer.util;

import com.peter.importerservice.service.importer.ImportType;

import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Optional;
import java.util.StringTokenizer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class ImportTypeResolverUtils {

    public Optional<ImportType> getImportTypeFromUrl() {

        var types =
                Stream.of(ImportType.values()).collect(Collectors.toMap(Enum::name, type -> type));

        var curRequest =
                ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

        var tokenizer = new StringTokenizer(curRequest.getServletPath(), "/", false);
        while (tokenizer.hasMoreTokens()) {
            var token = tokenizer.nextToken();
            if (types.containsKey(token)) {
                return Optional.of(types.get(token));
            }
        }
        return Optional.empty();
    }
}
