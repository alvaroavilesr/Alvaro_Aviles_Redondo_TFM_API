package es.upm.tfm.adapters.mysqldb.response;

import lombok.*;

import java.util.Date;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class OrderResponse {

    private Long orderId;

    private Date date;

    private String address;

    private String userName;

    private double price;

    private int itemAmount;

    private Set<ItemOrderResponse> itemsOrder;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderResponse that = (OrderResponse) o;
        return Objects.equals(date, that.date) &&
                Objects.equals(address, that.address) &&
                Objects.equals(userName, that.userName) &&
                Objects.equals(itemAmount, that.itemAmount) &&
                Objects.equals(price, that.price) &&
                Objects.equals(itemsOrder, that.itemsOrder);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId, address);
    }
}
