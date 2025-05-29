package com.ol.app.cards.it;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.matchesPattern;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.Is.is;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.ol.app.cards.dao.CardEntity;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

class CardIntegrationTest extends AbstractIntegrationTest {

  @Test
  void shouldCreateCard() {
    buildRestAssured().when()
        .contentType(APPLICATION_JSON_VALUE)
        .body("""
            {
              "phone": "0999123341",
              "cardNumber": "4532123456788",
              "cardType": "CREDIT",
              "totalLimit": 10000,
              "amountUsed": 2500,
              "availableAmount": 7500
            }""")
        .post("/api/v1/cards")
        .then().assertThat().log().all()
        .statusCode(HttpStatus.CREATED.value())
        .header("Location", matchesPattern("http://localhost:%d/api/v1/cards/\\d+".formatted(port)));

    assertThat(cardRepository.findAll()).hasSize(1)
        .first()
        .satisfies(card -> assertThat(card.getId()).isNotNull())
        .extracting(CardEntity::getPhone, CardEntity::getCardNumber, CardEntity::getCardType, CardEntity::getTotalLimit,
            CardEntity::getAmountUsed, CardEntity::getAvailableAmount, CardEntity::getCreatedBy)
        .containsExactly("0999123341", "4532123456788", "CREDIT", 10000, 2500, 7500, "system");
  }
  
  @Test
  void shouldReturnBadRequestForInvalidAttributes() {
    buildRestAssured().when()
        .contentType(APPLICATION_JSON_VALUE)
        .body("""
            {
              "phone": "111",
              "cardNumber": "4532123456788",
              "cardType": "CREDIT",
              "totalLimit": -121,
              "amountUsed": -100,
              "availableAmount": -21
            }""")
        .post("/api/v1/cards")
        .then().assertThat().log().all()
        .statusCode(HttpStatus.BAD_REQUEST.value())
        .body("timestamp", is(notNullValue()))
        .body("status", is(400))
        .body("path", is("/api/v1/cards"))
        .body("errors", hasItems(
            "Phone number must be exactly 10 digits",
            "Total limit must be zero or positive",
            "Amount used must be zero or positive",
            "Available amount must be zero or positive"
        ));
  }

  @Test
  void shouldReturnConflictForNotUniquePhone() {
    CardEntity card = new CardEntity();
    card.setPhone("0999123340");
    card.setCardNumber("4532123456788");
    card.setCardType("CREDIT");
    card.setTotalLimit(10000);
    card.setAmountUsed(2500);
    card.setAvailableAmount(7500);
    card.setCreatedBy("system");
    cardRepository.save(card);

    buildRestAssured().when()
        .contentType(APPLICATION_JSON_VALUE)
        .body("""
            {
              "phone": "0999123340",
              "cardNumber": "4532123456789",
              "cardType": "CREDIT",
              "totalLimit": 10000,
              "amountUsed": 2500,
              "availableAmount": 7500
            }""")
        .post("/api/v1/cards")
        .then().assertThat().log().all()
        .statusCode(HttpStatus.CONFLICT.value())
        .body("timestamp", is(notNullValue()))
        .body("status", is(409))
        .body("path", is("/api/v1/cards"))
        .body("error", is("Card with provided phone already exists"));
  }

  @Test
  void shouldUpdateCard() {
    CardEntity card = new CardEntity();
    card.setPhone("0999123340");
    card.setCardNumber("4532123456788");
    card.setCardType("CREDIT");
    card.setTotalLimit(10000);
    card.setAmountUsed(2500);
    card.setAvailableAmount(7500);
    card.setCreatedBy("system");
    Long cardId = cardRepository.save(card).getId();

    buildRestAssured().when()
        .contentType(APPLICATION_JSON_VALUE)
        .body("""
            {
              "phone": "0999123341",
              "cardNumber": "4532123456789",
              "cardType": "PREMIUM",
              "totalLimit": 100000,
              "amountUsed": 20000,
              "availableAmount": 80000
            }""")
        .put("/api/v1/cards/%d".formatted(cardId))
        .then().assertThat().log().all()
        .statusCode(HttpStatus.NO_CONTENT.value());

    assertThat(cardRepository.findById(cardId)).isPresent()
        .get()
        .satisfies(cardEntity -> assertThat(cardEntity.getPhone()).isEqualTo("0999123341"))
        .satisfies(cardEntity -> assertThat(cardEntity.getCardNumber()).isEqualTo("4532123456789"))
        .satisfies(cardEntity -> assertThat(cardEntity.getCardType()).isEqualTo("PREMIUM"))
        .satisfies(cardEntity -> assertThat(cardEntity.getTotalLimit()).isEqualTo(100000))
        .satisfies(cardEntity -> assertThat(cardEntity.getAmountUsed()).isEqualTo(20000))
        .satisfies(cardEntity -> assertThat(cardEntity.getAvailableAmount()).isEqualTo(80000));
  }

