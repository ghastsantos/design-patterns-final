package br.pucpr.logkit;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Objects;

public final class FileSink implements LogSink {
  private final BufferedWriter writer;
  private boolean closed;

  public FileSink(Path path) {
    Objects.requireNonNull(path, "path cannot be null");
    try {
      writer =
          Files.newBufferedWriter(
              path,
              StandardCharsets.UTF_8,
              StandardOpenOption.CREATE,
              StandardOpenOption.APPEND);
    } catch (IOException exception) {
      throw new IllegalStateException("Could not open log file: " + path, exception);
    }
  }

  @Override
  public synchronized void onLog(LogEvent event, String formattedMessage) {
    Objects.requireNonNull(event, "event cannot be null");
    Objects.requireNonNull(formattedMessage, "formattedMessage cannot be null");
    if (closed) {
      throw new IllegalStateException("FileSink is closed");
    }

    try {
      writer.write(formattedMessage);
      writer.newLine();
      writer.flush();
    } catch (IOException exception) {
      throw new IllegalStateException("Could not write to log file", exception);
    }
  }

  @Override
  public synchronized void close() {
    if (closed) {
      return;
    }
    closed = true;
    try {
      writer.close();
    } catch (IOException exception) {
      throw new IllegalStateException("Could not close log file", exception);
    }
  }
}
