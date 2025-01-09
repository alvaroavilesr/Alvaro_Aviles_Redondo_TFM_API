package es.upm.tfm.domain.models;

import lombok.*;

import java.util.Date;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Order {

    private Date date;
    private String address;
    private User user;
    private Set<ItemOrder> itemsOrder;
}
