package it.academy.demo.model;

import it.academy.demo.entity.Currency;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateCurrencyModel {
    String name;

    Double rate;

    public Currency toCurrency() {
        return Currency.builder()
                .id(null)
                .name(name)
                .rate(rate)
                .build();
    }
}
