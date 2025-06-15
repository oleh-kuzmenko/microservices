package com.ol.app.accounts.it;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.hamcrest.core.Is.is;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.ol.app.accounts.infrastructure.persistence.entity.AccountEntity;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

class CustomerIntegrationTest extends AbstractIntegrationTest {

  @Test
  void shouldReturnCustomerByPhone() {
    var account = new AccountEntity();
    account.setName("David Morgan");
    account.setEmail("david11@gmail.com");
    account.setPhone("30999123341");
    account.setAccountType("SAVINGS");
    account.setAddress("London");
    account.setCreatedBy("system");
    accountJpaRepository.save(account);

    stubFor(WireMock.get(urlEqualTo("/api/v1/cards?phone=30999123341"))
        .willReturn(aResponse()
            .withStatus(200)
            .withHeader("Content-Type", "application/json")
            .withBody("""
                {
                  "id": 123,
                  "phone": "30999123341",
                  "cardNumber": "4111111111111111",
                  "cardType": "VISA",
                  "totalLimit": 5000,
                  "amountUsed": 1500,
                  "availableAmount": 3500,
                  "createdAt": "2024-06-10T12:34:56+00:00",
                  "createdBy": "admin",
                  "updatedAt": "2024-06-11T09:20:00+00:00",
                  "updatedBy": "user1"
                }""")));

    stubFor(WireMock.get(urlEqualTo("/api/v1/loans?phone=30999123341"))
        .willReturn(aResponse()
            .withStatus(200)
            .withHeader("Content-Type", "application/json")
            .withBody("""
                {
                   "id": 101,
                   "phone": "30999123341",
                   "loanNumber": "LN-20240610-001",
                   "loanType": "PERSONAL",
                   "totalLoan": 10000,
                   "amountPaid": 2500,
                   "address": "123 Main St, Springfield",
                   "createdAt": "2024-06-10T12:34:56+00:00",
                   "createdBy": "admin",
                   "updatedAt": "2024-06-11T09:20:00+00:00",
                   "updatedBy": "user1"
                 }""")));

    buildRestAssured().when()
        .contentType(APPLICATION_JSON_VALUE)
        .queryParam("phone", "30999123341")
        .get("/api/v1/customers")
        .then().assertThat().log().all()
        .statusCode(HttpStatus.OK.value())
        .body("name", is("David Morgan"))
        .body("email", is("david11@gmail.com"))
        .body("phone", is("30999123341"))
        .body("address", is("London"))
        .body("account_type", is("SAVINGS"))
        .body("card_number", is("4111111111111111"))
        .body("card_type", is("VISA"))
        .body("total_limit", is(5000.0f))
        .body("amount_used", is(1500.0f))
        .body("available_amount", is(3500.0f))
        .body("loan_number", is("LN-20240610-001"))
        .body("loan_type", is("PERSONAL"))
        .body("total_loan", is(10000.0f))
        .body("amount_paid", is(2500.0f));
  }
}
