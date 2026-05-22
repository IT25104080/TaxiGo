package com.taxi.taxibookingplatform.controller;

import com.taxi.taxibookingplatform.model.Booking;
import com.taxi.taxibookingplatform.model.Payment;
import com.taxi.taxibookingplatform.model.User;
import com.taxi.taxibookingplatform.model.UserView;
import com.taxi.taxibookingplatform.service.BookingFileHandler;
import com.taxi.taxibookingplatform.service.PaymentFileHandler;
import com.taxi.taxibookingplatform.service.SessionKeys;
import com.taxi.taxibookingplatform.service.UserFileHandler;
import com.taxi.taxibookingplatform.util.ValidationUtils;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * ============================================================================
 * OOP CONCEPT: DEPENDENCY INJECTION (DI) & OBJECT COMPOSITION
 * ============================================================================
 * Collaborates with BookingFileHandler, PaymentFileHandler, and UserFileHandler 
 * polymorphically via loose constructor composition.
 * ============================================================================
 */
@Controller
public class PaymentController {

    private final BookingFileHandler bookingFileHandler;
    private final PaymentFileHandler paymentFileHandler;
    private final UserFileHandler userFileHandler;

    public PaymentController(BookingFileHandler bookingFileHandler, 
                             PaymentFileHandler paymentFileHandler, 
                             UserFileHandler userFileHandler) {
        this.bookingFileHandler = bookingFileHandler;
        this.paymentFileHandler = paymentFileHandler;
        this.userFileHandler = userFileHandler;
    }

    @GetMapping("/payment")
    public String showPaymentPortal(@RequestParam String bookingId, @RequestParam(required = false) String error, HttpSession session, Model model) throws IOException {
        User user = getLoggedInUser(session);
        if (user == null) {
            return "redirect:/user/login";
        }
        Booking booking = bookingFileHandler.getBookingById(bookingId);
        if (booking == null || !booking.getUserId().equals(user.getUserId())) {
            return "redirect:/user/dashboard";
        }
        model.addAttribute("booking", booking);
        model.addAttribute("user", UserView.from(user));
        if (error != null) {
            model.addAttribute("error", error);
        }
        return "payment";
    }

    @PostMapping("/payment/complete")
    public String completePayment(
            @RequestParam String bookingId,
            @RequestParam String cardNumber,
            @RequestParam String expiryDate,
            @RequestParam String cvv,
            HttpSession session,
            Model model) throws IOException {

        User user = getLoggedInUser(session);
        if (user == null) {
            return "redirect:/user/login";
        }

        Booking booking = bookingFileHandler.getBookingById(bookingId);
        if (booking == null || !booking.getUserId().equals(user.getUserId())) {
            return "redirect:/user/dashboard";
        }

        // Validate inputs using ValidationUtils
        if (!ValidationUtils.isValidCardNumber(cardNumber)) {
            model.addAttribute("booking", booking);
            model.addAttribute("user", UserView.from(user));
            model.addAttribute("error", "Invalid credit card number. Please check digits and try again.");
            return "payment";
        }

        if (!ValidationUtils.isValidCardExpiry(expiryDate)) {
            model.addAttribute("booking", booking);
            model.addAttribute("user", UserView.from(user));
            model.addAttribute("error", "Invalid or expired expiration date. Expected format MM/YY.");
            return "payment";
        }

        if (!ValidationUtils.isValidCardCVV(cvv)) {
            model.addAttribute("booking", booking);
            model.addAttribute("user", UserView.from(user));
            model.addAttribute("error", "Invalid CVV security code. Expected 3 or 4 digits.");
            return "payment";
        }

        // Mask the credit card number (e.g., **** **** **** 4567)
        String cleaned = cardNumber.replace(" ", "").replace("-", "");
        String maskedCard = "**** **** **** " + cleaned.substring(cleaned.length() - 4);

        // Generate Transaction ID
        String paymentId = "PM" + UUID.randomUUID().toString().replace("-", "").substring(0, 8).toUpperCase();

        // Create Payment record
        Payment payment = new Payment(
                paymentId,
                bookingId,
                user.getUserId(),
                booking.getFare(),
                maskedCard,
                "SUCCESS",
                LocalDateTime.now()
        );

        // Save Transaction
        paymentFileHandler.save(payment);

        // Confirm booking
        bookingFileHandler.updateStatus(bookingId, "CONFIRMED");

        return "redirect:/user/dashboard?booked=true";
    }

    private User getLoggedInUser(HttpSession session) throws IOException {
        Object userId = session.getAttribute(SessionKeys.USER_ID);
        if (userId == null) return null;
        return userFileHandler.getUserById(userId.toString());
    }
}
