package br.pucpr.logkit;

public interface LogSink extends AutoCloseable {
  void onLog(LogEvent event, String formattedMessage);

  @Override
  default void close() {}
}
