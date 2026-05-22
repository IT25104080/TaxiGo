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

/**
 * ============================================================================
 * OOP CONCEPT: DEPENDENCY INJECTION (DI) & OBJECT COMPOSITION
 * ============================================================================
 * Instead of statically binding to procedural helpers, this controller uses 
 * Dependency Injection (DI) through constructor arguments. It is composed of 
 * its collaborators ('BookingFileHandler' and 'UserFileHandler'), showing low 
 * coupling, loose cohesion, and object modularity.
 * ============================================================================
 */
@Controller
public class BookingController {

    private final BookingFileHandler bookingFileHandler;
    private final UserFileHandler userFileHandler;

    public BookingController(BookingFileHandler bookingFileHandler, UserFileHandler userFileHandler) {
        this.bookingFileHandler = bookingFileHandler;
        this.userFileHandler = userFileHandler;
    }

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

        // TODO (dev): Temporary test validation to demonstrate validation errors.
        // Remove this before production. Submitting a booking with pickup="trigger-validation-error"
        // will show the validation message below.
        if (pickup != null && "trigger-validation-error".equalsIgnoreCase(pickup.trim())) {
            model.addAttribute("error", "Validation error: pickup value not accepted (test).");
            model.addAttribute("user", UserView.from(user));
            return "book-ride";
        }

        if (pickup == null || pickup.isBlank() || dropoff == null || dropoff.isBlank()) {
            model.addAttribute("error", "Pickup and Drop-off locations are required.");
            model.addAttribute("user", UserView.from(user));
            return "book-ride";
        }

        LocalDate parsedDate;
        try {
            parsedDate = LocalDate.parse(pickupDate);
        } catch (Exception e) {
            model.addAttribute("error", "Please select a valid date.");
            model.addAttribute("user", UserView.from(user));
            return "book-ride";
        }

        if (parsedDate.isBefore(LocalDate.now())) {
            model.addAttribute("error", "Pickup date cannot be in the past.");
            model.addAttribute("user", UserView.from(user));
            return "book-ride";
        }

        LocalTime parsedTime = (pickupTime == null || pickupTime.isBlank())
                ? LocalTime.now() : LocalTime.parse(pickupTime);

        if (parsedDate.equals(LocalDate.now()) && parsedTime.isBefore(LocalTime.now().minusMinutes(5))) {
            model.addAttribute("error", "Pickup time must be in the future.");
            model.addAttribute("user", UserView.from(user));
            return "book-ride";
        }

        boolean premium = user instanceof PremiumPassenger;
        double fare = FareCalculator.estimate(pickup, dropoff, vehicleType, premium);

        Booking booking = new Booking(
                "BK" + UUID.randomUUID().toString().replace("-", "").substring(0, 8),
                user.getUserId(),
                pickup.trim(),
                dropoff.trim(),
                parsedDate,
                parsedTime,
                vehicleType,
                "PENDING_PAYMENT",
                fare,
                notes != null ? notes.trim() : "",
                LocalDateTime.now()
        );
        bookingFileHandler.addBooking(booking);
        return "redirect:/payment?bookingId=" + booking.getBookingId();
    }

    @PostMapping("/user/bookings/cancel")
    public String cancelBooking(@RequestParam String bookingId, HttpSession session) throws IOException {
        User user = getLoggedInUser(session);
        if (user == null) {
            return "redirect:/user/login";
        }
        bookingFileHandler.cancelBooking(bookingId, user.getUserId());
        return "redirect:/user/dashboard";
    }

    private User getLoggedInUser(HttpSession session) throws IOException {
        Object userId = session.getAttribute(SessionKeys.USER_ID);
        if (userId == null) return null;
        return userFileHandler.getUserById(userId.toString());
    }
}
