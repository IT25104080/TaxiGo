package com.taxi.taxibookingplatform.controller;

import com.taxi.taxibookingplatform.model.Booking;
import com.taxi.taxibookingplatform.model.PremiumPassenger;
import com.taxi.taxibookingplatform.model.User;
import com.taxi.taxibookingplatform.model.UserView;
import com.taxi.taxibookingplatform.service.BookingFileHandler;
import com.taxi.taxibookingplatform.service.FareCalculator;
import com.taxi.taxibookingplatform.service.SessionKeys;
import com.taxi.taxibookingplatform.service.UserFileHandler;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

@Controller
public class BookingController {

    @GetMapping("/book-ride")
    public String bookRideForm(HttpSession session, Model model) throws IOException {
        User user = getLoggedInUser(session);
        if (user == null) {
            return "redirect:/user/login?redirect=book";
        }
        model.addAttribute("user", UserView.from(user));
        return "book-ride";
    }

    @PostMapping("/book-ride")
    public String createBooking(
            @RequestParam String pickup,
            @RequestParam String dropoff,
            @RequestParam String pickupDate,
            @RequestParam(required = false) String pickupTime,
            @RequestParam(defaultValue = "standard") String vehicleType,
            @RequestParam(required = false) String notes,
            HttpSession session,
            Model model) throws IOException {

        User user = getLoggedInUser(session);
        if (user == null) {
            return "redirect:/user/login";
        }

        boolean premium = user instanceof PremiumPassenger;
        double fare = FareCalculator.estimate(pickup, dropoff, vehicleType, premium);

        LocalTime parsedTime = (pickupTime == null || pickupTime.isBlank())
                ? LocalTime.now() : LocalTime.parse(pickupTime);

        Booking booking = new Booking(
                "BK" + UUID.randomUUID().toString().replace("-", "").substring(0, 8),
                user.getUserId(),
                pickup.trim(),
                dropoff.trim(),
                LocalDate.parse(pickupDate),
                parsedTime,
                vehicleType,
                "PENDING_PAYMENT",
                fare,
                notes != null ? notes.trim() : "",
                LocalDateTime.now()
        );
        BookingFileHandler.addBooking(booking);
        return "redirect:/payment?bookingId=" + booking.getBookingId();
    }

    @GetMapping("/payment")
    public String showPaymentPortal(@RequestParam String bookingId, HttpSession session, Model model) throws IOException {
        User user = getLoggedInUser(session);
        if (user == null) {
            return "redirect:/user/login";
        }
        Booking booking = BookingFileHandler.getBookingById(bookingId);
        if (booking == null || !booking.getUserId().equals(user.getUserId())) {
            return "redirect:/user/dashboard";
        }
        model.addAttribute("booking", booking);
        model.addAttribute("user", UserView.from(user));
        return "payment";
    }

    @PostMapping("/payment/complete")
    public String completePayment(@RequestParam String bookingId, HttpSession session) throws IOException {
        User user = getLoggedInUser(session);
        if (user == null) {
            return "redirect:/user/login";
        }
        Booking booking = BookingFileHandler.getBookingById(bookingId);
        if (booking != null && booking.getUserId().equals(user.getUserId())) {
            BookingFileHandler.updateStatus(bookingId, "CONFIRMED");
            return "redirect:/user/dashboard?booked=true";
        }
        return "redirect:/user/dashboard";
    }

    @PostMapping("/user/bookings/cancel")
    public String cancelBooking(@RequestParam String bookingId, HttpSession session) throws IOException {
        User user = getLoggedInUser(session);
        if (user == null) {
            return "redirect:/user/login";
        }
        BookingFileHandler.cancelBooking(bookingId, user.getUserId());
        return "redirect:/user/dashboard";
    }

    private User getLoggedInUser(HttpSession session) throws IOException {
        Object userId = session.getAttribute(SessionKeys.USER_ID);
        if (userId == null) return null;
        return UserFileHandler.getUserById(userId.toString());
    }
}
