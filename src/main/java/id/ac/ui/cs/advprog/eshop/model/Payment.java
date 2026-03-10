package id.ac.ui.cs.advprog.eshop.model;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Getter
public class Payment {
    private final String id;
    private final String method;
    private String status;
    private final Map<String, String> paymentData;

    private static final List<String> VALID_STATUSES = Arrays.asList(
            "WAITING_PAYMENT",
            "SUCCESS",
            "REJECTED"
    );

    public Payment(String id, String method, Map<String, String> paymentData) {
        if (paymentData == null) {
            throw new IllegalArgumentException("paymentData must not be null");
        }
        this.id = id;
        this.method = method;
        this.paymentData = paymentData;
        this.status = "WAITING_PAYMENT";
    }

    public Payment(String id, String method, String status, Map<String, String> paymentData) {
        this(id, method, paymentData);
        this.setStatus(status);
    }

    public void setStatus(String status) {
        if (!VALID_STATUSES.contains(status)) {
            throw new IllegalArgumentException("Invalid payment status: " + status);
        }
        this.status = status;
    }
}