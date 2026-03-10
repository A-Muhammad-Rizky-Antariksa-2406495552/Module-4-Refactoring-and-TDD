package id.ac.ui.cs.advprog.eshop.model;

import id.ac.ui.cs.advprog.eshop.enums.PaymentMethod;
import id.ac.ui.cs.advprog.eshop.enums.PaymentStatus;
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
        Payment payment = new Payment("pay-001", PaymentMethod.VOUCHER_CODE.getValue(), voucherData);
        assertEquals("pay-001", payment.getId());
        assertEquals(PaymentMethod.VOUCHER_CODE.getValue(), payment.getMethod());
        assertEquals(PaymentStatus.WAITING_PAYMENT.getValue(), payment.getStatus());
        assertEquals(voucherData, payment.getPaymentData());
    }

    @Test
    void testCreatePaymentWithSuccessStatus() {
        Payment payment = new Payment("pay-001", PaymentMethod.VOUCHER_CODE.getValue(),
                PaymentStatus.SUCCESS.getValue(), voucherData);
        assertEquals(PaymentStatus.SUCCESS.getValue(), payment.getStatus());
    }

    @Test
    void testCreatePaymentWithRejectedStatus() {
        Payment payment = new Payment("pay-001", PaymentMethod.VOUCHER_CODE.getValue(),
                PaymentStatus.REJECTED.getValue(), voucherData);
        assertEquals(PaymentStatus.REJECTED.getValue(), payment.getStatus());
    }

    @Test
    void testCreatePaymentWithInvalidStatus() {
        assertThrows(IllegalArgumentException.class, () ->
                new Payment("pay-001", PaymentMethod.VOUCHER_CODE.getValue(),
                        "INVALID_STATUS", voucherData)
        );
    }

    @Test
    void testCreatePaymentNullPaymentData() {
        assertThrows(IllegalArgumentException.class, () ->
                new Payment("pay-001", PaymentMethod.VOUCHER_CODE.getValue(), null)
        );
    }

    @Test
    void testSetStatusToSuccess() {
        Payment payment = new Payment("pay-001", PaymentMethod.VOUCHER_CODE.getValue(), voucherData);
        payment.setStatus(PaymentStatus.SUCCESS.getValue());
        assertEquals(PaymentStatus.SUCCESS.getValue(), payment.getStatus());
    }

    @Test
    void testSetStatusToRejected() {
        Payment payment = new Payment("pay-001", PaymentMethod.VOUCHER_CODE.getValue(), voucherData);
        payment.setStatus(PaymentStatus.REJECTED.getValue());
        assertEquals(PaymentStatus.REJECTED.getValue(), payment.getStatus());
    }

    @Test
    void testSetStatusWithInvalidValue() {
        Payment payment = new Payment("pay-001", PaymentMethod.VOUCHER_CODE.getValue(), voucherData);
        assertThrows(IllegalArgumentException.class, () ->
                payment.setStatus("INVALID_STATUS")
        );
    }

    @Test
    void testCreatePaymentWithBankTransferMethod() {
        Payment payment = new Payment("pay-002", PaymentMethod.BANK_TRANSFER.getValue(), bankTransferData);
        assertEquals(PaymentMethod.BANK_TRANSFER.getValue(), payment.getMethod());
        assertEquals(PaymentStatus.WAITING_PAYMENT.getValue(), payment.getStatus());
        assertEquals(bankTransferData, payment.getPaymentData());
    }
}