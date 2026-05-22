package com.taxi.taxibookingplatform.controller;

import com.taxi.taxibookingplatform.model.CustomerLogin;
import com.taxi.taxibookingplatform.model.Passenger;
import com.taxi.taxibookingplatform.model.PremiumPassenger;
import com.taxi.taxibookingplatform.model.User;
import com.taxi.taxibookingplatform.model.UserView;
import com.taxi.taxibookingplatform.service.BookingFileHandler;
import com.taxi.taxibookingplatform.service.SessionKeys;
import com.taxi.taxibookingplatform.service.UserFileHandler;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Controller
public class UserController {

    @GetMapping("/user/login")
    public String showLogin(@RequestParam(required = false) String registered, Model model) {
        if (registered != null) {
            model.addAttribute("message", "Account created. Please sign in.");
        }
        return "User/login";
    }

    @PostMapping("/user/login")
    public String login(
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam(required = false) String redirect,
            HttpSession session,
            Model model) throws IOException {

        if ("admin@taxigo.lk".equalsIgnoreCase(email) && "admin123".equals(password)) {
            session.setAttribute("adminLoggedIn", true);
            return "redirect:/admin/dashboard";
        }

        User user = UserFileHandler.getUserByEmail(email);
        if (user == null || !new CustomerLogin().login(user, email, password)) {
            model.addAttribute("error", "Invalid email or password");
            if ("book".equals(redirect)) {
                model.addAttribute("redirectBook", true);
            }
            return "User/login";
        }
        session.setAttribute(SessionKeys.USER_ID, user.getUserId());
        if ("book".equals(redirect)) {
            return "redirect:/book-ride";
        }
        return "redirect:/user/dashboard";
    }

    @GetMapping("/user/register")
    public String showRegister() {
        return "User/register";
    }

    @PostMapping("/user/register")
    public String register(
            @RequestParam String name,
            @RequestParam String email,
            @RequestParam String phone,
            @RequestParam String password,
            @RequestParam(defaultValue = "regular") String userType,
            Model model) throws IOException {

        if (!com.taxi.taxibookingplatform.util.ValidationUtils.isValidName(name)) {
            model.addAttribute("error", "Name must contain only alphabetic characters and spaces (2 to 50 characters).");
            return "User/register";
        }
        if (!com.taxi.taxibookingplatform.util.ValidationUtils.isValidEmail(email)) {
            model.addAttribute("error", "Please provide a valid email address (e.g., name@domain.com).");
            return "User/register";
        }
        if (!com.taxi.taxibookingplatform.util.ValidationUtils.isValidPhone(phone)) {
            model.addAttribute("error", "Please enter a valid Sri Lankan phone number (e.g., 0771234567 or +94771234567).");
            return "User/register";
        }
        if (!com.taxi.taxibookingplatform.util.ValidationUtils.isValidPassword(password)) {
            model.addAttribute("error", "Password must be at least 8 characters long and contain at least one uppercase letter, one lowercase letter, one numeric digit, and one special character.");
            return "User/register";
        }

        if (UserFileHandler.getUserByEmail(email) != null) {
            model.addAttribute("error", "An account with this email already exists");
            return "User/register";
        }

        String userId = "USR" + UUID.randomUUID().toString().replace("-", "").substring(0, 8);
        User user;
        if ("premium".equals(userType)) {
            user = new PremiumPassenger(userId, name, email, password, phone,
                    LocalDate.now(), "GOLD", 0.10, 0);
        } else {
            user = new Passenger(userId, name, email, password, phone,
                    LocalDate.now(), "", "CARD");
        }
        UserFileHandler.addUser(user);
        return "redirect:/user/login?registered";
    }

