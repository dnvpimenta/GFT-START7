package Exercícios.Tributos;

// Classe principal para testar o sistema de tributos
public class CalculadoraTributos {
    public static void main(String[] args) {
        // Criando produtos de cada tipo
        Produto[] produtos = {
            new Alimentacao("Arroz 5kg", 25.90),
            new SaudeBemEstar("Vitamina C", 19.50),
            new Vestuario("Camiseta", 49.90),
            new Cultura("Livro", 35.00),
            new Alimentacao("Feijão 1kg", 8.75),
            new SaudeBemEstar("Protetor Solar", 32.40)
        };

        // Calculando e exibindo os tributos
        System.out.println("Cálculo de Tributos por Produto:");
        System.out.println("--------------------------------");
        
        double totalTributos = 0;
        double totalValorProdutos = 0;

        for (Produto produto : produtos) {
            System.out.println(produto);
            totalTributos += produto.calcularTributo();
            totalValorProdutos += produto.getValor();
        }

        System.out.println("\nResumo:");
        System.out.printf("Total em produtos: R$%.2f%n", totalValorProdutos);
        System.out.printf("Total em tributos: R$%.2f%n", totalTributos);
        System.out.printf("Percentual médio de tributos: %.2f%%%n", 
                         (totalTributos / totalValorProdutos * 100));
    }
}