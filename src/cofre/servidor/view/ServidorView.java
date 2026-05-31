package cofre.servidor.view;

import cofre.compartilhado.Constantes;
import cofre.servidor.components.LogPanel;
import cofre.servidor.components.StatCard;
import cofre.servidor.model.CofreModel;
import cofre.servidor.model.ResultadoJogada;
import cofre.servidor.network.ServidorTCP;
import cofre.servidor.theme.TemaServidor;

import javax.swing.*;
import java.awt.*;

/**
 * Janela principal do Servidor: compõe os componentes visuais
 * e conecta-os ao backend via callbacks.
 */
public class ServidorView {

    private final JFrame   frame;
    private final StatCard cardFundo;
    private final StatCard cardClientes;
    private final StatCard cardAcertos;
    private final LogPanel logPanel;

    private final CofreModel cofre = new CofreModel();
    private final Object     lock  = new Object();

    public ServidorView() {
        frame = new JFrame("🏦  Cofre Digital — Servidor");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 520);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.getContentPane().setBackground(TemaServidor.BG);
        frame.setLayout(new BorderLayout());

        frame.add(criarHeader(), BorderLayout.NORTH);

        JPanel center = new JPanel(new BorderLayout());
        center.setBackground(TemaServidor.BG);

        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 10, 0));
        statsPanel.setBackground(TemaServidor.BG);
        statsPanel.setBorder(BorderFactory.createEmptyBorder(12, 12, 6, 12));

        cardFundo    = new StatCard("FUNDO ACUMULADO",    "R$ 0", TemaServidor.ACCENT);
        cardClientes = new StatCard("CLIENTES ATENDIDOS", "0",    TemaServidor.TEXT_MAIN);
        cardAcertos  = new StatCard("COFRES ABERTOS",     "0",    TemaServidor.VERDE);

        statsPanel.add(cardFundo);
        statsPanel.add(cardClientes);
        statsPanel.add(cardAcertos);
        center.add(statsPanel, BorderLayout.NORTH);

        logPanel = new LogPanel();
        center.add(logPanel, BorderLayout.CENTER);
        frame.add(center, BorderLayout.CENTER);

        frame.add(criarFooter(), BorderLayout.SOUTH);
    }

    public void mostrar() {
        frame.setVisible(true);
        iniciarBackend();
    }

    private void iniciarBackend() {
        ServidorTCP tcp = new ServidorTCP(cofre, lock, this::onResultado, this::onLog);
        Thread t = new Thread(tcp, "servidor-main");
        t.setDaemon(true);
        t.start();
    }

    private void onResultado(ResultadoJogada r) {
        SwingUtilities.invokeLater(() -> {
            cardFundo.setValue("R$ " + cofre.getFundo());
            cardClientes.setValue(String.valueOf(cofre.getTotalClientes()));
            cardAcertos.setValue(String.valueOf(cofre.getTotalAcertos()));
        });
        if (r.isAcerto()) {
            logPanel.append("✅ ACERTO  | " + r.getNomeJogador()
                    + " | aposta=" + r.getAposta()
                    + " sorteio=" + r.getSorteio()
                    + " | prémio=R$" + r.getPremioOuFundo() + " | fundo zerado");
        } else {
            logPanel.append("❌ ERRO    | " + r.getNomeJogador()
                    + " | aposta=" + r.getAposta()
                    + " sorteio=" + r.getSorteio()
                    + " | fundo=R$" + r.getPremioOuFundo());
        }
    }

    private void onLog(String msg) { logPanel.append(msg); }

    private JPanel criarHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(TemaServidor.CARD);
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, TemaServidor.ACCENT));
        header.setPreferredSize(new Dimension(700, 64));

        JLabel titulo = new JLabel("  🏦  COFRE DIGITAL  —  SERVIDOR");
        titulo.setFont(TemaServidor.FONT_TITULO);
        titulo.setForeground(TemaServidor.ACCENT);
        header.add(titulo, BorderLayout.WEST);

        JLabel status = new JLabel("● AGUARDANDO CONEXÕES   ");
        status.setFont(TemaServidor.FONT_STATUS);
        status.setForeground(TemaServidor.VERDE);
        header.add(status, BorderLayout.EAST);

        return header;
    }

    private JPanel criarFooter() {
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.LEFT));
        footer.setBackground(TemaServidor.CARD);
        footer.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, TemaServidor.BORDA));

        JLabel label = new JLabel("  Porta: " + Constantes.PORTA
                + "   |   UFPB · DCX · Prof. Lucas da Silva Cruz");
        label.setFont(TemaServidor.FONT_FOOTER);
        label.setForeground(TemaServidor.TEXT_DIM);
        footer.add(label);
        return footer;
    }
}
