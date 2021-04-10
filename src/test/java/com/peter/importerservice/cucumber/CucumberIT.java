package com.peter.importerservice.cucumber;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        plugin = "pretty",
        features = "src/test/features",
        strict = true,
        tags = {"not @ignore"})
public class CucumberIT {}
