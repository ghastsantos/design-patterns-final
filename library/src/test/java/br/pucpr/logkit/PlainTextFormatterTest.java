package br.pucpr.logkit;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Instant;
import org.junit.jupiter.api.Test;

class PlainTextFormatterTest {
  @Test
  void formatsTimestampLevelLoggerAndMessage() {
    LogEvent event =
        new LogEvent(
            Instant.parse("2026-11-05T12:34:56Z"), LogLevel.INFO, "checkout", "order accepted");

    String formatted = new PlainTextFormatter().format(event);

    assertEquals("2026-11-05T12:34:56Z INFO  [checkout] order accepted", formatted);
  }
}
