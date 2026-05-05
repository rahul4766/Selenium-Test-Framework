# 🧪 Selenium Test Automation Framework – OrangeHRM

A modular, scalable test automation framework built from scratch using **Selenium WebDriver**, **TestNG**, and **Maven**, tested against the [OrangeHRM](https://opensource-demo.orangehrmlive.com/) open-source HR application as a real-world test environment.

---

## 🚀 Tech Stack

| Category | Tools |
|---|---|
| Language | Java |
| Automation | Selenium WebDriver |
| Test Framework | TestNG |
| Build Tool | Maven |
| Design Pattern | Page Object Model (POM) |
| Reporting | Extent Reports |
| Logging | Log4j |
| CI/CD | Jenkins |
| Version Control | Git & GitHub |
| Database | SQL |
| API Testing | Dummy endpoints |

---

## 📁 Project Structure

```
OrangeHRMProject/
├── src/main/java/
│   ├── com.orangehrm.actiondriver/
│   │   └── ActionDriver.java       # Custom wrapper for Selenium actions
│   ├── com.orangehrm.base/
│   │   └── BaseClass.java          # Driver setup, teardown & config
│   ├── com.orangehrm.listeners/
│   │   └── TestListener.java       # TestNG listener for events & reporting
│   ├── com.orangehrm.pages/
│   │   ├── LoginPage.java          # POM class for Login page
│   │   └── HomePage.java           # POM class for Home page
│   └── com.orangehrm.utilities/    # Helpers (Excel reader, DB utils, etc.)
├── src/main/resources/
│   ├── config.properties           # Environment config (URL, credentials)
│   └── log4j.xml                   # Log4j logging configuration
├── src/test/java/
│   └── com.orangehrm.test/         # TestNG test classes
└── src/test/resources/
    ├── ExtentReport/               # Generated HTML test reports
    ├── screenshots/                # Failure screenshots
    ├── testdata/                   # Excel files for data-driven testing
    └── testng.xml                  # TestNG suite configuration
```

---

## ✅ Features

- **Page Object Model (POM)** – Clean separation of test logic and UI interactions for easy maintenance
- **Data-Driven Testing** – Excel-driven test data using Apache POI with TestNG `@DataProvider`
- **Extent Reports** – Rich HTML reports with test pass/fail status and execution logs
- **Log4j Logging** – Real-time execution logs for debugging and traceability
- **Retry Analyzer** – Automatically retries failed tests to reduce flaky failures
- **TestNG Listeners** – Custom listeners for test lifecycle events
- **SQL Database Testing** – Validates data integrity via direct DB queries
- **API Testing** – Basic API test coverage using dummy endpoints
- **Jenkins CI Pipeline** – Integrated with GitHub to trigger test runs on push and send email build reports

---

## 🔧 Setup & Installation

### Prerequisites
- Java JDK 11+
- Maven 3.6+
- Chrome Browser + ChromeDriver (matching version)
- Jenkins (for CI)

### Clone the repo
```bash
git clone https://github.com/rahul4766/Selenium-Test-Framework.git
cd Selenium-Test-Framework
```

### Install dependencies
```bash
mvn clean install -DskipTests
```

### Run tests
```bash
mvn test
```

---

## 📊 Reports

After execution, Extent Reports are generated at:
```
reports/ExtentReport.html
```
Open in any browser to view detailed test results with pass/fail status and logs.

---

## ⚙️ Jenkins CI

The project includes a Jenkins pipeline that:
- Triggers automated test runs on every GitHub push
- Sends build-status email reports after execution

---

## 👤 Author

**Rahul Kumar**  
QA Automation Engineer | Bengaluru, India  

