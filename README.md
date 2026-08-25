# Sistema de Gerenciamento de Alunos com WebSockets (Java puro)

Servidor WebSocket implementado em Java puro, sem nenhuma biblioteca externa —
o protocolo (handshake HTTP e framing) é implementado à mão, seguindo a
RFC 6455.

## Status atual

✅ Handshake HTTP → WebSocket
✅ Encode/decode de frames (texto, close, ping/pong)
✅ Echo de mensagens de texto (prova de conceito)
⬜ Modelo de `Aluno`
⬜ Persistência em CSV
⬜ Comandos de CRUD (add/list/edit/remove)
⬜ Broadcast para os outros clientes conectados

## Estrutura

```
WebSocketServer.java     -> ponto de entrada; ServerSocket + uma thread por cliente
ClientHandler.java       -> handshake de uma conexão + loop de leitura/resposta de frames
WebSocketFrame.java      -> encode/decode de frames WebSocket (RFC 6455)
WebSocketHandshake.java  -> upgrade HTTP -> WebSocket (cálculo do Sec-WebSocket-Accept)
```

### Por que não usar `BufferedReader` no handshake?

`WebSocketHandshake` lê os headers HTTP byte a byte, na mão, em vez de usar
`BufferedReader`. Isso é proposital: um `BufferedReader` faz *read-ahead* no
stream e pode consumir, junto com a última linha dos headers, os primeiros
bytes do frame WebSocket que vem logo em seguida no mesmo socket TCP —
corrompendo a conexão. Lendo byte a byte, paramos exatamente no fim dos
headers (`\r\n\r\n`).

## Como compilar e rodar

Requer JDK 11+ (usa `java.net.http.WebSocket` apenas no cliente de teste;
o servidor em si não depende de nada além de `java.net` e `java.io`).

```bash
javac *.java

# Sobe o servidor na porta 8080 (padrão) ou na porta informada
java WebSocketServer 8080
```

Para testar rapidamente, qualquer cliente WebSocket serve (navegador,
`wscat`, Postman, etc.). Exemplo em JavaScript, no console do navegador:

```js
const ws = new WebSocket("ws://localhost:8080");
ws.onmessage = (e) => console.log("recebido:", e.data);
ws.onopen = () => ws.send("hello");
```

Resposta esperada: `echo: hello`.

## Limitações conhecidas (por enquanto)

- Sem suporte a fragmentação de frames (mensagens grandes divididas em vários
  frames `FIN=0`).
- Sem TLS (`ws://`, não `wss://`).
- Sem tratamento de extensões (`Sec-WebSocket-Extensions`).
- Uma thread por conexão (sem pool/NIO) — adequado para o escopo do projeto,
  não para alta concorrência.

## Próximos passos

1. Modelo `Aluno` (id, nome, curso, idade etc.).
2. `AlunoRepository` lendo/escrevendo um arquivo CSV.
3. Protocolo de mensagens simples e delimitado (ex.: `ADD|nome|curso|idade`,
   `LIST`, `REMOVE|id`) trocado sobre os frames de texto já implementados.
4. Lista de conexões ativas no servidor + broadcast das mudanças para todos
   os clientes conectados.