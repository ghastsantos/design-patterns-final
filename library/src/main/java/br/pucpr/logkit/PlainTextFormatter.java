package br.pucpr.logkit;

import java.util.Objects;

public final class PlainTextFormatter implements LogFormatter {
  @Override
  public String format(LogEvent event) {
    Objects.requireNonNull(event, "event cannot be null");
    return "%s %-5s [%s] %s"
        .formatted(event.timestamp(), event.level(), event.loggerName(), event.message());
  }
}
