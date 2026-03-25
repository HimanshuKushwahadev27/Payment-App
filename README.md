#  Payment-portal Fintech Fullstack (Production-Grade Microservices)

A **scalable, event-driven fintech backend platform** built using **Spring Boot microservices**, implementing a **wallet system, payment orchestration, and distributed transactions using the Saga pattern**.

This project simulates real-world payment systems like Paytm/Stripe/PayPal with **double-entry ledger accounting, idempotent APIs, and fault-tolerant architecture**.

---

#  System Architecture

##  High-Level Architecture

```
Client (Web / Mobile)
        │
        ▼
   API Gateway
        │
        ▼
-------------------------------------------------
|               Microservices Layer             |
-------------------------------------------------
| user-service        wallet-service           |
| payment-service     transaction-service      |
| notification-service                        |
-------------------------------------------------
        │
        ▼
-------------------------------------------------
| Infrastructure                               |
-------------------------------------------------
| Kafka (Event Bus)                            |
| PostgreSQL (DB per service)                  |
| Redis (Caching / Locks)                      |
| Keycloak (Authentication)                    |
| Stripe (Payment Gateway)                     |
-------------------------------------------------
```

---

# Core Workflows

##  1. User Onboarding

```
User → Keycloak (Signup/Login)
        │
        ▼
User Service (create profile)
        │
        ▼
Kafka → UserCreated Event
        │
        ▼
Wallet Service → Create Account + Wallet
```

---

##  2. Add Money (Card / UPI via Stripe)

```
Client
   │
   ▼
Payment Service (create payment)
   │
   ▼
Stripe (PaymentIntent)
   │
   ▼
Webhook → Payment Service
   │
   ▼
Payment SUCCESS
   │
   ▼
Kafka → PaymentSuccess
   │
   ▼
Transaction Service (record)
   │
   ▼
Wallet Service (CREDIT)
```

---

##  3. Wallet Transfer (Saga Pattern)

```
User A → Send ₹500 → User B
        │
        ▼
Payment Service (orchestrator)
        │
        ▼
Kafka → PaymentInitiated
        │
        ▼
Wallet Service
(debit sender, credit receiver)
        │
        ▼
Kafka → WalletTransferCompleted
        │
        ▼
Transaction Service
        │
        ▼
Notification Service
```

---

##  4. Failure Handling (Compensation)

```
PaymentInitiated
       │
       ▼
Wallet Debit Success
       │
       ▼
Credit Failed 
       │
       ▼
Compensation Event
       │
       ▼
Refund Sender Wallet
```

---

# 💾 Wallet Design (Double Entry Ledger)

Unlike basic wallet systems, this project uses **double-entry accounting**.

## Structure

```
Account
   │
   ├── LedgerEntries
   │      ├── DEBIT
   │      └── CREDIT
   │
   └── WalletBalance (materialized view)
```

## Rule

```
SUM(DEBITS) = SUM(CREDITS)
```

This ensures:

* financial correctness
* auditability
* consistency under failures

---

#  Tech Stack

| Layer            | Technology              |
| ---------------- | ----------------------- |
| Backend          | Spring Boot (Java)      |
| Auth             | Keycloak (OAuth2 / JWT) |
| Messaging        | Kafka                   |
| Database         | PostgreSQL              |
| Cache            | Redis                   |
| Payments         | Stripe                  |
| Build            | Maven (Multi-module)    |
| Containerization | Docker                  |

---
##  KYC & Document Verification

This system includes a **KYC (Know Your Customer)** module to verify user identity, similar to real fintech platforms.

---

##  KYC Workflow


User → Upload Documents (PAN / Aadhaar / Selfie)
→ OTP Verification (Phone)
→ KYC Status = PENDING
→ Admin Verification
→ VERIFIED / REJECTED


---

##  KYC Features

- OTP-based user verification  
- Document upload (PAN, Aadhaar, Selfie)  
- Admin approval flow  
- Status tracking: `PENDING → VERIFIED → REJECTED`  
- Secure document storage  

---

##  KYC Architecture


Frontend (Angular)
│
▼
User Service (KYC Module)
│
├── OTP Service (In-memory / Redis-ready)
├── Document Service
▼
MinIO (Object Storage)
▼
PostgreSQL (KYC + Document Metadata)


---

##  Document Storage (MinIO - S3 Compatible)

To handle file uploads efficiently, this project uses **MinIO**, an S3-compatible object storage.

###  Why MinIO?

- Direct upload from frontend (via pre-signed URLs)  
- Reduces backend load  
- Scalable and production-ready  
- Easily migratable to AWS S3  

---

##  File Upload Flow


Frontend → Request Upload URL
→ Backend generates Pre-Signed URL
→ Frontend uploads directly to MinIO
→ Backend stores file URL in DB


---

## 📡 Example API Flow


http
GET  /api/user/file/upload-url
PUT  (Pre-signed URL) → MinIO
POST /api/user/file/save-url
🗂️ Bucket Structure
kyc-documents/
   └── kyc/
       └── {userId}/
            ├── pan.jpg
            ├── aadhaar.jpg
            └── selfie.jpg

---
#  Key Features

*  Event-driven microservices architecture
*  Saga pattern for distributed transactions
*  Double-entry ledger system
*  Idempotent payment APIs
*  Stripe payment gateway integration
*  Kafka-based asynchronous communication
*  Secure authentication using Keycloak
*  Fault-tolerant and scalable design

---

#  Project Structure (Multi-Module)

```
parent
│
├── user-service
├── wallet-service
├── payment-service
├── transaction-service
├── notification-service
│
└── common (shared DTOs, enums)
```

---

#  Payment Lifecycle

```
CREATED
   ↓
PROCESSING
   ↓
SUCCESS / FAILED
   ↓
REFUNDED (optional but added)
```

---

#  Getting Started

## Prerequisites

* Java 17+
* Docker & Docker Compose
* Kafka
* PostgreSQL
* Keycloak

---

## Run Locally

```
docker-compose up --build
```

---

## Access Services

| Service     | URL                   |
| ----------- | --------------------- |
| API Gateway | http://localhost:8081 |
| Keycloak    | http://localhost:8181 |
| Kafka       | localhost:9092        |

---

#  Event-Driven Communication

Example Kafka topics:

```
payment-initiated
payment-success
wallet-transfer-completed
transaction-created
notification-events
```

---

#  Future Improvements

* Distributed Rate Limiter (Redis + Lua)
* Job Scheduler for retries
* OpenSearch for transaction search
* Prometheus + Grafana monitoring
* CI/CD pipeline (GitHub Actions)
* Kubernetes deployment

---

#  Why This Project Stands Out

This is not a CRUD project — it demonstrates:

* distributed systems design
* real-world fintech architecture
* event-driven microservices
* failure handling using Saga
* financial data consistency

---

#  Author

**Himanshu Kushwaha**

---
