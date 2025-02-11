package es.upm.tfm.adapters.mysqldb.response;

import lombok.*;

import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ItemOrderResponse {

    private Long itemOrderId;

    private Long amount;

    private ItemResponse item;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemOrderResponse that = (ItemOrderResponse) o;
        return Objects.equals(itemOrderId, that.itemOrderId) &&
                Objects.equals(amount, that.amount) &&
                Objects.equals(item, that.item);
    }

    @Override
    public int hashCode() {
        return Objects.hash(itemOrderId, amount);
    }
}
