package com.ol.app.accounts.it;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.matchesPattern;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.Is.is;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.ol.app.accounts.infrastructure.persistence.entity.AccountEntity;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

class AccountIntegrationTest extends AbstractIntegrationTest {

  @Test
  void shouldCreateAccount() {
    buildRestAssured().when()
        .contentType(APPLICATION_JSON_VALUE)
        .body("""
            {
              "name": "David",
              "email": "david11@gmail.com",
              "phone": "30999123341",
              "account_type": "SAVINGS",
              "address": "London"
            }""")
        .post("/api/v1/accounts")
        .then().assertThat().log().all()
        .statusCode(HttpStatus.CREATED.value())
        .header("Location", matchesPattern("http://localhost:%d/api/v1/accounts/\\d+".formatted(port)));

    assertThat(accountJpaRepository.findAll()).hasSize(1)
        .first()
        .satisfies(customer -> assertThat(customer.getId()).isNotNull())
        .satisfies(customer -> assertThat(customer.getCreatedAt()).isNotNull())
        .extracting(AccountEntity::getName, AccountEntity::getEmail, AccountEntity::getPhone, AccountEntity::getAccountType,
            AccountEntity::getAddress, AccountEntity::getCreatedBy)
        .containsExactly("David", "david11@gmail.com", "30999123341", "SAVINGS", "London", "system");
  }

  @Test
  void shouldNotCreateCustomerWhenEmailNotUnique() {
    var account = new AccountEntity();
    account.setName("David Morgan");
    account.setEmail("david11@gmail.com");
    account.setPhone("30999123341");
    account.setAccountType("SAVINGS");
    account.setAddress("London");
    account.setCreatedBy("system");
    accountJpaRepository.save(account);

    buildRestAssured().when()
        .contentType(APPLICATION_JSON_VALUE)
        .body("""
            {
              "name": "David",
              "email": "david11@gmail.com",
              "phone": "30999123341",
              "account_type": "SAVINGS",
              "address": "London"
            }""")
        .post("/api/v1/accounts")
        .then().assertThat().log().all()
        .statusCode(HttpStatus.BAD_REQUEST.value())
        .body("timestamp", is(notNullValue()))
        .body("status", is(400))
        .body("path", is("/api/v1/accounts"))
        .body("error", is("Account with such email or phone already exists"));

    assertThat(accountJpaRepository.findAll()).size().isEqualTo(1);
  }

  @Test
  void shouldNotCreateAccountWhenNameIsBlank() {
    buildRestAssured().when()
        .contentType(APPLICATION_JSON_VALUE)
        .body("""
            {
              "name": "",
              "email": "david11@gmail.com",
              "phone": "30999123341",
              "account_type": "SAVINGS",
              "address": "London"
            }""")
        .post("/api/v1/accounts")
        .then().assertThat().log().all()
        .statusCode(HttpStatus.BAD_REQUEST.value())
        .body("timestamp", is(notNullValue()))
        .body("status", is(400))
        .body("path", is("/api/v1/accounts"))
        .body("error", is("Attribute 'name' error: size must be between 2 and 50"));

    assertThat(accountJpaRepository.findAll()).isEmpty();
  }

  @Test
  void shouldReturnAccountById() {
    var account = new AccountEntity();
    account.setName("David Morgan");
    account.setEmail("david11@gmail.com");
    account.setPhone("30999123341");
    account.setAccountType("SAVINGS");
    account.setAddress("London");
    account.setCreatedBy("system");
    var savedAccount = accountJpaRepository.save(account);

    buildRestAssured().when()
        .contentType(APPLICATION_JSON_VALUE)
        .get("/api/v1/accounts/%d".formatted(savedAccount.getId()))
        .then().assertThat().log().all()
        .statusCode(HttpStatus.OK.value())
        .body("created_at", is(notNullValue()))
        .body("name", is("David Morgan"))
        .body("email", is("david11@gmail.com"))
        .body("phone", is("30999123341"))
        .body("address", is("London"))
        .body("account_type", is("SAVINGS"));
  }

  @Test
  void shouldReturnAccountByEmail() {
    var account = new AccountEntity();
    account.setName("David Morgan");
    account.setEmail("david11@gmail.com");
    account.setPhone("30999123341");
    account.setAccountType("SAVINGS");
    account.setAddress("London");
    account.setCreatedBy("system");
    accountJpaRepository.save(account);

    buildRestAssured().when()
        .contentType(APPLICATION_JSON_VALUE)
        .queryParam("email", "david11@gmail.com")
        .get("/api/v1/accounts")
        .then().assertThat().log().all()
        .statusCode(HttpStatus.OK.value())
        .body("created_at", is(notNullValue()))
        .body("name", is("David Morgan"))
        .body("email", is("david11@gmail.com"))
        .body("phone", is("30999123341"))
        .body("address", is("London"))
        .body("account_type", is("SAVINGS"));
  }

