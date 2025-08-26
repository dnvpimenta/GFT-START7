package exercicios.tributos;

// Classe para produtos de Vestuário (2.5% de tributo)
public class Vestuario extends Produto {
    public Vestuario(String nome, double valor) {
        super(nome, valor);
    }

    @Override
    public double calcularTributo() {
        return valor * 0.025;
    }
}