# 🔐 Cofre Digital

Aplicação cliente/servidor em Java que simula um cofre com código secreto e fundo acumulado entre os jogadores. Desenvolvida como trabalho prático da disciplina de Sistemas distribuídos.



---

## Como funciona o jogo

Cada cliente tenta adivinhar um código secreto de 0 a 999. A cada tentativa, o fundo do cofre cresce. Quem acertar leva 60% do que está acumulado.

**Regras:**

- O cliente envia seu nome e um número inteiro de 0 a 999
- O servidor sorteia um número aleatório de 0 a 999 para aquela jogada
- O fundo começa em R$ 0 e cresce **R$ 2** a cada jogada (de qualquer cliente)
- Se **acertar**: recebe `"Cofre aberto, [Nome]! Ganhou R$ XXX"` e o fundo zera
- Se **errar**: recebe `"Código errado, [Nome]. O cofre tem R$ YYY acumulados."`

---

## Tecnologias

| Camada | Tecnologia |
|---|---|
| Linguagem | Java 11+ |
| Comunicação | Sockets TCP (`java.net.Socket`) |
| Serialização | `ObjectOutputStream` / `ObjectInputStream` |
| Interface gráfica | Java Swing |
| Concorrência | `Thread` por cliente + `synchronized` |
| Build | `javac` (sem Maven/Gradle) |

---

## Arquitetura

O projeto é um **monólito modular**: um único repositório com dois processos independentes — servidor e cliente — que se comunicam via TCP.

```
src/cofre/
│
├── compartilhado/        ← DTOs trafegados pela rede (ambos os lados)
│   ├── Constantes.java     porta 12345, incremento R$2, prêmio 60%
│   ├── Pedido.java         { nome: String, aposta: int }  → cliente envia
│   └── Resposta.java       { mensagem: String, ... }      ← servidor devolve
│
├── servidor/             ← BACKEND (sem nenhum import de Swing)
│   ├── MainServidor.java   ponto de entrada — abre a janela do servidor
│   ├── model/
│   │   ├── CofreModel.java       estado compartilhado (fundo, acertos, clientes)
│   │   └── ResultadoJogada.java  DTO interno de resultado
│   ├── service/
│   │   └── JogoService.java      lógica do jogo — sorteio, prêmio, acesso sincronizado
│   ├── network/
│   │   ├── ServidorTCP.java      abre ServerSocket, aceita conexões, cria threads
│   │   └── ClienteHandler.java   atende 1 cliente por thread (lê Pedido, envia Resposta)
│   ├── view/
│   │   └── ServidorView.java     janela Swing com log e estatísticas em tempo real
│   ├── components/         LogPanel, StatCard
│   └── theme/              TemaServidor (cores e fontes)
│
└── cliente/              ← FRONTEND (sem nenhuma lógica de negócio)
    ├── MainCliente.java    ponto de entrada — monta dependências e abre a janela
    ├── model/
    │   └── JogadaResult.java     resultado de uma jogada do ponto de vista do cliente
    ├── network/
    │   └── ConexaoServidor.java  único lugar que abre Socket e usa ObjectStreams
    ├── service/
    │   └── ClienteService.java   executa jogadas em background, mantém contadores
    ├── view/
    │   └── ClienteView.java      janela Swing — só apresentação, sem código de rede
    ├── components/         FormularioPanel, HistoricoPanel, MiniStatCard
    └── theme/              TemaCliente (cores e fontes)
```

### Fluxo de uma jogada

```
Cliente                          Servidor
──────                           ────────
FormularioPanel (UI)
  └─ ClienteView.onJogar()
       └─ ClienteService.jogar()          (thread separada)
            └─ ConexaoServidor
                 └─ Socket → Pedido ──────► ClienteHandler (nova thread)
                                                └─ JogoService.processarJogada()
                                                     ├─ sorteia número
                                                     ├─ synchronized(lock)
                                                     │    └─ atualiza CofreModel
                                                     └─ monta Resposta
                 └─ Resposta ◄────────────────────────
            └─ JogadaResult
       └─ atualiza UI (SwingUtilities.invokeLater)
```

### Por que `synchronized`?

