package exercicios.ingresso;
public class cinema {
    public static void main(String[] args) {
        // Criando ingressos de diferentes tipos
        Ingresso ingressoNormal = new Ingresso(30.0, "Interestelar", false);
        Ingresso meiaEntrada = new meiaEntrada(30.0, "Interestelar", false);
        ingressoFamilia familiaPequena = new ingressoFamilia(30.0, "Toy Story", true, 2);
        ingressoFamilia familiaGrande = new ingressoFamilia(30.0, "Toy Story", true, 5);

        // Exibindo informações dos ingressos
        System.out.println(ingressoNormal);
        System.out.println(meiaEntrada);
        System.out.println(familiaPequena);
        System.out.println(familiaGrande);

        // Verificando os valores reais
        System.out.println("\nValores reais:");
        System.out.println("Ingresso normal: R$" + ingressoNormal.getValorReal());
        System.out.println("Meia entrada: R$" + meiaEntrada.getValorReal());
        System.out.println("Família pequena (2 pessoas): R$" + familiaPequena.getValorReal());
        System.out.println("Família grande (5 pessoas): R$" + familiaGrande.getValorReal());
    }
}