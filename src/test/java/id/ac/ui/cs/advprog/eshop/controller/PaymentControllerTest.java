package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.service.OrderService;
import id.ac.ui.cs.advprog.eshop.service.PaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PaymentController.class)
class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PaymentService paymentService;

    @MockBean
    private OrderService orderService;

    private Payment payment;
    private Order order;

    @BeforeEach
    void setUp() {
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("voucherCode", "ESHOP1234ABC5678");
        payment = new Payment("pay-001", "VOUCHER_CODE", "SUCCESS", paymentData, "order-001");

        List<Product> products = new ArrayList<>();
        Product product = new Product();
        product.setProductId("prod-001");
        product.setProductName("Product 1");
        product.setProductQuantity(2);
        products.add(product);
        order = new Order("order-001", products, 1708560000L, "authorA");
    }

    @Test
    void testGetPaymentDetailPage() throws Exception {
        mockMvc.perform(get("/payment/detail"))
                .andExpect(status().isOk())
                .andExpect(view().name("paymentDetail"));
    }

    @Test
    void testGetPaymentDetailById_found() throws Exception {
        when(paymentService.getPayment("pay-001")).thenReturn(payment);

        mockMvc.perform(get("/payment/detail/pay-001"))
                .andExpect(status().isOk())
                .andExpect(view().name("paymentDetail"))
                .andExpect(model().attributeExists("payment"));
    }

    @Test
    void testGetPaymentDetailById_notFound() throws Exception {
        when(paymentService.getPayment("nonexistent")).thenReturn(null);

        mockMvc.perform(get("/payment/detail/nonexistent"))
                .andExpect(status().isOk())
                .andExpect(view().name("paymentDetail"))
                .andExpect(model().attribute("payment", (Object) null));
    }

    @Test
    void testGetPaymentAdminList() throws Exception {
        when(paymentService.getAllPayments()).thenReturn(List.of(payment));

        mockMvc.perform(get("/payment/admin/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("paymentAdminList"))
                .andExpect(model().attributeExists("payments"));
    }

    @Test
    void testGetPaymentAdminList_empty() throws Exception {
        when(paymentService.getAllPayments()).thenReturn(List.of());

        mockMvc.perform(get("/payment/admin/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("paymentAdminList"))
                .andExpect(model().attributeExists("payments"));
    }

    @Test
    void testGetPaymentAdminDetail_found() throws Exception {
        when(paymentService.getPayment("pay-001")).thenReturn(payment);

        mockMvc.perform(get("/payment/admin/detail/pay-001"))
                .andExpect(status().isOk())
                .andExpect(view().name("paymentAdminDetail"))
                .andExpect(model().attributeExists("payment"));
    }

    @Test
    void testGetPaymentAdminDetail_notFound() throws Exception {
        when(paymentService.getPayment("nonexistent")).thenReturn(null);

        mockMvc.perform(get("/payment/admin/detail/nonexistent"))
                .andExpect(status().isOk())
                .andExpect(view().name("paymentAdminDetail"))
                .andExpect(model().attribute("payment", (Object) null));
    }

    @Test
    void testPostSetStatus_paymentFound_withOrder() throws Exception {
        when(paymentService.getPayment("pay-001")).thenReturn(payment);
        when(orderService.findById("order-001")).thenReturn(order);
        when(paymentService.setStatus(any(Payment.class), eq("REJECTED"), any(Order.class)))
                .thenReturn(payment);

        mockMvc.perform(post("/payment/admin/set-status/pay-001")
                        .param("status", "REJECTED"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/payment/admin/list"));
    }

    @Test
    void testPostSetStatus_paymentFound_orderNull() throws Exception {
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("voucherCode", "ESHOP1234ABC5678");
        Payment paymentNoOrder = new Payment("pay-002", "VOUCHER_CODE", "WAITING_PAYMENT", paymentData, null);

        when(paymentService.getPayment("pay-002")).thenReturn(paymentNoOrder);
        when(paymentService.setStatus(any(Payment.class), eq("SUCCESS"), isNull()))
                .thenReturn(paymentNoOrder);

        mockMvc.perform(post("/payment/admin/set-status/pay-002")
                        .param("status", "SUCCESS"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/payment/admin/list"));
    }

    @Test
    void testPostSetStatus_paymentNotFound() throws Exception {
        when(paymentService.getPayment("nonexistent")).thenReturn(null);

        mockMvc.perform(post("/payment/admin/set-status/nonexistent")
                        .param("status", "SUCCESS"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/payment/admin/list"));
    }
}