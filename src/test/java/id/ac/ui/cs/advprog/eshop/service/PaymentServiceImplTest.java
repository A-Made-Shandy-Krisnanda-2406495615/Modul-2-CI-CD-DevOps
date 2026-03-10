package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.enums.OrderStatus;
import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PaymentServiceImplTest {

    @InjectMocks
    PaymentServiceImpl paymentService;

    @Mock
    PaymentRepository paymentRepository;

    Order order;

    @BeforeEach
    void setUp() {
        List<Product> products = new ArrayList<>();
        Product product1 = new Product();
        product1.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product1.setProductName("Sampo Cap Bambang");
        product1.setProductQuantity(2);
        products.add(product1);

        order = new Order("13652556-012a-4c07-b546-54eb1396d79b",
                products, 1708560000L, "Safira Sudrajat");
    }

    @Test
    void testAddPaymentWithValidVoucherCode() {
        Map<String, String> paymentData = Map.of("voucherCode", "ESHOP1234ABC5678");
        doAnswer(invocation -> invocation.getArgument(0)).when(paymentRepository).save(any(Payment.class));

        Payment result = paymentService.addPayment(order, "VOUCHER_CODE", paymentData);

        verify(paymentRepository, times(1)).save(any(Payment.class));
        assertEquals("VOUCHER_CODE", result.getMethod());
        assertEquals("SUCCESS", result.getStatus());
        assertEquals(order, result.getOrder());
    }

    @Test
    void testAddPaymentWithInvalidVoucherCode() {
        Map<String, String> paymentData = Map.of("voucherCode", "MEOW1234ABC5678");
        doAnswer(invocation -> invocation.getArgument(0)).when(paymentRepository).save(any(Payment.class));

        Payment result = paymentService.addPayment(order, "VOUCHER_CODE", paymentData);

        verify(paymentRepository, times(1)).save(any(Payment.class));
        assertEquals("REJECTED", result.getStatus());
    }

    @Test
    void testAddPaymentWithValidCodData() {
        Map<String, String> paymentData = Map.of(
                "address", "Jl. Margonda Raya No. 100",
                "deliveryFee", "20000"
        );
        doAnswer(invocation -> invocation.getArgument(0)).when(paymentRepository).save(any(Payment.class));

        Payment result = paymentService.addPayment(order, "COD", paymentData);

        verify(paymentRepository, times(1)).save(any(Payment.class));
        assertEquals("SUCCESS", result.getStatus());
        assertEquals(order, result.getOrder());
    }

    @Test
    void testAddPaymentWithInvalidCodData() {
        Map<String, String> paymentData = Map.of(
                "address", "",
                "deliveryFee", "20000"
        );
        doAnswer(invocation -> invocation.getArgument(0)).when(paymentRepository).save(any(Payment.class));

        Payment result = paymentService.addPayment(order, "COD", paymentData);

        verify(paymentRepository, times(1)).save(any(Payment.class));
        assertEquals("REJECTED", result.getStatus());
    }
//
//    @Test
//    void testSetStatusToSuccessShouldSetOrderStatusToSuccess() {
//        Payment payment = new Payment(order, "COD", Map.of(
//                "address", "Jl. Margonda Raya No. 100",
//                "deliveryFee", "20000"
//        ));
//        doReturn(payment).when(paymentRepository).save(payment);
//
//        Payment result = paymentService.setStatus(payment, "SUCCESS");
//
//        assertEquals("SUCCESS", result.getStatus());
//        assertEquals(OrderStatus.SUCCESS.getValue(), order.getStatus());
//        verify(paymentRepository, times(1)).save(payment);
//    }
//
//    @Test
//    void testSetStatusToRejectedShouldSetOrderStatusToFailed() {
//        Payment payment = new Payment(order, "COD", Map.of(
//                "address", "Jl. Margonda Raya No. 100",
//                "deliveryFee", "20000"
//        ));
//        doReturn(payment).when(paymentRepository).save(payment);
//
//        Payment result = paymentService.setStatus(payment, "REJECTED");
//
//        assertEquals("REJECTED", result.getStatus());
//        assertEquals(OrderStatus.FAILED.getValue(), order.getStatus());
//        verify(paymentRepository, times(1)).save(payment);
//    }
//
//    @Test
//    void testSetStatusToUnknownShouldNotChangeOrderStatus() {
//        Payment payment = new Payment(order, "COD", Map.of(
//                "address", "Jl. Margonda Raya No. 100",
//                "deliveryFee", "20000"
//        ));
//        doReturn(payment).when(paymentRepository).save(payment);
//
//        Payment result = paymentService.setStatus(payment, "PENDING_REVIEW");
//
//        assertEquals("PENDING_REVIEW", result.getStatus());
//        assertEquals(OrderStatus.WAITING_PAYMENT.getValue(), order.getStatus());
//        verify(paymentRepository, times(1)).save(payment);
//    }

    @Test
    void testGetPaymentIfFound() {
        Payment payment = new Payment(order, "VOUCHER_CODE", Map.of(
                "voucherCode", "ESHOP1234ABC5678"
        ));
        doReturn(payment).when(paymentRepository).findById(payment.getId());

        Payment result = paymentService.getPayment(payment.getId());

        assertEquals(payment.getId(), result.getId());
    }

    @Test
    void testGetPaymentIfNotFound() {
        doReturn(null).when(paymentRepository).findById("zczc");
        assertNull(paymentService.getPayment("zczc"));
    }

    @Test
    void testGetAllPayments() {
        Payment payment1 = new Payment(order, "VOUCHER_CODE", Map.of(
                "voucherCode", "ESHOP1234ABC5678"
        ));
        Payment payment2 = new Payment(order, "COD", Map.of(
                "address", "Jl. Margonda Raya No. 100",
                "deliveryFee", "20000"
        ));
        List<Payment> payments = List.of(payment1, payment2);
        doReturn(payments).when(paymentRepository).findAll();

        List<Payment> results = paymentService.getAllPayments();

        assertEquals(2, results.size());
        assertTrue(results.contains(payment1));
        assertTrue(results.contains(payment2));
    }
}
