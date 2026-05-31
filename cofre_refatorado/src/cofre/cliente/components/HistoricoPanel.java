package cofre.cliente.components;

import cofre.cliente.theme.TemaCliente;
import javax.swing.*;
import java.awt.*;

/** Painel de histórico de jogadas com scroll automático. */
public class HistoricoPanel extends JPanel {

    private final JTextArea logArea;

    public HistoricoPanel() {
        setLayout(new BorderLayout());
        setBackground(TemaCliente.BG);
        setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel titulo = new JLabel("▸ HISTÓRICO");
        titulo.setFont(TemaCliente.FONT_LOG_TIT);
        titulo.setForeground(TemaCliente.TEXT_DIM);
        titulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 4, 0));
        add(titulo, BorderLayout.NORTH);

        logArea = new JTextArea(6, 40);
        logArea.setEditable(false);
        logArea.setBackground(TemaCliente.CARD);
        logArea.setForeground(TemaCliente.TEXT_MAIN);
        logArea.setFont(TemaCliente.FONT_LOG);
        logArea.setLineWrap(true);
        logArea.setWrapStyleWord(true);
        logArea.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));

        JScrollPane scroll = new JScrollPane(logArea);
        scroll.setBorder(BorderFactory.createLineBorder(TemaCliente.BORDA, 1));
        scroll.setMaximumSize(new Dimension(488, 140));
        add(scroll, BorderLayout.CENTER);
    }

    public void append(boolean acerto, String mensagem) {
        String linha = (acerto ? "✅ " : "❌ ") + mensagem + "\n";
        SwingUtilities.invokeLater(() -> {
            logArea.append(linha);
            logArea.setCaretPosition(logArea.getDocument().getLength());
        });
    }
}
