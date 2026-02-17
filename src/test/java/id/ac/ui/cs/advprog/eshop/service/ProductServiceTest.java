package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {
    private static final String PRODUCT_NAME_BAMBANG = "Sampo Cap Bambang";
    private static final String PRODUCT_ID_ABC = "ABC";

    @Mock
    ProductRepository productRepository;

    @InjectMocks
    ProductServiceImpl productService;

    @Test
    void testCreateWhenProductIdNullShouldGenerateIdAndSave() {
        Product product = new Product();
        product.setProductName(PRODUCT_NAME_BAMBANG);
        product.setProductQuantity(100);

        Product createdProduct = productService.create(product);

        assertSame(product, createdProduct);
        assertNotNull(product.getProductId());
        assertFalse(product.getProductId().isBlank());
        verify(productRepository).create(product);
    }

    @Test
    void testCreateWhenProductIdBlankShouldGenerateIdAndSave() {
        Product product = new Product();
        product.setProductId("   ");
        product.setProductName(PRODUCT_NAME_BAMBANG);
        product.setProductQuantity(100);

        productService.create(product);

        assertNotNull(product.getProductId());
        assertFalse(product.getProductId().isBlank());
        verify(productRepository).create(product);
    }

    @Test
    void testCreateWhenProductIdProvidedShouldNotOverwriteId() {
        Product product = new Product();
        product.setProductId(PRODUCT_ID_ABC);
        product.setProductName(PRODUCT_NAME_BAMBANG);
        product.setProductQuantity(100);

        productService.create(product);

        assertEquals(PRODUCT_ID_ABC, product.getProductId());
        verify(productRepository).create(product);
    }

    @Test
    void testFindAll() {
        Product product1 = new Product();
        product1.setProductId("P1");
        product1.setProductName(PRODUCT_NAME_BAMBANG);
        product1.setProductQuantity(100);

        Product product2 = new Product();
        product2.setProductId("P2");
        product2.setProductName("Sampo Cap Usep");
        product2.setProductQuantity(50);

        when(productRepository.findAll()).thenReturn(List.of(product1, product2).iterator());

        List<Product> products = productService.findAll();
        assertEquals(2, products.size());
        assertSame(product1, products.get(0));
        assertSame(product2, products.get(1));
        verify(productRepository).findAll();
    }

    @Test
    void testDelete() {
        String productId = PRODUCT_ID_ABC;

        productService.delete(productId);

        verify(productRepository).deleteById(productId);
    }

    @Test
    void testFindById() {
        String productId = PRODUCT_ID_ABC;

        Product product = new Product();
        product.setProductId(productId);
        product.setProductName(PRODUCT_NAME_BAMBANG);
        product.setProductQuantity(100);
        when(productRepository.findById(productId)).thenReturn(product);

        assertSame(product, productService.findById(productId));
        verify(productRepository).findById(productId);
    }

    @Test
    void testEdit() {
        Product product = new Product();
        product.setProductId(PRODUCT_ID_ABC);
        product.setProductName(PRODUCT_NAME_BAMBANG);
        product.setProductQuantity(100);

        Product updatedProduct = new Product();
        updatedProduct.setProductId(PRODUCT_ID_ABC);
        updatedProduct.setProductName("Sampo Cap Bang");
        updatedProduct.setProductQuantity(50);

        when(productRepository.edit(product)).thenReturn(updatedProduct);

        assertSame(updatedProduct, productService.edit(product));
        verify(productRepository).edit(product);
    }

}
