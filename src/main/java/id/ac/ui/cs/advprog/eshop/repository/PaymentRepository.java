package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Payment;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class PaymentRepository {
    private List<Payment> paymentData = new ArrayList<>();

    public Payment save(Payment payment) {
        int paymentIndex = findPaymentIndexById(payment.getId());
        if (paymentIndex != -1) {
            paymentData.set(paymentIndex, payment);
            return payment;
        }

        paymentData.add(payment);
        return payment;
    }

    public Payment findById(String paymentId) {
        for (Payment savedPayment : paymentData) {
            if (savedPayment.getId().equals(paymentId)) {
                return savedPayment;
            }
        }

        return null;
    }

    public List<Payment> findAll() {
        return paymentData;
    }

    private int findPaymentIndexById(String paymentId) {
        for (int i = 0; i < paymentData.size(); i++) {
            if (paymentData.get(i).getId().equals(paymentId)) {
                return i;
            }
        }
        return -1;
    }
}
