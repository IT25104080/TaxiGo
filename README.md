# <p align="center">🚖 TaxiGo – Premium Ride-Hailing & Fleet Platform</p>

<p align="center">
  <img src="https://img.shields.io/badge/Java-17-orange?style=for-the-badge&logo=java" alt="Java 17">
  <img src="https://img.shields.io/badge/Spring%20Boot-4.0.6-brightgreen?style=for-the-badge&logo=springboot" alt="Spring Boot 4.0.6">
  <img src="https://img.shields.io/badge/Thymeleaf-HTML5-blue?style=for-the-badge&logo=thymeleaf" alt="Thymeleaf HTML5">
  <img src="https://img.shields.io/badge/Tailwind%20CSS-Modern-38B2AC?style=for-the-badge&logo=tailwindcss" alt="Tailwind CSS">
</p>

<p align="center">
  Welcome to <b>TaxiGo</b>, a lightweight, high-performance ride-hailing and fleet booking management system. Featuring an ultra-premium, dark-theme glassmorphic interface, TaxiGo leverages Spring Boot and Thymeleaf on the backend, beautifully styled with utility-first Tailwind CSS on the frontend. The platform operates with a custom file-based data storage architecture designed specifically to model real-world Object-Oriented Programming (OOP) concepts.
</p>

---

## 🚀 Core Features

- 💎 **Premium Glassmorphism UI**: High-end dark-mode design with glowing HSL amber accents, subtle micro-animations, smooth hover scaling, and full responsive layouts designed for mobile, tablet, and desktop viewports.
- 👥 **Advanced Membership Matrix**:
  - **Regular Passengers**: Standard profiles holding preferences, default addresses, and preferred payment systems.
  - **Premium Passengers**: Tiered membership tracking (e.g. VIP Level *GOLD*) offering automatic fare discounts, dynamic loyalty point rewards, and customized dashboard cards.
- 🧮 **Dynamic Fare Engine**: Dynamic calculations based on direct distances, ride categories (Sedan, SUV, Luxury), and passenger loyalty coefficients.
- 💳 **Mock Transaction Gateway**: Full payment state flow simulation resulting in logged receipts, booking status changes, and transaction records.
- 🔒 **Secure Support Center (Restricted Form)**:
  - Supports registered customer inquiries directly to the support desk.
  - Guests are greeted with a beautiful locked lock-screen block prompting them to sign in or register.
  - Autocomplete forms for authenticated users with read-only name and email validation.
- 📊 **Insight-Rich Admin Console**: Single-pane analytics tracking platform revenue, total bookings, active vehicle inventory, and user CRUD logs.

---

## 📂 Project Anatomy

Below is the directory mapping for the core system components:

```text
TaxiGo/
├── data/                           # Plain Text Database Files (File-Based Data Store)
│   ├── booking.txt                 # Booking transaction records
│   ├── contacts.txt                # Contact message support tickets
│   ├── payments.txt                # Payment log records
│   ├── taxi.txt                    # Active taxi fleets list
│   └── users.txt                   # Customers (Regular & Premium Passengers)
├── pom.xml                         # Maven dependencies & build properties
└── src/
    └── main/
        ├── java/
        │   └── com/
        │       └── taxi/
        │           └── taxibookingplatform/
        │               ├── TaxiBookingPlatformApplication.java
        │               ├── controller/             # Spring MVC Web Controllers
        │               │   ├── AdminController.java
        │               │   ├── BookingController.java
        │               │   ├── GlobalControllerAdvice.java  # Exposes session objects (currentUser) globally
        │               │   ├── HomeController.java         # Main pages and Contact Us handling
        │               │   ├── PaymentController.java      # Process payments and generate receipts
        │               │   └── UserController.java         # Login, Registration, & Profiles
        │               ├── model/                  # Domain Models (OOP Classes)
        │               │   ├── Authenticatable.java
        │               │   ├── User.java           # Base abstract class for customers
        │               │   ├── Passenger.java      # Concrete class for standard customers
        │               │   ├── PremiumPassenger.java # Concrete class for VIP customers
        │               │   ├── Taxi.java           # Fleet vehicle model
        │               │   ├── Booking.java        # Ride booking model
        │               │   ├── Payment.java        # Billing model
        │               │   ├── ContactMessage.java # Customer support tickets
        │               │   ├── CustomerLogin.java  # Authentication handler interface implementation
        │               │   └── UserView.java       # Read-only profile view DTO
        │               ├── service/                # Business Logic & Text Persistence IO Handlers
        │               │   ├── UserFileHandler.java
        │               │   ├── TaxiFileHandler.java
        │               │   ├── BookingFileHandler.java
        │               │   ├── PaymentFileHandler.java
        │               │   ├── ContactFileHandler.java
        │               │   ├── FareCalculator.java
        │               │   └── SessionKeys.java    # Standardizes Http Session properties
        │               └── util/                   # Shared Utility Classes
        │                   └── ValidationUtils.java # Server-side phone, name, and email regex validators
        └── resources/
            ├── application.properties              # Core configuration parameters (context path, ports)
            └── templates/                          # Thymeleaf HTML View Templates
                ├── User/                           # Passenger specific pages
                │   ├── dashboard.html
                │   ├── login.html
                │   ├── profile.html
                │   └── register.html
                ├── admin/                          # Control Panel templates
                │   └── dashboard.html
                ├── fragments/                      # Reusable components
                │   └── layout.html                 # Parent master template
                ├── index.html                      # Landing page
                ├── about.html                      # Dynamic about page
                ├── book-ride.html                  # Booking form page
                ├── contact.html                    # Restructured, conditional contact page
                ├── faq.html                        # Help center page
                ├── services.html                   # Platform services outline
                ├── taxis.html                      # Fleet inventory explorer
                ├── payment.html                    # Checkout page
                ├── privacy.html                    # Privacy policies
                ├── terms.html                      # Terms of service
                ├── user-list.html                  # CRUD users listing
                ├── user-form.html                  # CRUD create form
                └── user-edit.html                  # CRUD edit form
```

