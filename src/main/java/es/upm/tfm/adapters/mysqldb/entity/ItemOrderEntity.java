package es.upm.tfm.adapters.mysqldb.entity;

import es.upm.tfm.domain.models.ItemOrder;
import lombok.*;

import jakarta.persistence.*;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@ToString
@Table(name = "ItemOrder")
public class ItemOrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "itemOrderId")
    private Long itemOrderId;

    @Column(name = "amount")
    private Long amount;

    @ManyToOne
    @JoinColumn(name = "itemId")
    private ItemEntity item;

}
