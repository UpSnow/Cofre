# Cofre Digital — Monólito Modular Refatorado

Mesmo projeto, mesma pasta `src/`, mesmo `out/`.
A diferença está na **separação clara de responsabilidades**:
cada classe faz uma única coisa.

---

## Estrutura de pacotes

```
src/cofre/
│
├── compartilhado/              ← DTOs trafegados entre cliente e servidor
│   ├── Constantes.java           porta, host e regras do jogo
│   ├── Pedido.java               enviado pelo cliente → servidor  (Serializable)
│   └── Resposta.java             enviado pelo servidor → cliente  (Serializable)
│
│   ─────────────────────────────────────────────────────────────────────────────
│
├── servidor/                   ← BACKEND — zero import de Swing
│   │
│   ├── MainServidor.java         ponto de entrada: abre a janela do servidor
│   │
│   ├── model/
│   │   ├── CofreModel.java         estado do cofre (fundo, acertos, clientes)
│   │   └── ResultadoJogada.java    DTO interno de resultado (servidor → view)
│   │
│   ├── service/
│   │   └── JogoService.java        LÓGICA DE NEGÓCIO: sorteio, prêmio, lock
│   │
│   ├── network/
│   │   ├── ServidorTCP.java        abre ServerSocket, aceita conexões
│   │   └── ClienteHandler.java     atende 1 conexão por thread
│   │
│   ├── components/
│   │   ├── LogPanel.java           painel de log com timestamp e scroll
│   │   └── StatCard.java           card reutilizável: título + valor dinâmico
│   │
│   ├── theme/
│   │   └── TemaServidor.java       cores e fontes da janela do servidor
│   │
│   └── view/
│       └── ServidorView.java       janela Swing + dispara ServidorTCP via callbacks
│
│   ─────────────────────────────────────────────────────────────────────────────
│
└── cliente/                    ← FRONTEND — zero lógica de negócio
    │
    ├── MainCliente.java          ponto de entrada: monta dependências e abre janela
    │
    ├── model/
    │   └── JogadaResult.java       resultado de uma jogada do ponto de vista do cliente
    │
    ├── network/
    │   └── ConexaoServidor.java    ÚNICO lugar que abre Socket e usa ObjectStreams
    │
    ├── service/
    │   └── ClienteService.java     orquestra jogadas em background, mantém contadores
    │
    ├── components/
    │   ├── FormularioPanel.java    campo nome + spinner código + botão jogar
    │   ├── HistoricoPanel.java     log das jogadas com scroll automático
    │   └── MiniStatCard.java       mini-card: tentativas / vitórias
    │
    ├── theme/
    │   └── TemaCliente.java        cores e fontes da janela do cliente
    │
    └── view/
        └── ClienteView.java        PURA APRESENTAÇÃO — monta Swing, chama service
```

---

## O que mudou em relação ao monólito original

| Arquivo | Situação | O que foi corrigido |
|---|---|---|
| `MainServidor.java` | **Reescrito** | Antes rodava sem GUI no terminal. Agora abre a `ServidorView` (que já existia e iniciava o TCP) |
| `MainCliente.java` | **Reescrito** | Agora monta a cadeia `ConexaoServidor → ClienteService → ClienteView` via injeção de dependências |
| `ClienteView.java` | **Reescrito** | Antes misturava código TCP (`Socket`, `ObjectOutputStream`) com a UI. Agora delega tudo ao `ClienteService` |
| `ConexaoServidor.java` | **Criado** | Existia no original mas não era usado. Agora é a única classe que faz I/O de rede no cliente |
| `ClienteService.java` | **Criado** | Existia no original mas não era usado. Agora é a única classe que mantém contadores da sessão |
| Todos os demais | Inalterados | `JogoService`, `ServidorTCP`, `ClienteHandler`, `ServidorView`, `CofreModel`, DTOs, temas, components |

---

## Fluxo de dados

### Lado Cliente (frontend)
```
MainCliente
  └─ new ConexaoServidor()        (camada de rede: sabe abrir TCP)
  └─ new ClienteService(conexao)  (camada de serviço: orquestra + contadores)
  └─ new ClienteView(service)     (camada de apresentação: só Swing)
       └─ onJogar() → service.jogar(nome, aposta, onSucesso, onErro)
                           └─ conexao.enviarPedido(nome, aposta)
                                  └─ Socket → Pedido → Resposta → JogadaResult
```

### Lado Servidor (backend)
```
MainServidor
  └─ new ServidorView().mostrar()
       └─ iniciarBackend()
            └─ new ServidorTCP(cofre, lock, onResultado, onLog)
                 └─ ss.accept() → new ClienteHandler(socket, jogoService, onResultado)
                                        └─ jogoService.processarJogada(pedido)
                                             └─ CofreModel (estado sincronizado)
```

---

## Como compilar e rodar

### Compilar tudo
```bat
compilar_tudo.bat
```

### Rodar o servidor (terminal 1)
```bat
java -cp out cofre.servidor.MainServidor
```
> Abre a janela do servidor com log e estatísticas em tempo real.

### Rodar um ou mais clientes (terminal 2, 3 ...)
```bat
java -cp out cofre.cliente.MainCliente
```
> Abre a janela do cliente. Pode abrir quantas quiser — o servidor atende simultaneamente.

---

UFPB · DCX · Prof. Lucas da Silva Cruz
