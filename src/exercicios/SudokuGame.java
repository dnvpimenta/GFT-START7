package exercicios;

import java.util.*;
import java.util.stream.IntStream;

@SuppressWarnings("unused")
public class SudokuGame {
    private static final int SIZE = 9;
    private static final int SUBGRID_SIZE = 3;
    private static final int EMPTY = 0;
    
    private int[][] board;
    private boolean[][] fixedCells;
    private boolean gameStarted;
    
    public SudokuGame(String[] args) {
        board = new int[SIZE][SIZE];
        fixedCells = new boolean[SIZE][SIZE];
        gameStarted = false;
        initializeBoardFromArgs(args);
    }
    
    private void initializeBoardFromArgs(String[] args) {
        if (args.length > 0) {
            try {
                for (String arg : args) {
                    String[] parts = arg.split(",");
                    if (parts.length == 3) {
                        int row = Integer.parseInt(parts[0]);
                        int col = Integer.parseInt(parts[1]);
                        int value = Integer.parseInt(parts[2]);
                        
                        if (isValidPosition(row, col) && isValidValue(value)) {
                            board[row][col] = value;
                            fixedCells[row][col] = true;
                            gameStarted = true;
                        }
                    }
                }
            } catch (NumberFormatException e) {
                System.out.println("Erro nos argumentos. Use formato: row,col,value");
            }
        }
    }
    
    private boolean isValidPosition(int row, int col) {
        return row >= 0 && row < SIZE && col >= 0 && col < SIZE;
    }
    
    private boolean isValidValue(int value) {
        return value >= 1 && value <= 9;
    }
    
