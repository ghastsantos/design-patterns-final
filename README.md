# LogKit - biblioteca de logging para Java

**Autor:** [preencha com seu nome completo]  
**Vídeo de apresentação:** [cole aqui o link não listado do YouTube]

LogKit é uma biblioteca Java para registrar eventos dentro do processo da aplicação. O projeto contém a biblioteca em `library/` e um cliente independente em `demo/`, que importa a biblioteca como uma dependência Gradle.

Ela oferece níveis de log, filtro por nível mínimo, formatação legível ou JSON Lines, saída no console ou em arquivo e uma interface para criar novos destinos no próprio projeto cliente.

## Requisitos

- JDK 21;
- JUnit 6 para executar a suíte de testes;
- Windows PowerShell ou um terminal compatível com Gradle.

## Executar o exemplo

Na raiz do repositório, rode:

```powershell
.\gradlew.bat :demo:run
```

O exemplo mostra mensagens filtradas por nível, saída em texto, saída JSON Lines, gravação em `output/application.log` e um destino personalizado de telemetria. Mensagens abaixo de `DEBUG` não aparecem no primeiro logger; mensagens abaixo de `WARN` não chegam ao destino de telemetria.

Para gerar o pacote da biblioteca:

```powershell
.\gradlew.bat :library:jar
```

O arquivo JAR será gerado em `library/build/libs/`. O módulo cliente do mesmo repositório usa a biblioteca por meio de `implementation(project(":library"))`. Em outro projeto Gradle, copie o JAR para `libs/` e declare:

```kotlin
dependencies {
    implementation(files("libs/library-1.0.0.jar"))
}
```

Para executar os testes automatizados da biblioteca:

```powershell
.\gradlew.bat :library:test
```

## Exemplo de uso

```java
import br.pucpr.logkit.ConsoleSink;
import br.pucpr.logkit.FileSink;
import br.pucpr.logkit.JsonLineFormatter;
import br.pucpr.logkit.LogLevel;
import br.pucpr.logkit.Logger;
import br.pucpr.logkit.LoggerBuilder;
import java.nio.file.Path;

try (Logger logger = new LoggerBuilder()
    .name("pedido")
    .minimumLevel(LogLevel.INFO)
    .formatter(new JsonLineFormatter())
    .addSink(new ConsoleSink())
    .addSink(new FileSink(Path.of("pedido.jsonl")))
    .build()) {
  logger.debug("Detalhe de diagnóstico ignorado neste nível.");
  logger.info("Pedido recebido.");
  logger.error("Falha ao processar o pagamento.");
}
```

O logger fecha os destinos ao sair do bloco `try`. O `FileSink` cria o arquivo se necessário, acrescenta novas linhas ao final e usa UTF-8. A pasta pai do arquivo precisa existir. Falhas de abertura, gravação e fechamento são apresentadas como `IllegalStateException` com a causa original. Se um destino falhar ao receber um evento, os outros ainda são notificados; depois a falha é propagada ao código cliente.

## Níveis disponíveis

| Nível mínimo | Eventos entregues |
| --- | --- |
| `TRACE` | TRACE, DEBUG, INFO, WARN e ERROR |
| `DEBUG` | DEBUG, INFO, WARN e ERROR |
| `INFO` | INFO, WARN e ERROR |
| `WARN` | WARN e ERROR |
| `ERROR` | ERROR |

## Estender a biblioteca

Para adicionar um destino, implemente `LogSink` no projeto cliente. O destino recebe o evento estruturado e a linha já formatada, então pode enviar métricas, guardar eventos em memória ou encaminhar os dados a outro componente da própria aplicação.

```java
public final class MeuDestino implements LogSink {
  @Override
  public void onLog(LogEvent event, String formattedMessage) {
    if (event.level().includes(LogLevel.ERROR)) {
      // Integrar com a regra específica da aplicação cliente.
    }
  }
}
```

Para adicionar outro formato, implemente `LogFormatter` e passe a instância a `.formatter(...)`. O `JsonLineFormatter` escapa aspas, barras invertidas e caracteres de controle para manter uma linha JSON válida por evento.

## Padrões de projeto

### Strategy - formatação intercambiável

`LogFormatter` define o contrato de formatação. `PlainTextFormatter` e `JsonLineFormatter` oferecem estratégias concretas. O `LoggerBuilder` recebe uma estratégia e o logger a utiliza sem conhecer os detalhes de cada formato. Isso permite incluir um formato novo implementando a interface, sem alterar o núcleo da biblioteca.

### Observer - destinos inscritos nos eventos

Cada `LogSink` é um observador inscrito durante a configuração com `.addSink(...)`. Depois de filtrar e formatar um evento, `Logger` notifica os destinos. O cliente pode adicionar ou remover destinos na configuração sem mudar a lógica do logger.

### Builder - configuração legível

`LoggerBuilder` reúne nome, nível mínimo, formatador e destinos, valida a configuração e cria um `Logger`. Assim, a construção do logger fica clara e não exige um construtor com muitos parâmetros.

## Estrutura

```text
library/  API pública e implementação da biblioteca
demo/     aplicação cliente independente que importa library
docs/     roteiro da apresentação e documentos do projeto
```

## Apresentação

Use [`docs/roteiro-apresentacao.md`](docs/roteiro-apresentacao.md) como roteiro. O vídeo deve ter até 10 minutos e mostrar o repositório, a execução, o cliente estendendo a biblioteca e a defesa dos padrões Strategy e Observer.
