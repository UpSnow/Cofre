package cofre.cliente.network;

import cofre.cliente.model.JogadaResult;
import cofre.compartilhado.Constantes;
import cofre.compartilhado.Pedido;
import cofre.compartilhado.Resposta;

import java.io.*;
import java.net.Socket;

/**
 * Camada de rede do Cliente.
 *
 * Responsabilidade única: abrir uma conexão TCP com o servidor,
 * serializar o {@link Pedido} e desserializar a {@link Resposta},
 * devolvendo um {@link JogadaResult} para quem chamou.
 *
 * Não sabe nada de Swing, nem de regras de negócio.
 */
public class ConexaoServidor {

    private final String host;
    private final int    porta;

    /** Usa host e porta definidos em {@link Constantes}. */
    public ConexaoServidor() {
        this(Constantes.HOST_PADRAO, Constantes.PORTA);
    }

    public ConexaoServidor(String host, int porta) {
        this.host  = host;
        this.porta = porta;
    }

    /**
     * Envia um pedido ao servidor e retorna o resultado.
     *
     * @throws IOException            falha de rede ou servidor offline
     * @throws ClassNotFoundException protocolo incompatível
     */
    public JogadaResult enviarPedido(String nome, int aposta)
            throws IOException, ClassNotFoundException {

        try (Socket             socket = new Socket(host, porta);
             ObjectOutputStream out    = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream  in     = new ObjectInputStream(socket.getInputStream())) {

            out.writeObject(new Pedido(nome, aposta));
            out.flush();

            Resposta resp = (Resposta) in.readObject();
            return new JogadaResult(resp.getMensagem(), resp.isAcerto(), resp.getPremioOuFundo());
        }
    }
}
