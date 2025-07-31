package Exercícios.Tributos;

// Classe abstrata base para todos os produtos
public abstract class Produto {
    protected String nome;
    protected double valor;

    public Produto(String nome, double valor) {
        this.nome = nome;
        this.valor = valor;
    }

    // Método abstrato que cada classe concreta deve implementar
    public abstract double calcularTributo();

    public String getNome() {
        return nome;
    }

    public double getValor() {
        return valor;
    }

    @Override
    public String toString() {
        return String.format("%s - Valor: R$%.2f - Tributo: R$%.2f (%.2f%%)", 
               nome, valor, calcularTributo(), (calcularTributo() / valor * 100));
    }
}
