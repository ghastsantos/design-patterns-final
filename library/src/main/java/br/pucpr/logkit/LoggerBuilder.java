package br.pucpr.logkit;

import java.util.ArrayList;
import java.util.List;

public final class LoggerBuilder {
  private String name;
  private LogLevel minimumLevel = LogLevel.INFO;
  private LogFormatter formatter = new PlainTextFormatter();
  private final List<LogSink> sinks = new ArrayList<>();

  public LoggerBuilder name(String name) {
    if (name == null || name.isBlank()) {
      throw new IllegalArgumentException("Logger name cannot be blank");
    }
    this.name = name;
    return this;
  }

  public LoggerBuilder minimumLevel(LogLevel minimumLevel) {
    if (minimumLevel == null) {
      throw new IllegalArgumentException("Minimum log level cannot be null");
    }
    this.minimumLevel = minimumLevel;
    return this;
  }

  public LoggerBuilder formatter(LogFormatter formatter) {
    if (formatter == null) {
      throw new IllegalArgumentException("Formatter cannot be null");
    }
    this.formatter = formatter;
    return this;
  }

  public LoggerBuilder addSink(LogSink sink) {
    if (sink == null) {
      throw new IllegalArgumentException("Sink cannot be null");
    }
    sinks.add(sink);
    return this;
  }

  public Logger build() {
    if (name == null || name.isBlank()) {
      throw new IllegalArgumentException("Logger name must be configured");
    }
    if (sinks.isEmpty()) {
      throw new IllegalArgumentException("At least one log sink must be configured");
    }
    return new Logger(name, minimumLevel, formatter, sinks);
  }
}
