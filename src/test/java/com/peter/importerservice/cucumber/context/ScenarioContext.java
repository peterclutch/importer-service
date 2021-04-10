package com.peter.importerservice.cucumber.context;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ScenarioContext<T extends Enum> {

  private Map<String, Object> scenarioContext;

  public ScenarioContext() {
    scenarioContext = new HashMap<>();
  }

  public void setContext(T key, Object value) {
    scenarioContext.put(key.toString(), value);
  }

  public Object getContext(T key) {
    return scenarioContext.get(key.toString());
  }

  public Boolean contains(T key) {
    return scenarioContext.containsKey(key.toString());
  }
}