    @GetMapping("/user/dashboard")
    public String dashboard(
            @RequestParam(required = false) String booked,
            HttpSession session,
            Model model) throws IOException {
        User user = requireLoggedInUser(session);
        if (user == null) {
            return "redirect:/user/login";
        }
        model.addAttribute("user", UserView.from(user));
        model.addAttribute("bookings", BookingFileHandler.getBookingsByUserId(user.getUserId()));
        if (booked != null) {
            model.addAttribute("message", "Your ride has been booked successfully!");
        }
        return "User/dashboard";
    }

    @GetMapping("/user/profile")
    public String profile(HttpSession session, Model model) throws IOException {
        User user = requireLoggedInUser(session);
        if (user == null) {
            return "redirect:/user/login";
        }
        model.addAttribute("user", UserView.from(user));
        model.addAttribute("bookings", BookingFileHandler.getBookingsByUserId(user.getUserId()));
        return "User/profile";
    }

    @PostMapping("/user/profile/update")
    public String updateProfile(
            @RequestParam String name,
            @RequestParam String email,
            @RequestParam String phone,
            @RequestParam(required = false) String password,
            HttpSession session,
            Model model) throws IOException {

        User existing = requireLoggedInUser(session);
        if (existing == null) {
            return "redirect:/user/login";
        }

        if (!com.taxi.taxibookingplatform.util.ValidationUtils.isValidName(name)) {
            model.addAttribute("error", "Name must contain only alphabetic characters and spaces (2 to 50 characters).");
            model.addAttribute("user", UserView.from(existing));
            model.addAttribute("bookings", BookingFileHandler.getBookingsByUserId(existing.getUserId()));
            return "User/profile";
        }
        if (!com.taxi.taxibookingplatform.util.ValidationUtils.isValidEmail(email)) {
            model.addAttribute("error", "Please provide a valid email address.");
            model.addAttribute("user", UserView.from(existing));
            model.addAttribute("bookings", BookingFileHandler.getBookingsByUserId(existing.getUserId()));
            return "User/profile";
        }
        if (!com.taxi.taxibookingplatform.util.ValidationUtils.isValidPhone(phone)) {
            model.addAttribute("error", "Please enter a valid Sri Lankan phone number.");
            model.addAttribute("user", UserView.from(existing));
            model.addAttribute("bookings", BookingFileHandler.getBookingsByUserId(existing.getUserId()));
            return "User/profile";
        }
        if (password != null && !password.isBlank() && !com.taxi.taxibookingplatform.util.ValidationUtils.isValidPassword(password)) {
            model.addAttribute("error", "Password must be at least 8 characters long and contain at least one uppercase letter, one lowercase letter, one numeric digit, and one special character.");
            model.addAttribute("user", UserView.from(existing));
            model.addAttribute("bookings", BookingFileHandler.getBookingsByUserId(existing.getUserId()));
            return "User/profile";
        }

        User duplicate = UserFileHandler.getUserByEmail(email);
        if (duplicate != null && !duplicate.getUserId().equals(existing.getUserId())) {
            model.addAttribute("error", "An account with this email already exists");
            model.addAttribute("user", UserView.from(existing));
            model.addAttribute("bookings", BookingFileHandler.getBookingsByUserId(existing.getUserId()));
            return "User/profile";
        }

        String newPassword = (password != null && !password.isBlank())
                ? password : existing.getPassword();
        User updated;
        if (existing instanceof PremiumPassenger pp) {
            updated = new PremiumPassenger(existing.getUserId(), name, email, newPassword, phone,
                    existing.getRegisteredDate(), pp.getMembershipLevel(),
                    pp.getDiscountRate(), pp.getLoyaltyPoints());
        } else if (existing instanceof Passenger p) {
            updated = new Passenger(existing.getUserId(), name, email, newPassword, phone,
                    existing.getRegisteredDate(), p.getAddress(), p.getPreferredPayment());
        } else {
            model.addAttribute("error", "Unsupported account type");
            model.addAttribute("user", UserView.from(existing));
            return "User/profile";
        }
        UserFileHandler.updateUser(updated);
        return "redirect:/user/dashboard?success=profileUpdated";
    }

