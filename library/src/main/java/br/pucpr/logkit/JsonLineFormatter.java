package br.pucpr.logkit;

import java.util.Objects;

public final class JsonLineFormatter implements LogFormatter {
  @Override
  public String format(LogEvent event) {
    Objects.requireNonNull(event, "event cannot be null");
    return "{\"timestamp\":\"%s\",\"level\":\"%s\",\"logger\":\"%s\",\"message\":\"%s\"}"
        .formatted(
            escape(event.timestamp().toString()),
            escape(event.level().name()),
            escape(event.loggerName()),
            escape(event.message()));
  }

  private static String escape(String value) {
    StringBuilder escaped = new StringBuilder(value.length());
    for (int index = 0; index < value.length(); index++) {
      char character = value.charAt(index);
      switch (character) {
        case '"' -> escaped.append("\\\"");
        case '\\' -> escaped.append("\\\\");
        case '\b' -> escaped.append("\\b");
        case '\f' -> escaped.append("\\f");
        case '\n' -> escaped.append("\\n");
        case '\r' -> escaped.append("\\r");
        case '\t' -> escaped.append("\\t");
        default -> {
          if (character < 0x20) {
            escaped.append("\\u%04x".formatted((int) character));
          } else {
            escaped.append(character);
          }
        }
      }
    }
    return escaped.toString();
  }
}
