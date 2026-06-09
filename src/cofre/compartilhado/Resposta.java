package cofre.compartilhado;

import java.io.Serializable;

/**
 * Devolvido pelo servidor ao cliente com o resultado da jogada.
 *
 * O enunciado define o contrato mínimo como { mensagem: String }.
 * Os dois campos extras (acerto e premioOuFundo) são extensões que
 * permitem à interface gráfica do cliente exibir ícones e contadores
 * sem precisar interpretar o texto da mensagem.
 * A mensagem em si segue exatamente o texto especificado no enunciado.
 */
public class Resposta implements Serializable {
    private static final long serialVersionUID = 2L;

    private final String  mensagem;
    private final boolean acerto;
    private final double     premioOuFundo;

    public Resposta(String mensagem, boolean acerto, double premioOuFundo) {
        this.mensagem      = mensagem;
        this.acerto        = acerto;
        this.premioOuFundo = premioOuFundo;
    }

    public String  getMensagem()      { return mensagem;      }
    public boolean isAcerto()         { return acerto;        }
    public double     getPremioOuFundo() { return premioOuFundo; }
}
