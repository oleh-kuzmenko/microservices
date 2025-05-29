package com.ol.app.cards.rest;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

import java.time.OffsetDateTime;

import com.ol.app.cards.dao.CardRepository;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
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
@RequestMapping("/api/v1/cards")
public class CardController {

  private final CardRepository cardRepository;

  private final CardMapper cardMapper;

  @PostMapping
  @ResponseStatus(code = CREATED)
  void createCard(@Valid @RequestBody CardRequest request, HttpServletResponse response) {
    if (cardRepository.findByPhone(request.phone()).isPresent()) {
      throw new ResponseStatusException(HttpStatus.CONFLICT, "Card with provided phone already exists");
    }

    Long cardId = cardRepository.save(cardMapper.toLoanEntity(request)).getId();

    response.addHeader("Location", ServletUriComponentsBuilder
        .fromCurrentRequest()
        .path("/{card_id}")
        .buildAndExpand(cardId).toUri().toString());
  }

  @PutMapping("/{card_id}")
  @ResponseStatus(code = NO_CONTENT)
  void updateCard(@PathVariable("card_id") Long cardId, @Valid @RequestBody CardRequest request) {
    cardRepository.findById(cardId).ifPresentOrElse(cardEntity -> cardRepository.save(cardMapper.updateCardEntity(cardEntity, request)), () -> {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Card not found");
    });
  }
  
  @DeleteMapping("/{card_id}")
  @ResponseStatus(code = NO_CONTENT)
  void deleteLoan(@PathVariable("card_id") Long cardId) {
    cardRepository.deleteById(cardId);
  }
  
  @GetMapping
  CardResponse getCard(@RequestParam String phone) {
    return cardMapper.toCardResponse(cardRepository.findByPhone(phone)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Card not found")));
  }
  
  @GetMapping("/{card_id}")
  CardResponse getCard(@PathVariable("card_id") Long cardId) {
    return cardMapper.toCardResponse(cardRepository.findById(cardId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Card not found")));
  }

  public record CardRequest(@NotBlank(message = "Phone number is required")
                            @Pattern(regexp = "^\\d{10}$", message = "Phone number must be exactly 10 digits")
                            @Size(max = 10, message = "Phone number must not exceed 10 characters") String phone,
                            @NotBlank(message = "Card number is required") String cardNumber,
                            @NotBlank(message = "Card type is required") String cardType,
                            @NotNull(message = "Total limit is required")
                            @PositiveOrZero(message = "Total limit must be zero or positive") Integer totalLimit,
                            @NotNull(message = "Amount used is required")
                            @PositiveOrZero(message = "Amount used must be zero or positive") Integer amountUsed,
                            @NotNull(message = "Available amount is required")
                            @PositiveOrZero(message = "Available amount must be zero or positive") Integer availableAmount) {

  }

  public record CardResponse(Integer id,
                             String phone,
                             String cardNumber,
                             String cardType,
                             Integer totalLimit,
                             Integer amountUsed,
                             Integer availableAmount,
                             OffsetDateTime createdAt,
                             String createdBy,
                             OffsetDateTime updatedAt,
                             String updatedBy) {

  }
}
