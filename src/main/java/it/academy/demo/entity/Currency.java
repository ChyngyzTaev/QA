package it.academy.demo.entity;

import it.academy.demo.model.CurrencyModel;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;

@Entity
@Table(name = "currency")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Currency {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "rate", unique = true, nullable = false)
    Double rate;

    @Column(name = "name", unique = true, nullable = false)
    String name;

    public CurrencyModel toModel(){
        return CurrencyModel.builder()
                .id(id)
                .name(name)
                .rate(rate)
                .build();
    }
}
