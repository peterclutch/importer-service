package com.peter.importerservice.cucumber.config;

import com.peter.importerservice.ImporterServiceApplication;
import com.peter.importerservice.cucumber.context.World;
import cucumber.api.java.Before;
import lombok.extern.slf4j.Slf4j;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.support.AllEncompassingFormHttpMessageConverter;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
@ComponentScan(basePackages = {"com.peter.importerservice.cucumber"})
//@EnableJpaRepositories(value = {"com.peter.importerservice.cucumber.repository"})
@Slf4j
public class CucumberContextConfiguration {

  private final String SERVER_URL = "http://localhost";

  @Autowired private World world;

  @LocalServerPort protected int port;

  @Before
  public void configure() {
    world.reinit(SERVER_URL + ":" + port);
  }

  @TestConfiguration
  static class MultipartConfiguration {
    @Bean
    public FormHttpMessageConverter multipartConverter() {
      return new AllEncompassingFormHttpMessageConverter();
    }
  }

//  @TestConfiguration
//  static class LiquibaseConfiguration {
//
//    @Primary
//    @DependsOn("dataSourceCustom")
//    @Bean(name = "liquibaseBean")
//    public Liquibase liquibase(DataSource dataSource) throws SQLException, LiquibaseException {
//      Database database =
//          DatabaseFactory.getInstance()
//              .findCorrectDatabaseImplementation(new JdbcConnection(dataSource.getConnection()));
//      Liquibase liquibase =
//          new Liquibase("config/liquibase/master.xml", new ClassLoaderResourceAccessor(), database);
//      liquibase.update("test");
//      return liquibase;
//    }
//  }

}
