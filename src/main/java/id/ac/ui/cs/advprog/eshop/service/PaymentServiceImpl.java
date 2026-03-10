package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.enums.OrderStatus;
import id.ac.ui.cs.advprog.eshop.enums.PaymentMethod;
import id.ac.ui.cs.advprog.eshop.enums.PaymentStatus;
import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Override
    public Payment addPayment(Order order, String method, Map<String, String> paymentData) {
        String status = determineStatus(method, paymentData);
        Payment payment = new Payment(UUID.randomUUID().toString(), method, status, paymentData, order.getId());
        return paymentRepository.save(payment);
    }

    @Override
    public Payment setStatus(Payment payment, String status, Order order) {
        if (!PaymentStatus.contains(status)) {
            throw new IllegalArgumentException("Invalid payment status: " + status);
        }
        payment.setStatus(status);
        if (order != null) {
            if (PaymentStatus.SUCCESS.getValue().equals(status)) {
                order.setStatus(OrderStatus.SUCCESS.getValue());
            } else if (PaymentStatus.REJECTED.getValue().equals(status)) {
                order.setStatus(OrderStatus.FAILED.getValue());
            }
        }
        return paymentRepository.save(payment);
    }

    @Override
    public Payment getPayment(String paymentId) {
        return paymentRepository.getPayment(paymentId);
    }

    @Override
    public List<Payment> getAllPayments() {
        return paymentRepository.getAllPayments();
    }

    private String determineStatus(String method, Map<String, String> paymentData) {
        if (PaymentMethod.VOUCHER_CODE.getValue().equals(method)) {
            return validateVoucherCode(paymentData.get("voucherCode"))
                    ? PaymentStatus.SUCCESS.getValue()
                    : PaymentStatus.REJECTED.getValue();
        } else if (PaymentMethod.BANK_TRANSFER.getValue().equals(method)) {
            return validateBankTransfer(paymentData)
                    ? PaymentStatus.SUCCESS.getValue()
                    : PaymentStatus.REJECTED.getValue();
        }
        return PaymentStatus.REJECTED.getValue();
    }

    private boolean validateVoucherCode(String voucherCode) {
        if (voucherCode == null) return false;
        if (voucherCode.length() != 16) return false;
        if (!voucherCode.startsWith("ESHOP")) return false;

        long numericCount = voucherCode.chars()
                .filter(Character::isDigit)
                .count();
        return numericCount == 8;
    }

    private boolean validateBankTransfer(Map<String, String> paymentData) {
        String bankName = paymentData.get("bankName");
        String referenceCode = paymentData.get("referenceCode");
        return isNotEmpty(bankName) && isNotEmpty(referenceCode);
    }

    private boolean isNotEmpty(String value) {
        return value != null && !value.isEmpty();
    }
}