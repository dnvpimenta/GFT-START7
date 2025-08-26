package aulas;
import java.time.Year;
import java.util.Scanner; // Importa a classe Year para obter o ano atual

public class calculadoraIdade {
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print("Digite o seu nome: ");
            String nome = scanner.nextLine();
            
            System.out.print("Digite o seu ano de nascimento: ");
            int anoNascimento = scanner.nextInt();
            
            // Obtém o ano atual do sistema automaticamente
            int anoAtual = Year.now().getValue();
            int idade = anoAtual - anoNascimento;
            
            System.out.println("Olá " + nome + ", você tem " + idade + " anos de idade!");
        }
    }
}