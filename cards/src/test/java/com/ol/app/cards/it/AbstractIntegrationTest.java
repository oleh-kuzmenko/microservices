package com.ol.app.cards.it;


import com.ol.app.cards.dao.CardRepository;
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
      .withDatabaseName("cards_db")
      .withUsername("dev")
      .withPassword("pass")
      .withExposedPorts(5432)
      .waitingFor(Wait.forListeningPorts());

  static {
    POSTGRES.start();
    System.setProperty("CARDS_DB_PORT", POSTGRES.getMappedPort(5432).toString());
  }

  @Autowired
  protected CardRepository cardRepository;

  @BeforeEach
  void reset() {
    cardRepository.deleteAll();
  }

  protected RequestSpecification buildRestAssured() {
    return RestAssured.given().port(port);
  }
}
