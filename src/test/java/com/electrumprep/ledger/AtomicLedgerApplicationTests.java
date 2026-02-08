package com.electrumprep.ledger;

// 📦 IMPORTS
// We are grabbing testing tools from the library.
// "JUnit" is the standard tool for testing Java code.
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

// -----------------------------------------------------------------------------------
// 🧪 THE SANITY CHECK (The Fire Drill)
// This file is automatically created by Spring Boot.
// Its only job is to answer one question: "Does the app even start?"
// -----------------------------------------------------------------------------------

@SpringBootTest // 🏷️ STICKER: Tells Java: "Please try to start the WHOLE application just like real life."
class AtomicLedgerApplicationTests {

	// 🕵️ THE TEST CASE
	// @Test = "This is a checklist item. If it crashes, fail the build."
	@Test
	void contextLoads() {
		// 🤷‍♂️ EMPTY METHOD?
		// Yes! The code inside is empty, but the test is NOT useless.

		// How it works:
		// 1. Spring Boot tries to start the "Application Context" (The Brain).
		// 2. It connects to the Database, loads the AI Service, and sets up the Controller.
		// 3. If ANY of that fails (e.g., wrong password, missing file), this test crashes.
		// 4. If it reaches this line without crashing, the test passes! ✅

		// Think of it like turning the car key to see if the engine starts.
		// You aren't driving anywhere yet, just checking the battery.
	}

}