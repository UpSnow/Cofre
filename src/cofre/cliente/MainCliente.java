package cofre.cliente;

import cofre.cliente.network.ConexaoServidor;
import cofre.cliente.service.ClienteService;
import cofre.cliente.view.ClienteView;

import javax.swing.SwingUtilities;

/**
 * Ponto de entrada do Cliente.
 *
 * Monta a cadeia de dependências (composição manual):
 *
 *   ConexaoServidor  ← sabe falar TCP com o servidor
 *        ↓
 *   ClienteService   ← orquestra jogadas em background, mantém contadores
 *        ↓
 *   ClienteView      ← pura apresentação Swing, sem código de rede
 *
 * Nenhuma das três camadas precisa conhecer as outras diretamente
 * além da que está imediatamente abaixo.
 */
public class MainCliente {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ConexaoServidor conexao  = new ConexaoServidor();
            ClienteService  service  = new ClienteService(conexao);
            ClienteView     view     = new ClienteView(service);
            view.mostrar();
        });
    }
}
