# 🏦 Atomic Ledger: AI-Powered Banking Core

![Java 21](https://img.shields.io/badge/Java-21-orange?style=flat-square&logo=openjdk)
![Spring Boot 3](https://img.shields.io/badge/Spring_Boot-3.3-green?style=flat-square&logo=springboot)
![Google Gemini](https://img.shields.io/badge/AI-Google_Gemini_2.0-blue?style=flat-square&logo=google)
![PostgreSQL](https://img.shields.io/badge/Database-Supabase_PostgreSQL-336791?style=flat-square&logo=postgresql)
![Docker](https://img.shields.io/badge/Deployment-Docker_&_Render-2496ED?style=flat-square&logo=docker)

**Atomic Ledger** is a cloud-native financial switching engine built with **Java 21** and **Spring Boot 3**. It simulates a real-world banking core that intercepts raw transaction data, routes payments to the correct banking ledger (Investec, Absa, etc.), and uses **Generative AI (Google Gemini)** to enrich and categorize spending data in real-time.

> **🚀 Live Demo:** [https://atomic-ledger.onrender.com](https://atomic-ledger.onrender.com)
> *(Note: The demo runs on a free tier and may take 50s to wake up)*

---

## 📸 Dashboard

*(Add your screenshot here: `image_c34c7a.png`)*
*The application features a real-time dashboard that displays transaction history, status, and AI-predicted categories.*

---

## ⚡ Key Features

### 1. 🧠 AI-Powered Enrichment
* Uses **Google Gemini 2.0 Flash** via REST API to analyze transaction references (e.g., "Uber * 8721").
* Automatically assigns standardized categories: `Food & Dining`, `Transport`, `Entertainment`, `Utilities`.
* **Resilient Design:** If the AI service times out, the transaction still processes successfully with an "Unknown" tag to ensure 99.99% uptime.

### 2. 🚦 Smart Routing Engine
* Parses BIN codes (e.g., `INV-2026`) to route funds to the correct settlement bank (Investec, Absa, Standard Bank).
* Validates transaction integrity (e.g., blocking negative amounts or invalid formats).

### 3. 🚀 High Performance & Caching
* **In-Memory Caching:** Implements Spring Boot Caching (`@EnableCaching`) to store AI results.
* **Benefit:** Repeat transactions (e.g., "Netflix") are processed instantly (0ms latency) without hitting Google's API quota.

### 4. 🔒 Enterprise Security
* **Zero Hardcoded Secrets:** API Keys and Database Passwords are managed via **Environment Variables**.
* **Cloud Database:** Immutable transaction logs are stored in a secured **Supabase PostgreSQL** cluster.

---

## 🛠️ Tech Stack

* **Core:** Java 21 (OpenJDK), Spring Boot 3.3
* **Database:** PostgreSQL (Supabase Cloud), Hibernate/JPA
* **AI Integration:** Google Gemini Model (REST Template, JSON Processing)
* **Frontend:** HTML5, Bootstrap 5, Vanilla JS (Fetch API)
* **DevOps:** Docker, Render (Cloud PaaS), Maven
* **Testing:** JUnit 5, Mockito

---

## ⚙️ Architecture Flow

1.  **Client:** User submits payment via Web Dashboard (POST Request).
2.  **Controller:** Spring Boot receives the payload.
3.  **Switch Service:**
    * Checks Cache: *"Have we seen this reference before?"*
    * **If No:** Calls Google Gemini API to categorize.
    * **If Yes:** Returns cached category instantly.
4.  **Routing:** Determines destination bank based on Account ID.
5.  **Persistence:** Saves transaction log to PostgreSQL.
6.  **Response:** Returns JSON status to Client (Success/Fail).

---

## 📦 How to Run Locally

### Prerequisites
* Java 21+ installed
* Maven installed

### 1. Clone the Repository
```bash
git clone [https://github.com/Lwazi-M/atomic-ledger.git](https://github.com/Lwazi-M/atomic-ledger.git)
cd atomic-ledger
```

### 2. Configure Environment Variables
⚠️ **Security Notice:** This project uses Environment Variables to protect secrets.
You must set the following variables in your IDE (IntelliJ) or Terminal:

| Variable Name | Value | Description |
| :--- | :--- | :--- |
| `GEMINI_API_KEY` | `AIzaSy...` | Your Google Gemini API Key |
| `DB_PASSWORD` | `******` | Your Supabase Database Password |

### 3. Run the Application
```bash
mvn spring-boot:run
```

4.  **Access Dashboard:**
    Open `http://localhost:8080` in your browser.

## 🧪 Testing

Run the automated unit test suite to verify routing logic:
```bash
mvn test