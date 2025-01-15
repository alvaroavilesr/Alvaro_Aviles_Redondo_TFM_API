package es.upm.tfm.adapters.mysqldb.entity;

import javax.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@ToString
@Table(name = "Items")
public class ItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "itemId")
    private Long itemId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "longDescription")
    private String longDescription;

    @Column(name = "size")
    private String size;

    @Column(name = "price")
    private double price;

    @Column(name = "image")
    private String image;

    @ManyToOne
    @JoinColumn(name = "categoryId")
    private CategoryEntity category;

}
