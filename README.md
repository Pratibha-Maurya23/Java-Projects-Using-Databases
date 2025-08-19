# Java Database Management Systems

## 1. Hotel Reservation System

**Core Features**:
- Room reservation with guest details (name, contact, room number)
- View all reservations in formatted table
- Find room number by reservation ID
- Update existing reservations (guest info, room number)
- Delete reservations
- Animated exit sequence

**Database Operations**:
- MySQL (`hotel_db` database)
- Tables: `reservations` (id, guest_name, room_no, contact_no, date)
- CRUD operations using JDBC Statements

**Code Structure**:
- Single-class implementation (`Main.java`)
- Methods for each operation:
  - `reserveRoom()`, `viewReservations()`
  - `getRoomNumber()`, `updateReservation()`
  - `deleteReservation()`, `reservationExists()`

---

## 2. Banking Management System

**Core Features**:
- User registration and login
- Account creation
- Transactions:
  - Money deposit/withdrawal
  - Funds transfer
  - Balance inquiry
- Session management

**Database Operations**:
- MySQL (`banking_system` database)
- Multi-class structure:
  - `User.java` (authentication)
  - `Accounts.java` (account operations)
  - `AccountManager.java` (transactions)
- Prepared statements for security

**Workflow**:
1. Register → Login → Account creation
2. Transaction menu:
   - Debit/Credit/Transfer/Balance
   - Logout option

---

## 3. Hospital Management System

**Core Features**:
- Patient registration/viewing
- Doctor information management
- Appointment scheduling with:
  - Patient-Doctor validation
  - Date availability checks
- MySQL integration

**Database Operations**:
- Tables: `patients`, `doctors`, `appointments`
- JDBC with prepared statements
- Conflict checking for appointments

**Class Structure**:
- `Patient.java`: CRUD operations
- `Doctor.java`: View doctor info
- `Main.java`: Menu driver

---

## Project Structure
Java-DB-Systems/
<br>
├── HotelReservation/
<br>
│ └── Main.java # All reservation operations
<br>
│
<br>
├── BankingSystem/
<br>
│ ├── BankingApp.java # Main driver
<br>
│ ├── User.java # Authentication
<br>
│ ├── Accounts.java # Account management
<br>
│ └── AccountManager.java # Transactions
<br>
│
<br>
└── HospitalManagement/
<br>
├── Main.java # Entry point
<br>
├── Patient.java # Patient operations
<br>
└── Doctor.java # Doctor operations
<br>


## Common Features
- Console-based interfaces
- MySQL JDBC connectivity
- Input validation
- Resource cleanup (connections, scanners)
- Error handling for SQL operations

**Author**:  
<br>
Pratibha Murya
<br>
pratibhacse20@gmail.com
<br>
https://github.com/Pratibha-Maurya23
