package es.upm.tfm.domain.models;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Item {

    private String name;
    private String description;
    private String longDescription;
    private String size;
    private double price;
    private String image;
    private Category category;
}
