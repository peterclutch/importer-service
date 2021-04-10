package com.peter.importerservice.cucumber.context;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.ResourceHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestClientException;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import static java.lang.String.format;

@Component
@Slf4j
public class World {

  private TestRestTemplate restTemplate;

  private ScenarioContext<ScenarioContextKeys> scenarioContext;

  private String baseUrl;

  private final MappingJackson2HttpMessageConverter jacksonMessageConverter;
  private final ResourceHttpMessageConverter resourceHttpMessageConverter;
  private final FormHttpMessageConverter formHttpMessageConverter;

  public World(
      MappingJackson2HttpMessageConverter jacksonMessageConverter,
      FormHttpMessageConverter formHttpMessageConverter) {

    this.resourceHttpMessageConverter = new ResourceHttpMessageConverter();
    this.jacksonMessageConverter = jacksonMessageConverter;
    this.formHttpMessageConverter = formHttpMessageConverter;
    reinit();
  }

  public TestRestTemplate getAnonymousRestTemplate() {
    return configureRestTemplate();
  }

  public TestRestTemplate getMultipartTestRestTemplate() {
    restTemplate.getRestTemplate().getMessageConverters().add(formHttpMessageConverter);
    return restTemplate;
  }

  public TestRestTemplate getTestRestTemplate() {
    return restTemplate;
  }

  public Object get(ScenarioContextKeys key) {
    return scenarioContext.getContext(key);
  }

  public <T> T get(final ScenarioContextKeys key, final Class<T> clazz) {
    return clazz.cast(this.get(key));
  }

  public void set(ScenarioContextKeys key, Object value) {
    scenarioContext.setContext(key, value);
  }

  public void reinit(String baseUrl) {
    this.reinit();
    this.baseUrl = baseUrl;
  }

  public void reinit() {
    this.restTemplate = configureRestTemplate();
    this.scenarioContext = new ScenarioContext<>();
  }

  public String getBaseUrl() {
    return baseUrl;
  }

  private TestRestTemplate configureRestTemplate() {
    TestRestTemplate testRestTemplate = new TestRestTemplate();
    testRestTemplate
        .getRestTemplate()
        .setMessageConverters(
            List.of(
                jacksonMessageConverter, resourceHttpMessageConverter, formHttpMessageConverter));
    testRestTemplate.getRestTemplate().setErrorHandler(customResponseErrorHandler());

    return testRestTemplate;
  }

  private ResponseErrorHandler customResponseErrorHandler() {
    return new ResponseErrorHandler() {
      @Override
      public boolean hasError(ClientHttpResponse response) throws IOException {
        return !response.getStatusCode().is2xxSuccessful();
      }

      @Override
      public void handleError(ClientHttpResponse response) throws IOException {
        final var uuid = UUID.randomUUID();
        var exception =
            HttpClientErrorException.create(
                response.getStatusCode(),
                response.getStatusText() + ", error uuid: " + uuid,
                response.getHeaders(),
                response.getBody().readAllBytes(),
                null);

        log.error("HTTP error[uuid: {}, body: {}]", uuid, exception.getResponseBodyAsString());
        throw exception;
      }
    };
  }

  public <T> T getFromApi(
      final Class<T> type, final String pathTemplate, final Object... pathParameters)
      throws RestClientException {
    return restTemplate.getForObject(format(baseUrl + pathTemplate, pathParameters), type);
  }

  public <T> ResponseEntity<T> getEntityFromApi(
      final ParameterizedTypeReference<T> type,
      final String pathTemplate,
      final Object... pathParameters)
      throws RestClientException {
    try {
      return restTemplate.exchange(
          format(baseUrl + pathTemplate, pathParameters), HttpMethod.GET, null, type);
    } catch (final HttpClientErrorException hceEx) {
      return new ResponseEntity<>(hceEx.getStatusCode());
    }
  }

  public <T> ResponseEntity<T> getEntityFromApi(
      final Class<T> type, final String pathTemplate, final Object... pathParameters) {
    try {
      return restTemplate.getForEntity(format(baseUrl + pathTemplate, pathParameters), type);
    } catch (final HttpClientErrorException hceEx) {
      return new ResponseEntity<>(hceEx.getStatusCode());
    }
  }
}
