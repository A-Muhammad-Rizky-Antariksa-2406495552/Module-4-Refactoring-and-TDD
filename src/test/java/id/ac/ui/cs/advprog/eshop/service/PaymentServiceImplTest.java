package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.enums.OrderStatus;
import id.ac.ui.cs.advprog.eshop.enums.PaymentMethod;
import id.ac.ui.cs.advprog.eshop.enums.PaymentStatus;
import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceImplTest {
    @Mock
    private PaymentRepository paymentRepository;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    private Order order;
    private Map<String, String> voucherData;
    private Map<String, String> invalidVoucherData;
    private Map<String, String> bankTransferData;
    private Map<String, String> invalidBankTransferData;

    @BeforeEach
    void setUp() {
        List<Product> products = new ArrayList<>();
        Product product = new Product();
        product.setProductId("prod-001");
        product.setProductName("Product 1");
        product.setProductQuantity(2);
        products.add(product);

        order = new Order("order-001", products, 1708560000L, "authorA");

        voucherData = new HashMap<>();
        voucherData.put("voucherCode", "ESHOP1234ABC5678");

        invalidVoucherData = new HashMap<>();
        invalidVoucherData.put("voucherCode", "ESHOP123");

        bankTransferData = new HashMap<>();
        bankTransferData.put("bankName", "BCA");
        bankTransferData.put("referenceCode", "REF123456");

        invalidBankTransferData = new HashMap<>();
        invalidBankTransferData.put("bankName", "");
        invalidBankTransferData.put("referenceCode", "REF123456");
    }

    @Test
    void testAddPaymentVoucherCodeValid() {
        Payment mockPayment = new Payment("pay-001", PaymentMethod.VOUCHER_CODE.getValue(),
                PaymentStatus.SUCCESS.getValue(), voucherData);
        when(paymentRepository.save(any(Payment.class))).thenReturn(mockPayment);

        Payment result = paymentService.addPayment(order, PaymentMethod.VOUCHER_CODE.getValue(), voucherData);

        assertEquals(PaymentStatus.SUCCESS.getValue(), result.getStatus());
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    void testAddPaymentVoucherCodeInvalid() {
        Payment mockPayment = new Payment("pay-001", PaymentMethod.VOUCHER_CODE.getValue(),
                PaymentStatus.REJECTED.getValue(), invalidVoucherData);
        when(paymentRepository.save(any(Payment.class))).thenReturn(mockPayment);

        Payment result = paymentService.addPayment(order, PaymentMethod.VOUCHER_CODE.getValue(), invalidVoucherData);

        assertEquals(PaymentStatus.REJECTED.getValue(), result.getStatus());
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    void testAddPaymentVoucherCodeNotStartWithESHOP() {
        Map<String, String> badVoucher = new HashMap<>();
        badVoucher.put("voucherCode", "XXXXX1234ABC5678");

        Payment result = paymentService.addPayment(order, PaymentMethod.VOUCHER_CODE.getValue(), badVoucher);
        assertEquals(PaymentStatus.REJECTED.getValue(), result.getStatus());
    }

    @Test
    void testAddPaymentVoucherCodeInsufficientNumericChars() {
        Map<String, String> badVoucher = new HashMap<>();
        badVoucher.put("voucherCode", "ESHOP123ABCDEFGH");

        Payment result = paymentService.addPayment(order, PaymentMethod.VOUCHER_CODE.getValue(), badVoucher);
        assertEquals(PaymentStatus.REJECTED.getValue(), result.getStatus());
    }

    @Test
    void testAddPaymentBankTransferValid() {
        Payment mockPayment = new Payment("pay-002", PaymentMethod.BANK_TRANSFER.getValue(),
                PaymentStatus.SUCCESS.getValue(), bankTransferData);
        when(paymentRepository.save(any(Payment.class))).thenReturn(mockPayment);

        Payment result = paymentService.addPayment(order, PaymentMethod.BANK_TRANSFER.getValue(), bankTransferData);

        assertEquals(PaymentStatus.SUCCESS.getValue(), result.getStatus());
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    void testAddPaymentBankTransferEmptyBankName() {
        Payment result = paymentService.addPayment(order, PaymentMethod.BANK_TRANSFER.getValue(), invalidBankTransferData);
        assertEquals(PaymentStatus.REJECTED.getValue(), result.getStatus());
    }

    @Test
    void testAddPaymentBankTransferNullReferenceCode() {
        Map<String, String> data = new HashMap<>();
        data.put("bankName", "BCA");
        data.put("referenceCode", null);

        Payment result = paymentService.addPayment(order, PaymentMethod.BANK_TRANSFER.getValue(), data);
        assertEquals(PaymentStatus.REJECTED.getValue(), result.getStatus());
    }

    @Test
    void testSetStatusSuccessCascadesToOrder() {
        Payment payment = new Payment("pay-001", PaymentMethod.VOUCHER_CODE.getValue(), voucherData);
        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);

        Payment result = paymentService.setStatus(payment, PaymentStatus.SUCCESS.getValue(), order);

        assertEquals(PaymentStatus.SUCCESS.getValue(), result.getStatus());
        assertEquals(OrderStatus.SUCCESS.getValue(), order.getStatus());
    }

    @Test
    void testSetStatusRejectedCascadesToOrder() {
        Payment payment = new Payment("pay-001", PaymentMethod.VOUCHER_CODE.getValue(), voucherData);
        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);

        Payment result = paymentService.setStatus(payment, PaymentStatus.REJECTED.getValue(), order);

        assertEquals(PaymentStatus.REJECTED.getValue(), result.getStatus());
        assertEquals(OrderStatus.FAILED.getValue(), order.getStatus());
    }

    @Test
    void testSetStatusInvalid() {
        Payment payment = new Payment("pay-001", PaymentMethod.VOUCHER_CODE.getValue(), voucherData);
        assertThrows(IllegalArgumentException.class, () ->
                paymentService.setStatus(payment, "INVALID_STATUS", order)
        );
    }

    @Test
    void testGetPaymentByValidId() {
        Payment payment = new Payment("pay-001", PaymentMethod.VOUCHER_CODE.getValue(), voucherData);
        when(paymentRepository.getPayment("pay-001")).thenReturn(payment);

        Payment result = paymentService.getPayment("pay-001");
        assertNotNull(result);
        assertEquals("pay-001", result.getId());
    }

    @Test
    void testGetPaymentByInvalidId() {
        when(paymentRepository.getPayment("nonexistent")).thenReturn(null);

        Payment result = paymentService.getPayment("nonexistent");
        assertNull(result);
    }

    @Test
    void testGetAllPayments() {
        Payment p1 = new Payment("pay-001", PaymentMethod.VOUCHER_CODE.getValue(), voucherData);
        Payment p2 = new Payment("pay-002", PaymentMethod.BANK_TRANSFER.getValue(), bankTransferData);
        when(paymentRepository.getAllPayments()).thenReturn(List.of(p1, p2));

        List<Payment> result = paymentService.getAllPayments();
        assertEquals(2, result.size());
    }
}