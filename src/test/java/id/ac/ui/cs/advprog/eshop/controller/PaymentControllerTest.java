package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.service.PaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaymentControllerTest {
    @Mock
    private PaymentService paymentService;

    @Mock
    private Model model;

    @InjectMocks
    private PaymentController controller;

    private Payment payment;

    @BeforeEach
    void setUp() {
        Product product = new Product();
        product.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product.setProductName("Sampo Cap Bambang");
        product.setProductQuantity(2);
        List<Product> products = new ArrayList<>();
        products.add(product);

        Order order = new Order("13652556-012a-4c07-b546-54eb1396d79b",
                products, 1708560000L, "Safira Sudrajat");
        payment = new Payment(order, "VOUCHER_CODE", Map.of(
                "voucherCode", "ESHOP1234ABC5678"
        ));
    }

    @Test
    void testPaymentDetailFormPage() {
        assertEquals("PaymentDetail", controller.paymentDetailFormPage());
    }

    @Test
    void testPaymentDetailPage() {
        when(paymentService.getPayment(payment.getId())).thenReturn(payment);

        String view = controller.paymentDetailPage(payment.getId(), model);

        verify(model).addAttribute("payment", payment);
        assertEquals("PaymentDetail", view);
    }

    @Test
    void testPaymentAdminListPage() {
        List<Payment> payments = List.of(payment);
        when(paymentService.getAllPayments()).thenReturn(payments);

        String view = controller.paymentAdminListPage(model);

        verify(model).addAttribute("payments", payments);
        assertEquals("PaymentList", view);
    }

    @Test
    void testPaymentAdminDetailPage() {
        when(paymentService.getPayment(payment.getId())).thenReturn(payment);

        String view = controller.paymentAdminDetailPage(payment.getId(), model);

        verify(model).addAttribute("payment", payment);
        assertEquals("PaymentAdminDetail", view);
    }

    @Test
    void testPaymentAdminSetStatusWhenPaymentFound() {
        when(paymentService.getPayment(payment.getId())).thenReturn(payment);

        String view = controller.paymentAdminSetStatus(payment.getId(), "REJECTED");

        verify(paymentService).setStatus(payment, "REJECTED");
        assertEquals("redirect:/payment/admin/detail/" + payment.getId(), view);
    }

    @Test
    void testPaymentAdminSetStatusWhenPaymentNotFound() {
        String paymentId = "not-found";
        when(paymentService.getPayment(paymentId)).thenReturn(null);

        String view = controller.paymentAdminSetStatus(paymentId, "SUCCESS");

        verify(paymentService, never()).setStatus(any(Payment.class), anyString());
        assertEquals("redirect:/payment/admin/detail/" + paymentId, view);
    }
}
