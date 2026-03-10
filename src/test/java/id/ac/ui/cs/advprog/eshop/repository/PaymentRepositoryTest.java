package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.enums.PaymentMethod;
import id.ac.ui.cs.advprog.eshop.enums.PaymentStatus;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class PaymentRepositoryTest {
    private PaymentRepository paymentRepository;
    private Map<String, String> voucherData;
    private Map<String, String> bankTransferData;

    @BeforeEach
    void setUp() {
        paymentRepository = new PaymentRepository();

        voucherData = new HashMap<>();
        voucherData.put("voucherCode", "ESHOP1234ABC5678");

        bankTransferData = new HashMap<>();
        bankTransferData.put("bankName", "BCA");
        bankTransferData.put("referenceCode", "REF123456");
    }

    @Test
    void testSaveNewPayment() {
        Payment payment = new Payment("pay-001", PaymentMethod.VOUCHER_CODE.getValue(), voucherData);
        Payment saved = paymentRepository.save(payment);

        assertNotNull(saved);
        assertEquals("pay-001", saved.getId());
        assertEquals(PaymentMethod.VOUCHER_CODE.getValue(), saved.getMethod());
    }

    @Test
    void testSaveUpdateExistingPayment() {
        Payment payment = new Payment("pay-001", PaymentMethod.VOUCHER_CODE.getValue(), voucherData);
        paymentRepository.save(payment);

        Payment updatedPayment = new Payment("pay-001", PaymentMethod.VOUCHER_CODE.getValue(),
                PaymentStatus.SUCCESS.getValue(), voucherData);
        Payment result = paymentRepository.save(updatedPayment);

        assertEquals(PaymentStatus.SUCCESS.getValue(), result.getStatus());
        assertEquals(1, paymentRepository.getAllPayments().size());
    }

    @Test
    void testGetPaymentByValidId() {
        Payment payment = new Payment("pay-001", PaymentMethod.VOUCHER_CODE.getValue(), voucherData);
        paymentRepository.save(payment);

        Payment found = paymentRepository.getPayment("pay-001");
        assertNotNull(found);
        assertEquals("pay-001", found.getId());
    }

    @Test
    void testGetPaymentByInvalidId() {
        Payment found = paymentRepository.getPayment("nonexistent-id");
        assertNull(found);
    }

    @Test
    void testGetAllPayments() {
        Payment payment1 = new Payment("pay-001", PaymentMethod.VOUCHER_CODE.getValue(), voucherData);
        Payment payment2 = new Payment("pay-002", PaymentMethod.BANK_TRANSFER.getValue(), bankTransferData);
        paymentRepository.save(payment1);
        paymentRepository.save(payment2);

        List<Payment> all = paymentRepository.getAllPayments();
        assertEquals(2, all.size());
    }

    @Test
    void testGetAllPaymentsEmpty() {
        List<Payment> all = paymentRepository.getAllPayments();
        assertTrue(all.isEmpty());
    }

    @Test
    void testGetPaymentReturnsCorrectPayment() {
        Payment payment = new Payment("pay-001", "VOUCHER_CODE", voucherData);
        paymentRepository.save(payment);
        Payment found = paymentRepository.getPayment("pay-001");
        assertNotNull(found);
        assertEquals("pay-001", found.getId());
    }

    @Test
    void testGetPaymentWrongId() {
        Payment payment = new Payment("pay-001", "VOUCHER_CODE", voucherData);
        paymentRepository.save(payment);

        Payment found = paymentRepository.getPayment("pay-999");
        assertNull(found);
    }
}