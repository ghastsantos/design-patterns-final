package br.pucpr.logkit;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class LogLevelTest {
  @Test
  void includesLevelsAtOrAboveTheMinimum() {
    assertTrue(LogLevel.WARN.includes(LogLevel.INFO));
    assertTrue(LogLevel.WARN.includes(LogLevel.WARN));
    assertFalse(LogLevel.DEBUG.includes(LogLevel.INFO));
  }
}
