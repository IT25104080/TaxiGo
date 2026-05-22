# 🚖 TaxiGo – Premium Ride-Hailing & Booking Platform

Welcome to **TaxiGo**, a modern, lightweight, high-performance ride-hailing and fleet booking management system. Designed with a stunning, premium dark-theme glassmorphic interface, TaxiGo leverages Spring Boot and Thymeleaf on the backend, styled with utility-first Tailwind CSS on the frontend. It operates with a robust file-based custom data storage layer to model classic Object-Oriented Programming (OOP) concepts.

---

## 🚀 Key Features

- **Premium Modern Glassmorphism UI**: Beautiful dark-mode design with glowing amber accents, micro-animations, smooth hover states, and complete responsiveness across mobile, tablet, and desktop viewports.
- **Robust Passenger & Membership System**:
  - **Regular Passengers**: Standard ride-hailing profile with address and payment method options.
  - **Premium Passengers**: Tiered membership system (e.g. *GOLD* level) offering exclusive discount rates, loyalty point rewards, and customized experiences.
- **Intuitive Booking & Fare Calculation Engine**:
  - Direct booking form with distance estimation and dynamic fare computation.
  - Custom vehicle classes (Sedan, SUV, Luxury) with distinct pricing multipliers.
- **Secure Mock Payment Gateway**: Seamless integration from ride booking to payment simulation, issuing status updates and digital transaction records.
- **Locked Support Center (Restricted Message Box)**:
  - Supports registered customer inquiries directly to the support desk.
  - Guests are greeted with a beautiful locked lock-screen block prompting them to sign in or register.
  - Autocomplete forms for authenticated users with read-only name and email validation.
- **Stat-Rich Administrator Dashboard**:
  - Provides a single-pane-of-glass overview of system activities.
  - Displays dynamic analytics: total booking counts, gross platform revenue, registered user statistics, active taxis, and logs.
  - Allows full CRUD management of passengers and premium riders.

---

## 📂 Project Directory Structure

```text
TaxiGo/
├── data/                           # Plain Text Database Files (File-Based Data Store)
│   ├── booking.txt                 # Booking transactions
│   ├── contacts.txt                # Contact messages
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

## 🛠️ Technology Stack

- **Backend**: Spring Boot 4.x (MVC, Thymeleaf, Web DevTools)
- **Frontend**: HTML5, Thymeleaf Template Engine, Vanilla CSS + Tailwind CSS integration, FontAwesome 6 icons.
- **JDK**: Java 17
- **Build Tool**: Maven 3.x
- **Database/Persistence**: Plain Text Flat Files (delimited text architecture inside `data/`)

---

## 💎 OOP Concepts Illustrated

The TaxiGo codebase is meticulously engineered as a showcase of core **Object-Oriented Programming (OOP)** principles:

1. **Inheritance**: The domain models use inheritance cleanly. `User` is an abstract base class holding shared attributes (ID, Name, Email, Password, Phone, etc.). `Passenger` (standard regular customer) and `PremiumPassenger` (VIP level tracking) extend `User` to add specific metadata fields.
2. **Polymorphism**:
   - Class polymorphism is demonstrated in `UserFileHandler` parsing and saving lists of `User` objects polymorphically as either standard or premium passenger entities based on data tags.
   - Dynamic fare computations apply rate multipliers polymorphically based on the concrete runtime type of the passenger logged in.
3. **Encapsulation**: Strict domain-driven encapsulation using private fields, accessor/mutator methods, and protecting internal class structures from leakages.
4. **Composition & Dependency Injection**: Controllers dynamically collaborate with file handlers and calculators polymorphically via loose constructor-based composition, ensuring clean decoupling of concerns.

---

## ⚙️ Configuration & Quick Start

### 1. Prerequisites
- **Java SE Development Kit (JDK)**: Version 17 or higher.
- **Apache Maven**: Make sure Maven is installed and loaded in your system environment variable path.

### 2. Configure Port & Context Path
By default, the application runs under:
- **Port**: `8080` (can be configured in `src/main/resources/application.properties` with `server.port`)
- **Context Path**: `/taxi`

### 3. Build & Run Application
From the project root directory, run:
```bash
# Clean previous builds and compile
mvn clean compile

# Launch the Spring Boot Web Server
mvn spring-boot:run
```

Once successfully started, open your browser and navigate to:
👉 **[http://localhost:8080/taxi](http://localhost:8080/taxi)**

---

## 🔐 Credentials for Testing

| Role | Username / Email | Password |
| :--- | :--- | :--- |
| **Administrator** | `admin@taxigo.lk` | `admin123` |
| **Registered Customer** | `buddhima@gmail.com` | `wasd1211` |

---

## 📄 Main Mapped Endpoints

| URL Route | Request Method | Accessible By | Description |
| :--- | :---: | :---: | :--- |
| `/taxi/` | `GET` | All | Core Landing Page |
| `/taxi/user/login` | `GET` / `POST` | All | Customer Sign In |
| `/taxi/user/register` | `GET` / `POST` | All | Customer Account Registration |
| `/taxi/user/dashboard` | `GET` | Customer | Customer Ride Logs Dashboard |
| `/taxi/book-ride` | `GET` | Customer | Interactive Ride Request Form |
| `/taxi/contact` | `GET` / `POST` | Logged Customers Only | Secure Customer Support Inquiry Center |
| `/taxi/admin/dashboard` | `GET` | Admin | Comprehensive Stats & Activity Monitor |
