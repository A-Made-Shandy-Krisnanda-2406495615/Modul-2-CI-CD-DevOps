package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/product")
public class ProductController {
    private static final String MODEL_ATTR_PRODUCT = "product";
    private static final String REDIRECT_PRODUCT_LIST = "redirect:/product/list";

    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    @GetMapping("/create")
    public String createProductPage(Model model){
        Product product = new Product();
        model.addAttribute(MODEL_ATTR_PRODUCT, product);
        return "CreateProduct";
    }

    @PostMapping("/create")
    public String productListPost(@ModelAttribute(MODEL_ATTR_PRODUCT) Product product){
        service.create(product);
        return REDIRECT_PRODUCT_LIST;
    }

    @GetMapping("/list")
    public String listProductPage(Model model){
        List<Product> allProducts = service.findAll();
        model.addAttribute("products", allProducts);
        return "ProductList";
    }

    @PostMapping("/delete/{id}")
    public String deleteProduct(@PathVariable String id){
        service.delete(id);
        return REDIRECT_PRODUCT_LIST;
    }

    @GetMapping("/edit/{id}")
    public String editProductPage(@PathVariable String id, Model model){
        Product product = service.findById(id);
        if(product == null){
            return REDIRECT_PRODUCT_LIST;
        }
        model.addAttribute(MODEL_ATTR_PRODUCT, product);
        return "EditProduct";
    }

    @PostMapping("/edit/{id}")
    public String editProduct(@PathVariable String id, @ModelAttribute(MODEL_ATTR_PRODUCT) Product product){
        product.setProductId(id);
        service.edit(product);
        return REDIRECT_PRODUCT_LIST;
    }
}
