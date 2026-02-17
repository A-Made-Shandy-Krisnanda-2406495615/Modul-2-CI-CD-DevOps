package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ProductRepositoryTest {

    @InjectMocks
    ProductRepository productRepository;
    @Test
    void testCreatAndFind() {
        Product product = new Product();
        product.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product.setProductName("Sampo Cap Bambang");
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
        product1.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product1.setProductName("Sampo Cap Bambang");
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
        product1.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product1.setProductName("Sampo Cap Bambang");
        product1.setProductQuantity(100);
        productRepository.create(product1);

        assertEquals(product1,  productRepository.findById(product1.getProductId()));
        assertNull(productRepository.findById("000000"));
    }

    @Test
    void testDeleteProduct() {
        Product product = new Product();
        product.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product.setProductName("Sampo Cap Bambang");
        product.setProductQuantity(100);
        productRepository.create(product);

        Iterator<Product> productIterator = productRepository.findAll();
        assertTrue(productIterator.hasNext());
        productRepository.deleteById(product.getProductId());
        assertFalse(productIterator.hasNext());
    }

    @Test
    void testEditProduct() {
        Product oldProduct = new Product();
        oldProduct.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        oldProduct.setProductName("Sampo Cap Bambang");
        oldProduct.setProductQuantity(100);
        productRepository.create(oldProduct);

        Product newProduct = new Product();
        newProduct.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        newProduct.setProductName("Sampo Cap Bang");
        newProduct.setProductQuantity(20);
        productRepository.edit(newProduct);

        Product updatedProduct = productRepository.findById(newProduct.getProductId());

        assertEquals(newProduct.getProductId(), updatedProduct.getProductId());
        assertEquals(newProduct.getProductName(), updatedProduct.getProductName());
        assertEquals(newProduct.getProductQuantity(), updatedProduct.getProductQuantity());

        Product fakeProduct = new Product();
        fakeProduct.setProductId("eb558e9f-1c39-460e-8860");
        fakeProduct.setProductName("Sampo Cap Bang");
        fakeProduct.setProductQuantity(20);
        productRepository.edit(fakeProduct);

        assertNull(productRepository.edit(fakeProduct));

        Product nullProduct = new Product();
        productRepository.edit(nullProduct);
        assertNull(productRepository.edit(nullProduct));

        assertNull(productRepository.edit(null));



    }
}
