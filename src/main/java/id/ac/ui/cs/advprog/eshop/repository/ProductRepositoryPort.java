package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Product;

import java.util.Iterator;
import java.util.Optional;

public interface ProductRepositoryPort {
    Product create(Product product);
    Iterator<Product> findAll();
    void deleteById(String id);
    Optional<Product> findById(String id);
}
