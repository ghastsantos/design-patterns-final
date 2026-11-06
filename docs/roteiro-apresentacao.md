# Roteiro do vídeo - LogKit

**Nome completo:** [preencher antes da gravação]  
**Link do vídeo não listado:** [preencher depois de publicar]

Tempo alvo: 7 a 9 minutos. O vídeo da atividade pode ter no máximo 10 minutos.

## 0:00-0:45 - Visão geral

- Apresente o problema: cada aplicação costuma misturar criação de mensagens, escolha de formato e destino de saída.
- Mostre o objetivo da biblioteca: configurar logging reutilizável dentro da aplicação, sem serviço externo.
- Mostre a separação `library/` e `demo/`.

## 0:45-2:00 - Como importar e executar

- Abra o `README.md` e mostre o comando `.\gradlew.bat :demo:run`.
- Explique que `demo` depende de `library` usando `implementation(project(":library"))`.
- Execute o comando e aponte o filtro de nível, as linhas JSON, a telemetria e o arquivo criado em `output/application.log`.

## 2:00-3:00 - Uso da API

- Abra `demo/src/main/java/br/pucpr/demo/DemoApplication.java`.
- Mostre a configuração fluente por meio de `LoggerBuilder`.
- Explique os níveis `TRACE`, `DEBUG`, `INFO`, `WARN` e `ERROR` e o fechamento automático dos destinos com `try`.

## 3:00-5:15 - Padrão Strategy

- Abra `LogFormatter.java`, `PlainTextFormatter.java` e `JsonLineFormatter.java`.
- Explique o contexto: o mesmo evento pode ser apresentado em texto ou JSON conforme a necessidade da aplicação.
- Mostre que o logger recebe o contrato `LogFormatter` e não precisa mudar quando uma implementação é trocada.
- Demonstre as duas configurações no cliente e cite como um formato próprio pode ser adicionado.

## 5:15-7:30 - Padrão Observer

- Abra `LogSink.java` e `Logger.java`.
- Explique que o logger publica cada evento aceito aos destinos registrados.
- Mostre `ConsoleSink`, `FileSink` e a classe `TelemetrySink` criada no módulo cliente.
- Relacione a decisão ao problema: adicionar um destino novo não exige alterar `Logger` ou os destinos já existentes.

## 7:30-8:30 - Extensibilidade e encerramento

- Mostre os pontos de extensão: `LogFormatter` para formatos e `LogSink` para destinos.
- Resuma que a aplicação pode combinar nível, formato e destinos sem depender de um backend separado.
- Encerre indicando o README e o comando para gerar o JAR.

## Checklist antes de enviar

- [ ] Preencher o nome completo no README e neste roteiro.
- [ ] Executar a demonstração e conferir `output/application.log`.
- [ ] Gravar a tela com áudio claro e duração de até 10 minutos.
- [ ] Publicar o vídeo no YouTube como não listado.
- [ ] Colar o link do vídeo no README e no campo de entrega da disciplina.
- [ ] Hospedar o repositório em GitHub, GitLab ou serviço equivalente e entregar somente o link do repositório na plataforma.
