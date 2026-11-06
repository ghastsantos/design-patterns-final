package br.pucpr.logkit;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class LoggerBuilderTest {
  @Test
  void buildRequiresAName() {
    LoggerBuilder builder = new LoggerBuilder().addSink((event, line) -> {});

    assertThrows(IllegalArgumentException.class, builder::build);
  }

  @Test
  void buildRequiresAtLeastOneSink() {
    LoggerBuilder builder = new LoggerBuilder().name("checkout");

    assertThrows(IllegalArgumentException.class, builder::build);
  }

  @Test
  void configurationRejectsNullValues() {
    LoggerBuilder builder = new LoggerBuilder();

    assertThrows(IllegalArgumentException.class, () -> builder.minimumLevel(null));
    assertThrows(IllegalArgumentException.class, () -> builder.formatter(null));
    assertThrows(IllegalArgumentException.class, () -> builder.addSink(null));
  }

  @Test
  void nameRejectsBlankValues() {
    LoggerBuilder builder = new LoggerBuilder();

    assertThrows(IllegalArgumentException.class, () -> builder.name("  "));
  }
}
