package SudokuProject;

public class SudokuSolver {
	
	private static final int board_SIZE = 9;
	private static final int BOX_SIZE = 3;
	
    private int[][] board;
    
    public int[][] getBoard(){
    	return this.board;
    }

    public SudokuSolver(int[][] board) {
        this.board = board;
    }

    public boolean solve() {
        int row = -1;
        int col = -1;
        boolean isEmpty = true;

        // boş hücreleri bul
        for (int i = 0; i < board_SIZE; i++) {
            for (int j = 0; j < board_SIZE; j++) {
                if (board[i][j] == 0) {
                    row = i;
                    col = j;
                    isEmpty = false;
                    break;
                }
            }
            if (!isEmpty) {
                break;
            }
        }

        // boş hücre yoksa tamamlandı
        if (isEmpty) {
            return true;
        }

        // boş hücreye geçerli sayıları yerleştir
        for (int num = 1; num <= board_SIZE; num++) {
            if (isValid(row, col, num)) {
                board[row][col] = num;
                if (solve()) {
                    return true;
                } else {
                    board[row][col] = 0;
                }
            }
        }
        return false;
    }

    // geçerli sayı kontrolü
    private boolean isValid(int row, int col, int num) {
        // satırdaki sayıların kontrolü
        for (int i = 0; i < board_SIZE; i++) {
            if (board[row][i] == num) {
                return false;
            }
        }
        
        // sütundaki sayıların kontrolü
        for (int i = 0; i < board_SIZE; i++) {
            if (board[i][col] == num) {
                return false;
            }
        }

        // bölgedeki sayıların kontrolü
        int rowStart = row - row % BOX_SIZE;
        int colStart = col - col % BOX_SIZE;
        for (int i = rowStart; i < rowStart + BOX_SIZE; i++) {
            for (int j = colStart; j < colStart + BOX_SIZE; j++) {
                if (board[i][j] == num) {
                    return false;
                }
            }
        }

        return true;
    }
}

