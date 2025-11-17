# TestFusionX

**TestFusionX** is a unified, extensible, enterprise-grade automation framework designed to support *all major testing types* including:

* **UI Testing (Selenium/TestNG/Cucumber)**
* **API Testing (RestAssured/HTTP Client)**
* **Database Testing (JDBC/SQL Utilities)**
* **Integration Testing**
* **File & FTP Testing**
* **Performance Testing (JMeter/Gatling Integration)**
* **Security Testing (OWASP/DAST/SAST Integrations)**
* **Accessibility Testing (Axe-core integration)**
* **AI-Assisted Testing (self-healing locators, generative test steps)**

This README focuses on **UI Automation Module** as Phase 1.

---

## 🚀 Overview

TestFusionX provides a modular, scalable, and clean automation architecture supporting:

* Multi-browser & multi-platform testing
* Parallel execution with TestNG
* Thread-safe drivers & configuration
* Cucumber + TestNG hybrid execution
* Reusable Page Object Model (POM)
* Centralized reporting (Allure / Extent)
* Utility-driven design
* Test lifecycle hooks
* Extensible plugin structure for future testing modules

---

## 📁 Project Structure

```
src
 ├── main
 │    └── java
 │         └── com.testfusionx
 │               ├── config
 │               ├── core
 │               ├── drivers
 │               ├── exceptions
 │               ├── helpers
 │               ├── pages
 │               ├── reporting
 │               └── utils
 │
 └── test
      └── java
           └── com.testfusionx.tests
                 ├── ui
                 ├── hooks
                 └── runners
```

---

## 🧩 Modules

### **1. UI Testing Module (Phase 1)**

Includes:

* Selenium WebDriver setup
* ThreadLocal driver handling
* Page Object Model
* WebElement utilities
* Synchronization helpers
* Reporting & logging
* Parallel execution support

### Future Modules (Phase 2+):

* API Testing
* Performance Testing
* Security Testing
* Accessibility Testing
* AI-Assisted Testing
* File/FTP Testing
* Database Testing
* Integration Testing

---

## ⚙️ Key Components

### **1. config package**

* `ConfigProvider` → Reads properties, stored in ThreadLocal.
* `EnvironmentManager` → Loads env-specific configs.

### **2. core package**

* `BaseTest` → Test lifecycle & driver initialization.
* `TestContext` → Shared data holder per test.

### **3. drivers package**

* `DriverFactory` → Creates browser drivers.
* `DriverManager` → Manages ThreadLocal drivers.

### **4. pages package**

* Contains all Page Object classes.
* `BasePage` → Shared page methods.

### **5. helpers package**

Includes utilities for:

* Actions (click, type, scroll)
* Waits
* Screenshots
* JavaScript interactions

### **6. hooks package**

* `BeforeAfterHook` → Setup & teardown for each test.

### **7. runners package**

* TestNG & Cucumber runners.

---

## 🕸 Architecture Diagram

```
TestNG Runner
     │
     ▼
 BaseTest ───────────────► BeforeAfterHook
     │                             │
     ▼                             ▼
DriverFactory ───────► DriverManager (ThreadLocal)
     │                             │
     ▼                             ▼
 WebDriver                ConfigProvider (ThreadLocal)
     │                             │
     └──────────────┬──────────────┘
                    ▼
              BasePage
                    │
                    ▼
             Page Objects
                    │
                    ▼
                UI Tests
```

---

## ▶️ Running the Tests

### Run UI tests:

```
mvn clean test -Dsuite=ui
```

### Cross-browser execution:

```
mvn clean test -Dbrowser=chrome
```

```
mvn clean test -Dbrowser=firefox
```

### Parallel execution:

Handled by `testng-ui-parallel.xml`

---

## 🧪 Sample Test Flow

1. TestNG runner triggers execution.
2. `BeforeAfterHook` initializes ThreadLocal driver.
3. `BaseTest` loads configs & context.
4. Test uses Page Objects for actions.
5. On failure, screenshots + logs captured.
6. Driver closes at teardown.

---

## 🔒 Licensing

TestFusionX is released under the **MIT License**.

---

## 🤝 Contributing

Contributions are welcome! Once the full multi-testing framework is ready, a CONTRIBUTING.md file will be added.

---

## 📬 Support

For feature requests or issues, create a GitHub Issue.

---

## 🌟 Future Roadmap

* API automation module
* DB automation module
* Performance testing integration
* Security scanning integration
* AI-powered self-healing locators
* Dashboard for consolidated reporting

---

## 📌 Author

**Utkarsh Goswami** — Test Architect

---

## 🚀 Let's Build the Most Complete Automation Framework!
