package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.service.OrderService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderControllerTest {
    @Mock
    private OrderService orderService;

    @Mock
    private Model model;

    @InjectMocks
    private OrderController controller;

    @Test
    void testCreateOrderPage() {
        assertEquals("CreateOrder", controller.createOrderPage());
    }

    @Test
    void testCreateOrderPost() {
        String author = "Safira Sudrajat";

        String view = controller.createOrderPost(author);

        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
        verify(orderService).createOrder(orderCaptor.capture());
        Order capturedOrder = orderCaptor.getValue();

        assertEquals(author, capturedOrder.getAuthor());
        assertFalse(capturedOrder.getProducts().isEmpty());
        assertEquals("redirect:/order/history", view);
    }

    @Test
    void testOrderHistoryPage() {
        assertEquals("OrderHistory", controller.orderHistoryPage());
    }

    @Test
    void testOrderHistoryPost() {
        String author = "Safira Sudrajat";
        List<Order> orders = List.of();
        when(orderService.findAllByAuthor(author)).thenReturn(orders);

        String view = controller.orderHistoryPost(author, model);

        verify(model).addAttribute("orders", orders);
        assertEquals("OrderHistory", view);
    }

    @Test
    void testOrderPayPage() {
        String orderId = "ORDER-1";

        String view = controller.orderPayPage(orderId, model);

        verify(model).addAttribute("orderId", orderId);
        assertEquals("OrderPay", view);
    }

    @Test
    void testOrderPayPost() {
        String orderId = "ORDER-1";

        String view = controller.orderPayPost(orderId, model);

        verify(model).addAttribute(eq("paymentId"), eq(""));
        assertEquals("PaymentResult", view);
    }
}
