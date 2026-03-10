package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.enums.PaymentMethod;
import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.service.OrderService;
import id.ac.ui.cs.advprog.eshop.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/order")
public class OrderController {

    private static final String ORDER_HISTORY_VIEW = "orderHistory";
    private static final String CREATE_ORDER_VIEW = "createOrder";
    private static final String PAY_ORDER_VIEW = "payOrder";
    private static final String PAYMENT_RESULT_VIEW = "paymentResult";
    private static final String REDIRECT_ORDER_HISTORY = "redirect:/order/history";

    @Autowired
    private OrderService orderService;

    @Autowired
    private PaymentService paymentService;

    @GetMapping("/create")
    public String createOrderPage() {
        return CREATE_ORDER_VIEW;
    }

    @GetMapping("/history")
    public String orderHistoryPage() {
        return ORDER_HISTORY_VIEW;
    }

    @PostMapping("/history")
    public String orderHistoryPost(@RequestParam String author, Model model) {
        List<Order> orders = orderService.findAllByAuthor(author);
        model.addAttribute("orders", orders);
        model.addAttribute("author", author);
        return ORDER_HISTORY_VIEW;
    }

    @GetMapping("/pay/{orderId}")
    public String payOrderPage(@PathVariable String orderId, Model model) {
        Order order = orderService.findById(orderId);
        model.addAttribute("order", order);
        return PAY_ORDER_VIEW;
    }

    @PostMapping("/pay/{orderId}")
    public String payOrderPost(@PathVariable String orderId,
                               @RequestParam String paymentMethod,
                               @RequestParam(required = false) String voucherCode,
                               @RequestParam(required = false) String bankName,
                               @RequestParam(required = false) String referenceCode,
                               Model model) {
        Order order = orderService.findById(orderId);

        Map<String, String> paymentData = new HashMap<>();
        if (PaymentMethod.VOUCHER_CODE.getValue().equals(paymentMethod)) {
            paymentData.put("voucherCode", voucherCode);
        } else if (PaymentMethod.BANK_TRANSFER.getValue().equals(paymentMethod)) {
            paymentData.put("bankName", bankName);
            paymentData.put("referenceCode", referenceCode);
        }

        Payment payment = paymentService.addPayment(order, paymentMethod, paymentData);

        model.addAttribute("paymentId", payment.getId());
        model.addAttribute("status", payment.getStatus());
        return PAYMENT_RESULT_VIEW;
    }
}