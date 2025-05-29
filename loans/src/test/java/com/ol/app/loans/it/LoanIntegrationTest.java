package com.ol.app.loans.it;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.matchesPattern;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.core.Is.is;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.ol.app.loans.dao.LoanEntity;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

class LoanIntegrationTest extends AbstractIntegrationTest {

  @Test
  void shouldCreateLoan() {
    buildRestAssured().when()
        .contentType(APPLICATION_JSON_VALUE)
        .body("""
            {
              "phone": "0999123341",
              "loanNumber": "LN123456",
              "loanType": "PERSONAL",
              "totalLoan": 10000,
              "amountPaid": 2500,
              "address": "123 Main St, London"
            }""")
        .post("/api/v1/loans")
        .then().assertThat().log().all()
        .statusCode(HttpStatus.CREATED.value())
        .header("Location", matchesPattern("http://localhost:%d/api/v1/loans/\\d+".formatted(port)));

    assertThat(loanRepository.findAll()).hasSize(1)
        .first()
        .satisfies(customer -> assertThat(customer.getId()).isNotNull())
        .extracting(LoanEntity::getPhone, LoanEntity::getLoanNumber, LoanEntity::getLoanType, LoanEntity::getTotalLoan,
            LoanEntity::getAmountPaid, LoanEntity::getAddress, LoanEntity::getCreatedBy)
        .containsExactly("0999123341", "LN123456", "PERSONAL", 10000, 2500, "123 Main St, London", "system");
  }
  
  @Test
  void shouldReturnBadRequestForInvalidAttributes() {
    buildRestAssured().when()
        .contentType(APPLICATION_JSON_VALUE)
        .body("""
            {
              "phone": "111",
              "loanNumber": "LN123456",
              "loanType": "PERSONAL",
              "totalLoan": -121,
              "amountPaid": 2500,
              "address": ""
            }""")
        .post("/api/v1/loans")
        .then().assertThat().log().all()
        .statusCode(HttpStatus.BAD_REQUEST.value())
        .body("timestamp", is(notNullValue()))
        .body("status", is(400))
        .body("path", is("/api/v1/loans"))
        .body("errors", hasItems(
            "Phone number must be exactly 10 digits",
            "Total loan amount must be zero or positive",
            "Address is required"
        ));
  }

  @Test
  void shouldReturnConflictForNotUniquePhone() {
    LoanEntity loan = new LoanEntity();
    loan.setPhone("0999123340");
    loan.setLoanNumber("LN123455");
    loan.setLoanType("PERSONAL");
    loan.setTotalLoan(10000);
    loan.setAmountPaid(2500);
    loan.setAddress("123 Main St, London");
    loan.setCreatedBy("system");
    loanRepository.save(loan);

    buildRestAssured().when()
        .contentType(APPLICATION_JSON_VALUE)
        .body("""
            {
              "phone": "0999123340",
              "loanNumber": "LN123456",
              "loanType": "PERSONAL",
              "totalLoan": 10000,
              "amountPaid": 2500,
              "address": "123 Main St, London"
            }""")
        .post("/api/v1/loans")
        .then().assertThat().log().all()
        .statusCode(HttpStatus.CONFLICT.value())
        .body("timestamp", is(notNullValue()))
        .body("status", is(409))
        .body("path", is("/api/v1/loans"))
        .body("error", is("Loan with provided phone already exists"));
  }

  @Test
  void shouldUpdateLoan() {
    LoanEntity loan = new LoanEntity();
    loan.setPhone("0999123340");
    loan.setLoanNumber("LN123455");
    loan.setLoanType("PERSONAL");
    loan.setTotalLoan(10000);
    loan.setAmountPaid(2500);
    loan.setAddress("123 Main St, London");
    loan.setCreatedBy("system");
    Long loanId = loanRepository.save(loan).getId();

    buildRestAssured().when()
        .contentType(APPLICATION_JSON_VALUE)
        .body("""
            {
              "phone": "0999123341",
              "loanNumber": "LN123456",
              "loanType": "PREMIUM",
              "totalLoan": 10000000,
              "amountPaid": 20,
              "address": "123 Madison Ave, New York"
            }""")
        .put("/api/v1/loans/%d".formatted(loanId))
        .then().assertThat().log().all()
        .statusCode(HttpStatus.NO_CONTENT.value());

    assertThat(loanRepository.findById(loanId)).isPresent()
        .get()
        .satisfies(loanEntity -> assertThat(loanEntity.getPhone()).isEqualTo("0999123341"))
        .satisfies(loanEntity -> assertThat(loanEntity.getLoanNumber()).isEqualTo("LN123456"))
        .satisfies(loanEntity -> assertThat(loanEntity.getLoanType()).isEqualTo("PREMIUM"))
        .satisfies(loanEntity -> assertThat(loanEntity.getTotalLoan()).isEqualTo(10000000))
        .satisfies(loanEntity -> assertThat(loanEntity.getAmountPaid()).isEqualTo(20))
        .satisfies(loanEntity -> assertThat(loanEntity.getAddress()).isEqualTo("123 Madison Ave, New York"));
  }

