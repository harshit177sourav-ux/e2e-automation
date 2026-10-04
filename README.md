# e2e-automation

A Java test automation framework for API (and, soon, UI) testing. It uses **REST Assured** for HTTP calls, **TestNG** as the test runner and **Jackson** for JSON models. Everything is driven by **Maven** and by per-environment config files.

Today it tests the public [Restful-Booker](https://restful-booker.herokuapp.com) API. The config also has placeholders for a UI target ([Sauce Demo](https://www.saucedemo.com)), but no browser-automation library is wired in yet.

---

## Contents

- [Tech stack](#tech-stack)
- [Prerequisites](#prerequisites)
- [Quick start: clone to run](#quick-start-clone-to-run)
- [Running tests](#running-tests)
- [Configuration](#configuration)
- [Project structure](#project-structure)
- [Writing a new test](#writing-a-new-test)
- [Contributing](#contributing)
- [Troubleshooting](#troubleshooting)

---

## Tech stack

| Concern | Tool | Version |
|---|---|---|
| Language | Java | 17+ (`maven.compiler.release` = 17) |
| Build | Apache Maven | 3.9+ |
| Test runner | TestNG | 7.10.2 |
| HTTP / API | REST Assured | 5.5.0 |
| JSON | Jackson Databind | 2.17.2 |
| Test execution | Maven Surefire | 3.5.2 |

---

## Prerequisites

You need three things installed:

| Tool | Check with | Install |
|---|---|---|
| Git | `git --version` | https://git-scm.com/downloads |
| JDK 17 or newer | `java -version` | https://adoptium.net (or `brew install --cask temurin`) |
| Maven 3.9+ | `mvn -v` | https://maven.apache.org/install.html (or `brew install maven`) |

You also need internet access, because the tests call a live public API.

---

## Quick start: clone to run

```bash
# 1. Clone
git clone https://github.com/harshit177sourav-ux/e2e-automation.git
cd e2e-automation

# 2. Run the smoke suite (downloads dependencies on first run)
mvn test
```

The first run downloads dependencies, so it takes a minute. After that it's fast.

**What success looks like:** Maven finishes with `BUILD SUCCESS`. The `Tests run: ..., Failures: 0, Errors: 0` line comes before it. If you're running with `-q` (quiet), you'll only see the test output and no errors.

---

## Running tests

All commands run from the project root.

| Goal | Command |
|---|---|
| Run the default smoke suite | `mvn test` |
| Run a different suite file | `mvn test -Dsuite.file=src/test/resources/suites/<name>.xml` |
| Run a single test class | `mvn test -Dtest=BookingModelTest` |
| Run a single test method | `mvn test -Dtest=FirstApiTest#listBookingReturnsData` |
| Clean build output, then run | `mvn clean test` |
| Compile only (no tests) | `mvn test-compile` |

**Note:** the default suite is [smoke.xml](src/test/resources/suites/smoke.xml). It runs `SanityTest`, `ConfigTest` and `FirstApiTest`. `BookingModelTest` is not in that suite. It creates data on the remote API, so run it by name as shown above or add it to a suite.

Reports are written to `target/surefire-reports/`. Open `index.html` there for a browsable summary.

---

## Configuration

Settings are looked up in this order, and the first match wins:

1. **JVM system property**: `-Dapi.base.url=...`
2. **Environment variable**: the key upper-cased with dots as underscores, so `api.base.url` becomes `API_BASE_URL`
3. **Properties file**: `src/main/resources/config/<env>.properties`

### Available keys

| Key | Default (`qa`) | Purpose |
|---|---|---|
| `api.base.url` | `https://restful-booker.herokuapp.com` | Base URI for all API calls |
| `ui.base.url` | `https://www.saucedemo.com` | Base URL for UI tests (reserved) |
| `browser` | `chrome` | Browser name (reserved for UI tests) |
| `headless` | `false` | Run the browser without a window (reserved for UI tests) |
| `explicit.wait.second` | `10` | Explicit wait timeout in seconds |

### Examples

```bash
# Point at a different API for one run
mvn test -Dapi.base.url=https://staging.example.com

# Same thing with an environment variable
API_BASE_URL=https://staging.example.com mvn test
```

### Adding another environment

1. Copy `src/main/resources/config/qa.properties` to `config/<env>.properties`, for example `staging.properties`.
2. Run with `-Denv=staging`:

```bash
mvn test -Denv=staging
```

The default environment is `qa`. A missing key fails fast with an `IllegalStateException` that names the key.

### Secrets

Don't commit secrets to properties files. Read them from environment variables with `Config.secret("MY_VAR")`. It throws immediately if the variable is unset or blank.

---

## Project structure

```
e2e-automation/
├── pom.xml                                   # Dependencies, Java version, Surefire config
└── src/
    ├── main/
    │   ├── java/com/harshitsourav/framework/
    │   │   ├── api/
    │   │   │   ├── ApiClient.java            # Shared REST Assured request spec
    │   │   │   └── models/                   # JSON POJOs (Jackson)
    │   │   │       ├── Booking.java
    │   │   │       ├── BookingDates.java
    │   │   │       └── CreatedBooking.java
    │   │   └── config/
    │   │       └── Config.java               # Layered config lookup
    │   └── resources/config/
    │       └── qa.properties                 # Default environment settings
    └── test/
        ├── java/com/harshitsourav/tests/
        │   ├── SanityTest.java               # Proves the framework runs
        │   ├── ConfigTest.java               # Config behaviour
        │   └── api/
        │       ├── FirstApiTest.java         # GET /booking examples
        │       └── BookingModelTest.java     # POST /booking with models
        └── resources/suites/
            └── smoke.xml                     # TestNG suite run by default
```

### Key pieces

- **`Config`** gives you typed access to settings: `Config.apiBaseUrl()`, `Config.explicitWait()`, and so on.
- **`ApiClient.spec()`** returns a ready-made request spec. It sets the base URI and JSON content type, and it hides `Authorization` and `Cookie` headers in failure logs.
- **Models** are plain classes with a no-arg constructor, getters and setters, and `equals`/`hashCode`. Jackson maps them to and from JSON. Unknown JSON fields are ignored and null fields are omitted when sending.

---

## Writing a new test

1. Create a class under `src/test/java/com/harshitsourav/tests/api/`.
2. Use `ApiClient.spec()` and models:

```java
package com.harshitsourav.tests.api;

import static io.restassured.RestAssured.given;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.harshitsourav.framework.api.ApiClient;
import com.harshitsourav.framework.api.models.Booking;

public class GetBookingTest {

    @Test
    public void fetchBookingById() {
        Booking booking = given(ApiClient.spec())
                .pathParam("id", 1)
                .when().get("/booking/{id}")
                .then().statusCode(200)
                .extract().as(Booking.class);

        Assert.assertNotNull(booking.getFirstname());
    }
}
```

3. Register the class in a suite file, such as `smoke.xml`, or run it directly with `mvn test -Dtest=GetBookingTest`.

---

## Contributing

1. Create a branch from `main`: `git checkout -b feature/<short-name>`
2. Make your changes and run `mvn test` to confirm everything passes.
3. Commit and push your branch, then open a pull request against `main`.
4. A PR needs one approval from someone other than its author before it can be merged.

---

## Troubleshooting

| Symptom | Likely cause and fix |
|---|---|
| `release version 17 not supported` | Your JDK is older than 17. Install JDK 17+ and check `java -version` and `mvn -v` (Maven prints the JDK it uses). |
| `mvn: command not found` | Maven isn't installed or isn't on your `PATH`. |
| `No config file found on class path /config/<env>.properties` | You passed `-Denv=<env>` but no matching file exists in `src/main/resources/config/`. |
| `Missing Config key ...` | The key isn't in a system property, an environment variable or the properties file. |
| Connection or timeout errors, or non-200 responses | The target API is down or unreachable. Check `api.base.url` and your network. Restful-Booker is a shared public service and is sometimes slow. |
| `SLF4J: Failed to load class "org.slf4j.impl.StaticLoggerBinder"` | Harmless. No logging backend is configured, so logs are silently dropped. |
| Stale or odd build output | Run `mvn clean test`. |
