package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.service.PaymentService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/payment")
public class PaymentController {
    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping("/detail")
    @ResponseBody
    public String paymentDetailFormPage() {
        return "<!DOCTYPE html>" +
                "<html lang='en'>" +
                "<head><meta charset='UTF-8'><title>Payment Detail</title></head>" +
                "<body>" +
                "<h3>Payment Detail</h3>" +
                "</body>" +
                "</html>";
    }

    @GetMapping("/detail/{paymentId}")
    @ResponseBody
    public String paymentDetailPage(@PathVariable String paymentId) {
        Payment payment = paymentService.getPayment(paymentId);
        if (payment == null) {
            return notFoundPage("Payment Detail");
        }

        return "<!DOCTYPE html>" +
                "<html lang='en'>" +
                "<head><meta charset='UTF-8'><title>Payment Detail</title></head>" +
                "<body>" +
                "<h3>Payment Detail</h3>" +
                "<p>Payment ID: <span id='paymentIdValue'>" + payment.getId() + "</span></p>" +
                "<p>Status: <span id='paymentStatusValue'>" + payment.getStatus() + "</span></p>" +
                "</body>" +
                "</html>";
    }

    @GetMapping("/admin/list")
    @ResponseBody
    public String paymentAdminListPage() {
        List<Payment> payments = paymentService.getAllPayments();
        StringBuilder rows = new StringBuilder();
        for (Payment payment : payments) {
            rows.append("<tr>")
                    .append("<td>").append(payment.getId()).append("</td>")
                    .append("<td>").append(payment.getStatus()).append("</td>")
                    .append("<td>")
                    .append("<a href='/payment/admin/detail/").append(payment.getId()).append("'>Detail</a>")
                    .append("</td>")
                    .append("</tr>");
        }

        return "<!DOCTYPE html>" +
                "<html lang='en'>" +
                "<head><meta charset='UTF-8'><title>Payment List</title></head>" +
                "<body>" +
                "<h3>Payment List</h3>" +
                "<table><tbody>" + rows + "</tbody></table>" +
                "</body>" +
                "</html>";
    }

    @GetMapping("/admin/detail/{paymentId}")
    @ResponseBody
    public String paymentAdminDetailPage(@PathVariable String paymentId) {
        Payment payment = paymentService.getPayment(paymentId);
        if (payment == null) {
            return notFoundPage("Payment Admin Detail");
        }

        return "<!DOCTYPE html>" +
                "<html lang='en'>" +
                "<head><meta charset='UTF-8'><title>Payment Admin Detail</title></head>" +
                "<body>" +
                "<h3>Payment Admin Detail</h3>" +
                "<p>Payment ID: <span id='paymentIdValue'>" + payment.getId() + "</span></p>" +
                "<p>Payment Status: <span id='paymentStatusValue'>" + payment.getStatus() + "</span></p>" +
                "<p>Order Status: <span id='orderStatusValue'>" + payment.getOrder().getStatus() + "</span></p>" +
                "<form action='/payment/admin/set-status/" + payment.getId() + "' method='post'>" +
                "<input type='hidden' name='status' value='SUCCESS'/>" +
                "<button id='acceptButton' type='submit'>Accept</button>" +
                "</form>" +
                "<form action='/payment/admin/set-status/" + payment.getId() + "' method='post'>" +
                "<input type='hidden' name='status' value='REJECTED'/>" +
                "<button id='rejectButton' type='submit'>Reject</button>" +
                "</form>" +
                "</body>" +
                "</html>";
    }

    @PostMapping("/admin/set-status/{paymentId}")
    public String paymentAdminSetStatus(
            @PathVariable String paymentId,
            @RequestParam("status") String status
    ) {
        Payment payment = paymentService.getPayment(paymentId);
        if (payment != null) {
            paymentService.setStatus(payment, status);
        }
        return "redirect:/payment/admin/detail/" + paymentId;
    }

    private String notFoundPage(String title) {
        return "<!DOCTYPE html>" +
                "<html lang='en'>" +
                "<head><meta charset='UTF-8'><title>" + title + "</title></head>" +
                "<body>" +
                "<h3>Not Found</h3>" +
                "</body>" +
                "</html>";
    }
}