    public void startNewGame() {
        // Limpa o tabuleiro, mantendo apenas células fixas
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (!fixedCells[i][j]) {
                    board[i][j] = EMPTY;
                }
            }
        }
        gameStarted = true;
        System.out.println("Novo jogo iniciado!");
    }
    
    public void placeNumber(int number, int row, int col) {
        if (!gameStarted) {
            System.out.println("Jogo não iniciado. Use a opção 1 primeiro.");
            return;
        }
        
        if (!isValidPosition(row, col)) {
            System.out.println("Posição inválida! Use valores entre 0 e 8.");
            return;
        }
        
        if (!isValidValue(number)) {
            System.out.println("Número inválido! Use valores entre 1 e 9.");
            return;
        }
        
        if (fixedCells[row][col]) {
            System.out.println("Não é possível modificar uma célula fixa!");
            return;
        }
        
        if (board[row][col] != EMPTY) {
            System.out.println("Posição já preenchida! Use a opção de remover primeiro.");
            return;
        }
        
        board[row][col] = number;
        System.out.println("Número " + number + " colocado na posição [" + row + "," + col + "]");
    }
    
    public void removeNumber(int row, int col) {
        if (!gameStarted) {
            System.out.println("Jogo não iniciado.");
            return;
        }
        
        if (!isValidPosition(row, col)) {
            System.out.println("Posição inválida!");
            return;
        }
        
        if (fixedCells[row][col]) {
            System.out.println("Não é possível remover uma célula fixa!");
            return;
        }
        
        if (board[row][col] == EMPTY) {
            System.out.println("Posição já está vazia!");
            return;
        }
        
        board[row][col] = EMPTY;
        System.out.println("Número removido da posição [" + row + "," + col + "]");
    }
    
    public void displayBoard() {
        if (!gameStarted) {
            System.out.println("Jogo não iniciado.");
            return;
        }
        
        System.out.println("\n=== TABULEIRO SUDOKU ===");
        System.out.println("   0 1 2   3 4 5   6 7 8");
        System.out.println("  +-------+-------+-------+");
        
        for (int i = 0; i < SIZE; i++) {
            System.out.print(i + " | ");
            for (int j = 0; j < SIZE; j++) {
                if (board[i][j] == EMPTY) {
                    System.out.print(". ");
                } else {
                    System.out.print(board[i][j] + " ");
                }
                
                if ((j + 1) % SUBGRID_SIZE == 0 && j < SIZE - 1) {
                    System.out.print("| ");
                }
            }
            System.out.println("|");
            
            if ((i + 1) % SUBGRID_SIZE == 0 && i < SIZE - 1) {
                System.out.println("  +-------+-------+-------+");
            }
        }
        System.out.println("  +-------+-------+-------+");
        
        // Legenda
        System.out.println("Legenda: . = vazio, * = número fixo");
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (fixedCells[i][j]) {
                    System.out.println("Posição [" + i + "," + j + "] = " + board[i][j] + " (fixo)");
                }
            }
        }
    }
    
    public void checkGameStatus() {
        if (!gameStarted) {
            System.out.println("Status: NÃO INICIADO (sem erros)");
            return;
        }
        
        boolean hasErrors = hasConflicts();
        boolean isComplete = isBoardComplete();
        
        System.out.println("\n=== STATUS DO JOGO ===");
        
        if (isComplete) {
            if (hasErrors) {
                System.out.println("Status: COMPLETO (com erros)");
            } else {
                System.out.println("Status: COMPLETO (sem erros) - PARABÉNS!");
            }
        } else {
            if (hasErrors) {
                System.out.println("Status: INCOMPLETO (com erros)");
            } else {
                System.out.println("Status: INCOMPLETO (sem erros)");
            }
        }
        
        // Mostrar estatísticas
        int emptyCells = countEmptyCells();
        int fixedCellsCount = countFixedCells();
        int userCells = SIZE * SIZE - emptyCells - fixedCellsCount;
        
        System.out.println("Células vazias: " + emptyCells);
        System.out.println("Células fixas: " + fixedCellsCount);
        System.out.println("Células do usuário: " + userCells);
    }
    
    private boolean hasConflicts() {
        // Verifica linhas
        for (int i = 0; i < SIZE; i++) {
            if (hasDuplicatesInRow(i)) return true;
        }
        
        // Verifica colunas
        for (int j = 0; j < SIZE; j++) {
            if (hasDuplicatesInColumn(j)) return true;
        }
        
        // Verifica subgrades 3x3
        for (int i = 0; i < SIZE; i += SUBGRID_SIZE) {
            for (int j = 0; j < SIZE; j += SUBGRID_SIZE) {
                if (hasDuplicatesInSubgrid(i, j)) return true;
            }
        }
        
        return false;
    }
    
    private boolean hasDuplicatesInRow(int row) {
        Set<Integer> seen = new HashSet<>();
        for (int j = 0; j < SIZE; j++) {
            int value = board[row][j];
            if (value != EMPTY) {
                if (seen.contains(value)) return true;
                seen.add(value);
            }
        }
        return false;
    }
    
    private boolean hasDuplicatesInColumn(int col) {
        Set<Integer> seen = new HashSet<>();
        for (int i = 0; i < SIZE; i++) {
            int value = board[i][col];
            if (value != EMPTY) {
                if (seen.contains(value)) return true;
                seen.add(value);
            }
        }
        return false;
    }
    
    private boolean hasDuplicatesInSubgrid(int startRow, int startCol) {
        Set<Integer> seen = new HashSet<>();
        for (int i = startRow; i < startRow + SUBGRID_SIZE; i++) {
            for (int j = startCol; j < startCol + SUBGRID_SIZE; j++) {
                int value = board[i][j];
                if (value != EMPTY) {
                    if (seen.contains(value)) return true;
                    seen.add(value);
                }
            }
        }
        return false;
    }
    
    private boolean isBoardComplete() {
        return countEmptyCells() == 0;
    }
    
    private int countEmptyCells() {
        int count = 0;
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (board[i][j] == EMPTY) {
                    count++;
                }
            }
        }
        return count;
    }
    
    private int countFixedCells() {
        int count = 0;
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (fixedCells[i][j]) {
                    count++;
                }
            }
        }
        return count;
    }
    
    public void clearUserNumbers() {
        if (!gameStarted) {
            System.out.println("Jogo não iniciado.");
            return;
        }
        
        int removed = 0;
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (!fixedCells[i][j] && board[i][j] != EMPTY) {
                    board[i][j] = EMPTY;
                    removed++;
                }
            }
        }
        System.out.println(removed + " números do usuário removidos. Células fixas mantidas.");
    }
    
    public void finishGame() {
        if (!gameStarted) {
            System.out.println("Jogo não iniciado.");
            return;
        }
        
        if (!isBoardComplete()) {
            System.out.println("Jogo não pode ser finalizado! Ainda há células vazias.");
            return;
        }
        
        if (hasConflicts()) {
            System.out.println("Jogo finalizado com ERROS! Existem números em posições conflitantes.");
        } else {
            System.out.println("PARABÉNS! Jogo finalizado com SUCESSO! Todas as posições estão corretas.");
        }
        
        displayBoard();
    }
    
    public static void main(String[] args) {
        SudokuGame game = new SudokuGame(args);
        Scanner scanner = new Scanner(System.in);
        
        while (true) {
            System.out.println("\n=== MENU SUDOKU ===");
            System.out.println("1. Iniciar novo jogo");
            System.out.println("2. Colocar novo número");
            System.out.println("3. Remover número");
            System.out.println("4. Verificar jogo (exibir tabuleiro)");
            System.out.println("5. Verificar status do jogo");
            System.out.println("6. Limpar (remover números do usuário)");
            System.out.println("7. Finalizar jogo");
            System.out.println("8. Sair");
            System.out.print("Escolha uma opção: ");
            
            try {
                int choice = scanner.nextInt();
                
                switch (choice) {
                    case 1:
                        game.startNewGame();
                        game.displayBoard();
                        break;
                        
                    case 2:
                        System.out.print("Digite o número (1-9): ");
                        int number = scanner.nextInt();
                        System.out.print("Digite a linha (0-8): ");
                        int row = scanner.nextInt();
                        System.out.print("Digite a coluna (0-8): ");
                        int col = scanner.nextInt();
                        game.placeNumber(number, row, col);
                        break;
                        
                    case 3:
                        System.out.print("Digite a linha (0-8): ");
                        row = scanner.nextInt();
                        System.out.print("Digite a coluna (0-8): ");
                        col = scanner.nextInt();
                        game.removeNumber(row, col);
                        break;
                        
                    case 4:
                        game.displayBoard();
                        break;
                        
                    case 5:
                        game.checkGameStatus();
                        break;
                        
                    case 6:
                        game.clearUserNumbers();
                        break;
                        
                    case 7:
                        game.finishGame();
                        break;
                        
                    case 8:
                        System.out.println("Obrigado por jogar!");
                        scanner.close();
                        return;
                        
                    default:
                        System.out.println("Opção inválida!");
                }
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida! Digite um número.");
                scanner.nextLine(); // Limpa o buffer
            }
        }
    }
}