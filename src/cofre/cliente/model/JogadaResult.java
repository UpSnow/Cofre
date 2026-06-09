package cofre.cliente.model;

/**
 * Resultado de uma jogada do ponto de vista do cliente.
 */
public class JogadaResult {

    private final String  mensagem;
    private final boolean acerto;
    private final double     premioOuFundo;

    public JogadaResult(String mensagem, boolean acerto, double premioOuFundo) {
        this.mensagem      = mensagem;
        this.acerto        = acerto;
        this.premioOuFundo = premioOuFundo;
    }

    public String  getMensagem()      { return mensagem;      }
    public boolean isAcerto()         { return acerto;        }
    public double     getPremioOuFundo() { return premioOuFundo; }
}
