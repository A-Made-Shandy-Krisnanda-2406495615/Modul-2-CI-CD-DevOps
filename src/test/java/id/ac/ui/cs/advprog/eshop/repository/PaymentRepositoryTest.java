package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Payment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PaymentRepositoryTest {
    PaymentRepository paymentRepository;
    Payment payment1;
    Payment payment2;

    @BeforeEach
    void setUp() {
        paymentRepository = new PaymentRepository();

        payment1 = new Payment("VOUCHER_CODE", Map.of(
                "voucherCode", "ESHOP1234ABC5678"
        ));

        payment2 = new Payment("COD", Map.of(
                "address", "Jl. Margonda Raya No. 100",
                "deliveryFee", "20000"
        ));
    }

    @Test
    void testSaveCreate() {
        Payment result = paymentRepository.save(payment1);
        Payment findResult = paymentRepository.findById(payment1.getId());

        assertEquals(payment1.getId(), result.getId());
        assertEquals(payment1.getId(), findResult.getId());
        assertEquals(payment1.getMethod(), findResult.getMethod());
        assertEquals(payment1.getStatus(), findResult.getStatus());
    }

    @Test
    void testSaveUpdate() {
        paymentRepository.save(payment1);

        payment1.setStatus("REJECTED");
        Payment result = paymentRepository.save(payment1);
        Payment findResult = paymentRepository.findById(payment1.getId());

        assertEquals(payment1.getId(), result.getId());
        assertEquals("REJECTED", findResult.getStatus());
    }

    @Test
    void testFindByIdIfIdNotFound() {
        Payment findResult = paymentRepository.findById("zczc");
        assertNull(findResult);
    }

    @Test
    void testFindAll() {
        paymentRepository.save(payment1);
        paymentRepository.save(payment2);

        List<Payment> results = paymentRepository.findAll();
        assertEquals(2, results.size());
        assertEquals(payment1.getId(), results.get(0).getId());
        assertEquals(payment2.getId(), results.get(1).getId());
    }

    @Test
    void testFindAllIfEmpty() {
        List<Payment> results = paymentRepository.findAll();
        assertTrue(results.isEmpty());
    }
}
