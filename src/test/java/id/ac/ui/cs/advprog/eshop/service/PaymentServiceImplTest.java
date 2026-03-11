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
    private static final String KEY_VOUCHER_CODE = "voucherCode";
    private static final String KEY_ADDRESS = "address";
    private static final String KEY_DELIVERY_FEE = "deliveryFee";
    private static final String METHOD_VOUCHER_CODE = "VOUCHER_CODE";
    private static final String METHOD_COD = "COD";
    private static final String STATUS_SUCCESS = "SUCCESS";
    private static final String STATUS_REJECTED = "REJECTED";
    private static final String ADDRESS = "Jl. Margonda Raya No. 100";
    private static final String DELIVERY_FEE = "20000";

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
        Map<String, String> paymentData = Map.of(KEY_VOUCHER_CODE, "ESHOP1234ABC5678");
        doAnswer(invocation -> invocation.getArgument(0)).when(paymentRepository).save(any(Payment.class));

        Payment result = paymentService.addPayment(order, METHOD_VOUCHER_CODE, paymentData);

        verify(paymentRepository, times(1)).save(any(Payment.class));
        assertEquals(METHOD_VOUCHER_CODE, result.getMethod());
        assertEquals(STATUS_SUCCESS, result.getStatus());
        assertEquals(order, result.getOrder());
    }

    @Test
    void testAddPaymentWithInvalidVoucherCode() {
        Map<String, String> paymentData = Map.of(KEY_VOUCHER_CODE, "MEOW1234ABC5678");
        doAnswer(invocation -> invocation.getArgument(0)).when(paymentRepository).save(any(Payment.class));

        Payment result = paymentService.addPayment(order, METHOD_VOUCHER_CODE, paymentData);

        verify(paymentRepository, times(1)).save(any(Payment.class));
        assertEquals(STATUS_REJECTED, result.getStatus());
    }

    @Test
    void testAddPaymentWithValidCodData() {
        Map<String, String> paymentData = Map.of(
                KEY_ADDRESS, ADDRESS,
                KEY_DELIVERY_FEE, DELIVERY_FEE
        );
        doAnswer(invocation -> invocation.getArgument(0)).when(paymentRepository).save(any(Payment.class));

        Payment result = paymentService.addPayment(order, METHOD_COD, paymentData);

        verify(paymentRepository, times(1)).save(any(Payment.class));
        assertEquals(STATUS_SUCCESS, result.getStatus());
        assertEquals(order, result.getOrder());
    }

    @Test
    void testAddPaymentWithInvalidCodData() {
        Map<String, String> paymentData = Map.of(
                KEY_ADDRESS, "",
                KEY_DELIVERY_FEE, DELIVERY_FEE
        );
        doAnswer(invocation -> invocation.getArgument(0)).when(paymentRepository).save(any(Payment.class));

        Payment result = paymentService.addPayment(order, METHOD_COD, paymentData);

        verify(paymentRepository, times(1)).save(any(Payment.class));
        assertEquals(STATUS_REJECTED, result.getStatus());
    }

    @Test
    void testSetStatusToSuccessShouldSetOrderStatusToSuccess() {
        Payment payment = new Payment(order, METHOD_COD, Map.of(
                KEY_ADDRESS, ADDRESS,
                KEY_DELIVERY_FEE, DELIVERY_FEE
        ));
        doReturn(payment).when(paymentRepository).save(payment);

        Payment result = paymentService.setStatus(payment, STATUS_SUCCESS);

        assertEquals(STATUS_SUCCESS, result.getStatus());
        assertEquals(OrderStatus.SUCCESS.getValue(), order.getStatus());
        verify(paymentRepository, times(1)).save(payment);
    }

    @Test
    void testSetStatusToRejectedShouldSetOrderStatusToFailed() {
        Payment payment = new Payment(order, METHOD_COD, Map.of(
                KEY_ADDRESS, ADDRESS,
                KEY_DELIVERY_FEE, DELIVERY_FEE
        ));
        doReturn(payment).when(paymentRepository).save(payment);

        Payment result = paymentService.setStatus(payment, STATUS_REJECTED);

        assertEquals(STATUS_REJECTED, result.getStatus());
        assertEquals(OrderStatus.FAILED.getValue(), order.getStatus());
        verify(paymentRepository, times(1)).save(payment);
    }

    @Test
    void testSetStatusToUnknownShouldNotChangeOrderStatus() {
        Payment payment = new Payment(order, METHOD_COD, Map.of(
                KEY_ADDRESS, ADDRESS,
                KEY_DELIVERY_FEE, DELIVERY_FEE
        ));
        doReturn(payment).when(paymentRepository).save(payment);

        Payment result = paymentService.setStatus(payment, "PENDING_REVIEW");

        assertEquals("PENDING_REVIEW", result.getStatus());
        assertEquals(OrderStatus.WAITING_PAYMENT.getValue(), order.getStatus());
        verify(paymentRepository, times(1)).save(payment);
    }

    @Test
    void testGetPaymentIfFound() {
        Payment payment = new Payment(order, METHOD_VOUCHER_CODE, Map.of(
                KEY_VOUCHER_CODE, "ESHOP1234ABC5678"
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
        Payment payment1 = new Payment(order, METHOD_VOUCHER_CODE, Map.of(
                KEY_VOUCHER_CODE, "ESHOP1234ABC5678"
        ));
        Payment payment2 = new Payment(order, METHOD_COD, Map.of(
                KEY_ADDRESS, ADDRESS,
                KEY_DELIVERY_FEE, DELIVERY_FEE
        ));
        List<Payment> payments = List.of(payment1, payment2);
        doReturn(payments).when(paymentRepository).findAll();

        List<Payment> results = paymentService.getAllPayments();

        assertEquals(2, results.size());
        assertTrue(results.contains(payment1));
        assertTrue(results.contains(payment2));
    }
}
