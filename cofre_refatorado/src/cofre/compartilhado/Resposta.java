package cofre.compartilhado;

import java.io.Serializable;

/** Devolvido pelo servidor ao cliente com o resultado da jogada. */
public class Resposta implements Serializable {
    private static final long serialVersionUID = 2L;

    private final String  mensagem;
    private final boolean acerto;
    private final int     premioOuFundo;

    public Resposta(String mensagem, boolean acerto, int premioOuFundo) {
        this.mensagem      = mensagem;
        this.acerto        = acerto;
        this.premioOuFundo = premioOuFundo;
    }

    public String  getMensagem()      { return mensagem;      }
    public boolean isAcerto()         { return acerto;        }
    public int     getPremioOuFundo() { return premioOuFundo; }
}
