package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.service.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {
    private static final String PRODUCT_ID = "P-001";
    private static final String REDIRECT_PRODUCT_LIST = "redirect:/product/list";

    @Mock
    private ProductService service;

    @Mock
    private Model model;

    @InjectMocks
    private ProductController controller;

    @Test
    void testCreateProductPage() {
        String view = controller.createProductPage(model);

        ArgumentCaptor<Product> productCaptor = ArgumentCaptor.forClass(Product.class);
        verify(model).addAttribute(eq("product"), productCaptor.capture());
        assertNotNull(productCaptor.getValue());
        assertEquals("CreateProduct", view);
    }

    @Test
    void testProductListPost() {
        Product product = new Product();
        product.setProductId(PRODUCT_ID);

        String view = controller.productListPost(product, model);

        verify(service).create(product);
        assertEquals(REDIRECT_PRODUCT_LIST, view);
    }

    @Test
    void testListProductPage() {
        Product product = new Product();
        product.setProductId(PRODUCT_ID);
        List<Product> products = List.of(product);
        when(service.findAll()).thenReturn(products);

        String view = controller.listProductPage(model);

        verify(model).addAttribute("products", products);
        assertEquals("ProductList", view);
    }

    @Test
    void testDeleteProduct() {
        String view = controller.deleteProduct(PRODUCT_ID);

        verify(service).delete(PRODUCT_ID);
        assertEquals(REDIRECT_PRODUCT_LIST, view);
    }

    @Test
    void testEditProductPageWhenProductFound() {
        Product product = new Product();
        product.setProductId(PRODUCT_ID);
        when(service.findById(PRODUCT_ID)).thenReturn(product);

        String view = controller.editProductPage(PRODUCT_ID, model);

        verify(model).addAttribute("product", product);
        assertEquals("EditProduct", view);
    }

    @Test
    void testEditProductPageWhenProductNotFound() {
        when(service.findById(PRODUCT_ID)).thenReturn(null);

        String view = controller.editProductPage(PRODUCT_ID, model);

        verify(model, never()).addAttribute(eq("product"), any());
        assertEquals(REDIRECT_PRODUCT_LIST, view);
    }

    @Test
    void testEditProduct() {
        Product product = new Product();
        product.setProductId(PRODUCT_ID);

        String view = controller.editProduct(PRODUCT_ID, product);

        verify(service).edit(product);
        assertEquals(REDIRECT_PRODUCT_LIST, view);
    }
}
