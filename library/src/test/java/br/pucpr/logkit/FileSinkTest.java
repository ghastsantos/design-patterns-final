package br.pucpr.logkit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class FileSinkTest {
  @TempDir Path temporaryDirectory;

  @Test
  void appendsMessagesAndClosesTheFile() throws Exception {
    Path logFile = temporaryDirectory.resolve("application.log");
    LogEvent event = new LogEvent(Instant.parse("2026-11-05T12:34:56Z"), LogLevel.INFO, "app", "one");
    FileSink firstSink = new FileSink(logFile);
    firstSink.onLog(event, "line one");
    firstSink.close();

    FileSink secondSink = new FileSink(logFile);
    secondSink.onLog(event, "line two");
    secondSink.close();

    assertEquals(List.of("line one", "line two"), Files.readAllLines(logFile, StandardCharsets.UTF_8));
    assertThrows(IllegalStateException.class, () -> secondSink.onLog(event, "after close"));
  }
}
