package cofre.servidor.model;

/**
 * Resultado interno de uma jogada: transporta dados do serviço para a rede.
 */
public class ResultadoJogada {

    private final String  nomeJogador;
    private final int     aposta;
    private final int     sorteio;
    private final boolean acerto;
    private final double     premioOuFundo;
    private final String  mensagem;

    public ResultadoJogada(String nomeJogador, int aposta, int sorteio,
                           boolean acerto, double premioOuFundo, String mensagem) {
        this.nomeJogador   = nomeJogador;
        this.aposta        = aposta;
        this.sorteio       = sorteio;
        this.acerto        = acerto;
        this.premioOuFundo = premioOuFundo;
        this.mensagem      = mensagem;
    }

    public String  getNomeJogador()   { return nomeJogador;   }
    public int     getAposta()        { return aposta;        }
    public int     getSorteio()       { return sorteio;       }
    public boolean isAcerto()         { return acerto;        }
    public double     getPremioOuFundo() { return premioOuFundo; }
    public String  getMensagem()      { return mensagem;      }
}
