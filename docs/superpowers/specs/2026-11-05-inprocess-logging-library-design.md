# Biblioteca de Logging In-Process - Design

## Intenção

Entregar uma biblioteca Java importável para aplicações que precisem registrar eventos sem depender de um serviço externo. O projeto deve deixar claro como adicionar novos formatos e destinos sem alterar o núcleo.

## Objetivos

- Fornecer API fluente para criar loggers com nome, nível mínimo, formato e destinos.
- Incluir níveis TRACE, DEBUG, INFO, WARN e ERROR.
- Produzir linhas de log em formato legível e JSON Lines.
- Oferecer destinos prontos para console e arquivo.
- Permitir que aplicações adicionem seus próprios destinos implementando uma interface pública.
- Demonstrar o uso da biblioteca em um módulo cliente separado.
- Documentar instalação local, configuração, extensibilidade e os padrões Strategy e Observer.

## Fora de escopo

- Serviço de backend, API REST ou aplicação final voltada ao usuário.
- Rede, envio remoto, rotação de arquivos, configuração por YAML/JSON, dependências de logging de terceiros ou integração com frameworks.
- Publicação no Maven Central, hospedagem do repositório e gravação/publicação do vídeo, que dependem de contas externas do aluno.

## Decisões

- Linguagem: Java 21, alinhada à atividade mais recente do workspace.
- Build: Gradle com módulos `library` e `demo`; `demo` importa `library` como dependência Gradle.
- Dependências de execução: nenhuma.
- Strategy: `LogFormatter` define a estratégia de conversão de `LogEvent` em texto. `PlainTextFormatter` e `JsonLineFormatter` são estratégias intercambiáveis.
- Observer: `Logger` notifica cada `LogSink` inscrito quando um evento passa pelo nível mínimo. A interface recebe o evento estruturado e sua linha formatada, permitindo destinos personalizados.
- Builder: `LoggerBuilder` concentra a configuração e entrega uma instância validada de `Logger`.
- A formatação ocorre uma vez por evento e só depois do filtro de nível.
- `FileSink` abre o arquivo em modo append e é fechado junto ao `Logger`.

## API prevista

- `LogLevel`: enum com níveis ordenados TRACE a ERROR.
- `LogEvent`: registro imutável com instante, nível, nome do logger e mensagem.
- `LogFormatter`: `String format(LogEvent event)`.
- `LogSink`: `void onLog(LogEvent event, String formattedMessage)` e `close()` padrão sem operação.
- `Logger`: métodos `trace`, `debug`, `info`, `warn`, `error`, mais `close()`.
- `LoggerBuilder`: `name(String)`, `minimumLevel(LogLevel)`, `formatter(LogFormatter)`, `addSink(LogSink)` e `build()`.
- Destinos prontos: `ConsoleSink` e `FileSink`.

## Critérios de aceite

- O cliente de demonstração compila como módulo separado e usa somente a API pública da biblioteca.
- A demonstração mostra filtro de nível, saída legível, saída JSON, arquivo e um destino personalizado.
- O README inclui os comandos Windows para executar e empacotar a biblioteca, exemplos de importação e tabela dos padrões.
- Um roteiro de vídeo de até 10 minutos cobre visão geral, uso, extensão e defesa de dois padrões.
- Nome do aluno e URL publicada do vídeo ficam indicados como campos a preencher.