    @PostMapping("/user/delete")
    public String deleteAccount(HttpSession session) throws IOException {
        User user = requireLoggedInUser(session);
        if (user == null) {
            return "redirect:/user/login";
        }
        UserFileHandler.deleteUser(user.getUserId());
        session.invalidate();
        return "redirect:/user/login?success=accountDeleted";
    }

    @GetMapping("/user/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/user/login";
    }

    private User requireLoggedInUser(HttpSession session) throws IOException {
        Object userId = session.getAttribute(SessionKeys.USER_ID);
        if (userId == null) {
            return null;
        }
        return UserFileHandler.getUserById(userId.toString());
    }

    @GetMapping("/users")
    public String viewAllUsers(Model model) throws IOException {
        List<User> userList = UserFileHandler.getAllUsers();
        model.addAttribute("userList", userList);
        return "user-list";
    }

    @GetMapping("/users/new")
    public String showRegisterForm() {
        return "user-form";
    }

    @PostMapping("/users/passenger")
    public String createPassenger(
            @RequestParam String userId, @RequestParam String name,
            @RequestParam String email, @RequestParam String password,
            @RequestParam String phone, @RequestParam String address,
            @RequestParam String preferredPayment) throws IOException {

        Passenger p = new Passenger(userId, name, email, password, phone,
                                    LocalDate.now(), address, preferredPayment);
        UserFileHandler.addUser(p);
        return "redirect:/users";
    }

    @PostMapping("/users/premium")
    public String createPremiumPassenger(
            @RequestParam String userId, @RequestParam String name,
            @RequestParam String email, @RequestParam String password,
            @RequestParam String phone, @RequestParam String membershipLevel,
            @RequestParam double discountRate, @RequestParam int loyaltyPoints) throws IOException {

        PremiumPassenger pp = new PremiumPassenger(userId, name, email, password, phone,
                                                    LocalDate.now(), membershipLevel,
                                                    discountRate, loyaltyPoints);
        UserFileHandler.addUser(pp);
        return "redirect:/users";
    }

    @GetMapping("/users/edit/{id}")
    public String showEditForm(@PathVariable String id, Model model) throws IOException {
        User user = UserFileHandler.getUserById(id);
        if (user instanceof PremiumPassenger) {
            model.addAttribute("type", "PREMIUM");
            model.addAttribute("premiumPassenger", (PremiumPassenger) user);
        } else if (user instanceof Passenger) {
            model.addAttribute("type", "PASSENGER");
            model.addAttribute("passenger", (Passenger) user);
        } else {
            return "redirect:/users";
        }
        return "user-edit";
    }

    @PostMapping("/users/update/passenger")
    public String updatePassenger(
            @RequestParam String userId, @RequestParam String name,
            @RequestParam String email, @RequestParam String password,
            @RequestParam String phone, @RequestParam String date,
            @RequestParam String address, @RequestParam String preferredPayment) throws IOException {

        Passenger p = new Passenger(userId, name, email, password, phone,
                                    LocalDate.parse(date), address, preferredPayment);
        UserFileHandler.updateUser(p);
        return "redirect:/users";
    }

    @PostMapping("/users/update/premium")
    public String updatePremiumPassenger(
            @RequestParam String userId, @RequestParam String name,
            @RequestParam String email, @RequestParam String password,
            @RequestParam String phone, @RequestParam String date,
            @RequestParam String membershipLevel, @RequestParam double discountRate,
            @RequestParam int loyaltyPoints) throws IOException {

        PremiumPassenger pp = new PremiumPassenger(userId, name, email, password, phone,
                                                    LocalDate.parse(date), membershipLevel,
                                                    discountRate, loyaltyPoints);
        UserFileHandler.updateUser(pp);
        return "redirect:/users";
    }

    @GetMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable String id) throws IOException {
        UserFileHandler.deleteUser(id);
        return "redirect:/users";
    }
}