  @Test
  void shouldReturnNotFoundWhenUpdateNotExistingCard() {
    buildRestAssured().when()
        .contentType(APPLICATION_JSON_VALUE)
        .body("""
            {
              "phone": "0999123341",
              "cardNumber": "4532123456789",
              "cardType": "PREMIUM",
              "totalLimit": 100000,
              "amountUsed": 20000,
              "availableAmount": 80000
            }""")
        .put("/api/v1/cards/1")
        .then().assertThat().log().all()
        .statusCode(HttpStatus.NOT_FOUND.value())
        .body("timestamp", is(notNullValue()))
        .body("status", is(404))
        .body("path", is("/api/v1/cards/1"))
        .body("error", is("Card not found"));
  }

  @Test
  void shouldReturnNoContentWhenDeleteCardById() {
    CardEntity card = new CardEntity();
    card.setPhone("0999123340");
    card.setCardNumber("4532123456788");
    card.setCardType("CREDIT");
    card.setTotalLimit(10000);
    card.setAmountUsed(2500);
    card.setAvailableAmount(7500);
    card.setCreatedBy("system");
    Long cardId = cardRepository.save(card).getId();

    buildRestAssured().when()
        .contentType(APPLICATION_JSON_VALUE)
        .delete("/api/v1/cards/%d".formatted(cardId))
        .then().assertThat().log().all()
        .statusCode(HttpStatus.NO_CONTENT.value());

    assertThat(cardRepository.findById(cardId)).isEmpty();
  }

  @Test
  void shouldReturnNoContentWhenDeleteNotExistedCardById() {
    buildRestAssured().when()
        .contentType(APPLICATION_JSON_VALUE)
        .delete("/api/v1/cards/7")
        .then().assertThat().log().all()
        .statusCode(HttpStatus.NO_CONTENT.value());
  }

  @Test
  void shouldReturnCardByPhone() {
    CardEntity card = new CardEntity();
    card.setPhone("0999123340");
    card.setCardNumber("4532123456788");
    card.setCardType("CREDIT");
    card.setTotalLimit(10000);
    card.setAmountUsed(2500);
    card.setAvailableAmount(7500);
    card.setCreatedBy("system");
    cardRepository.save(card);

    buildRestAssured().when()
        .contentType(APPLICATION_JSON_VALUE)
        .get("/api/v1/cards?phone=0999123340")
        .then().assertThat().log().all()
        .statusCode(HttpStatus.OK.value())
        .body("phone", is("0999123340"))
        .body("cardNumber", is("4532123456788"))
        .body("cardType", is("CREDIT"))
        .body("totalLimit", is(10000))
        .body("amountUsed", is(2500))
        .body("availableAmount", is(7500));
  }

  @Test
  void shouldReturnCardById() {
    CardEntity card = new CardEntity();
    card.setPhone("0999123340");
    card.setCardNumber("4532123456788");
    card.setCardType("CREDIT");
    card.setTotalLimit(10000);
    card.setAmountUsed(2500);
    card.setAvailableAmount(7500);
    card.setCreatedBy("system");
    Long cardId = cardRepository.save(card).getId();

    buildRestAssured().when()
        .contentType(APPLICATION_JSON_VALUE)
        .get("/api/v1/cards/%d".formatted(cardId))
        .then().assertThat().log().all()
        .statusCode(HttpStatus.OK.value())
        .body("phone", is("0999123340"))
        .body("cardNumber", is("4532123456788"))
        .body("cardType", is("CREDIT"))
        .body("totalLimit", is(10000))
        .body("amountUsed", is(2500))
        .body("availableAmount", is(7500));
  }

  @Test
  void shouldReturnNotFoundWhenGetNotExistingCardById() {
    buildRestAssured().when()
        .contentType(APPLICATION_JSON_VALUE)
        .get("/api/v1/cards/1")
        .then().assertThat().log().all()
        .statusCode(HttpStatus.NOT_FOUND.value())
        .body("timestamp", is(notNullValue()))
        .body("status", is(404))
        .body("path", is("/api/v1/cards/1"))
        .body("error", is("Card not found"));
  }
  
  @Test
  void shouldReturnNotFoundWhenGetNotExistingCardByPhone() {
    buildRestAssured().when()
        .contentType(APPLICATION_JSON_VALUE)
        .get("/api/v1/cards?phone=0999123340")
        .then().assertThat().log().all()
        .statusCode(HttpStatus.NOT_FOUND.value())
        .body("timestamp", is(notNullValue()))
        .body("status", is(404))
        .body("path", is("/api/v1/cards"))
        .body("error", is("Card not found"));
  }

}
