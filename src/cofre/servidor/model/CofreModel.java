package cofre.servidor.model;

import cofre.compartilhado.Constantes;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Estado do cofre: fundo acumulado, clientes atendidos e acertos.
 * O fundo deve ser acessado dentro de bloco synchronized externo.
 */
public class CofreModel {

    private int fundo = 0;
    private final AtomicInteger totalClientes = new AtomicInteger(0);
    private final AtomicInteger totalAcertos  = new AtomicInteger(0);

    public int incrementarFundo() {
        fundo += Constantes.INCREMENTO_FUNDO;
        return fundo;
    }

    public double pagarPremioEZerar() {
        double premio =  fundo * Constantes.PERCENTUAL_PREMIO;
        fundo = 0;
        totalAcertos.incrementAndGet();
        return premio;
    }

    public int getFundo()            { return fundo;                       }
    public int getTotalClientes()    { return totalClientes.get();         }
    public int getTotalAcertos()     { return totalAcertos.get();          }
    public int incrementarClientes() { return totalClientes.incrementAndGet(); }
}