---

## 💎 OOP Design Showcase

The system's codebase demonstrates robust **Object-Oriented Programming (OOP)** patterns:

*   **Inheritance**: Modifies common properties cleanly via the abstract parent `User` class. Extensions such as `Passenger` and `PremiumPassenger` introduce custom data fields without duplicating boilerplate characteristics.
*   **Polymorphism**:
    *   File parsing handlers leverage subclass mapping during file read/write iterations based on class tag identifiers.
    *   Fare pricing engines dynamically apply multipliers depending on the runtime instance type of the active user session.
*   **Encapsulation**: Strict use of accessor/mutator structures to protect field values, coupled with domain model helper methods to encapsulate state changes safely.
*   **Composition & Dependency Injection**: Controllers collaborate polymorphically with handlers and calculators via constructor injection, maintaining clean separation of concerns.

---

## ⚙️ Quick Start & Local Setup

### 📋 Prerequisites
- **JDK 17** or higher installed.
- **Maven 3.x** configured in your environment system path.

### 🔌 Running the Server

1. **Clean & Compile**:
   ```bash
   mvn clean compile
   ```
2. **Start Server**:
   ```bash
   mvn spring-boot:run
   ```
3. **Open Platform**:
   Navigate to 👉 **[http://localhost:8080/taxi](http://localhost:8080/taxi)** (default port `8080`, base context `/taxi`).

---

## 🔐 Credentials Information

> [!IMPORTANT]
> To ensure platform security and support developer guidelines, raw passwords and sensitive test credentials are not stored publicly.
> - **Default Administrative Accounts**: Internal default admin users are configured securely inside [UserController.java](file:///d:/Git%20Uploads/TaxiGo/src/main/java/com/taxi/taxibookingplatform/controller/UserController.java).
> - **Registered Customer Accounts**: Active test passenger details can be read from the local file-based data store at [data/users.txt](file:///d:/Git%20Uploads/TaxiGo/data/users.txt), or new accounts can be registered on-the-fly via the Sign Up form.

---

## 📄 Main Route Endpoint Reference

| URL Route | Request Method | Allowed Role | Feature Summary |
| :--- | :---: | :---: | :--- |
| `/taxi/` | `GET` | All | Interactive Core Landing Page |
| `/taxi/user/login` | `GET` / `POST` | All | Account Login Portal |
| `/taxi/user/register` | `GET` / `POST` | All | New Account Sign Up |
| `/taxi/user/dashboard` | `GET` | Passenger | Passenger Activity dashboard |
| `/taxi/book-ride` | `GET` | Passenger | Dynamic Fare Booking Form |
| `/taxi/contact` | `GET` / `POST` | Logged In Only | Secure Contact and Message Center |
| `/taxi/admin/dashboard` | `GET` | Administrator | Stats Center & User CRUD Controls |
