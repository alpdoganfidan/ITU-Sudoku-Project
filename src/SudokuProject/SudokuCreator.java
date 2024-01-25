package SudokuProject;

import java.util.Random;

public class SudokuCreator {
    
    private static final int board_SIZE = 9;
    private static final int BOX_SIZE = 3;
    private Difficulty difficulty;
    
    private int[][] board;
    
    public SudokuCreator(Difficulty difficulty) {
        this.board = new int[board_SIZE][board_SIZE];
        this.difficulty = difficulty;
    }
    
    // İstenen zorluk seviyesine göre sudoku tablosu oluşturulur.
    public int[][] create() {
        fillDiagonal();
        fillRemaining(0, BOX_SIZE);
        removeCells(difficulty.getMinCellsToRemove(),
        		difficulty.getMaxCellsToRemove());
        return board;
    }
    
    //  Diyagonal kutular doldurulur. Bu kutuları doldurulması çözümün benzersiz olmasını sağlayarak, Sudoku oyununun mantığına uygun bir şekilde çözülmesini garanti eder.
    private void fillDiagonal() {
        for (int i = 0; i < board_SIZE; i += BOX_SIZE) {
            fillBox(i, i);
        }
    }
    
    // İç içe döngülerle, 3x3 kutunun her hücresini dolaşarak doğru ve benzersiz bir değer bulana kadar rasgele sayıları dener.
    private void fillBox(int row, int col) {
        int num;
        Random rand = new Random();
        
        for (int i = 0; i < BOX_SIZE; i++) {
            for (int j = 0; j < BOX_SIZE; j++) {
                do {
                    num = rand.nextInt(board_SIZE) + 1;
                } while (!isValid(row, col, num));
                board[row + i][col + j] = num;
            }
        }
    }
    
    // Belirli bir hücreye yerleştirilecek sayının geçerli olup olmadığını satır, sütun ve 3x3 kutuyu kontrol ederek sağlar.
    private boolean isValid(int row, int col, int num) {
    	// Satırı ve sütunu kontrol et
        for (int i = 0; i < board_SIZE; i++) {
            if (board[row][i] == num || board[i][col] == num) {
                return false;
            }
        }
        // 3x3 bölgeyi kontrol et
        int boxRow = row - row % BOX_SIZE;
        int boxCol = col - col % BOX_SIZE;
        for (int i = boxRow; i < boxRow + BOX_SIZE; i++) {
            for (int j = boxCol; j < boxCol + BOX_SIZE; j++) {
                if (board[i][j] == num) {
                    return false;
                }
            }
        }
        return true;
    }
    
    // (backtracking) algoritması kullanılır. Recursion yapıya sahiptir.
    private boolean fillRemaining(int row, int col) {
        if (col >= board_SIZE && row < board_SIZE - 1) {
            row++;
            col = 0;
        }
        if (row >= board_SIZE && col >= board_SIZE) {
            return true;
        }
        if (row < BOX_SIZE) {
            if (col < BOX_SIZE) {
                col = BOX_SIZE;
            }
        } else if (row < board_SIZE - BOX_SIZE) {
            if (col == (int)(row / BOX_SIZE) * BOX_SIZE) {
                col += BOX_SIZE;
            }
        } else {
            if (col == board_SIZE - BOX_SIZE) {
                row++;
                col = 0;
                if (row >= board_SIZE) {
                    return true;
                }
            }
        }
        
        for (int num = 1; num <= board_SIZE; num++) {
            if (isValid(row, col, num)) {
                board[row][col] = num;
                if (fillRemaining(row, col + 1)) {
                    return true;
                }
                board[row][col] = 0;
            }
        }
        return false;
    }
    
    // Count, zorluk seviyesine bağlı max ve min değer arasında bir değer alır. Count sayısı 0 olana kadar random sayılar silinir.
    private void removeCells(int minCount, int maxCount) {
        Random rand = new Random();
        int count = minCount + rand.nextInt(maxCount - minCount + 1);
        while (count > 0) {
            int row = rand.nextInt(board_SIZE);
            int col = rand.nextInt(board_SIZE);
            if (board[row][col] != 0) {
                count--;
                board[row][col] = 0;
            }
        }
    }
    // oluşturulan tabloyu print eder
    public static void printBoard(int[][] board) {
        for (int i = 0; i < board_SIZE; i++) {
            if (i % 3 == 0 && i != 0) {
                System.out.println("---------------------");
            }
            for (int j = 0; j < board_SIZE; j++) {
                if (j % 3 == 0 && j != 0) {
                    System.out.print("| ");
                }
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
    }
    
    
}
