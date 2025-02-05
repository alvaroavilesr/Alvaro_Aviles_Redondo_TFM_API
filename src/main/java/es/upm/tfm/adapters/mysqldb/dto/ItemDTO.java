package es.upm.tfm.adapters.mysqldb.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemDTO {

    @NotBlank(message = "Name field can not be empty")
    private String name;

    @NotBlank(message = "Description field can not be empty")
    private String description;

    @NotBlank(message = "Long description field can not be empty")
    private String longDescription;

    @NotNull
    @Pattern(regexp="(XS|S|M|L|XL|XXL)", message="Invalid size")
    private String size;

    @Min(value = 0, message = "Price can not be lesser than 0")
    private double price;

    @NotBlank(message = "Image field can not be empty")
    private String image;
}
