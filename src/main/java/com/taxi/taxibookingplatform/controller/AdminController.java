package com.taxi.taxibookingplatform.controller;

import com.taxi.taxibookingplatform.model.Booking;
import com.taxi.taxibookingplatform.model.ContactMessage;
import com.taxi.taxibookingplatform.model.Taxi;
import com.taxi.taxibookingplatform.model.User;
import com.taxi.taxibookingplatform.service.BookingFileHandler;
import com.taxi.taxibookingplatform.service.ContactFileHandler;
import com.taxi.taxibookingplatform.service.TaxiFileHandler;
import com.taxi.taxibookingplatform.service.UserFileHandler;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.List;

@Controller
public class AdminController {

    private boolean isNotAdmin(HttpSession session) {
        Object admin = session.getAttribute("adminLoggedIn");
        return admin == null || !(boolean) admin;
    }

    @GetMapping("/admin/dashboard")
    public String dashboard(HttpSession session, Model model) throws IOException {
        if (isNotAdmin(session)) {
            return "redirect:/user/login";
        }

        // Fetch bookings, users, contact messages, and taxis
        List<Booking> bookings = BookingFileHandler.getAllBookings();
        List<User> users = UserFileHandler.getAllUsers();
        List<ContactMessage> messages = ContactFileHandler.getAllMessages();
        List<Taxi> taxis = TaxiFileHandler.getAllTaxis();

        // Calculate statistics
        long totalBookings = bookings.size();
        long pendingBookings = bookings.stream().filter(b -> "PENDING".equalsIgnoreCase(b.getStatus())).count();
        long activeBookings = bookings.stream().filter(b -> "APPROVED".equalsIgnoreCase(b.getStatus())).count();
        long totalUsers = users.size();
        long totalMessages = messages.size();
        long totalTaxis = taxis.size();
        long availableTaxis = taxis.stream().filter(t -> "AVAILABLE".equalsIgnoreCase(t.getStatus())).count();

        // Add attributes to model
        model.addAttribute("bookings", bookings);
        model.addAttribute("users", users);
        model.addAttribute("messages", messages);
        model.addAttribute("taxis", taxis);
        
        model.addAttribute("totalBookings", totalBookings);
        model.addAttribute("pendingBookings", pendingBookings);
        model.addAttribute("activeBookings", activeBookings);
        model.addAttribute("totalUsers", totalUsers);
        model.addAttribute("totalMessages", totalMessages);
        model.addAttribute("totalTaxis", totalTaxis);
        model.addAttribute("availableTaxis", availableTaxis);

        return "admin/dashboard";
    }

    @PostMapping("/admin/bookings/update-status")
    public String updateBookingStatus(
            @RequestParam String bookingId,
            @RequestParam String status,
            HttpSession session) throws IOException {
        if (isNotAdmin(session)) {
            return "redirect:/user/login";
        }
        BookingFileHandler.updateStatus(bookingId, status);
        return "redirect:/admin/dashboard?success=statusUpdated";
    }

    @PostMapping("/admin/users/delete/{id}")
    public String deleteUser(@PathVariable String id, HttpSession session) throws IOException {
        if (isNotAdmin(session)) {
            return "redirect:/user/login";
        }
        UserFileHandler.deleteUser(id);
        return "redirect:/admin/dashboard?success=userDeleted";
    }

    @PostMapping("/admin/messages/delete/{id}")
    public String deleteMessage(@PathVariable String id, HttpSession session) throws IOException {
        if (isNotAdmin(session)) {
            return "redirect:/user/login";
        }
        ContactFileHandler.deleteMessage(id);
        return "redirect:/admin/dashboard?success=messageDeleted";
    }

