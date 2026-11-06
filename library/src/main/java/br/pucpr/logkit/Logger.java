package br.pucpr.logkit;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

public final class Logger implements AutoCloseable {
  private final String name;
  private final LogLevel minimumLevel;
  private final LogFormatter formatter;
  private final List<LogSink> sinks;
  private boolean closed;

  Logger(String name, LogLevel minimumLevel, LogFormatter formatter, List<LogSink> sinks) {
    this.name = name;
    this.minimumLevel = minimumLevel;
    this.formatter = formatter;
    this.sinks = List.copyOf(sinks);
  }

  public void trace(String message) {
    log(LogLevel.TRACE, message);
  }

  public void debug(String message) {
    log(LogLevel.DEBUG, message);
  }

  public void info(String message) {
    log(LogLevel.INFO, message);
  }

  public void warn(String message) {
    log(LogLevel.WARN, message);
  }

  public void error(String message) {
    log(LogLevel.ERROR, message);
  }

  private synchronized void log(LogLevel level, String message) {
    if (closed) {
      throw new IllegalStateException("Logger is closed");
    }
    Objects.requireNonNull(message, "message cannot be null");
    if (!level.includes(minimumLevel)) {
      return;
    }

    LogEvent event = new LogEvent(Instant.now(), level, name, message);
    String formattedMessage =
        Objects.requireNonNull(formatter.format(event), "formatter returned a null message");
    RuntimeException failure = null;
    for (LogSink sink : sinks) {
      try {
        sink.onLog(event, formattedMessage);
      } catch (RuntimeException exception) {
        if (failure == null) {
          failure = exception;
        } else {
          failure.addSuppressed(exception);
        }
      }
    }
    if (failure != null) {
      throw failure;
    }
  }

  @Override
  public synchronized void close() {
    if (closed) {
      return;
    }
    closed = true;
    RuntimeException failure = null;
    for (LogSink sink : sinks) {
      try {
        sink.close();
      } catch (RuntimeException exception) {
        if (failure == null) {
          failure = exception;
        } else {
          failure.addSuppressed(exception);
        }
      }
    }
    if (failure != null) {
      throw failure;
    }
  }
}
