package es.upm.tfm.adapters.mysqldb.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderUpdateDTO {

    private Date date;

    @NotBlank(message = "Address field can not be empty")
    private String address;

}
