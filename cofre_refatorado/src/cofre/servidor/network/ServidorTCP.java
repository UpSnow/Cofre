package cofre.servidor.network;

import cofre.compartilhado.Constantes;
import cofre.servidor.model.CofreModel;
import cofre.servidor.model.ResultadoJogada;
import cofre.servidor.service.JogoService;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.function.Consumer;

/**
 * Abre o ServerSocket e despacha um ClienteHandler por conexão recebida.
 */
public class ServidorTCP implements Runnable {

    private final JogoService               jogoService;
    private final Consumer<ResultadoJogada> onResultado;
    private final Consumer<String>          onLog;

    public ServidorTCP(CofreModel cofre, Object lock,
                       Consumer<ResultadoJogada> onResultado,
                       Consumer<String> onLog) {
        this.jogoService = new JogoService(cofre, lock);
        this.onResultado = onResultado;
        this.onLog       = onLog;
    }

    @Override
    public void run() {
        onLog.accept("Servidor iniciado na porta " + Constantes.PORTA);
        try (ServerSocket ss = new ServerSocket(Constantes.PORTA)) {
            while (!Thread.currentThread().isInterrupted()) {
                Socket cliente = ss.accept();
                int num = jogoService.getCofre().incrementarClientes();
                onLog.accept("Conexão #" + num + " aceita");
                new Thread(
                    new ClienteHandler(cliente, jogoService, onResultado),
                    "cliente-" + num
                ).start();
            }
        } catch (IOException e) {
            onLog.accept("ERRO: " + e.getMessage());
        }
    }

    public JogoService getJogoService() { return jogoService; }
}
