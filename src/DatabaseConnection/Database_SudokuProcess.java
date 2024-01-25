package DatabaseConnection;

import java.util.Dictionary;
import java.util.Hashtable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Database_SudokuProcess {
	
	private final static String TABLE_NAME = "sudoku_table";
	private final static String COLUMN_GAMEID = "GAMEID";
	
	private final static String COLUMN_INITIAL_NUMBERS = "initial_numbers";
	private final static String COLUMN_SOLUTION_NUMBERS = "solution_numbers";
	private final static String COLUMN_CURRENT_NUMBERS = "current_numbers";
	private final static String COLUMN_DIFFICULTY = "difficulty";
	private final static String COLUMN_USERNAME = "username";
	
	// İlgili GAMEID için Sudokunun zorluk seviyesi, başlangıç tablosu, cevap tablosu ve mevcut tabloyu içeren bilgilerin çekilmesi sağlanır.
    public static Dictionary<String, String> loadSudokuTables(String GAMEID) {
        
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        
        Dictionary<String, String> dictNumbers= new Hashtable<>();
        
        
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " +
                COLUMN_GAMEID + " = ?;";
    	
        try {
            // Veritabanı bağlantısının oluşturulması
        	connection = Database_Utils.dbConnector();
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, GAMEID);
            // Sudoku bilgilerinin veritabanından alınması
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
            	
                // Sudoku başlangıç durumu, çözümü ve güncel durumu alınır
                String initialNumbersString = resultSet.getString(COLUMN_INITIAL_NUMBERS);
                String solutionNumbersString = resultSet.getString(COLUMN_SOLUTION_NUMBERS);
                String currentNumbersString = resultSet.getString(COLUMN_CURRENT_NUMBERS);
                String difficulty =  resultSet.getString(COLUMN_DIFFICULTY);
                
                dictNumbers.put(COLUMN_INITIAL_NUMBERS, initialNumbersString);
                dictNumbers.put(COLUMN_SOLUTION_NUMBERS, solutionNumbersString);
                dictNumbers.put(COLUMN_CURRENT_NUMBERS, currentNumbersString);
                dictNumbers.put(COLUMN_DIFFICULTY, difficulty);

            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                Database_Utils.dbDisconnector(connection);
            }
        }
        return dictNumbers;
    }
	
    // Database'de en yüksek gameid bilgisine 1 eklenerek yeni GAMEID satırı oluşturulur ve içerisine zorluk seviyesi, başlangıç tablosu, cevap tablosu ve mevcut tabloyu içeren bilgiler eklenir.
    public static boolean insertSudokuTables(Dictionary<String, String> dictNumbers) {
        
    	boolean result = false;
    	
    	int newGameId = getMaxGameId()+1;
    	
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        
        String query = "INSERT INTO " + TABLE_NAME + " (" +
        		COLUMN_GAMEID + ", " +
        		COLUMN_USERNAME + ", " +
        		COLUMN_INITIAL_NUMBERS + ", " +
        		COLUMN_SOLUTION_NUMBERS + ", " +
        		COLUMN_CURRENT_NUMBERS + ", " +
        		COLUMN_DIFFICULTY + ") VALUES (?, ?, ?, ?, ?, ?)";
    	
        try {
            connection = Database_Utils.dbConnector();;
            
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, Integer.toString(newGameId));
            preparedStatement.setString(2, dictNumbers.get(COLUMN_USERNAME));
            preparedStatement.setString(3, dictNumbers.get(COLUMN_INITIAL_NUMBERS));
            preparedStatement.setString(4, dictNumbers.get(COLUMN_SOLUTION_NUMBERS));
            preparedStatement.setString(5, dictNumbers.get(COLUMN_CURRENT_NUMBERS));
            preparedStatement.setString(6, dictNumbers.get(COLUMN_DIFFICULTY));
            
            
            System.out.println(preparedStatement.toString());
            int rowsAffected = preparedStatement.executeUpdate();
            result = (rowsAffected > 0);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
            	Database_Utils.dbDisconnector(connection);
            }
        }
        return result;
    }
    
    // Database'den en yüksek Gameid bilgisi çekilir
    private static int getMaxGameId() {
        
    	int maxGameId = -1;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        
        String query = "SELECT MAX("+COLUMN_GAMEID+") FROM " + TABLE_NAME;
    	
        try {
            // Veritabanı bağlantısının oluşturulması
        	connection = Database_Utils.dbConnector();
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                maxGameId = resultSet.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                Database_Utils.dbDisconnector(connection);
            }
        }
        return maxGameId;
    }
	
    // Mevcut tablo bilgisini Database'de ilgili GAMEID satırında günceller.
    public static boolean saveSudokuTable(String GAMEID, String CurrentNumbers) {

    	boolean result = false;
    	
        Connection connection = null;
        PreparedStatement preparedStatement = null;
    	
        String query = "UPDATE " + TABLE_NAME + " " +
        		"SET "+ COLUMN_CURRENT_NUMBERS +" = ? "+
        		"WHERE "+COLUMN_GAMEID+" = ?;";
        
        try {
            connection = Database_Utils.dbConnector();;
            
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, CurrentNumbers);
            preparedStatement.setString(2, GAMEID);
            
            //System.out.println(preparedStatement.toString());
            int rowsAffected = preparedStatement.executeUpdate();
            result = (rowsAffected > 0);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
            	Database_Utils.dbDisconnector(connection);
            }
        }
        return result;
    }
    
    // String, 9x9 array'e parse edilir.
	public static int[][] parseNumbers(String numbersString) {
		
		int BOARD_SIZE = 9;
		
	    int[][] numbers = new int[BOARD_SIZE][BOARD_SIZE];
	    int index = 0;
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                numbers[i][j] = Character.getNumericValue(numbersString.charAt(index));
                index++;
                
            }
        }
        return numbers;
	}
	
	// 9x9 array, string'e dönüştürülür.
    public static String convertToString(int[][] array) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[i].length; j++) {
                sb.append(array[i][j]);
            }
        }
        return sb.toString();
    }
    
}
