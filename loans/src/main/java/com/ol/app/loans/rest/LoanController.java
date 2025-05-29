package com.ol.app.loans.rest;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

import java.time.OffsetDateTime;

import com.ol.app.loans.dao.LoanRepository;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/loans")
public class LoanController {

  private final LoanRepository loanRepository;

  private final LoanMapper loanMapper;

  @PostMapping
  @ResponseStatus(code = CREATED)
  void createLoan(@Valid @RequestBody LoanRequest request, HttpServletResponse response) {
    if (loanRepository.findByPhone(request.phone()).isPresent()) {
      throw new ResponseStatusException(HttpStatus.CONFLICT, "Loan with provided phone already exists");
    }

    Long loadId = loanRepository.save(loanMapper.toLoanEntity(request)).getId();

    response.addHeader("Location", ServletUriComponentsBuilder
        .fromCurrentRequest()
        .path("/{account_id}")
        .buildAndExpand(loadId).toUri().toString());
  }

  @PutMapping("/{loan_id}")
  @ResponseStatus(code = NO_CONTENT)
  void updateLoan(@PathVariable("loan_id") Long loanId, @Valid @RequestBody LoanRequest request) {
    loanRepository.findById(loanId).ifPresentOrElse(loanEntity -> loanRepository.save(loanMapper.updateLoanEntity(loanEntity, request)), () -> {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Loan not found");
    });
  }
  
  @DeleteMapping("/{loan_id}")
  @ResponseStatus(code = NO_CONTENT)
  void deleteLoan(@PathVariable("loan_id") Long loanId) {
    loanRepository.deleteById(loanId);
  }
  
  @GetMapping
  LoanResponse getLoan(@RequestParam String phone) {
    return loanMapper.toLoanResponse(loanRepository.findByPhone(phone)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Loan not found")));
  }
  
  @GetMapping("/{loan_id}")
  LoanResponse getLoan(@PathVariable("loan_id") Long loanId) {
    return loanMapper.toLoanResponse(loanRepository.findById(loanId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Loan not found")));
  }

  public record LoanRequest(@NotBlank(message = "Phone number is required")
                            @Pattern(regexp = "^\\d{10}$", message = "Phone number must be exactly 10 digits")
                            @Size(max = 10, message = "Phone number must not exceed 10 characters") String phone,
                            @NotBlank(message = "Loan number is required")
                            @Size(max = 100, message = "Loan number must not exceed 100 characters") String loanNumber,
                            @NotBlank(message = "Loan type is required")
                            @Size(max = 100, message = "Loan type must not exceed 100 characters") String loanType,
                            @NotNull(message = "Total loan amount is required") 
                            @PositiveOrZero(message = "Total loan amount must be zero or positive") Integer totalLoan,
                            @NotNull(message = "Amount paid is required") 
                            @PositiveOrZero(message = "Amount paid must be zero or positive") Integer amountPaid,
                            @NotBlank(message = "Address is required") String address) {

  }

  public record LoanResponse(Integer id,
                             String phone,
                             String loanNumber,
                             String loanType,
                             Integer totalLoan,
                             Integer amountPaid,
                             String address,
                             OffsetDateTime createdAt,
                             String createdBy,
                             OffsetDateTime updatedAt,
                             String updatedBy) {

  }
}
