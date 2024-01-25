package SudokuProject;

import java.util.Arrays;
import java.util.Dictionary;
import java.util.Hashtable;

import javax.swing.SwingUtilities;

import DatabaseConnection.Database_SudokuProcess;

public class SudokuInitialGUI {

	private static final int BOARD_SIZE = 9;
	private final static String COLUMN_INITIAL_NUMBERS = "initial_numbers";
	private final static String COLUMN_SOLUTION_NUMBERS = "solution_numbers";
	private final static String COLUMN_CURRENT_NUMBERS = "current_numbers";
	private final static String COLUMN_DIFFICULTY = "difficulty";
	private final static String COLUMN_USERNAME = "username";
	
	private static int[][] initial_board = new int[BOARD_SIZE][BOARD_SIZE];
	private static int[][] current_board = new int[BOARD_SIZE][BOARD_SIZE];
	private static int[][] answer_board = new int[BOARD_SIZE][BOARD_SIZE];
	
	// Oyun load edilmek istendiğinde bu metod kullanılır
	public static boolean loadSudoku(String GAMEID, String username) {
		GAMEID = "60";
		
		Dictionary<String, String> dictNumbers= new Hashtable<>();
		dictNumbers = Database_SudokuProcess.loadSudokuTables(GAMEID);
		
		
		String initialNumbersString = dictNumbers.get(COLUMN_INITIAL_NUMBERS);
        String solutionNumbersString = dictNumbers.get(COLUMN_SOLUTION_NUMBERS);
        String currentNumbersString = dictNumbers.get(COLUMN_CURRENT_NUMBERS);
        String difficulty = dictNumbers.get(COLUMN_DIFFICULTY);
        
		initial_board = Database_SudokuProcess.parseNumbers(initialNumbersString);
		answer_board = Database_SudokuProcess.parseNumbers(solutionNumbersString);
		current_board = Database_SudokuProcess.parseNumbers(currentNumbersString);
		
		
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
            	System.out.println(Database_SudokuProcess.convertToString(initial_board));
            	System.out.println(Database_SudokuProcess.convertToString(answer_board));
            	System.out.println(Database_SudokuProcess.convertToString(current_board));
            	
         
                new SudokuTableGUI(initial_board,answer_board,current_board,difficulty);
            }
        });
        
		return true;
	}
	
	// Yeni oyun yaratılması istendiğinde bu method kullanılır
	public static boolean newSudoku(String username, Difficulty difficulty) {
		Dictionary<String, String> dictNumbers= new Hashtable<>();
		
		SudokuCreator generator = new SudokuCreator(difficulty);
		int[][] board = generator.create();
		
		
		int[][] initial_board = new int[BOARD_SIZE][BOARD_SIZE];
    	for (int row = 0; row < BOARD_SIZE; row++) {
            System.arraycopy(board[row], 0, initial_board[row], 0, BOARD_SIZE);
        }
    	
		int[][] current_board = new int[BOARD_SIZE][BOARD_SIZE];
    	for (int row = 0; row < BOARD_SIZE; row++) {
            System.arraycopy(board[row], 0, current_board[row], 0, BOARD_SIZE);
        }
		
    	SudokuSolver solver = new SudokuSolver(board);
        if (solver.solve()) {
            solver.getBoard();
            
    		int[][] answer_board = new int[BOARD_SIZE][BOARD_SIZE];
        	for (int row = 0; row < BOARD_SIZE; row++) {
                System.arraycopy(board[row], 0, answer_board[row], 0, BOARD_SIZE);
            }
            
            String initialNumbersString = Database_SudokuProcess.convertToString(initial_board);
            String solutionNumbersString = Database_SudokuProcess.convertToString(board);
            String currentNumbersString = Database_SudokuProcess.convertToString(current_board);
            String difficultyString = difficulty.toString();
    		
            dictNumbers.put(COLUMN_USERNAME, username);
            dictNumbers.put(COLUMN_INITIAL_NUMBERS, initialNumbersString);
            dictNumbers.put(COLUMN_SOLUTION_NUMBERS, solutionNumbersString);
            dictNumbers.put(COLUMN_CURRENT_NUMBERS, currentNumbersString);
            dictNumbers.put(COLUMN_DIFFICULTY, difficultyString);
    		
    		Database_SudokuProcess.insertSudokuTables(dictNumbers);
            
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    new SudokuTableGUI(initial_board,board,current_board,difficulty.toString());
                }
            });
        } else {
            System.out.println("The board cannot be solved.");
        }
        return true;
	}
}
