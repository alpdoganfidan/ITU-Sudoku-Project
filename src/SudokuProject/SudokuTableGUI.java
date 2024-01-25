package SudokuProject;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.DecimalFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;

import javax.swing.*;

import DatabaseConnection.Database_SudokuProcess;

import java.util.Timer;
import java.util.TimerTask;

public class SudokuTableGUI extends JFrame {
    private JButton[][] cells;
    private JButton[] numberButtons;
    private JPanel topPanel;
    private JPanel sudokuPanel;
    private JPanel numberPanel;
    private JLabel livesLabel;
    private static JLabel timeLabel;
    
    private static Instant startTime;
    
    private static int health = 3;
    private int selectedRow = -1;
    private int selectedColumn = -1;
    private static final int BOARD_SIZE = 9;
    private int[][] INITIAL_BOARD;
    private int[][] CURRENT_BOARD;
    private int[][] ANSWER_BOARD;
    private static final int CELL_SIZE = 60;
    
    private static final Color defaultColor = Color.white;
    private static final Color gridColor = Color.black;
    private static final Color highlightColor = Color.pink;
    private static final Color selectedColor = new Color(204, 136, 153);
    private static final Color changeableCellForegroundColor = Color.darkGray;
    private static final Color unchangeableCellForegroundColor = Color.blue;
    private static final Color warningCellForegroundColor = Color.red;
    
    
    public SudokuTableGUI(int[][] INITIAL_BOARD, int[][] ANSWER_BOARD, int[][] CURRENT_BOARD, String difficulty) {
    	
    	this.INITIAL_BOARD = INITIAL_BOARD;
    	this.ANSWER_BOARD = ANSWER_BOARD;
    	this.CURRENT_BOARD = CURRENT_BOARD;
    	

        setTitle("Sudoku TEST");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Top Panel
        topPanel = new JPanel(new GridLayout(1, BOARD_SIZE));
        topPanel.setLayout(new BorderLayout());
        
        JButton saveButton = new JButton("Save Game");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	String currentNumbersString = Database_SudokuProcess.convertToString(CURRENT_BOARD);
            	Database_SudokuProcess.saveSudokuTable("60",currentNumbersString);
            }
        });
        saveButton.setHorizontalAlignment(SwingConstants.LEFT);
        topPanel.add(saveButton, BorderLayout.NORTH);
        
        JLabel scoreLabel = new JLabel("Difficulty : "+difficulty);
        scoreLabel.setHorizontalAlignment(SwingConstants.LEFT);
        topPanel.add(scoreLabel, BorderLayout.WEST);

        // Geçen zaman etiketi
        timeLabel = new JLabel("Elapsed Time: 00:00:00");
        timeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        topPanel.add(timeLabel, BorderLayout.CENTER);

        // Can durumu etiketi
        livesLabel = new JLabel("Health: "+health);
        livesLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        topPanel.add(livesLabel, BorderLayout.EAST);
        
        // Başlangıç zamanını kaydetme
        startTime = Instant.now();

        // Zamanlayıcı (Timer) oluşturma
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                updateElapsedTime();
            }
        }, 0, 1000); // Her saniyede bir güncelleme yapmak için 1000 milisaniye (1 saniye) kullanılır.
        
        
        // 9x9 Sudoku paneli
        sudokuPanel = new JPanel(new GridLayout(BOARD_SIZE, BOARD_SIZE));
        sudokuPanel.setPreferredSize(new Dimension(BOARD_SIZE * CELL_SIZE, BOARD_SIZE * CELL_SIZE));
        cells = new JButton[BOARD_SIZE][BOARD_SIZE];

        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
            	// SUDOKU TABLOSUNDAKİ HÜCRELERİN BAŞLANGIÇLARI YAPILIYOR, INITIAL
                int value = INITIAL_BOARD[row][col];
                cells[row][col] = new JButton(value == 0 ? "" : String.valueOf(value));
                cells[row][col].setFont(new Font("Arial", Font.BOLD, 25));
                cells[row][col].setBackground(defaultColor);
                cells[row][col].setOpaque(true);
                cells[row][col].setBorder(BorderFactory.createLineBorder(gridColor));
                cells[row][col].addActionListener(new CellButtonListener(row, col));
                cells[row][col].addKeyListener(new NumberKeyListener());
                sudokuPanel.add(cells[row][col]);

                // (LOAD İÇİN ÖNEMLİ) ARRAYLARDA EĞER INITIAL_BOARD'IN HÜCRESİ 0 OLUP CURRENT_BOARD'IN HÜCRESİ 0'DAN FARKLIYSA HÜCRE DEĞİŞTİRİLEBİLİR OLARAK DOLDURULUYOR
                if (INITIAL_BOARD[row][col]==0 && CURRENT_BOARD[row][col]!=0) {
                	
                	cells[row][col].setForeground(changeableCellForegroundColor);
                	cells[row][col].setText(String.valueOf(CURRENT_BOARD[row][col]));
                	checkWrongAnswer(row,col);
                }
                
                // 3x3 GRİDLER KALINLAŞTIRILIYOR
                if ((row + 1) % 3 == 0 && (col + 1) % 3 == 0) {
                    cells[row][col].setBorder(BorderFactory.createMatteBorder(1, 1, 3, 3, gridColor));
                } else if ((row + 1) % 3 == 0) {
                    cells[row][col].setBorder(BorderFactory.createMatteBorder(1, 1, 3, 1, gridColor));
                } else if ((col + 1) % 3 == 0) {
                	cells[row][col].setBorder(BorderFactory.createMatteBorder(1, 1, 1, 3, gridColor));
                }
                if (value != 0) {
                    cells[row][col].setForeground(unchangeableCellForegroundColor); // Siyah renk, değiştirilemeyen hücreler için
                }
            }
        }
        
    	


        // 1-9 arasında rakam butonları
        numberPanel = new JPanel(new GridLayout(1, BOARD_SIZE));
        numberPanel.setPreferredSize(new Dimension(BOARD_SIZE * CELL_SIZE, CELL_SIZE));
        numberButtons = new JButton[BOARD_SIZE];

        for (int i = 1; i <= BOARD_SIZE; i++) {
            numberButtons[i - 1] = new JButton(String.valueOf(i));
            numberButtons[i - 1].setFont(new Font("Arial", Font.BOLD, 25));
            numberButtons[i - 1].setPreferredSize(new Dimension(CELL_SIZE, CELL_SIZE));
            numberButtons[i - 1].addActionListener(new NumberButtonListener(i));
            numberPanel.add(numberButtons[i - 1]);
        }
        
        add(topPanel, BorderLayout.NORTH);
        add(sudokuPanel, BorderLayout.CENTER);
        add(numberPanel, BorderLayout.SOUTH);


        
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private static void updateElapsedTime() {
        Duration duration = Duration.between(startTime, Instant.now());
        long hours = duration.toHours();
        long minutes = duration.toMinutesPart();
        long seconds = duration.toSecondsPart();

        DecimalFormat formatter = new DecimalFormat("00");
        String formattedTime = formatter.format(hours) + ":" + formatter.format(minutes) + ":" + formatter.format(seconds);

        SwingUtilities.invokeLater(() -> {
            timeLabel.setText("Elapsed Time: " + formattedTime);
        });
    }
    
    private class CellButtonListener implements ActionListener {
        private int row;
        private int col;

        public CellButtonListener(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
        	
        	// DAHA ÖNCEKİ BOYANAN HÜCRE VE İLGİLİ HÜCRELERİNİ BEYAZA BOYAR
            if (selectedRow != -1 && selectedColumn != -1) {
                updateRelatedCellsColor(selectedRow, selectedColumn, defaultColor);
                updateSameValueCellsColor(selectedRow, selectedColumn, defaultColor);
                cells[selectedRow][selectedColumn].setBackground(defaultColor);
            }
        	
            selectedRow = row;
            selectedColumn = col;
            
            updateRelatedCellsColor(selectedRow, selectedColumn, highlightColor);
            updateSameValueCellsColor(selectedRow, selectedColumn, selectedColor);
            cells[selectedRow][selectedColumn].setBackground(selectedColor);
            
        }
    }
   
    private class NumberKeyListener implements KeyListener{

		@Override
		public void keyTyped(KeyEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void keyReleased(KeyEvent e) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void keyPressed(KeyEvent e) {
			int number = e.getKeyChar() - '0'; // Basılan tuşun değerini al
			
	        if (number >= 1 && number <= 9) {
	        	actionForActionPerformed(number);
	        }
		}
    }
    
    // RAKAM PANELİNDE SAYI SEÇİLDİĞİNDE UYGULANACAKLAR
    private class NumberButtonListener implements ActionListener {
        private int number;

        public NumberButtonListener(int number) {
            this.number = number;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
        	actionForActionPerformed(number);
        }
    }
    
    private void actionForActionPerformed(int number) {
    	if (selectedRow != -1 && selectedColumn != -1) {
       	 if (INITIAL_BOARD[selectedRow][selectedColumn] == 0) {// SADECE BAŞLANGIÇTA 0 OLAN HÜCRELERİ ETKİLER
       		cells[selectedRow][selectedColumn].setForeground(changeableCellForegroundColor); // HÜCRENİN DEĞERİ GRİ OLUR
       		 // DAHA ÖNCE SEÇİLEN SAYININ DEĞERİNE SAHİP DİĞER HÜCRELERİN RENGİ BEYAZ YAPILIR
       		 updateSameValueCellsColor(selectedRow, selectedColumn, defaultColor);
       		 // İLGİLİ HÜCRELER TEKRAR PINK'e BOYANIR
       		 updateRelatedCellsColor(selectedRow, selectedColumn, highlightColor);
       		 
       		 
       		 // EĞER AYNI RAKAMA 2.KEZ BASILIRSA DEĞER 0 OLUR
       		 if(CURRENT_BOARD[selectedRow][selectedColumn]!=0 && CURRENT_BOARD[selectedRow][selectedColumn]== number ) {
           	     CURRENT_BOARD[selectedRow][selectedColumn] = 0; // GÜNCEL TABLOYA TEKRAR 0 DEĞERİ ATANIR
           		 cells[selectedRow][selectedColumn].setText(String.valueOf("")); // HÜCRENİN DEĞERİ GÜNCELLENİR
           		 // DAHA ÖNCE SEÇİLEN SAYININ DEĞERİNE SAHİP DİĞER HÜCRELERİN RENGİ BEYAZ YAPILIR
           		 updateCellsColor(selectedRow, selectedColumn, defaultColor);
           		 // İLGİLİ HÜCRELER TEKRAR BOYANIR
           		 updateRelatedCellsColor(selectedRow, selectedColumn, highlightColor);
           		 cells[selectedRow][selectedColumn].setBackground(selectedColor);
       		 }
       		 else {
           	     CURRENT_BOARD[selectedRow][selectedColumn] = number; // Güncel tabloya seçilen sayıyı atama
           		 cells[selectedRow][selectedColumn].setText(String.valueOf(number)); // HÜCRENİN DEĞERİ GÜNCELLENİR
           		 
           		 checkWrongAnswer(selectedRow, selectedColumn);
         
           		 updateSameValueCellsColor(selectedRow, selectedColumn,selectedColor);
           		
       		 }
                
           
       		 validateWin();
            }
   	}
    }
    
    // AYNI DEĞERE SAHİP HÜCRELER BOYANIR
    private void updateSameValueCellsColor(int row, int col, Color color) {
    	
    	int selectedValue = CURRENT_BOARD[selectedRow][selectedColumn];
    	
    	for (int r = 0; r < BOARD_SIZE; r++) {
            for (int c = 0; c < BOARD_SIZE; c++) {
                if (r != selectedRow || c != selectedColumn) {
                    int value = CURRENT_BOARD[r][c];
                    if (value == selectedValue && selectedValue != 0) {
                        cells[r][c].setBackground(color);
                    }
                }
            }
        }

    }
    
    // TÜM HÜCRELER BOYANIR
    private void updateCellsColor(int row, int col, Color color) {
    	
    	for (int r = 0; r < BOARD_SIZE; r++) {
            for (int c = 0; c < BOARD_SIZE; c++) {
                        cells[r][c].setBackground(color);
                }
            }
    }

    // SEÇİLEN HÜCRENİN VERTICAL, HORIZONTAL VE AYNI 3x3 KUTUCUKTA YER ALAN HÜCRELER BOYANIR
    private void updateRelatedCellsColor(int row, int col, Color color) {
    	// SEÇİLEN HÜCRENİN VERTICAL VE HORIZONTAL HÜCRELERİ BOYANIYOR
        for (int i = 0; i < BOARD_SIZE; i++) {
            if (i != col) {
                cells[row][i].setBackground(color);
            }
            if (i != row) {
                cells[i][col].setBackground(color);
            }
        }
        
        // SEÇİLEN 3x3 KUTUCUKTAKİ HÜCRELER BOYANARAK HIGHLIGHT EDİLİYOR
        int startRow = (row / 3) * 3;
        int startCol = (col / 3) * 3;
        for (int i = startRow; i < startRow + 3; i++) {
            for (int j = startCol; j < startCol + 3; j++) {
                if (i != row || j != col) {
                    cells[i][j].setBackground(color);
                }
            }
        }
    }

    // İLGİLİ HÜCRE YANLIŞ DEĞER İLE DOLDURULURSA HÜCRENİN RENGİ KIRMIZIYA DÖNER VE CAN 1 AZALIR
    private void checkWrongAnswer(int row, int col) {
  		 if(CURRENT_BOARD[row][col]!=ANSWER_BOARD[row][col]) {
  			cells[row][col].setForeground(warningCellForegroundColor); 
  			livesLabel.setText("Health : "+--health);
  			if(health==0) {
  				JOptionPane.showMessageDialog(null, "No health left. Game Over!");
  			}
  		 }
    }

    // GÜNCEL TABLO VE CEVAP TABLOSU EŞİT OLDUĞUNDA OYUN BAŞARIYLA SONLANIR
    private void validateWin() {
        if (Arrays.deepEquals(ANSWER_BOARD, CURRENT_BOARD)) {
        	JOptionPane.showMessageDialog(null, "CONGRATULATIONS! YOU WIN!");
        }
    }
}
