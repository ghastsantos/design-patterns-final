# In-Process Logging Library Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [x]`) syntax for tracking.

**Goal:** Build an importable Java 21 logging library with configurable formatting and destinations, plus a separate client demo and submission documentation.

**Architecture:** A Gradle multi-project build separates the public `library` module from `demo`. `LoggerBuilder` configures immutable logger behavior; `Logger` filters and creates events, formats each event once, then notifies registered `LogSink` observers.

**Tech Stack:** Java 21, Gradle Kotlin DSL, JDK standard library only.

**Spec:** `docs/superpowers/specs/2026-11-05-inprocess-logging-library-design.md`

## Global Constraints

- Use Java 21.
- Keep production dependencies at zero; use JUnit only for tests.
- Keep the demo dependent only on the public `library` module API.
- Add JUnit tests for the public behavior of the library.
- Include user-facing documentation in Brazilian Portuguese; keep code identifiers in English.

## Review Focus

- Null or blank logger names and null builder values must fail during configuration with clear `IllegalArgumentException` messages.
- Events below the configured threshold must not be formatted or delivered to sinks.
- JSON strings must escape quotes, backslashes, and control characters into valid JSON Lines.
- File output must append, close resources, and surface I/O failures as clear `IllegalStateException` messages.
- One custom sink must receive the structured event and formatted line without requiring changes in the library module.

---

### Task 1: Create the Gradle multi-project structure

**Files:**
- Create: `settings.gradle.kts`
- Create: `build.gradle.kts`
- Create: `gradle.properties`
- Copy: Gradle wrapper scripts and `gradle/wrapper/` from `design-patterns-aula-05`
- Create: `library/build.gradle.kts`
- Create: `demo/build.gradle.kts`

**Interfaces:**
- Produces: Gradle projects `:library` and `:demo`; `:demo` declares `implementation(project(":library"))`.

- [x] Configure root project name `design-patterns-final`, group `br.pucpr`, version `1.0.0`, UTF-8 compilation, and Java 21 toolchains.
- [x] Configure `library` with the `java-library` plugin and no production dependencies.
- [x] Configure `demo` with `application`, main class `br.pucpr.demo.DemoApplication`, and dependency on `:library`.
- [x] Add a minimal `.gitignore` for Gradle and IDE outputs.

### Task 2: Implement the public logging library

**Files:**
- Create: `library/src/main/java/br/pucpr/logkit/LogLevel.java`
- Create: `library/src/main/java/br/pucpr/logkit/LogEvent.java`
- Create: `library/src/main/java/br/pucpr/logkit/LogFormatter.java`
- Create: `library/src/main/java/br/pucpr/logkit/PlainTextFormatter.java`
- Create: `library/src/main/java/br/pucpr/logkit/JsonLineFormatter.java`
- Create: `library/src/main/java/br/pucpr/logkit/LogSink.java`
- Create: `library/src/main/java/br/pucpr/logkit/ConsoleSink.java`
- Create: `library/src/main/java/br/pucpr/logkit/FileSink.java`
- Create: `library/src/main/java/br/pucpr/logkit/Logger.java`
- Create: `library/src/main/java/br/pucpr/logkit/LoggerBuilder.java`

**Interfaces:**
- `LogFormatter.format(LogEvent event) -> String`.
- `LogSink.onLog(LogEvent event, String formattedMessage) -> void`; default `close() -> void`.
- `LoggerBuilder.name(String)`, `minimumLevel(LogLevel)`, `formatter(LogFormatter)`, `addSink(LogSink)`, `build() -> Logger`.
- `Logger.trace/debug/info/warn/error(String) -> void`; `close() -> void`.

- [x] Define ordered levels TRACE, DEBUG, INFO, WARN, ERROR and immutable event fields `Instant timestamp`, `LogLevel level`, `String loggerName`, `String message`.
- [x] Implement plain text and JSON Lines formatter strategies, including JSON escaping for control characters.
- [x] Implement console and append-mode file sinks; translate I/O failures into `IllegalStateException` with the original cause.
- [x] Implement logger threshold filtering, one formatting call per accepted event, observer delivery to all configured sinks, and sink cleanup on close.
- [x] Validate builder inputs and require at least one sink before `build()`.

### Task 3: Demonstrate client use and prepare delivery documentation

**Files:**
- Create: `demo/src/main/java/br/pucpr/demo/DemoApplication.java`
- Create: `README.md`
- Create: `docs/roteiro-apresentacao.md`

**Interfaces:**
- Consumes: only the public API from `br.pucpr.logkit`.
- Produces: runnable example covering filtering, plain text, JSON, file output, and a custom sink.

- [x] Build a demo that configures separate plain-text and JSON loggers, writes a file, and implements a custom telemetry sink in the client module.
- [x] Document prerequisites, Windows commands, local Gradle dependency, API example, levels, extension steps, and Strategy/Observer explanations in Portuguese.
- [x] Provide a video outline capped at 10 minutes with slots for student name and YouTube URL, and prompts to explain two patterns with code locations and context.

### Task 4: Verify demo and distributable

**Files:**
- No new files.

- [x] Run `.\gradlew.bat :demo:run` and confirm the demo shows filtered output, JSON, file output, and telemetry output.
- [x] Run `.\gradlew.bat :library:jar` and confirm the distributable JAR is created under `library/build/libs/`.
- [x] Review README and video outline against every assignment delivery requirement; report anything blocked by external accounts (hosting the repo/video).

### Task 5: Cover public library behavior with automated tests

**Files:**
- Modify: `library/build.gradle.kts`
- Create: `library/src/test/java/br/pucpr/logkit/LogLevelTest.java`
- Create: `library/src/test/java/br/pucpr/logkit/LoggerBuilderTest.java`
- Create: `library/src/test/java/br/pucpr/logkit/LoggerTest.java`
- Create: `library/src/test/java/br/pucpr/logkit/JsonLineFormatterTest.java`
- Create: `library/src/test/java/br/pucpr/logkit/PlainTextFormatterTest.java`
- Create: `library/src/test/java/br/pucpr/logkit/FileSinkTest.java`
- Modify: `README.md`

- [x] Cover level ordering, builder validation, threshold filtering, one formatting call per accepted event, observer delivery despite one sink failure, idempotent closure, exact text and escaped JSON output, and file append behavior.
- [x] Run `./gradlew :library:test` and `./gradlew build`; confirm all tests pass and both modules build.
