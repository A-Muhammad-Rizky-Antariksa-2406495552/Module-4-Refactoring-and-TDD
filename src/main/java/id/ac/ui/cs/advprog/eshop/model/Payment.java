package id.ac.ui.cs.advprog.eshop.model;

import id.ac.ui.cs.advprog.eshop.enums.PaymentStatus;
import lombok.Getter;

import java.util.Map;

@Getter
public class Payment {
    private String id;
    private String orderId;
    private String method;
    private String status;
    private Map<String, String> paymentData;

    public Payment(String id, String method, Map<String, String> paymentData) {
        if (paymentData == null) {
            throw new IllegalArgumentException("paymentData must not be null");
        }
        this.id = id;
        this.method = method;
        this.paymentData = paymentData;
        this.status = PaymentStatus.WAITING_PAYMENT.getValue();
    }

    public Payment(String id, String method, String status, Map<String, String> paymentData) {
        this(id, method, paymentData);
        this.setStatus(status);
    }

    public Payment(String id, String method, Map<String, String> paymentData, String orderId) {
        this(id, method, paymentData);
        this.orderId = orderId;
    }

    public Payment(String id, String method, String status, Map<String, String> paymentData, String orderId) {
        this(id, method, paymentData, orderId);
        this.setStatus(status);
    }

    public void setStatus(String status) {
        if (!PaymentStatus.contains(status)) {
            throw new IllegalArgumentException("Invalid payment status: " + status);
        }
        this.status = status;
    }
}