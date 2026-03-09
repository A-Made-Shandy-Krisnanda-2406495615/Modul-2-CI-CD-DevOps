package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.service.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/order")
public class OrderController {
    private static final String REDIRECT_ORDER_HISTORY = "redirect:/order/history";
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/create")
    public String createOrderPage() {
        return "CreateOrder";
    }

    @PostMapping("/create")
    public String createOrderPost(@RequestParam("author") String author) {
        Product product = new Product();
        product.setProductId(UUID.randomUUID().toString());
        product.setProductName("Default Product");
        product.setProductQuantity(1);

        List<Product> products = new ArrayList<>();
        products.add(product);

        Order order = new Order(
                UUID.randomUUID().toString(),
                products,
                System.currentTimeMillis() / 1000,
                author
        );
        orderService.createOrder(order);
        return REDIRECT_ORDER_HISTORY;
    }

    @GetMapping("/history")
    public String orderHistoryPage() {
        return "OrderHistory";
    }

    @PostMapping("/history")
    public String orderHistoryPost(@RequestParam("author") String author, Model model) {
        List<Order> orders = orderService.findAllByAuthor(author);
        model.addAttribute("orders", orders);
        return "OrderHistory";
    }

    @GetMapping("/pay/{orderId}")
    public String orderPayPage(@PathVariable String orderId, Model model) {
        model.addAttribute("orderId", orderId);
        return "OrderPay";
    }

    @PostMapping("/pay/{orderId}")
    public String orderPayPost(@PathVariable String orderId, Model model) {
        model.addAttribute("paymentId", "");
        return "PaymentResult";
    }
}
