package com.peter.importerservice.cucumber.web.rest;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;

import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import com.fasterxml.jackson.module.afterburner.AfterburnerModule;

@Slf4j
public class QimaTestUtil {
    public static final Long TEST_BRAND_ID = 0L;

    public static final ObjectMapper mapper = createObjectMapper();

    public static ObjectMapper createObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.ACCEPT_FLOAT_AS_INT, false);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.registerModules(
                new JavaTimeModule(), new Jdk8Module(), new Hibernate5Module(), new AfterburnerModule());
        // Customized properties w.r.t. Spring Boot
        // (https://docs.spring.io/spring-boot/docs/current-SNAPSHOT/reference/htmlsingle/#howto-customize-the-jackson-objectmapper)
        mapper.disable(MapperFeature.DEFAULT_VIEW_INCLUSION);
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        return mapper;
    }
}
