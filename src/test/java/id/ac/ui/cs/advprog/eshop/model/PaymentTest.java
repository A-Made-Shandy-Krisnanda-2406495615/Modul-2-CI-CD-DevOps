package id.ac.ui.cs.advprog.eshop.model;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class PaymentTest {

    @Test
    void testCreatePaymentWithValidVoucherCode() {
        Map<String, String> paymentData = Map.of("voucherCode", "ESHOP1234ABC5678");

        Payment payment = new Payment("VOUCHER_CODE", paymentData);

        assertNotNull(payment.getId());
        assertEquals("VOUCHER_CODE", payment.getMethod());
        assertEquals("SUCCESS", payment.getStatus());
        assertEquals(paymentData, payment.getPaymentData());
    }

    @Test
    void testCreatePaymentWithInvalidVoucherCodeLength() {
        Map<String, String> paymentData = Map.of("voucherCode", "ESHOP1234ABC567");

        Payment payment = new Payment("VOUCHER_CODE", paymentData);

        assertEquals("REJECTED", payment.getStatus());
    }

    @Test
    void testCreatePaymentWithInvalidVoucherCodePrefix() {
        Map<String, String> paymentData = Map.of("voucherCode", "TOKO1234ABC5678");

        Payment payment = new Payment("VOUCHER_CODE", paymentData);

        assertEquals("REJECTED", payment.getStatus());
    }

    @Test
    void testCreatePaymentWithInvalidVoucherCodeDigitCount() {
        Map<String, String> paymentData = Map.of("voucherCode", "ESHOP12345ABC678");

        Payment payment = new Payment("VOUCHER_CODE", paymentData);

        assertEquals("REJECTED", payment.getStatus());
    }

    @Test
    void testCreatePaymentWithValidCodData() {
        Map<String, String> paymentData = Map.of(
                "address", "Jl. Margonda Raya No. 100",
                "deliveryFee", "20000"
        );

        Payment payment = new Payment("COD", paymentData);

        assertEquals("SUCCESS", payment.getStatus());
    }

    @Test
    void testCreatePaymentWithInvalidCodAddress() {
        Map<String, String> paymentData = Map.of(
                "address", "",
                "deliveryFee", "20000"
        );

        Payment payment = new Payment("COD", paymentData);

        assertEquals("REJECTED", payment.getStatus());
    }

    @Test
    void testCreatePaymentWithInvalidCodDeliveryFee() {
        Map<String, String> paymentData = Map.of(
                "address", "Jl. Margonda Raya No. 100",
                "deliveryFee", ""
        );

        Payment payment = new Payment("COD", paymentData);

        assertEquals("REJECTED", payment.getStatus());
    }
}
