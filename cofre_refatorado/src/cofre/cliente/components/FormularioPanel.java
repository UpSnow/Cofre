package cofre.cliente.components;

import cofre.cliente.theme.TemaCliente;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/** Formulário de entrada: nome, código e botão de jogar. */
public class FormularioPanel extends JPanel {

    private final JTextField nomeField;
    private final JSpinner   apostaSpinner;
    private final JButton    jogarBtn;

    public FormularioPanel(Runnable onJogar) {
        setLayout(new GridBagLayout());
        setBackground(TemaCliente.CARD);
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(TemaCliente.BORDA, 1),
                BorderFactory.createEmptyBorder(18, 20, 18, 20)));
        setMaximumSize(new Dimension(480, 180));
        setAlignmentX(Component.CENTER_ALIGNMENT);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill   = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        JLabel lNome = new JLabel("Nome:");
        lNome.setFont(TemaCliente.FONT_LABEL);
        lNome.setForeground(TemaCliente.TEXT_DIM);
        add(lNome, gbc);

        gbc.gridx = 1; gbc.weightx = 1;
        nomeField = new JTextField(16);
        estilizarCampo(nomeField);
        add(nomeField, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        JLabel lCod = new JLabel("Código (0-999):");
        lCod.setFont(TemaCliente.FONT_LABEL);
        lCod.setForeground(TemaCliente.TEXT_DIM);
        add(lCod, gbc);

        gbc.gridx = 1; gbc.weightx = 1;
        apostaSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 999, 1));
        estilizarSpinner(apostaSpinner);
        add(apostaSpinner, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        jogarBtn = new JButton("⚡  TENTAR ABRIR O COFRE");
        jogarBtn.setFont(TemaCliente.FONT_BTN);
        jogarBtn.setBackground(TemaCliente.ACCENT);
        jogarBtn.setForeground(new Color(20, 20, 20));
        jogarBtn.setFocusPainted(false);
        jogarBtn.setBorderPainted(false);
        jogarBtn.setOpaque(true);
        jogarBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        jogarBtn.setPreferredSize(new Dimension(300, 40));
        jogarBtn.addActionListener(e -> onJogar.run());
        jogarBtn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                if (jogarBtn.isEnabled()) jogarBtn.setBackground(new Color(255, 215, 50));
            }
            public void mouseExited(MouseEvent e) {
                jogarBtn.setBackground(TemaCliente.ACCENT);
            }
        });
        add(jogarBtn, gbc);
    }

    public String getNome()              { return nomeField.getText().trim();  }
    public int    getAposta()            { return (int) apostaSpinner.getValue(); }
    public void   setBloqueado(boolean b){ jogarBtn.setEnabled(!b);            }

    private static void estilizarCampo(JTextField tf) {
        tf.setBackground(TemaCliente.FIELD);
        tf.setForeground(TemaCliente.TEXT_MAIN);
        tf.setCaretColor(TemaCliente.TEXT_MAIN);
        tf.setFont(TemaCliente.FONT_FIELD);
        tf.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(TemaCliente.BORDA, 1),
                BorderFactory.createEmptyBorder(4, 8, 4, 8)));
    }

    private static void estilizarSpinner(JSpinner sp) {
        sp.setBackground(TemaCliente.FIELD);
        sp.setBorder(BorderFactory.createLineBorder(TemaCliente.BORDA, 1));
        JComponent ed = sp.getEditor();
        if (ed instanceof JSpinner.DefaultEditor def) {
            JTextField tf = def.getTextField();
            tf.setBackground(TemaCliente.FIELD);
            tf.setForeground(TemaCliente.TEXT_MAIN);
            tf.setFont(TemaCliente.FONT_FIELD);
            tf.setCaretColor(TemaCliente.TEXT_MAIN);
            tf.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 4));
        }
    }
}
