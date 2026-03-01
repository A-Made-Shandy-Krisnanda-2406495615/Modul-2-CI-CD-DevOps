package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Product;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class ProductRepository implements ProductRepositoryPort {
    private final List<Product> productData = new ArrayList<>();

    @Override
    public Product create(Product product) {
        productData.add(product);
        return product;
    }

    @Override
    public Iterator<Product> findAll() {
        return productData.iterator();
    }

    @Override
    public void deleteById(String id) {
        productData.removeIf(product -> Objects.equals(product.getProductId(), id));
    }

    @Override
    public Optional<Product> findById(String id) {
        for (Product product : productData) {
            if (Objects.equals(product.getProductId(), id)) {
                return Optional.of(product);
            }
        }
        return Optional.empty();
    }
}
