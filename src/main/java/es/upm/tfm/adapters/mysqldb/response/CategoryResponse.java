package es.upm.tfm.adapters.mysqldb.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class CategoryResponse {

    private Long categoryId;

    private String name;
}
