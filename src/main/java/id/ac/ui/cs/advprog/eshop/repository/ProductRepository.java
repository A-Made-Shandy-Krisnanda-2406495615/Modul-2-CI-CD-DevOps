package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Product;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

@Repository
public class ProductRepository {
    private final List<Product> productData = new ArrayList<>();

    public Product create(Product product){
        productData.add(product);
        return product;
    }

    public Iterator<Product> findAll(){
        return productData.iterator();
    }

    public void deleteById(String id){
        productData.removeIf(product -> Objects.equals(product.getProductId(), id));
    }

    public Product findById(String id){
        for(Product product : productData){
            if(Objects.equals(product.getProductId(), id)){
                return product;
            }
        }
        return null;
    }

    public Product edit(Product updatedProduct){
        if(updatedProduct == null || updatedProduct.getProductId() == null){
            return null;
        }
        Product existingProduct = findById(updatedProduct.getProductId());
        if(existingProduct == null){
            return null;
        }

        existingProduct.setProductName(updatedProduct.getProductName());
        existingProduct.setProductQuantity(updatedProduct.getProductQuantity());
        return existingProduct;
    }
}
