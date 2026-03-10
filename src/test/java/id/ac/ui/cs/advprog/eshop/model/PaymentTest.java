package id.ac.ui.cs.advprog.eshop.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class PaymentTest {
    private Map<String, String> voucherData;
    private Map<String, String> bankTransferData;

    @BeforeEach
    void setUp() {
        voucherData = new HashMap<>();
        voucherData.put("voucherCode", "ESHOP1234ABC5678");

        bankTransferData = new HashMap<>();
        bankTransferData.put("bankName", "BCA");
        bankTransferData.put("referenceCode", "REF123456");
    }

    @Test
    void testCreatePaymentDefaultStatus() {
        Payment payment = new Payment("pay-001", "VOUCHER_CODE", voucherData);
        assertEquals("pay-001", payment.getId());
        assertEquals("VOUCHER_CODE", payment.getMethod());
        assertEquals("WAITING_PAYMENT", payment.getStatus());
        assertEquals(voucherData, payment.getPaymentData());
    }

    @Test
    void testCreatePaymentWithSuccessStatus() {
        Payment payment = new Payment("pay-001", "VOUCHER_CODE", "SUCCESS", voucherData);
        assertEquals("SUCCESS", payment.getStatus());
    }

    @Test
    void testCreatePaymentWithRejectedStatus() {
        Payment payment = new Payment("pay-001", "VOUCHER_CODE", "REJECTED", voucherData);
        assertEquals("REJECTED", payment.getStatus());
    }

    @Test
    void testCreatePaymentWithInvalidStatus() {
        assertThrows(IllegalArgumentException.class, () ->
                new Payment("pay-001", "VOUCHER_CODE", "INVALID_STATUS", voucherData)
        );
    }

    @Test
    void testCreatePaymentNullPaymentData() {
        assertThrows(IllegalArgumentException.class, () ->
                new Payment("pay-001", "VOUCHER_CODE", null)
        );
    }

    @Test
    void testSetStatusToSuccess() {
        Payment payment = new Payment("pay-001", "VOUCHER_CODE", voucherData);
        payment.setStatus("SUCCESS");
        assertEquals("SUCCESS", payment.getStatus());
    }

    @Test
    void testSetStatusToRejected() {
        Payment payment = new Payment("pay-001", "VOUCHER_CODE", voucherData);
        payment.setStatus("REJECTED");
        assertEquals("REJECTED", payment.getStatus());
    }

    @Test
    void testSetStatusWithInvalidValue() {
        Payment payment = new Payment("pay-001", "VOUCHER_CODE", voucherData);
        assertThrows(IllegalArgumentException.class, () ->
                payment.setStatus("INVALID_STATUS")
        );
    }

    @Test
    void testCreatePaymentWithBankTransferMethod() {
        Payment payment = new Payment("pay-002", "BANK_TRANSFER", bankTransferData);
        assertEquals("BANK_TRANSFER", payment.getMethod());
        assertEquals("WAITING_PAYMENT", payment.getStatus());
        assertEquals(bankTransferData, payment.getPaymentData());
    }
}