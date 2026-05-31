package cofre.cliente.view;

import cofre.cliente.components.FormularioPanel;
import cofre.cliente.components.HistoricoPanel;
import cofre.cliente.components.MiniStatCard;
import cofre.cliente.model.JogadaResult;
import cofre.cliente.service.ClienteService;
import cofre.cliente.theme.TemaCliente;
import cofre.compartilhado.Constantes;

import javax.swing.*;
import java.awt.*;

/**
 * Frontend (View) do jogo — responsabilidade única: apresentação.
 *
 * Esta classe:
 *   ✔ Monta a janela Swing e seus componentes
 *   ✔ Coleta a entrada do usuário (nome e aposta)
 *   ✔ Delega a jogada ao {@link ClienteService}
 *   ✔ Atualiza a tela com o resultado recebido via callback
 *
 * Esta classe NÃO:
 *   ✗ Abre sockets nem faz I/O de rede (responsabilidade de ConexaoServidor)
 *   ✗ Mantém lógica de contagem própria (responsabilidade de ClienteService)
 *   ✗ Conhece Pedido, Resposta ou qualquer protocolo do servidor
 */
public class ClienteView {

    // ── Dependência injetada ──────────────────────────────────────────────────
    private final ClienteService service;

    // ── Componentes de UI ─────────────────────────────────────────────────────
    private final JFrame          frame;
    private final JLabel          cofreLabel;
    private final JLabel          statusLabel;
    private final FormularioPanel formulario;
    private final MiniStatCard    cardTentativas;
    private final MiniStatCard    cardVitorias;
    private final HistoricoPanel  historico;

    public ClienteView(ClienteService service) {
        this.service = service;

        frame = new JFrame("🔐  Cofre Digital — Cliente");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(520, 600);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.getContentPane().setBackground(TemaCliente.BG);
        frame.setLayout(new BorderLayout());

        statusLabel = new JLabel("PRONTO   ");
        frame.add(criarHeader(), BorderLayout.NORTH);

        // ── Centro ────────────────────────────────────────────────────────────
        JPanel center = new JPanel();
        center.setBackground(TemaCliente.BG);
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.setBorder(BorderFactory.createEmptyBorder(16, 16, 10, 16));

        cofreLabel = new JLabel("🔒", SwingConstants.CENTER);
        cofreLabel.setFont(new Font("Dialog", Font.PLAIN, 64));
        cofreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        center.add(cofreLabel);
        center.add(Box.createVerticalStrut(8));

        JLabel subtitulo = new JLabel("Adivinhe o código de 0 a 999", SwingConstants.CENTER);
        subtitulo.setFont(TemaCliente.FONT_SUBTIT);
        subtitulo.setForeground(TemaCliente.TEXT_DIM);
        subtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        center.add(subtitulo);
        center.add(Box.createVerticalStrut(20));

        formulario = new FormularioPanel(this::onJogar);
        center.add(formulario);
        center.add(Box.createVerticalStrut(14));

        JPanel statsRow = new JPanel(new GridLayout(1, 2, 10, 0));
        statsRow.setBackground(TemaCliente.BG);
        statsRow.setMaximumSize(new Dimension(480, 56));
        statsRow.setAlignmentX(Component.CENTER_ALIGNMENT);
        cardTentativas = new MiniStatCard("TENTATIVAS", "0", TemaCliente.TEXT_DIM);
        cardVitorias   = new MiniStatCard("VITÓRIAS",   "0", TemaCliente.VERDE);
        statsRow.add(cardTentativas);
        statsRow.add(cardVitorias);
        center.add(statsRow);
        center.add(Box.createVerticalStrut(12));

        historico = new HistoricoPanel();
        historico.setMaximumSize(new Dimension(488, 160));
        center.add(historico);

        frame.add(center, BorderLayout.CENTER);
        frame.add(criarFooter(), BorderLayout.SOUTH);
    }

    public void mostrar() { frame.setVisible(true); }

    // ── Ação do botão "Jogar" ─────────────────────────────────────────────────

    private void onJogar() {
        String nome   = formulario.getNome();
        int    aposta = formulario.getAposta();

        if (nome.isEmpty()) {
            JOptionPane.showMessageDialog(frame,
                    "Por favor, insira o seu nome.", "Campo obrigatório",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        formulario.setBloqueado(true);
        setStatus("CONECTANDO...", TemaCliente.ACCENT);

        // Delega ao ClienteService — a View não abre socket nem conhece protocolo
        service.jogar(nome, aposta,
                resultado -> SwingUtilities.invokeLater(() -> aplicarResposta(resultado)),
                erro      -> SwingUtilities.invokeLater(() -> mostrarErro(erro)));
    }

    // ── Atualização da UI ─────────────────────────────────────────────────────

    private void aplicarResposta(JogadaResult r) {
        cofreLabel.setText(r.isAcerto() ? "🔓" : "🔒");
        setStatus(r.isAcerto() ? "COFRE ABERTO!" : "CÓDIGO ERRADO",
                  r.isAcerto() ? TemaCliente.VERDE : TemaCliente.DANGER);

        historico.append(r.isAcerto(), r.getMensagem());
        cardTentativas.setValue(String.valueOf(service.getTentativas()));
        cardVitorias.setValue(String.valueOf(service.getVitorias()));

        if (r.isAcerto()) {
            JOptionPane.showMessageDialog(frame, r.getMensagem(),
                    "🏆 Parabéns!", JOptionPane.INFORMATION_MESSAGE);
            cofreLabel.setText("🔒");
        }

        formulario.setBloqueado(false);
    }

    private void mostrarErro(String msg) {
        setStatus("SERVIDOR OFFLINE", TemaCliente.DANGER);
        historico.append(false, "⚠️  " + msg);
        formulario.setBloqueado(false);
    }

    private void setStatus(String texto, Color cor) {
        statusLabel.setText(texto + "   ");
        statusLabel.setForeground(cor);
    }

    // ── Painéis fixos ─────────────────────────────────────────────────────────

    private JPanel criarHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(TemaCliente.CARD);
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, TemaCliente.ACCENT));
        header.setPreferredSize(new Dimension(520, 64));

        JLabel titulo = new JLabel("  🔐  COFRE DIGITAL");
        titulo.setFont(TemaCliente.FONT_TITULO);
        titulo.setForeground(TemaCliente.ACCENT);
        header.add(titulo, BorderLayout.WEST);

        statusLabel.setFont(TemaCliente.FONT_STATUS);
        statusLabel.setForeground(TemaCliente.VERDE);
        header.add(statusLabel, BorderLayout.EAST);

        return header;
    }

    private JPanel criarFooter() {
        JPanel footer = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));
        footer.setBackground(TemaCliente.CARD);
        footer.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, TemaCliente.BORDA));

        JLabel inf = new JLabel("  Conectando em "
                + Constantes.HOST_PADRAO + ":" + Constantes.PORTA
                + "   |   UFPB · DCX · Prof. Lucas da Silva Cruz");
        inf.setFont(TemaCliente.FONT_FOOTER);
        inf.setForeground(TemaCliente.TEXT_DIM);
        footer.add(inf);
        return footer;
    }
}
