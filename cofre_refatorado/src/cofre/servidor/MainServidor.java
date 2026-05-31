package cofre.servidor;

import cofre.servidor.view.ServidorView;
import javax.swing.SwingUtilities;

/**
 * Ponto de entrada do Servidor.
 *
 * Apenas instancia e exibe o ServidorView, que por sua vez
 * inicia o backend TCP internamente.
 *
 * Separação de responsabilidades:
 *   MainServidor → abre a janela
 *   ServidorView  → monta a GUI e dispara o ServidorTCP
 *   ServidorTCP   → aceita conexões e cria ClienteHandlers
 *   ClienteHandler→ lê Pedido, chama JogoService, devolve Resposta
 *   JogoService   → lógica de negócio (sorteio, prêmio, lock)
 */
public class MainServidor {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ServidorView().mostrar());
    }
}
