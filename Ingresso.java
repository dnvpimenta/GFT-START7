package Exercícios.Ingresso;
public class Ingresso {
    protected double valorBase;
    protected String nomeFilme;
    protected boolean isDublado;

    public Ingresso(double valorBase, String nomeFilme, boolean isDublado) {
        this.valorBase = valorBase;
        this.nomeFilme = nomeFilme;
        this.isDublado = isDublado;
    }

    public double getValorReal() {
        return valorBase;
    }

    public String getNomeFilme() {
        return nomeFilme;
    }

    public boolean isDublado() {
        return isDublado;
    }

    @Override
    public String toString() {
        return "Ingresso para '" + nomeFilme + "' - " + 
               (isDublado ? "Dublado" : "Legendado") + 
               " - Valor: R$" + String.format("%.2f", getValorReal());
    }
}