    @PostMapping("/admin/taxis/add")
    public String addTaxi(
            @RequestParam String modelName,
            @RequestParam String vehicleType,
            @RequestParam String licensePlate,
            @RequestParam String driverName,
            @RequestParam String driverPhone,
            @RequestParam double ratePerKm,
            @RequestParam String status,
            @RequestParam(required = false) String imageUrl,
            HttpSession session) throws IOException {
        if (isNotAdmin(session)) {
            return "redirect:/user/login";
        }

        if (modelName == null || modelName.isBlank()) {
            return "redirect:/admin/dashboard?error=" + java.net.URLEncoder.encode("Vehicle model name is required.", java.nio.charset.StandardCharsets.UTF_8);
        }
        if (!com.taxi.taxibookingplatform.util.ValidationUtils.isValidLicensePlate(licensePlate)) {
            return "redirect:/admin/dashboard?error=" + java.net.URLEncoder.encode("Invalid license plate format. Expected standard Sri Lankan format (e.g., WP CAD-4567 or CAD-4567).", java.nio.charset.StandardCharsets.UTF_8);
        }
        if (!com.taxi.taxibookingplatform.util.ValidationUtils.isValidName(driverName)) {
            return "redirect:/admin/dashboard?error=" + java.net.URLEncoder.encode("Chauffeur name must contain only alphabetic characters and spaces (2 to 50 characters).", java.nio.charset.StandardCharsets.UTF_8);
        }
        if (!com.taxi.taxibookingplatform.util.ValidationUtils.isValidPhone(driverPhone)) {
            return "redirect:/admin/dashboard?error=" + java.net.URLEncoder.encode("Please enter a valid Sri Lankan chauffeur phone number.", java.nio.charset.StandardCharsets.UTF_8);
        }
        if (ratePerKm <= 0) {
            return "redirect:/admin/dashboard?error=" + java.net.URLEncoder.encode("Rate per kilometer must be a positive number greater than 0.", java.nio.charset.StandardCharsets.UTF_8);
        }

        String img = (imageUrl == null) ? "" : imageUrl.trim();
        String id = "TX" + (1000 + TaxiFileHandler.getAllTaxis().size() + 1);
        Taxi taxi = new Taxi(id, modelName, vehicleType, licensePlate, driverName, driverPhone, ratePerKm, status, img);
        TaxiFileHandler.addTaxi(taxi);
        return "redirect:/admin/dashboard?success=taxiAdded";
    }

    @PostMapping("/admin/taxis/update")
    public String updateTaxi(
            @RequestParam String id,
            @RequestParam String modelName,
            @RequestParam String vehicleType,
            @RequestParam String licensePlate,
            @RequestParam String driverName,
            @RequestParam String driverPhone,
            @RequestParam double ratePerKm,
            @RequestParam String status,
            @RequestParam(required = false) String imageUrl,
            HttpSession session) throws IOException {
        if (isNotAdmin(session)) {
            return "redirect:/user/login";
        }

        if (modelName == null || modelName.isBlank()) {
            return "redirect:/admin/dashboard?error=" + java.net.URLEncoder.encode("Vehicle model name is required.", java.nio.charset.StandardCharsets.UTF_8);
        }
        if (!com.taxi.taxibookingplatform.util.ValidationUtils.isValidLicensePlate(licensePlate)) {
            return "redirect:/admin/dashboard?error=" + java.net.URLEncoder.encode("Invalid license plate format. Expected standard Sri Lankan format (e.g., WP CAD-4567 or CAD-4567).", java.nio.charset.StandardCharsets.UTF_8);
        }
        if (!com.taxi.taxibookingplatform.util.ValidationUtils.isValidName(driverName)) {
            return "redirect:/admin/dashboard?error=" + java.net.URLEncoder.encode("Chauffeur name must contain only alphabetic characters and spaces (2 to 50 characters).", java.nio.charset.StandardCharsets.UTF_8);
        }
        if (!com.taxi.taxibookingplatform.util.ValidationUtils.isValidPhone(driverPhone)) {
            return "redirect:/admin/dashboard?error=" + java.net.URLEncoder.encode("Please enter a valid Sri Lankan chauffeur phone number.", java.nio.charset.StandardCharsets.UTF_8);
        }
        if (ratePerKm <= 0) {
            return "redirect:/admin/dashboard?error=" + java.net.URLEncoder.encode("Rate per kilometer must be a positive number greater than 0.", java.nio.charset.StandardCharsets.UTF_8);
        }

        String img = (imageUrl == null) ? "" : imageUrl.trim();
        Taxi taxi = new Taxi(id, modelName, vehicleType, licensePlate, driverName, driverPhone, ratePerKm, status, img);
        TaxiFileHandler.updateTaxi(taxi);
        return "redirect:/admin/dashboard?success=taxiUpdated";
    }

    @PostMapping("/admin/taxis/delete/{id}")
    public String deleteTaxi(@PathVariable String id, HttpSession session) throws IOException {
        if (isNotAdmin(session)) {
            return "redirect:/user/login";
        }
        TaxiFileHandler.deleteTaxi(id);
        return "redirect:/admin/dashboard?success=taxiDeleted";
    }

    @GetMapping("/admin/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/user/login";
    }
}
