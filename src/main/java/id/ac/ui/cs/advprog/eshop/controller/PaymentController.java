package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.enums.PaymentStatus;
import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.service.OrderService;
import id.ac.ui.cs.advprog.eshop.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/payment")
public class PaymentController {

    private static final String PAYMENT_DETAIL_VIEW = "paymentDetail";
    private static final String PAYMENT_ADMIN_LIST_VIEW = "paymentAdminList";
    private static final String PAYMENT_ADMIN_DETAIL_VIEW = "paymentAdminDetail";
    private static final String REDIRECT_PAYMENT_ADMIN_LIST = "redirect:/payment/admin/list";

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private OrderService orderService;

    @GetMapping("/detail")
    public String paymentDetailPage() {
        return PAYMENT_DETAIL_VIEW;
    }

    @GetMapping("/detail/{paymentId}")
    public String paymentDetailById(@PathVariable String paymentId, Model model) {
        Payment payment = paymentService.getPayment(paymentId);
        model.addAttribute("payment", payment);
        model.addAttribute("paymentId", paymentId);
        return PAYMENT_DETAIL_VIEW;
    }

    @GetMapping("/admin/list")
    public String paymentAdminList(Model model) {
        List<Payment> payments = paymentService.getAllPayments();
        model.addAttribute("payments", payments);
        return PAYMENT_ADMIN_LIST_VIEW;
    }

    @GetMapping("/admin/detail/{paymentId}")
    public String paymentAdminDetail(@PathVariable String paymentId, Model model) {
        Payment payment = paymentService.getPayment(paymentId);
        model.addAttribute("payment", payment);
        model.addAttribute("paymentId", paymentId);
        return PAYMENT_ADMIN_DETAIL_VIEW;
    }

    @PostMapping("/admin/set-status/{paymentId}")
    public String setPaymentStatus(@PathVariable String paymentId,
                                   @RequestParam String status) {
        Payment payment = paymentService.getPayment(paymentId);
        if (payment != null) {
            Order order = payment.getOrderId() != null
                    ? orderService.findById(payment.getOrderId())
                    : null;
            paymentService.setStatus(payment, status, order);
        }
        return REDIRECT_PAYMENT_ADMIN_LIST;
    }
}