package es.upm.tfm.adapters.mysqldb.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class OrderDTO {

    private Date date;

    @NotBlank(message = "Address field can not be empty")
    private String address;

    private List<Long> itemIdsAndAmounts;
}
