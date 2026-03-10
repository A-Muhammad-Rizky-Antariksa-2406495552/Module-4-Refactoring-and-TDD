package id.ac.ui.cs.advprog.eshop.enums;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PaymentMethodTest {

    @Test
    void testContainsValidVoucherCode() {
        assertTrue(PaymentMethod.contains("VOUCHER_CODE"));
    }

    @Test
    void testContainsValidBankTransfer() {
        assertTrue(PaymentMethod.contains("BANK_TRANSFER"));
    }

    @Test
    void testContainsInvalidMethod() {
        assertFalse(PaymentMethod.contains("INVALID"));
    }

    @Test
    void testGetValue() {
        assertEquals("VOUCHER_CODE", PaymentMethod.VOUCHER_CODE.getValue());
        assertEquals("BANK_TRANSFER", PaymentMethod.BANK_TRANSFER.getValue());
    }
}