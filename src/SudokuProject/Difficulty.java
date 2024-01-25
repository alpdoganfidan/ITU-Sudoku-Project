package SudokuProject;

public enum Difficulty {

    EASY(36,41),
    MEDIUM(42,45),
    HARD(46,49),
    VERY_HARD(50,53),
    INSANE(54,57);
	
    private final int minCellsToRemove;
    private final int maxCellsToRemove;
    
    Difficulty(int minCellsToRemove, int maxCellsToRemove) {
        this.minCellsToRemove = minCellsToRemove;
        this.maxCellsToRemove = maxCellsToRemove;
    }
    
    public int getMinCellsToRemove() {
        return minCellsToRemove;
    }
    
    public int getMaxCellsToRemove() {
        return maxCellsToRemove;
    }
}

