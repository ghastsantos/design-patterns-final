package br.pucpr.demo;

import br.pucpr.logkit.ConsoleSink;
import br.pucpr.logkit.FileSink;
import br.pucpr.logkit.JsonLineFormatter;
import br.pucpr.logkit.LogEvent;
import br.pucpr.logkit.LogLevel;
import br.pucpr.logkit.LogSink;
import br.pucpr.logkit.Logger;
import br.pucpr.logkit.LoggerBuilder;
import br.pucpr.logkit.PlainTextFormatter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicInteger;

public final class DemoApplication {
  private DemoApplication() {}

  public static void main(String[] args) throws IOException {
    Files.createDirectories(Path.of("output"));

    System.out.println("=== Texto legível, filtro de nível e arquivo ===");
    try (Logger logger =
        new LoggerBuilder()
            .name("demo.application")
            .minimumLevel(LogLevel.DEBUG)
            .formatter(new PlainTextFormatter())
            .addSink(new ConsoleSink())
            .addSink(new FileSink(Path.of("output", "application.log")))
            .build()) {
      logger.trace("Esta mensagem não passa pelo filtro DEBUG.");
      logger.debug("Carregando a configuração da aplicação.");
      logger.info("Aplicação iniciada.");
      logger.warn("A resposta demorou mais que o esperado.");
    }

    System.out.println("\n=== Formato JSON Lines ===");
    try (Logger logger =
        new LoggerBuilder()
            .name("demo.json")
            .minimumLevel(LogLevel.INFO)
            .formatter(new JsonLineFormatter())
            .addSink(new ConsoleSink())
            .build()) {
      logger.info("JSON mantém aspas e quebras de linha escapadas: \"olá\"\nsegunda linha");
    }

    System.out.println("\n=== Destino personalizado do cliente ===");
    TelemetrySink telemetry = new TelemetrySink();
    try (Logger logger =
        new LoggerBuilder()
            .name("demo.telemetry")
            .minimumLevel(LogLevel.WARN)
            .addSink(telemetry)
            .build()) {
      logger.info("Ignorada pelo nível mínimo WARN.");
      logger.warn("A telemetria recebe este aviso.");
      logger.error("A telemetria recebe este erro.");
    }
    System.out.printf("Eventos WARN/ERROR recebidos pelo cliente: %d%n", telemetry.count());
  }

  private static final class TelemetrySink implements LogSink {
    private final AtomicInteger importantEvents = new AtomicInteger();

    @Override
    public void onLog(LogEvent event, String formattedMessage) {
      if (event.level().includes(LogLevel.WARN)) {
        importantEvents.incrementAndGet();
        System.out.printf("[telemetria do cliente] %s%n", formattedMessage);
      }
    }

    int count() {
      return importantEvents.get();
    }
  }
}
