package DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Database_UserProcess {
	
	private final static String TABLE_NAME = "user_table";
	private final static String COLUMN_KULLANICI_ADI = "username";
	private final static String COLUMN_SIFRE = "password";
	
	// Database bağlantısıyla ilgili kullanıcı ve şifreye ait kaydın olup olmadığı sorgulanır
	public static boolean checkCredentials(String username, String password) {

    	boolean result = false;
        
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        
        
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " +
                COLUMN_KULLANICI_ADI + " = ? AND " +
                COLUMN_SIFRE + " = ?;";
        
        try {

        	connection = Database_Utils.dbConnector();
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            resultSet = preparedStatement.executeQuery();

            result = resultSet.next(); // Eğer sonuç varsa, kullanıcı ve şifre doğrudur

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

        return result;
    }
	
	// Database bağlantısıyla kullanıcı kaydının olup olmadığı sorgulanır
	public static boolean checkUsername(String username) {

    	boolean result = false;
        
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        
        
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " +
                COLUMN_KULLANICI_ADI + " = ?;";
        
        try {

        	connection = Database_Utils.dbConnector();
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);
            resultSet = preparedStatement.executeQuery();

            result = resultSet.next();

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

        return result;
	}
	
	// Database bağlantısıyla ilgili kullanıcının kaydı yapılır
	public static boolean registerUser(String username, String password) {

    	boolean result = false;
        
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        
        
        String query = "INSERT INTO " + TABLE_NAME + " (" +
                COLUMN_KULLANICI_ADI + ", " +
                COLUMN_SIFRE + ") VALUES (?, ?)";
        
        try {
            connection = Database_Utils.dbConnector();;
            
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
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


}
