package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.enums.OrderStatus;
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

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @MockBean
    private PaymentService paymentService;

    private Order order;
    private Payment payment;

    @BeforeEach
    void setUp() {
        List<Product> products = new ArrayList<>();
        Product product = new Product();
        product.setProductId("prod-001");
        product.setProductName("Product 1");
        product.setProductQuantity(2);
        products.add(product);

        order = new Order("order-001", products, 1708560000L, "authorA");

        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("voucherCode", "ESHOP1234ABC5678");
        payment = new Payment("pay-001", "VOUCHER_CODE", "SUCCESS", paymentData, "order-001");
    }

    @Test
    void testGetCreateOrderPage() throws Exception {
        mockMvc.perform(get("/order/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("createOrder"));
    }

    @Test
    void testGetOrderHistoryPage() throws Exception {
        mockMvc.perform(get("/order/history"))
                .andExpect(status().isOk())
                .andExpect(view().name("orderHistory"));
    }

    @Test
    void testPostOrderHistory() throws Exception {
        when(orderService.findAllByAuthor("authorA")).thenReturn(List.of(order));

        mockMvc.perform(post("/order/history")
                        .param("author", "authorA"))
                .andExpect(status().isOk())
                .andExpect(view().name("orderHistory"))
                .andExpect(model().attributeExists("orders"))
                .andExpect(model().attribute("author", "authorA"));
    }

    @Test
    void testPostOrderHistory_noOrders() throws Exception {
        when(orderService.findAllByAuthor("unknown")).thenReturn(List.of());

        mockMvc.perform(post("/order/history")
                        .param("author", "unknown"))
                .andExpect(status().isOk())
                .andExpect(view().name("orderHistory"))
                .andExpect(model().attributeExists("orders"));
    }

    @Test
    void testGetPayOrderPage() throws Exception {
        when(orderService.findById("order-001")).thenReturn(order);

        mockMvc.perform(get("/order/pay/order-001"))
                .andExpect(status().isOk())
                .andExpect(view().name("payOrder"))
                .andExpect(model().attributeExists("order"));
    }

    @Test
    void testPostPayOrder_voucherCode() throws Exception {
        when(orderService.findById("order-001")).thenReturn(order);
        when(paymentService.addPayment(any(Order.class), eq("VOUCHER_CODE"), any(Map.class)))
                .thenReturn(payment);

        mockMvc.perform(post("/order/pay/order-001")
                        .param("paymentMethod", "VOUCHER_CODE")
                        .param("voucherCode", "ESHOP1234ABC5678"))
                .andExpect(status().isOk())
                .andExpect(view().name("paymentResult"))
                .andExpect(model().attributeExists("paymentId"))
                .andExpect(model().attributeExists("status"));
    }

    @Test
    void testPostPayOrder_bankTransfer() throws Exception {
        Map<String, String> bankData = new HashMap<>();
        bankData.put("bankName", "BCA");
        bankData.put("referenceCode", "REF123");
        Payment bankPayment = new Payment("pay-002", "BANK_TRANSFER", "SUCCESS", bankData, "order-001");

        when(orderService.findById("order-001")).thenReturn(order);
        when(paymentService.addPayment(any(Order.class), eq("BANK_TRANSFER"), any(Map.class)))
                .thenReturn(bankPayment);

        mockMvc.perform(post("/order/pay/order-001")
                        .param("paymentMethod", "BANK_TRANSFER")
                        .param("bankName", "BCA")
                        .param("referenceCode", "REF123"))
                .andExpect(status().isOk())
                .andExpect(view().name("paymentResult"))
                .andExpect(model().attributeExists("paymentId"))
                .andExpect(model().attributeExists("status"));
    }

    @Test
    void testPostPayOrder_unknownMethod() throws Exception {
        Map<String, String> data = new HashMap<>();
        Payment unknownPayment = new Payment("pay-003", "UNKNOWN", "REJECTED", data, "order-001");

        when(orderService.findById("order-001")).thenReturn(order);
        when(paymentService.addPayment(any(Order.class), eq("UNKNOWN"), any(Map.class)))
                .thenReturn(unknownPayment);

        mockMvc.perform(post("/order/pay/order-001")
                        .param("paymentMethod", "UNKNOWN"))
                .andExpect(status().isOk())
                .andExpect(view().name("paymentResult"));
    }
}