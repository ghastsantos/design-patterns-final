package br.pucpr.logkit;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Instant;
import org.junit.jupiter.api.Test;

class JsonLineFormatterTest {
  @Test
  void escapesQuotesBackslashesAndControlCharacters() {
    LogEvent event =
        new LogEvent(
            Instant.parse("2026-11-05T12:34:56Z"),
            LogLevel.ERROR,
            "order\"service",
            "quote \" slash \\ newline\ncontrol" + (char) 1);

    String formatted = new JsonLineFormatter().format(event);

    assertEquals(
        "{\"timestamp\":\"2026-11-05T12:34:56Z\",\"level\":\"ERROR\",\"logger\":\"order\\\"service\",\"message\":\"quote \\\" slash \\\\ newline\\ncontrol\\u0001\"}",
        formatted);
  }
}
