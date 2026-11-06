package br.pucpr.logkit;

@FunctionalInterface
public interface LogFormatter {
  String format(LogEvent event);
}
