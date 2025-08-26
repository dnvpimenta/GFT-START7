package Exercícios.Tributos;

// Classe para produtos de Cultura (4% de tributo)
public class Cultura extends Produto {
    public Cultura(String nome, double valor) {
        super(nome, valor);
    }

    @Override
    public double calcularTributo() {
        return valor * 0.04;
    }
}