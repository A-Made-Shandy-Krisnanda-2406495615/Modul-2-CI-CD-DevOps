package id.ac.ui.cs.advprog.eshop.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import java.util.UUID;

@Getter
@Setter
public class Payment {
    private static final String METHOD_VOUCHER_CODE = "VOUCHER_CODE";
    private static final String METHOD_COD = "COD";
    private static final String STATUS_SUCCESS = "SUCCESS";
    private static final String STATUS_REJECTED = "REJECTED";
    private static final String KEY_VOUCHER_CODE = "voucherCode";
    private static final String KEY_ADDRESS = "address";
    private static final String KEY_DELIVERY_FEE = "deliveryFee";
    private static final String VOUCHER_PATTERN = "^ESHOP\\d{4}[A-Za-z]{3}\\d{4}$";

    private String id;
    private Order order;
    private String method;
    private String status;
    private Map<String, String> paymentData;

    public Payment(String method, Map<String, String> paymentData) {
        this.id = UUID.randomUUID().toString();
        this.method = method;
        this.paymentData = paymentData;

        if (METHOD_VOUCHER_CODE.equals(method)) {
            this.status = isValidVoucherCode(paymentData.get(KEY_VOUCHER_CODE))
                    ? STATUS_SUCCESS
                    : STATUS_REJECTED;
        } else if (METHOD_COD.equals(method)) {
            this.status = isValidCodData(paymentData)
                    ? STATUS_SUCCESS
                    : STATUS_REJECTED;
        } else {
            this.status = STATUS_REJECTED;
        }
    }

    private boolean isValidVoucherCode(String voucherCode) {
        if (voucherCode == null) {
            return false;
        }
        return voucherCode.matches(VOUCHER_PATTERN);
    }

    private boolean isValidCodData(Map<String, String> paymentData) {
        String address = paymentData.get(KEY_ADDRESS);
        String deliveryFee = paymentData.get(KEY_DELIVERY_FEE);
        return isNonEmpty(address) && isNonEmpty(deliveryFee);
    }

    private boolean isNonEmpty(String value) {
        return value != null && !value.isEmpty();
    }

    public Payment(Order order, String method, Map<String, String> paymentData) {
        this(method, paymentData);
        this.order = order;
    }
}
