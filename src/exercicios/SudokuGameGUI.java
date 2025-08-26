package exercicios;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import java.util.stream.IntStream;

@SuppressWarnings("unused")
public class SudokuGameGUI {
    private static final int SIZE = 9;
    private static final int SUBGRID_SIZE = 3;
    private static final int EMPTY = 0;
    
    private int[][] board;
    private boolean[][] fixedCells;
    private Set<Integer>[][] draftCells;
    private boolean gameStarted;
    
    private JFrame frame;
    private JButton[][] cellButtons;
    private JTextArea statusArea;
    
    @SuppressWarnings("unchecked")
    public SudokuGameGUI(String[] args) {
        board = new int[SIZE][SIZE];
        fixedCells = new boolean[SIZE][SIZE];
        draftCells = new HashSet[SIZE][SIZE];
        gameStarted = false;
        
        initializeDraftCells();
        initializeBoardFromArgs(args);
        createAndShowGUI();
    }
    
    private void initializeDraftCells() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                draftCells[i][j] = new HashSet<>();
            }
        }
    }
    
    private void initializeBoardFromArgs(String[] args) {
        if (args.length > 0) {
            Arrays.stream(args)
                .forEach(arg -> {
                    String[] parts = arg.split(",");
                    if (parts.length == 3) {
                        try {
                            int row = Integer.parseInt(parts[0]);
                            int col = Integer.parseInt(parts[1]);
                            int value = Integer.parseInt(parts[2]);
                            
                            if (isValidPosition(row, col) && isValidValue(value)) {
                                board[row][col] = value;
                                fixedCells[row][col] = true;
                                gameStarted = true;
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Argumento inválido: " + arg);
                        }
                    }
                });
        }
    }
    
    private boolean isValidPosition(int row, int col) {
        return row >= 0 && row < SIZE && col >= 0 && col < SIZE;
    }
    
    private boolean isValidValue(int value) {
        return value >= 1 && value <= 9;
    }
    
    private void createAndShowGUI() {
        frame = new JFrame("Sudoku Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        
        // Painel do tabuleiro
        JPanel boardPanel = new JPanel(new GridLayout(SIZE, SIZE));
        boardPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        cellButtons = new JButton[SIZE][SIZE];
        
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                cellButtons[i][j] = createCellButton(i, j);
                boardPanel.add(cellButtons[i][j]);
            }
        }
        
        // Painel de controle
        JPanel controlPanel = new JPanel(new GridLayout(2, 1));
        
        // Painel de botões
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(createButton("Novo Jogo", e -> startNewGame()));
        buttonPanel.add(createButton("Verificar", e -> checkGameStatus()));
        buttonPanel.add(createButton("Limpar", e -> clearUserNumbers()));
        buttonPanel.add(createButton("Finalizar", e -> finishGame()));
        buttonPanel.add(createButton("Rascunho", e -> showDraftDialog()));
        
        // Área de status
        statusArea = new JTextArea(3, 40);
        statusArea.setEditable(false);
        JScrollPane statusScroll = new JScrollPane(statusArea);
        
        controlPanel.add(buttonPanel);
        controlPanel.add(statusScroll);
        
        frame.add(boardPanel, BorderLayout.CENTER);
        frame.add(controlPanel, BorderLayout.SOUTH);
        
        frame.pack();
        frame.setSize(600, 700);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        
        updateBoardDisplay();
    }
    
    private JButton createCellButton(int row, int col) {
        JButton button = new JButton();
        button.setPreferredSize(new Dimension(50, 50));
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setMargin(new Insets(0, 0, 0, 0));
        
        button.addActionListener(e -> handleCellClick(row, col));
        
        return button;
    }
    
    private JButton createButton(String text, ActionListener action) {
        JButton button = new JButton(text);
        button.addActionListener(action);
        return button;
    }
    
    private void handleCellClick(int row, int col) {
        if (!gameStarted) {
            showMessage("Inicie um novo jogo primeiro!");
            return;
        }
        
        if (fixedCells[row][col]) {
            showMessage("Esta célula é fixa e não pode ser modificada!");
            return;
        }
        
        String[] options = {"Inserir Número", "Inserir Rascunho", "Remover Número", "Remover Rascunho", "Cancelar"};
        int choice = JOptionPane.showOptionDialog(frame, "Escolha uma ação:", "Ação na Célula",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
        
        switch (choice) {
            case 0:
                insertNumber(row, col);
                break;
            case 1:
                insertDraftNumber(row, col);
                break;
            case 2:
                removeNumber(row, col);
                break;
            case 3:
                removeDraftNumber(row, col);
                break;
        }
    }
    
    private void insertNumber(int row, int col) {
        String input = JOptionPane.showInputDialog(frame, "Digite o número (1-9):");
        if (input != null) {
            try {
                int number = Integer.parseInt(input);
                placeNumber(number, row, col);
            } catch (NumberFormatException e) {
                showMessage("Número inválido!");
            }
        }
    }
    
    private void insertDraftNumber(int row, int col) {
        String input = JOptionPane.showInputDialog(frame, "Digite números de rascunho (1-9, separados por vírgula):");
        if (input != null) {
            try {
                String[] numbers = input.split(",");
                for (String numStr : numbers) {
                    int num = Integer.parseInt(numStr.trim());
                    if (isValidValue(num)) {
                        draftCells[row][col].add(num);
                    }
                }
                updateCellDisplay(row, col);
                showMessage("Números de rascunho adicionados!");
            } catch (NumberFormatException e) {
                showMessage("Números inválidos!");
            }
        }
    }
    
    private void removeDraftNumber(int row, int col) {
        if (draftCells[row][col].isEmpty()) {
            showMessage("Não há números de rascunho para remover!");
            return;
        }
        
        String input = JOptionPane.showInputDialog(frame, "Digite números para remover (1-9, separados por vírgula):");
        if (input != null) {
            try {
                String[] numbers = input.split(",");
                for (String numStr : numbers) {
                    int num = Integer.parseInt(numStr.trim());
                    if (isValidValue(num)) {
                        draftCells[row][col].remove(num);
                    }
                }
                updateCellDisplay(row, col);
                showMessage("Números de rascunho removidos!");
            } catch (NumberFormatException e) {
                showMessage("Números inválidos!");
            }
        }
    }
    
    private void showDraftDialog() {
        JTextArea draftInfo = new JTextArea();
        draftInfo.setEditable(false);
        
        StringBuilder sb = new StringBuilder("=== NÚMEROS DE RASCUNHO ===\n");
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (!draftCells[i][j].isEmpty()) {
                    sb.append(String.format("[%d,%d]: %s\n", i, j, draftCells[i][j]));
                }
            }
        }
        
        draftInfo.setText(sb.toString());
        JOptionPane.showMessageDialog(frame, new JScrollPane(draftInfo), "Rascunhos", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public void startNewGame() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (!fixedCells[i][j]) {
                    board[i][j] = EMPTY;
                    draftCells[i][j].clear();
                }
            }
        }
        gameStarted = true;
        updateBoardDisplay();
        showMessage("Novo jogo iniciado!");
    }
    
    public void placeNumber(int number, int row, int col) {
        if (!isValidPosition(row, col) || !isValidValue(number)) {
            showMessage("Posição ou número inválido!");
            return;
        }
        
        if (board[row][col] != EMPTY) {
            showMessage("Posição já preenchida!");
            return;
        }
        
        board[row][col] = number;
        draftCells[row][col].clear();
        updateCellDisplay(row, col);
        showMessage("Número " + number + " colocado na posição [" + row + "," + col + "]");
    }
    
    public void removeNumber(int row, int col) {
        if (board[row][col] == EMPTY) {
            showMessage("Posição já está vazia!");
            return;
        }
        
        board[row][col] = EMPTY;
        updateCellDisplay(row, col);
        showMessage("Número removido da posição [" + row + "," + col + "]");
    }
    
    private void updateBoardDisplay() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                updateCellDisplay(i, j);
            }
        }
    }
    
    private void updateCellDisplay(int row, int col) {
        JButton button = cellButtons[row][col];
        int value = board[row][col];
        
        if (value != EMPTY) {
            button.setText(String.valueOf(value));
            button.setFont(new Font("Arial", Font.BOLD, 16));
            button.setForeground(fixedCells[row][col] ? Color.BLUE : Color.BLACK);
            button.setBackground(Color.WHITE);
        } else if (!draftCells[row][col].isEmpty()) {
            button.setText(formatDraftNumbers(draftCells[row][col]));
            button.setFont(new Font("Arial", Font.PLAIN, 10));
            button.setForeground(Color.GRAY);
            button.setBackground(new Color(240, 240, 240));
        } else {
            button.setText("");
            button.setBackground(Color.WHITE);
        }
        
        if (hasConflicts() && value != EMPTY && hasConflictAt(row, col)) {
            button.setBackground(new Color(255, 200, 200));
        }
    }
    
    private String formatDraftNumbers(Set<Integer> drafts) {
        StringBuilder sb = new StringBuilder();
        sb.append("<html><center>");
        for (int i = 1; i <= 9; i++) {
            if (drafts.contains(i)) {
                sb.append(i);
            } else {
                sb.append("·");
            }
            if (i % 3 == 0) {
                sb.append("<br>");
            } else {
                sb.append(" ");
            }
        }
        sb.append("</center></html>");
        return sb.toString();
    }
    
    public void checkGameStatus() {
        if (!gameStarted) {
            showMessage("Jogo não iniciado.");
            return;
        }
        
        boolean hasErrors = hasConflicts();
        boolean isComplete = isBoardComplete();
        
        StringBuilder status = new StringBuilder("=== STATUS DO JOGO ===\n");
        
        if (isComplete) {
            status.append(hasErrors ? "Status: COMPLETO (com erros)\n" : "Status: COMPLETO (sem erros) - PARABÉNS!\n");
        } else {
            status.append(hasErrors ? "Status: INCOMPLETO (com erros)\n" : "Status: INCOMPLETO (sem erros)\n");
        }
        
        status.append("Células vazias: ").append(countEmptyCells()).append("\n");
        status.append("Células fixas: ").append(countFixedCells()).append("\n");
        status.append("Células do usuário: ").append(countUserCells()).append("\n");
        status.append("Células com rascunho: ").append(countDraftCells()).append("\n");
        
        statusArea.setText(status.toString());
        updateBoardDisplay();
    }
    
    private boolean hasConflicts() {
        for (int i = 0; i < SIZE; i++) {
            if (hasDuplicatesInRow(i)) return true;
        }
        
        for (int j = 0; j < SIZE; j++) {
            if (hasDuplicatesInColumn(j)) return true;
        }
        
        for (int i = 0; i < SIZE; i += SUBGRID_SIZE) {
            for (int j = 0; j < SIZE; j += SUBGRID_SIZE) {
                if (hasDuplicatesInSubgrid(i, j)) return true;
            }
        }
        
        return false;
    }
    
    private boolean hasConflictAt(int row, int col) {
        return hasDuplicateInRow(row, col) || hasDuplicateInColumn(row, col) || hasDuplicateInSubgrid(row, col);
    }
    
    private boolean hasDuplicateInRow(int row, int col) {
        int value = board[row][col];
        for (int j = 0; j < SIZE; j++) {
            if (j != col && board[row][j] == value) {
                return true;
            }
        }
        return false;
    }
    
    private boolean hasDuplicateInColumn(int row, int col) {
        int value = board[row][col];
        for (int i = 0; i < SIZE; i++) {
            if (i != row && board[i][col] == value) {
                return true;
            }
        }
        return false;
    }
    
    private boolean hasDuplicateInSubgrid(int row, int col) {
        int value = board[row][col];
        int startRow = (row / SUBGRID_SIZE) * SUBGRID_SIZE;
        int startCol = (col / SUBGRID_SIZE) * SUBGRID_SIZE;
        
        for (int i = startRow; i < startRow + SUBGRID_SIZE; i++) {
            for (int j = startCol; j < startCol + SUBGRID_SIZE; j++) {
                if (!(i == row && j == col) && board[i][j] == value) {
                    return true;
                }
            }
        }
        return false;
    }
    
    private boolean hasDuplicatesInRow(int row) {
        Set<Integer> seen = new HashSet<>();
        for (int j = 0; j < SIZE; j++) {
            int value = board[row][j];
            if (value != EMPTY) {
                if (!seen.add(value)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    private boolean hasDuplicatesInColumn(int col) {
        Set<Integer> seen = new HashSet<>();
        for (int i = 0; i < SIZE; i++) {
            int value = board[i][col];
            if (value != EMPTY) {
                if (!seen.add(value)) {
                    return true;
                }
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
                    if (!seen.add(value)) {
                        return true;
                    }
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
        for (int[] row : board) {
            for (int value : row) {
                if (value == EMPTY) {
                    count++;
                }
            }
        }
        return count;
    }
    
    private int countFixedCells() {
        int count = 0;
        for (boolean[] row : fixedCells) {
            for (boolean fixed : row) {
                if (fixed) {
                    count++;
                }
            }
        }
        return count;
    }
    
    private int countUserCells() {
        return SIZE * SIZE - countEmptyCells() - countFixedCells();
    }
    
    private int countDraftCells() {
        int count = 0;
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (!draftCells[i][j].isEmpty()) {
                    count++;
                }
            }
        }
        return count;
    }
    
    public void clearUserNumbers() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (!fixedCells[i][j]) {
                    board[i][j] = EMPTY;
                    draftCells[i][j].clear();
                }
            }
        }
        updateBoardDisplay();
        showMessage("Todos os números do usuário e rascunhos removidos!");
    }
    
    public void finishGame() {
        if (!isBoardComplete()) {
            showMessage("Jogo não pode ser finalizado! Ainda há células vazias.");
            return;
        }
        
        if (hasConflicts()) {
            showMessage("Jogo finalizado com ERROS! Existem números em posições conflitantes.");
        } else {
            showMessage("PARABÉNS! Jogo finalizado com SUCESSO! Todas as posições estão corretas.");
        }
        
        checkGameStatus();
    }
    
    private void showMessage(String message) {
        statusArea.setText(message);
        JOptionPane.showMessageDialog(frame, message);
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SudokuGameGUI(args));
    }
}