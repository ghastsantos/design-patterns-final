package br.pucpr.logkit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.Test;

class LoggerTest {
  @Test
  void filtersBeforeFormattingAndNotifiesEverySink() {
    AtomicInteger formattingCalls = new AtomicInteger();
    List<LogEvent> receivedEvents = new ArrayList<>();
    List<String> receivedLines = new ArrayList<>();
    LogFormatter formatter =
        event -> {
          formattingCalls.incrementAndGet();
          return event.level() + ":" + event.message();
        };
    LogSink firstSink =
        (event, line) -> {
          receivedEvents.add(event);
          receivedLines.add(line);
        };
    LogSink secondSink = (event, line) -> receivedEvents.add(event);

    Logger logger =
        new LoggerBuilder()
            .name("checkout")
            .minimumLevel(LogLevel.WARN)
            .formatter(formatter)
            .addSink(firstSink)
            .addSink(secondSink)
            .build();

    logger.debug("details");
    logger.warn("slow response");

    assertEquals(1, formattingCalls.get());
    assertEquals(2, receivedEvents.size());
    assertEquals(LogLevel.WARN, receivedEvents.get(0).level());
    assertEquals("WARN:slow response", receivedLines.get(0));
    logger.close();
  }

  @Test
  void notifiesRemainingSinksAndPropagatesSinkFailure() {
    List<LogEvent> receivedEvents = new ArrayList<>();
    LogSink failingSink =
        (event, line) -> {
          throw new IllegalStateException("sink unavailable");
        };
    LogSink workingSink = (event, line) -> receivedEvents.add(event);
    Logger logger =
        new LoggerBuilder()
            .name("checkout")
            .addSink(failingSink)
            .addSink(workingSink)
            .build();

    IllegalStateException failure =
        assertThrows(IllegalStateException.class, () -> logger.error("payment failed"));

    assertEquals("sink unavailable", failure.getMessage());
    assertEquals(1, receivedEvents.size());
    logger.close();
  }

  @Test
  void closeIsIdempotentAndRejectsFurtherLogging() {
    AtomicInteger closeCalls = new AtomicInteger();
    LogSink sink =
        new LogSink() {
          @Override
          public void onLog(LogEvent event, String line) {}

          @Override
          public void close() {
            closeCalls.incrementAndGet();
          }
        };
    Logger logger = new LoggerBuilder().name("checkout").addSink(sink).build();

    logger.close();
    logger.close();

    assertEquals(1, closeCalls.get());
    assertThrows(IllegalStateException.class, () -> logger.info("after close"));
  }
}
