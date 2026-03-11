package id.ac.ui.cs.advprog.eshop.model;

import id.ac.ui.cs.advprog.eshop.enums.OrderStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@Getter
public class Order {
    @Setter
    String id;
    @Setter
    List<Product> products;
    @Setter
    Long orderTime;
    @Setter
    String author;
    String status;

    public Order(String id, List<Product> products, long orderTime, String author) {
        this.id = id;
        this.orderTime = orderTime;
        this.author = author;
        this.status = OrderStatus.WAITING_PAYMENT.getValue();

        if (products.isEmpty()) {
            throw new IllegalArgumentException();
        } else {
            this.products = products;
        }
    }

    public Order(String id, List<Product> products, long orderTime, String author, String status) {
        this(id, products, orderTime, author);
        validateStatus(status);
        this.status = status;
    }

    public void setStatus(String status) {
        validateStatus(status);
        this.status = status;
    }

    private static void validateStatus(String status) {
        if (!OrderStatus.contains(status)) {
            throw new IllegalArgumentException();
        }
    }
}
