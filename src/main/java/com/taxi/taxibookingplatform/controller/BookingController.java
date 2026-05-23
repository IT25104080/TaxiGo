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
 * Controller responsible for managing passenger ride booking requests,
 * payment checkout redirects, payment completion status updates, and booking cancellations.
 */
@Controller
public class BookingController {

    /**
     * Handles GET requests to render the ride booking checkout page.
     * Validates that the passenger is authenticated in the active session.
     *
     * @param session the current HTTP session
     * @param model the UI Model to expose attributes to the template
     * @return the template name "book-ride" if authenticated, or a redirect to the login page otherwise
     * @throws IOException if user details lookup fails
     */
    @GetMapping("/book-ride")
    public String bookRideForm(HttpSession session, Model model) throws IOException {
        User user = getLoggedInUser(session);
        if (user == null) {
            return "redirect:/user/login?redirect=book";
        }
        model.addAttribute("user", UserView.from(user));
        return "book-ride";
    }

    /**
     * Processes the submitted booking details form, calculates the custom fare,
     * persists the new booking record with a PENDING_PAYMENT status, and routes to payment.
     *
     * @param pickup starting address
     * @param dropoff ending destination
     * @param pickupDate departure date
     * @param pickupTime optional departure time (defaults to current time if absent)
     * @param vehicleType requested category of taxi (standard, premium, xl)
     * @param notes optional driver instructions
     * @param session the current HTTP session
     * @param model the UI Model to expose attributes
     * @return redirect path to the payment portal
     * @throws IOException if booking persistence fails
     */
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

        // Apply discount modifier if user is a premium passenger
        boolean premium = user instanceof PremiumPassenger;
        double fare = FareCalculator.estimate(pickup, dropoff, vehicleType, premium);

        LocalTime parsedTime = (pickupTime == null || pickupTime.isBlank())
                ? LocalTime.now() : LocalTime.parse(pickupTime);

        LocalDate parsedDate;
        try {
            parsedDate = LocalDate.parse(pickupDate);
        } catch (Exception e) {
            try {
                parsedDate = LocalDate.parse(pickupDate, java.time.format.DateTimeFormatter.ofPattern("dd MM yyyy"));
            } catch (Exception ex) {
                try {
                    parsedDate = LocalDate.parse(pickupDate, java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy"));
                } catch (Exception ex2) {
                    parsedDate = LocalDate.now();
                }
            }
        }

        // Generate a random booking ID prefix with "BK"
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
        BookingFileHandler.addBooking(booking);
        return "redirect:/payment?bookingId=" + booking.getBookingId();
    }

    /**
     * Renders the payment transaction portal for a pending booking.
     * Restricts access only to the passenger who generated the booking.
     *
     * @param bookingId the booking ID requesting payment
     * @param session the current HTTP session
     * @param model the UI model to expose attributes
     * @return the template name "payment", or dashboard redirects on mismatch
     * @throws IOException if booking lookup fails
     */
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

    /**
     * Completes a simulated payment and updates the booking status to CONFIRMED.
     *
     * @param bookingId the unique identifier of the paid booking
     * @param session the current HTTP session
     * @return dashboard redirect with a success parameter
     * @throws IOException if status persistence fails
     */
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

    /**
     * Cancels an existing booking after authenticating ownership validation.
     *
     * @param bookingId the unique identifier of the booking to cancel
     * @param session the current HTTP session
     * @return dashboard redirect after cancellation
     * @throws IOException if cancellation write fails
     */
    @PostMapping("/user/bookings/cancel")
    public String cancelBooking(@RequestParam String bookingId, HttpSession session) throws IOException {
        User user = getLoggedInUser(session);
        if (user == null) {
            return "redirect:/user/login";
        }
        BookingFileHandler.cancelBooking(bookingId, user.getUserId());
        return "redirect:/user/dashboard";
    }

    /**
     * Resolves the currently logged-in user details using the user ID saved in HTTP Session.
     */
    private User getLoggedInUser(HttpSession session) throws IOException {
        Object userId = session.getAttribute(SessionKeys.USER_ID);
        if (userId == null) return null;
        return UserFileHandler.getUserById(userId.toString());
    }
}
