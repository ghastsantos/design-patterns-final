# Roteiro de gravação - LogKit

**Apresentador:** Gastão Eduardo Santos Borges<br>
**Link do vídeo não listado:** [preencher depois de publicar]

Roteiro individual para aproximadamente 8 minutos, com margem para pausas e troca de telas. O limite da atividade é 10 minutos. Os trechos entre colchetes são instruções de tela; as falas podem ser lidas como estão ou adaptadas.

## 0:00-0:40 - Apresentação e objetivo

**Na tela:** abra o repositório no GitHub e mostre o título e a estrutura `library/` e `demo/`.

**Fala sugerida:**

> Olá, meu nome é Gastão Eduardo Santos Borges e vou apresentar a LogKit, uma biblioteca Java de logging e telemetria in-process. O objetivo é permitir que uma aplicação cliente registre eventos com diferentes níveis, formatos e destinos, sem precisar hospedar um serviço separado. O repositório separa o pacote reutilizável, em `library`, de uma aplicação cliente de demonstração, em `demo`.

## 0:40-1:35 - Problema e visão geral

**Na tela:** mostre `README.md` e a API de `LoggerBuilder`.

**Fala sugerida:**

> Sem uma biblioteca, cada aplicação acaba misturando a decisão do que registrar com detalhes como montar o texto e escolher onde gravá-lo. Isso dificulta trocar o formato ou acrescentar um destino. A LogKit concentra esse fluxo em uma API pequena: o cliente configura um logger com nível mínimo, formatador e um ou mais destinos. A biblioteca já oferece saídas para console e arquivo, e expõe interfaces para o cliente acrescentar comportamentos próprios.

## 1:35-2:50 - Uso padrão da biblioteca

**Na tela:** execute `.\gradlew.bat :demo:run`; depois mostre `demo/src/main/java/br/pucpr/demo/DemoApplication.java`, especialmente o primeiro logger.

**Fala sugerida:**

> Vou executar o cliente de exemplo. Aqui o logger é configurado com o nome da aplicação, o nível mínimo DEBUG, o formatador de texto e dois destinos prontos: console e arquivo. A mensagem TRACE fica abaixo do nível mínimo e não é entregue. As mensagens DEBUG e acima aparecem no console e são acrescentadas a `output/application.log`. O bloco `try` fecha o logger e seus destinos ao terminar.
>
> O segundo exemplo troca o formatador por `JsonLineFormatter`, sem alterar o logger. Assim, o mesmo tipo de evento pode ser escrito como texto legível ou como uma linha JSON.

## 2:50-3:40 - Extensão pelo cliente

**Na tela:** mostre a classe `TelemetrySink` dentro de `DemoApplication.java` e a configuração do logger de telemetria.

**Fala sugerida:**

> Agora o cliente cria um destino que a biblioteca não conhece: `TelemetrySink`. Ele implementa `LogSink`, recebe o evento estruturado e a linha formatada, conta os eventos WARN e ERROR e imprime a telemetria. O logger é configurado para WARN, então INFO é filtrado e só os dois eventos importantes chegam ao destino. A classe foi escrita no módulo cliente: não precisei alterar o código da biblioteca para adicionar essa integração.

## 3:40-5:35 - Padrão 1: Strategy

**Na tela:** abra `library/src/main/java/br/pucpr/logkit/LogFormatter.java`, `PlainTextFormatter.java` e `JsonLineFormatter.java`; depois mostre onde `LoggerBuilder` recebe o formatador.

**Fala sugerida:**

> O primeiro padrão que vou defender é Strategy. O problema de design é que a aplicação pode precisar apresentar o mesmo evento em formatos diferentes. Se a classe `Logger` decidisse o formato com vários `if` ou `switch`, cada novo formato exigiria modificar o núcleo e aumentaria o acoplamento.
>
> `LogFormatter` define o contrato `format(LogEvent)`. `PlainTextFormatter` e `JsonLineFormatter` são estratégias concretas que implementam esse contrato. O `LoggerBuilder` recebe uma dessas estratégias na configuração e o logger apenas a utiliza. O exemplo mostra essa troca entre texto e JSON sem alterar o fluxo de logging.
>
> Escolhi Strategy porque o formato é uma decisão que varia independentemente da captura do evento e dos destinos. Para criar outro formato, o desenvolvedor implementa `LogFormatter` e passa sua classe ao builder. Isso atende ao Open/Closed Principle: a extensão acontece por uma implementação nova, sem editar as classes existentes.

## 5:35-7:30 - Padrão 2: Observer

**Na tela:** abra `LogSink.java`, depois o método `log` de `Logger.java`, e compare `ConsoleSink`, `FileSink` e `TelemetrySink`.

**Fala sugerida:**

> O segundo padrão é Observer. O problema é permitir que um mesmo evento chegue a vários destinos sem fazer o logger depender diretamente de cada implementação. `LogSink` define o contrato do observador. Durante a configuração, o cliente inscreve um ou mais destinos com `addSink`.
>
> No método `log`, o logger primeiro verifica o nível mínimo, cria o evento e formata a mensagem uma vez. Depois notifica cada destino registrado com `onLog`. Console, arquivo e telemetria recebem o evento pelo mesmo contrato, embora tenham comportamentos diferentes.
>
> Observer é adequado porque os destinos podem variar e ser combinados por aplicação. Para incluir um destino novo, o cliente implementa `LogSink` e o registra no builder. O código de `Logger` e os destinos que já existem não precisam ser alterados. A implementação também continua notificando os outros destinos se um deles lançar uma exceção; ao final, a falha é propagada ao cliente.

## 7:30-8:20 - Empacotamento e encerramento

**Na tela:** mostre os comandos do README e a dependência de projeto `implementation(project(":library"))` em `demo/build.gradle.kts`.

**Fala sugerida:**

> A biblioteca pode ser empacotada com `.\gradlew.bat :library:jar`; o JAR fica em `library/build/libs/` e pode ser importado por outro projeto. A demonstração é um cliente separado, usado para mostrar a integração. Também rodei a suíte automatizada com `.\gradlew.bat :library:test`: são 11 testes, todos aprovados. A LogKit entrega configurações prontas para uso e pontos de extensão para formatos e destinos próprios. Obrigado.

## Checklist final

- [ ] Conferir a pronúncia do nome na gravação e manter o nome completo no README.
- [ ] Gravar a tela e conferir duração menor que 10 minutos.
- [ ] Mostrar a execução do cliente e o arquivo `output/application.log`.
- [ ] Mostrar os trechos citados de Strategy e Observer e explicar o problema que cada um resolve.
- [ ] Publicar o vídeo no YouTube como não listado e colar o link no README.
- [ ] Entregar na plataforma apenas o link do repositório.
