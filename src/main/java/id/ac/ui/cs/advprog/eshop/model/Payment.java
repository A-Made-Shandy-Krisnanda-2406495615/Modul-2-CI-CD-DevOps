package id.ac.ui.cs.advprog.eshop.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import java.util.UUID;

@Getter
@Setter
public class Payment {
    private String id;
    private String method;
    private String status;
    private Map<String, String> paymentData;

    public Payment(String method, Map<String, String> paymentData) {
        this.id = UUID.randomUUID().toString();
        this.method = method;
        this.paymentData = paymentData;

        if ("VOUCHER_CODE".equals(method)) {
            this.status = isValidVoucherCode(paymentData.get("voucherCode")) ? "SUCCESS" : "REJECTED";
        } else if ("COD".equals(method)) {
            this.status = isValidCodData(paymentData) ? "SUCCESS" : "REJECTED";
        } else {
            this.status = "REJECTED";
        }
    }

    private boolean isValidVoucherCode(String voucherCode) {
        if (voucherCode == null) {
            return false;
        }
        return voucherCode.matches("^ESHOP\\d{4}[A-Za-z]{3}\\d{4}$");
    }

    private boolean isValidCodData(Map<String, String> paymentData) {
        String address = paymentData.get("address");
        String deliveryFee = paymentData.get("deliveryFee");
        return isNonEmpty(address) && isNonEmpty(deliveryFee);
    }

    private boolean isNonEmpty(String value) {
        return value != null && !value.isEmpty();
    }
}