O fundo é uma **variável compartilhada** entre todas as threads de clientes. Sem sincronização, duas threads poderiam ler o mesmo valor, incrementar separadamente e gravar valores errados (race condition). O bloco `synchronized(lock)` em `JogoService.processarJogada()` garante que só uma thread por vez lê e modifica o fundo.

---

## Pré-requisitos

- **Java 11 ou superior** instalado
- Verificar com: `java -version`

Não há dependências externas. Nenhum Maven, Gradle ou biblioteca de terceiros.

---

## Como compilar e rodar

### 1. Clonar o repositório

```bash
git clone https://github.com/seu-usuario/cofre-digital.git
cd cofre-digital
```

### 2. Compilar

**Windows:**
```bat
compilar_tudo.bat
```

**Linux / macOS:**
```bash
mkdir -p out
javac -encoding UTF-8 -d out \
  src/cofre/compartilhado/Constantes.java \
  src/cofre/compartilhado/Pedido.java \
  src/cofre/compartilhado/Resposta.java \
  src/cofre/servidor/model/CofreModel.java \
  src/cofre/servidor/model/ResultadoJogada.java \
  src/cofre/servidor/service/JogoService.java \
  src/cofre/servidor/network/ClienteHandler.java \
  src/cofre/servidor/network/ServidorTCP.java \
  src/cofre/servidor/theme/TemaServidor.java \
  src/cofre/servidor/components/StatCard.java \
  src/cofre/servidor/components/LogPanel.java \
  src/cofre/servidor/view/ServidorView.java \
  src/cofre/servidor/MainServidor.java \
  src/cofre/cliente/model/JogadaResult.java \
  src/cofre/cliente/network/ConexaoServidor.java \
  src/cofre/cliente/service/ClienteService.java \
  src/cofre/cliente/theme/TemaCliente.java \
  src/cofre/cliente/components/MiniStatCard.java \
  src/cofre/cliente/components/HistoricoPanel.java \
  src/cofre/cliente/components/FormularioPanel.java \
  src/cofre/cliente/view/ClienteView.java \
  src/cofre/cliente/MainCliente.java
```

### 3. Rodar o servidor

Abra um terminal e execute:

```bash
java -cp out cofre.servidor.MainServidor
```

A janela do servidor abre com log de conexões e estatísticas em tempo real. O servidor fica escutando na porta **12345**.

### 4. Rodar o(s) cliente(s)

Abra um ou mais terminais adicionais e execute em cada um:

```bash
java -cp out cofre.cliente.MainCliente
```

Cada cliente abre sua própria janela. Você pode abrir quantos quiser — o servidor atende todos simultaneamente, cada um em sua própria thread.

> O servidor precisa estar rodando antes de qualquer cliente tentar se conectar.

---

## Rodando em máquinas diferentes (rede local)

Por padrão o cliente conecta em `localhost`. Para rodar servidor e cliente em máquinas diferentes:

1. Descubra o IP da máquina do servidor (ex: `192.168.1.10`)
2. Edite `src/cofre/compartilhado/Constantes.java`:

```java
public static final String HOST_PADRAO = "192.168.1.10"; // IP do servidor
```

3. Recompile e rode normalmente.

---

## Estrutura de arquivos do repositório

```
cofre-digital/
├── src/                    código-fonte Java
├── compilar_tudo.bat       script de compilação Windows
```


## Conceitos de Redes aplicados

**Sockets TCP** — comunicação confiável, orientada a conexão. O servidor usa `ServerSocket.accept()` em loop; cada `accept()` devolve um `Socket` dedicado àquele cliente.

**Serialização Java** — os objetos `Pedido` e `Resposta` implementam `Serializable` e são trafegados diretamente pela rede via `ObjectOutputStream` / `ObjectInputStream`, sem necessidade de converter para JSON ou XML.

**Multithreading** — cada `ClienteHandler` roda em sua própria thread (`new Thread(...).start()`), permitindo que o servidor atenda vários clientes ao mesmo tempo sem bloquear.

**Região crítica** — o fundo do cofre é acessado por múltiplas threads concorrentemente. O bloco `synchronized(lock)` em `JogoService` garante exclusão mútua: incremento do fundo, verificação do acerto e pagamento do prêmio acontecem atomicamente.

