package com.electrumprep.ledger;

// 📦 IMPORTS
// We are grabbing the main "Spring Boot" tools from the garage.
// These tools help us build web apps without writing everything from scratch.
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;

// -----------------------------------------------------------------------------------
// 🚀 THE MAIN ENGINE (The Start Button)
// This is the most important file in the entire project.
// When you click "Play" in IntelliJ, Java looks for this specific file to start up.
// -----------------------------------------------------------------------------------

@SpringBootApplication // 🏷️ STICKER: Tells Java: "This is a Spring Boot App. Please set up everything automatically."
@EnableCaching         // 🧠 MEMORY UPGRADE: Turns on the "Short-Term Memory" (RAM).
// This allows our AI Service to remember answers (like "KFC = Food") instantly.
@EnableAsync           // ⚡ TURBO MODE: Turns on "Multitasking".
// This allows the app to do heavy work in the background without freezing the screen.
public class AtomicLedgerApplication {

	// 🔑 THE IGNITION KEY
	// This looks like a normal Java main method, but it does something special.
	public static void main(String[] args) {

		// 💥 START THE ENGINE!
		// "SpringApplication.run" does the following magic:
		// 1. Scans all your folders for components (Controller, Service, Repository).
		// 2. Starts the internal Web Server (Tomcat) on Port 8080.
		// 3. Connects to the Database using your password.
		// 4. Makes the app ready to receive traffic.
		SpringApplication.run(AtomicLedgerApplication.class, args);
	}

}