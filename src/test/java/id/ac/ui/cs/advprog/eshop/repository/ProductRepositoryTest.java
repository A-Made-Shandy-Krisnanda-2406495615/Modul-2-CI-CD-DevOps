package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Product;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ProductRepositoryTest {
    private static final String DEFAULT_PRODUCT_ID = "eb558e9f-1c39-460e-8860-71af6af63bd6";
    private static final String DEFAULT_PRODUCT_NAME = "Sampo Cap Bambang";

    @InjectMocks
    ProductRepository productRepository;
    @Test
    void testCreatAndFind() {
        Product product = new Product();
        product.setProductId(DEFAULT_PRODUCT_ID);
        product.setProductName(DEFAULT_PRODUCT_NAME);
        product.setProductQuantity(100);
        productRepository.create(product);

        Iterator<Product> productIterator = productRepository.findAll();
        assertTrue(productIterator.hasNext());
        Product savedProduct = productIterator.next();
        assertEquals(product.getProductId(), savedProduct.getProductId());
        assertEquals(product.getProductName(), savedProduct.getProductName());
        assertEquals(product.getProductQuantity(), savedProduct.getProductQuantity());
    }
    @Test
    void testFindAllIfEmpty() {
        Iterator<Product> productIterator = productRepository.findAll();
        assertFalse(productIterator.hasNext());
    }

    @Test
    void testFindAllIfMoreThanOneProduct() {
        Product product1 = new Product();
        product1.setProductId(DEFAULT_PRODUCT_ID);
        product1.setProductName(DEFAULT_PRODUCT_NAME);
        product1.setProductQuantity(100);
        productRepository.create(product1);

        Product product2 = new Product();
        product2.setProductId("a0f9de46-90b1-437d-a0bf-d0821dde9096");
        product2.setProductName("Sampo Cap Usep");
        product2.setProductQuantity(50);
        productRepository.create(product2);

        Iterator<Product> productIterator = productRepository.findAll();
        assertTrue(productIterator.hasNext());
        Product savedProduct = productIterator.next();
        assertEquals(product1.getProductId(), savedProduct.getProductId());
        savedProduct = productIterator.next();
        assertEquals(product2.getProductId(), savedProduct.getProductId());
        assertFalse(productIterator.hasNext());
    }

    @Test
    void testFindById() {
        Product product1 = new Product();
        product1.setProductId(DEFAULT_PRODUCT_ID);
        product1.setProductName(DEFAULT_PRODUCT_NAME);
        product1.setProductQuantity(100);
        productRepository.create(product1);

        assertEquals(product1, productRepository.findById(product1.getProductId()).orElse(null));
        assertTrue(productRepository.findById("000000").isEmpty());
    }

    @Test
    void testDeleteProduct() {
        Product product = new Product();
        product.setProductId(DEFAULT_PRODUCT_ID);
        product.setProductName(DEFAULT_PRODUCT_NAME);
        product.setProductQuantity(100);
        productRepository.create(product);

        Iterator<Product> productIterator = productRepository.findAll();
        assertTrue(productIterator.hasNext());
        productRepository.deleteById(product.getProductId());
        assertFalse(productIterator.hasNext());
    }

    @Test
    void testFindByIdOnDeletedProductShouldBeEmpty() {
        Product product = new Product();
        product.setProductId(DEFAULT_PRODUCT_ID);
        product.setProductName(DEFAULT_PRODUCT_NAME);
        product.setProductQuantity(100);
        productRepository.create(product);

        productRepository.deleteById(DEFAULT_PRODUCT_ID);

        assertTrue(productRepository.findById(DEFAULT_PRODUCT_ID).isEmpty());
    }
}
