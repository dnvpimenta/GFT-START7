package exercicios.ingresso;
public class ingressoFamilia extends Ingresso {
    private final int numeroPessoas;

    public ingressoFamilia(double valorBase, String nomeFilme, boolean isDublado, int numeroPessoas) {
        super(valorBase, nomeFilme, isDublado);
        this.numeroPessoas = numeroPessoas;
    }

    public int getNumeroPessoas() {
        return numeroPessoas;
    }

    @Override
    public double getValorReal() {
        double valorTotal = valorBase * numeroPessoas;
        if (numeroPessoas > 3) {
            valorTotal *= 0.95; // Aplica 5% de desconto
        }
        return valorTotal;
    }

    @Override
    public String toString() {
        return super.toString() + " (Ingresso Família para " + numeroPessoas + " pessoas)";
    }
}