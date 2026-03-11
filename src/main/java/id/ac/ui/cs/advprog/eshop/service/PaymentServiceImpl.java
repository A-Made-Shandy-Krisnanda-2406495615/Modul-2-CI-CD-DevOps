package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.enums.OrderStatus;
import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class PaymentServiceImpl implements PaymentService {
    private static final String STATUS_SUCCESS = "SUCCESS";
    private static final String STATUS_REJECTED = "REJECTED";

    @Autowired
    private PaymentRepository paymentRepository;

    @Override
    public Payment addPayment(Order order, String method, Map<String, String> paymentData) {
        Payment payment = new Payment(order, method, paymentData);
        return paymentRepository.save(payment);
    }

    @Override
    public Payment setStatus(Payment payment, String status) {
        payment.setStatus(status);
        if (STATUS_SUCCESS.equals(status)) {
            payment.getOrder().setStatus(OrderStatus.SUCCESS.getValue());
        } else if (STATUS_REJECTED.equals(status)) {
            payment.getOrder().setStatus(OrderStatus.FAILED.getValue());
        }
        return paymentRepository.save(payment);
    }

    @Override
    public Payment getPayment(String paymentId) {
        return paymentRepository.findById(paymentId);
    }

    @Override
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }
}
