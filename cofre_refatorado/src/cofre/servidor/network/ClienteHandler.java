package cofre.servidor.network;

import cofre.compartilhado.Pedido;
import cofre.compartilhado.Resposta;
import cofre.servidor.model.ResultadoJogada;
import cofre.servidor.service.JogoService;

import java.io.*;
import java.net.Socket;
import java.util.function.Consumer;

/**
 * Thread que atende uma única conexão: lê o Pedido, processa e envia a Resposta.
 */
public class ClienteHandler implements Runnable {

    private final Socket                    socket;
    private final JogoService               jogoService;
    private final Consumer<ResultadoJogada> onResultado;

    public ClienteHandler(Socket socket, JogoService jogoService,
                          Consumer<ResultadoJogada> onResultado) {
        this.socket      = socket;
        this.jogoService = jogoService;
        this.onResultado = onResultado;
    }

    @Override
    public void run() {
        try (ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream  in  = new ObjectInputStream(socket.getInputStream())) {

            Pedido          pedido    = (Pedido) in.readObject();
            ResultadoJogada resultado = jogoService.processarJogada(pedido);

            out.writeObject(new Resposta(
                    resultado.getMensagem(),
                    resultado.isAcerto(),
                    resultado.getPremioOuFundo()));
            out.flush();

            if (onResultado != null) onResultado.accept(resultado);

        } catch (Exception e) {
            System.err.println("[ClienteHandler] " + e.getMessage());
        } finally {
            try { socket.close(); } catch (IOException ignored) {}
        }
    }
}
