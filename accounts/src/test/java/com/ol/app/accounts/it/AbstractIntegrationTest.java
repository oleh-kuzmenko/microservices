package com.ol.app.accounts.it;


import com.ol.app.accounts.infrastructure.persistence.repository.AccountJpaRepository;
import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;

@ActiveProfiles("it")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class AbstractIntegrationTest {

  @LocalServerPort
  protected int port;

  public static GenericContainer POSTGRES = new PostgreSQLContainer("postgres:14-alpine")
      .withDatabaseName("accounts_db")
      .withUsername("dev")
      .withPassword("pass")
      .withExposedPorts(5432)
      .waitingFor(Wait.forListeningPorts());

  static {
    POSTGRES.start();
    System.setProperty("ACCOUNTS_DB_PORT", POSTGRES.getMappedPort(5432).toString());
  }

  @Autowired
  protected AccountJpaRepository accountJpaRepository;

  @BeforeEach
  void reset() {
    accountJpaRepository.deleteAll();
  }

  protected RequestSpecification buildRestAssured() {
    return RestAssured.given().port(port);
  }
}
