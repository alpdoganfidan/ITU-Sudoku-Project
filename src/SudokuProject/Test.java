package SudokuProject;

public class Test {

	private static final int BOARD_SIZE = 9;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
        /*int[][] board = {
		{1,0,0,8,4,0,0,0,0},
		{0,5,0,2,0,0,4,0,1},
		{8,0,0,0,0,0,7,0,0},
		{5,0,0,0,7,0,0,6,0},
		{0,0,1,0,2,0,0,9,0},
		{6,0,0,5,8,0,2,0,0},
		{0,0,7,0,0,0,0,0,4},
		{4,3,0,0,0,5,0,0,0},
		{0,0,0,0,0,0,0,0,9}
		};*/
		
		SudokuCreator generator = new SudokuCreator(Difficulty.INSANE);
		int[][] board = generator.create();
		

		
		SudokuCreator.printBoard(board);
    	System.out.println("/////////////////////////");
		
    	SudokuSolver solver = new SudokuSolver(board);
        if (solver.solve()) {
        	solver.getBoard();
        	SudokuCreator.printBoard(board);
        }
        
        
        //newSudoku("", Difficulty.EASY);
	}

}
