package Exercícios.Tributos;

// Classe para produtos de Alimentação (1% de tributo)
public class Alimentacao extends Produto {
    public Alimentacao(String nome, double valor) {
        super(nome, valor);
    }

    @Override
    public double calcularTributo() {
        return valor * 0.01;
    }
}