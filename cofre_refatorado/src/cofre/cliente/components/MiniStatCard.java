package cofre.cliente.components;

import cofre.cliente.theme.TemaCliente;
import javax.swing.*;
import java.awt.*;

/** Mini card de estatística (tentativas / vitórias). */
public class MiniStatCard extends JPanel {

    private final JLabel valorLabel;

    public MiniStatCard(String titulo, String valorInicial, Color corValor) {
        setLayout(new BorderLayout());
        setBackground(TemaCliente.CARD);
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(TemaCliente.BORDA, 1),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)));

        JLabel t = new JLabel(titulo);
        t.setFont(TemaCliente.FONT_MINI_T);
        t.setForeground(TemaCliente.TEXT_DIM);
        add(t, BorderLayout.NORTH);

        valorLabel = new JLabel(valorInicial);
        valorLabel.setFont(TemaCliente.FONT_MINI_V);
        valorLabel.setForeground(corValor);
        add(valorLabel, BorderLayout.CENTER);
    }

    public void setValue(String valor) { valorLabel.setText(valor); }
}
