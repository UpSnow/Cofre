package cofre.servidor.service;

import cofre.compartilhado.Constantes;
import cofre.compartilhado.Pedido;
import cofre.servidor.model.CofreModel;
import cofre.servidor.model.ResultadoJogada;

import java.util.Random;

/**
 * Lógica de negócio do jogo. Processa cada jogada de forma thread-safe.
 */
public class JogoService {

    private final CofreModel cofre;
    private final Object     lock;
    private final Random     rng = new Random();

    public JogoService(CofreModel cofre, Object lock) {
        this.cofre = cofre;
        this.lock  = lock;
    }

    public ResultadoJogada processarJogada(Pedido pedido) {
        int sorteio = rng.nextInt(Constantes.CODIGO_MAX + 1);

        synchronized (lock) {
            cofre.incrementarFundo();
            boolean acerto = pedido.getAposta() == sorteio;

            if (acerto) {
                double premio = cofre.pagarPremioEZerar();
                return new ResultadoJogada(
                        pedido.getNome(), pedido.getAposta(), sorteio,
                        true, premio,
                        // Mensagem exata definida no enunciado
                        "Cofre aberto, " + pedido.getNome()
                        + "! Ganhou R$ " + premio);
            } else {
                int fundo = cofre.getFundo();
                return new ResultadoJogada(
                        pedido.getNome(), pedido.getAposta(), sorteio,
                        false, fundo,
                        // Mensagem exata definida no enunciado
                        "Código errado, " + pedido.getNome()
                        + ". O cofre tem R$ " + fundo + " acumulados.");
            }
        }
    }

    public CofreModel getCofre() { return cofre; }
}
