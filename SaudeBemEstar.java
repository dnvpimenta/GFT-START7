package Exercícios.Tributos;

// Classe para produtos de Saúde e Bem Estar (1.5% de tributo)
public class SaudeBemEstar extends Produto {
    public SaudeBemEstar(String nome, double valor) {
        super(nome, valor);
    }

    @Override
    public double calcularTributo() {
        return valor * 0.015;
    }
}