package cofre.servidor.components;

import cofre.servidor.theme.TemaServidor;

import javax.swing.*;
import java.awt.*;

/**
 * Card de estatística reutilizável: título + valor dinâmico.
 */
public class StatCard extends JPanel {

    private final JLabel valorLabel;

    public StatCard(String titulo, String valorInicial, Color corValor) {
        setLayout(new BorderLayout());
        setBackground(TemaServidor.CARD);
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(TemaServidor.BORDA, 1),
                BorderFactory.createEmptyBorder(12, 14, 12, 14)));

        JLabel tLabel = new JLabel(titulo);
        tLabel.setFont(TemaServidor.FONT_CARD_T);
        tLabel.setForeground(TemaServidor.TEXT_DIM);
        add(tLabel, BorderLayout.NORTH);

        valorLabel = new JLabel(valorInicial);
        valorLabel.setFont(TemaServidor.FONT_CARD_V);
        valorLabel.setForeground(corValor);
        add(valorLabel, BorderLayout.CENTER);
    }

    /** Deve ser chamado na EDT. */
    public void setValue(String valor) { valorLabel.setText(valor); }
}
