package es.upm.tfm.domain.models;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ItemOrder {

    private Long amount;
    private Item item;
}
