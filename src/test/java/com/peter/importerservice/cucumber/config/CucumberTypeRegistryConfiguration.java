package com.peter.importerservice.cucumber.config;

import com.peter.importerservice.cucumber.context.ScenarioContextKeys;
import com.peter.importerservice.service.importer.ImportType;
import cucumber.api.TypeRegistry;
import cucumber.api.TypeRegistryConfigurer;
import io.cucumber.cucumberexpressions.ParameterType;
import io.cucumber.cucumberexpressions.Transformer;
import org.springframework.http.HttpStatus;

import java.util.Locale;

import static java.util.Locale.ENGLISH;

// Used by Cucumber
public class CucumberTypeRegistryConfiguration implements TypeRegistryConfigurer {

  @Override
  public Locale locale() {
    return ENGLISH;
  }

  @Override
  public void configureTypeRegistry(TypeRegistry typeRegistry) {
    typeRegistry.defineParameterType(
        new ParameterType<>(
            "ImportType", // name
            ".*?", // regexp
            ImportType.class,
            (String s) -> ImportType.valueOf(s.toUpperCase())));

    typeRegistry.defineParameterType(
        new ParameterType<>(
            "HttpStatus", // name
            ".*?", // regexp
            HttpStatus.class, // type
            (String s) -> HttpStatus.valueOf(s.toUpperCase())));

    typeRegistry.defineParameterType(
        new ParameterType<>(
            "responseState", // name
            "failed|succeeded|conflicted", // regexp
            String.class, // type
            (Transformer<String>) String::toLowerCase // transformer function
            ));

    typeRegistry.defineParameterType(
        new ParameterType<>(
            "char", // name
            "\\w{1}", // regexp
            Character.class, // type
            (String s) -> s.charAt(0)));

    typeRegistry.defineParameterType(
        new ParameterType<>(
            "ScenarioContextKeys", // name
            ".*?", // regexp
            ScenarioContextKeys.class, // type
            (String s) -> ScenarioContextKeys.valueOf(s.toUpperCase())));

    typeRegistry.defineParameterType(
        new ParameterType<>(
            "boolean", // name
            "(true|false)", // regexp
            Boolean.class, // type
                (Transformer<Boolean>) Boolean::valueOf));
  }

}