  @Test
  void shouldReturnBadRequestForInvalidEmail() {
    buildRestAssured().when()
        .contentType(APPLICATION_JSON_VALUE)
        .queryParam("email", "invalidEmail")
        .get("/api/v1/accounts")
        .then().assertThat().log().all()
        .statusCode(HttpStatus.BAD_REQUEST.value())
        .body("timestamp", is(notNullValue()))
        .body("status", is(400))
        .body("path", is("/api/v1/accounts"))
        .body("error", is(notNullValue()));
  }

  @Test
  void shouldReturnAccountNotFound() {
    buildRestAssured().when()
        .contentType(APPLICATION_JSON_VALUE)
        .queryParam("email", "david11@gmail.com")
        .get("/api/v1/accounts")
        .then().assertThat().log().all()
        .statusCode(HttpStatus.NOT_FOUND.value())
        .body("timestamp", is(notNullValue()))
        .body("status", is(404))
        .body("path", is("/api/v1/accounts"))
        .body("error", is("Account not found"));
  }

  @Test
  void shouldDeleteAccountById() {
    var account = new AccountEntity();
    account.setName("David Morgan");
    account.setEmail("david11@gmail.com");
    account.setPhone("30999123341");
    account.setAccountType("SAVINGS");
    account.setAddress("London");
    account.setCreatedBy("system");
    var savedAccount = accountJpaRepository.save(account);

    buildRestAssured().when()
        .contentType(APPLICATION_JSON_VALUE)
        .delete("/api/v1/accounts/%d".formatted(savedAccount.getId()))
        .then().assertThat().log().all()
        .statusCode(HttpStatus.NO_CONTENT.value());
  }

  @Test
  void shouldReturnNoContentWhenDeleteNotExistedAccountById() {
    buildRestAssured().when()
        .contentType(APPLICATION_JSON_VALUE)
        .delete("/api/v1/accounts/7")
        .then().assertThat().log().all()
        .statusCode(HttpStatus.NO_CONTENT.value());
  }

  @Test
  void shouldReturnNotFoundWhenUpdateNotExistedAccountById() {
    buildRestAssured().when()
        .contentType(APPLICATION_JSON_VALUE)
        .body("""
            {
              "name": "Felipe",
              "email": "david11@gmail.com",
              "phone": "30999123341",
              "account_type": "SAVINGS",
              "address": "London"
            }""")
        .put("/api/v1/accounts/7")
        .then().assertThat().log().all()
        .statusCode(HttpStatus.NOT_FOUND.value())
        .body("timestamp", is(notNullValue()))
        .body("status", is(404))
        .body("path", is("/api/v1/accounts/7"))
        .body("error", is("Account not found"));
  }

  @Test
  void shouldUpdateAccountById() {
    var account = new AccountEntity();
    account.setName("David Morgan");
    account.setEmail("david11@gmail.com");
    account.setPhone("30999123341");
    account.setAccountType("SAVINGS");
    account.setAddress("London");
    account.setCreatedBy("system");
    var saved = accountJpaRepository.save(account);

    buildRestAssured().when()
        .contentType(APPLICATION_JSON_VALUE)
        .body("""
            {
              "name": "Felipe",
              "email": "felipe@gmail.com",
              "phone": "30999111111",
              "account_type": "SAVINGS",
              "address": "Madrid"
            }""")
        .put("/api/v1/accounts/%d".formatted(saved.getId()))
        .then().assertThat().log().all()
        .statusCode(HttpStatus.NO_CONTENT.value());

    assertThat(accountJpaRepository.findAll()).hasSize(1)
        .first()
        .satisfies(customer -> assertThat(customer.getId()).isNotNull())
        .satisfies(customer -> assertThat(customer.getCreatedAt()).isNotNull())
        .satisfies(customer -> assertThat(customer.getUpdatedAt()).isNotNull())
        .extracting(AccountEntity::getName, AccountEntity::getEmail, AccountEntity::getPhone, AccountEntity::getAccountType,
            AccountEntity::getAddress, AccountEntity::getCreatedBy, AccountEntity::getUpdatedBy)
        .containsExactly("Felipe", "felipe@gmail.com", "30999111111", "SAVINGS", "Madrid", "system", "system");
  }
}