  @Test
  void shouldReturnNotFoundWhenUpdateNotExistingLoan() {
    buildRestAssured().when()
        .contentType(APPLICATION_JSON_VALUE)
        .body("""
            {
              "phone": "0999123341",
              "loanNumber": "LN123456",
              "loanType": "PREMIUM",
              "totalLoan": 10000000,
              "amountPaid": 20,
              "address": "123 Madison Ave, New York"
            }""")
        .put("/api/v1/loans/1")
        .then().assertThat().log().all()
        .statusCode(HttpStatus.NOT_FOUND.value())
        .body("timestamp", is(notNullValue()))
        .body("status", is(404))
        .body("path", is("/api/v1/loans/1"))
        .body("error", is("Loan not found"));
  }

  @Test
  void shouldReturnNoContentWhenDeleteLoanById() {
    LoanEntity loan = new LoanEntity();
    loan.setPhone("0999123340");
    loan.setLoanNumber("LN123455");
    loan.setLoanType("PERSONAL");
    loan.setTotalLoan(10000);
    loan.setAmountPaid(2500);
    loan.setAddress("123 Main St, London");
    loan.setCreatedBy("system");
    Long loanId = loanRepository.save(loan).getId();

    buildRestAssured().when()
        .contentType(APPLICATION_JSON_VALUE)
        .delete("/api/v1/loans/%d".formatted(loanId))
        .then().assertThat().log().all()
        .statusCode(HttpStatus.NO_CONTENT.value());

    assertThat(loanRepository.findById(loanId)).isEmpty();
  }

  @Test
  void shouldReturnNoContentWhenDeleteNotExistedLoanById() {
    buildRestAssured().when()
        .contentType(APPLICATION_JSON_VALUE)
        .delete("/api/v1/loans/7")
        .then().assertThat().log().all()
        .statusCode(HttpStatus.NO_CONTENT.value());
  }

  @Test
  void shouldReturnLoanByPhone() {
    LoanEntity loan = new LoanEntity();
    loan.setPhone("0999123340");
    loan.setLoanNumber("LN123455");
    loan.setLoanType("PERSONAL");
    loan.setTotalLoan(10000);
    loan.setAmountPaid(2500);
    loan.setAddress("123 Main St, London");
    loan.setCreatedBy("system");
    loanRepository.save(loan);

    buildRestAssured().when()
        .contentType(APPLICATION_JSON_VALUE)
        .get("/api/v1/loans?phone=0999123340")
        .then().assertThat().log().all()
        .statusCode(HttpStatus.OK.value())
        .body("phone", is("0999123340"))
        .body("loanNumber", is("LN123455"))
        .body("loanType", is("PERSONAL"))
        .body("totalLoan", is(10000))
        .body("amountPaid", is(2500))
        .body("address", is("123 Main St, London"));
  }

  @Test
  void shouldReturnLoanById() {
    LoanEntity loan = new LoanEntity();
    loan.setPhone("0999123340");
    loan.setLoanNumber("LN123455");
    loan.setLoanType("PERSONAL");
    loan.setTotalLoan(10000);
    loan.setAmountPaid(2500);
    loan.setAddress("123 Main St, London");
    loan.setCreatedBy("system");
    Long loanId = loanRepository.save(loan).getId();

    buildRestAssured().when()
        .contentType(APPLICATION_JSON_VALUE)
        .get("/api/v1/loans/%d".formatted(loanId))
        .then().assertThat().log().all()
        .statusCode(HttpStatus.OK.value())
        .body("phone", is("0999123340"))
        .body("loanNumber", is("LN123455"))
        .body("loanType", is("PERSONAL"))
        .body("totalLoan", is(10000))
        .body("amountPaid", is(2500))
        .body("address", is("123 Main St, London"));
  }

  @Test
  void shouldReturnNotFoundWhenGetNotExistingLoanById() {
    buildRestAssured().when()
        .contentType(APPLICATION_JSON_VALUE)
        .get("/api/v1/loans/1")
        .then().assertThat().log().all()
        .statusCode(HttpStatus.NOT_FOUND.value())
        .body("timestamp", is(notNullValue()))
        .body("status", is(404))
        .body("path", is("/api/v1/loans/1"))
        .body("error", is("Loan not found"));
  }
  
  @Test
  void shouldReturnNotFoundWhenGetNotExistingLoanByPhone() {
    buildRestAssured().when()
        .contentType(APPLICATION_JSON_VALUE)
        .get("/api/v1/loans?phone=0999123340")
        .then().assertThat().log().all()
        .statusCode(HttpStatus.NOT_FOUND.value())
        .body("timestamp", is(notNullValue()))
        .body("status", is(404))
        .body("path", is("/api/v1/loans"))
        .body("error", is("Loan not found"));
  }

}
