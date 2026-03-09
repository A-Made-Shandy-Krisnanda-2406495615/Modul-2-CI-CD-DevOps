package id.ac.ui.cs.advprog.eshop.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@Getter
@Setter
public class Order {
    private String id;
    private List<Product> products;
    private long orderTime;
    private String author;
    private String status;

    public Order(String id, List<Product> products, long orderTime, String author) {
    }

    public Order(String id, List<Product> products, long orderTime, String author, String status) {
    }
}
