package cofre.servidor.components;

import cofre.servidor.theme.TemaServidor;

import javax.swing.*;
import java.awt.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Painel de log com scroll automático e timestamp por linha.
 */
public class LogPanel extends JPanel {

    private final JTextArea logArea;

    public LogPanel() {
        setLayout(new BorderLayout());
        setBackground(TemaServidor.BG);
        setBorder(BorderFactory.createEmptyBorder(0, 12, 12, 12));

        JLabel titulo = new JLabel("▸ LOG DE EVENTOS");
        titulo.setFont(TemaServidor.FONT_LOG_TIT);
        titulo.setForeground(TemaServidor.TEXT_DIM);
        titulo.setBorder(BorderFactory.createEmptyBorder(4, 0, 4, 0));
        add(titulo, BorderLayout.NORTH);

        logArea = new JTextArea();
        logArea.setEditable(false);
        logArea.setBackground(TemaServidor.CARD);
        logArea.setForeground(TemaServidor.TEXT_MAIN);
        logArea.setFont(TemaServidor.FONT_LOG);
        logArea.setLineWrap(true);
        logArea.setWrapStyleWord(true);
        logArea.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
        logArea.setCaretColor(TemaServidor.ACCENT);

        JScrollPane scroll = new JScrollPane(logArea);
        scroll.setBorder(BorderFactory.createLineBorder(TemaServidor.BORDA, 1));
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        add(scroll, BorderLayout.CENTER);
    }

    /** Seguro para chamar de qualquer thread. */
    public void append(String mensagem) {
        String hora  = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        String linha = "[" + hora + "] " + mensagem + "\n";
        SwingUtilities.invokeLater(() -> {
            logArea.append(linha);
            logArea.setCaretPosition(logArea.getDocument().getLength());
        });
    }
}
