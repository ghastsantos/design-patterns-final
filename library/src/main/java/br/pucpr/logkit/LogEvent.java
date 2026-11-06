package br.pucpr.logkit;

import java.time.Instant;
import java.util.Objects;

public record LogEvent(Instant timestamp, LogLevel level, String loggerName, String message) {
  public LogEvent {
    Objects.requireNonNull(timestamp, "timestamp cannot be null");
    Objects.requireNonNull(level, "level cannot be null");
    if (loggerName == null || loggerName.isBlank()) {
      throw new IllegalArgumentException("loggerName cannot be blank");
    }
    Objects.requireNonNull(message, "message cannot be null");
  }
}
