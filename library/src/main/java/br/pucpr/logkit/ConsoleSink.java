package br.pucpr.logkit;

import java.util.Objects;

public final class ConsoleSink implements LogSink {
  @Override
  public void onLog(LogEvent event, String formattedMessage) {
    Objects.requireNonNull(event, "event cannot be null");
    System.out.println(Objects.requireNonNull(formattedMessage, "formattedMessage cannot be null"));
  }
}
