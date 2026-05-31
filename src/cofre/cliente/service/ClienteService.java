package cofre.cliente.service;

import cofre.cliente.model.JogadaResult;
import cofre.cliente.network.ConexaoServidor;

import java.util.function.Consumer;

/**
 * Serviço do Cliente: orquestra cada jogada em uma thread separada
 * para nunca bloquear a Event Dispatch Thread (EDT) do Swing.
 *
 * Responsabilidades:
 *   - Chamar {@link ConexaoServidor} em background
 *   - Manter contadores locais de tentativas e vitórias da sessão
 *   - Notificar a View via callbacks (onSucesso / onErro)
 *
 * Não contém nenhum código de UI nem de protocolo de rede.
 */
public class ClienteService {

    private final ConexaoServidor conexao;

    private int tentativas = 0;
    private int vitorias   = 0;

    public ClienteService(ConexaoServidor conexao) {
        this.conexao = conexao;
    }

    /**
     * Executa uma jogada em background.
     *
     * @param nome       nome do jogador
     * @param aposta     código escolhido (0–999)
     * @param onSucesso  chamado na thread de jogada quando o servidor responde
     * @param onErro     chamado na thread de jogada quando há falha de rede
     */
    public void jogar(String nome, int aposta,
                      Consumer<JogadaResult> onSucesso,
                      Consumer<String>       onErro) {

        new Thread(() -> {
            try {
                JogadaResult resultado = conexao.enviarPedido(nome, aposta);
                tentativas++;
                if (resultado.isAcerto()) vitorias++;
                onSucesso.accept(resultado);
            } catch (Exception e) {
                onErro.accept(e.getMessage() != null
                        ? e.getMessage()
                        : "Não foi possível conectar ao servidor.");
            }
        }, "jogada-thread").start();
    }

    public int getTentativas() { return tentativas; }
    public int getVitorias()   { return vitorias;   }
}
