package es.upm.tfm.adapters.mysqldb.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class CategoryDTO {

    @NotBlank(message = "Name field can not be empty")
    private String name;
}
