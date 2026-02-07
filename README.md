# 🏦 Atomic Ledger: AI-Powered Payment Switch

Atomic Ledger is a high-performance financial switching engine built with **Java 21** and **Spring Boot 3**. It simulates a real-world banking core that routes transactions between different banks (Investec, Absa, Standard Bank) and uses **Generative AI (Google Gemini)** to automatically categorize spending data in real-time.

## 🚀 Key Features

* **Smart Routing Engine:** Automatically routes payments to the correct destination bank based on BIN codes (e.g., `INV` -> Investec).
* **AI Enrichment:** Intercepts transaction metadata and uses **Google Gemini 2.0 Flash** to predict spending categories (e.g., "Netflix" -> "Entertainment").
* **Cloud Persistence:** Stores immutable transaction logs in a **Supabase PostgreSQL** cloud cluster.
* **Fraud Prevention:** Validates transaction integrity (e.g., blocking negative amounts) before processing.
* **REST API:** Exposes secure endpoints for transaction processing.

## 🛠️ Tech Stack

* **Backend:** Java 21 (OpenJDK), Spring Boot 3.3
* **Database:** PostgreSQL (Supabase Cloud), Hibernate/JPA
* **AI Integration:** Google Gemini Model (via REST Template)
* **Testing:** JUnit 5, Mockito
* **Tools:** Maven, Lombok, IntelliJ IDEA

## ⚙️ Architecture

1.  **Client** sends a JSON transaction request via the Web Dashboard.
2.  **Controller** receives the payload and passes it to the `TransactionSwitch` service.
3.  **Validation Layer** checks for fraud or invalid data.
4.  **AI Service** asynchronously calls Google Gemini to analyze the `reference`.
5.  **Repository Layer** saves the enriched transaction to the Cloud PostgreSQL database.
6.  **Response** is returned to the client with the routing status, AI category, and settlement details.

## 📦 How to Run

1.  **Clone the repository:**
    ```bash
    git clone [https://github.com/YourUsername/atomic-ledger.git](https://github.com/YourUsername/atomic-ledger.git)
    ```
2.  **Configure Environment:**
    Update `src/main/resources/application.properties` with your Database and AI API keys.
3.  **Run the Application:**
    ```bash
    mvn spring-boot:run
    ```
4.  **Access Dashboard:**
    Open `http://localhost:8080` in your browser.

## 🧪 Testing

Run the automated unit test suite to verify routing logic:
```bash
mvn test