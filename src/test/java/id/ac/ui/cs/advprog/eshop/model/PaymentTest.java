package id.ac.ui.cs.advprog.eshop.model;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class PaymentTest {
    private static final String KEY_VOUCHER_CODE = "voucherCode";
    private static final String KEY_DELIVERY_FEE = "deliveryFee";
    private static final String METHOD_VOUCHER_CODE = "VOUCHER_CODE";
    private static final String METHOD_COD = "COD";
    private static final String STATUS_REJECTED = "REJECTED";

    @Test
    void testCreatePaymentWithValidVoucherCode() {
        Map<String, String> paymentData = Map.of(KEY_VOUCHER_CODE, "ESHOP1234ABC5678");

        Payment payment = new Payment(METHOD_VOUCHER_CODE, paymentData);

        assertNotNull(payment.getId());
        assertEquals(METHOD_VOUCHER_CODE, payment.getMethod());
        assertEquals("SUCCESS", payment.getStatus());
        assertEquals(paymentData, payment.getPaymentData());
    }

    @Test
    void testCreatePaymentWithInvalidVoucherCodeLength() {
        Map<String, String> paymentData = Map.of(KEY_VOUCHER_CODE, "ESHOP1234ABC567");

        Payment payment = new Payment(METHOD_VOUCHER_CODE, paymentData);

        assertEquals(STATUS_REJECTED, payment.getStatus());
    }

    @Test
    void testCreatePaymentWithInvalidVoucherCodePrefix() {
        Map<String, String> paymentData = Map.of(KEY_VOUCHER_CODE, "TOKO1234ABC5678");

        Payment payment = new Payment(METHOD_VOUCHER_CODE, paymentData);

        assertEquals(STATUS_REJECTED, payment.getStatus());
    }

    @Test
    void testCreatePaymentWithInvalidVoucherCodeDigitCount() {
        Map<String, String> paymentData = Map.of(KEY_VOUCHER_CODE, "ESHOP12345ABC678");

        Payment payment = new Payment(METHOD_VOUCHER_CODE, paymentData);

        assertEquals(STATUS_REJECTED, payment.getStatus());
    }

    @Test
    void testCreatePaymentWithValidCodData() {
        Map<String, String> paymentData = Map.of(
                "address", "Jl. Margonda Raya No. 100",
                KEY_DELIVERY_FEE, "20000"
        );

        Payment payment = new Payment(METHOD_COD, paymentData);

        assertEquals("SUCCESS", payment.getStatus());
    }

    @Test
    void testCreatePaymentWithInvalidCodAddress() {
        Map<String, String> paymentData = Map.of(
                "address", "",
                KEY_DELIVERY_FEE, "20000"
        );

        Payment payment = new Payment(METHOD_COD, paymentData);

        assertEquals(STATUS_REJECTED, payment.getStatus());
    }

    @Test
    void testCreatePaymentWithInvalidCodDeliveryFee() {
        Map<String, String> paymentData = Map.of(
                "address", "Jl. Margonda Raya No. 100",
                KEY_DELIVERY_FEE, ""
        );

        Payment payment = new Payment(METHOD_COD, paymentData);

        assertEquals(STATUS_REJECTED, payment.getStatus());
    }

    @Test
    void testCreatePaymentWithNullVoucherCode() {
        Map<String, String> paymentData = new HashMap<>();

        Payment payment = new Payment(METHOD_VOUCHER_CODE, paymentData);

        assertEquals(STATUS_REJECTED, payment.getStatus());
    }

    @Test
    void testCreatePaymentWithUnknownMethod() {
        Map<String, String> paymentData = new HashMap<>();

        Payment payment = new Payment("TRANSFER_BANK", paymentData);

        assertEquals(STATUS_REJECTED, payment.getStatus());
    }

    @Test
    void testCreatePaymentWithNullCodAddress() {
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put(KEY_DELIVERY_FEE, "20000");

        Payment payment = new Payment(METHOD_COD, paymentData);

        assertEquals(STATUS_REJECTED, payment.getStatus());
    }
}
