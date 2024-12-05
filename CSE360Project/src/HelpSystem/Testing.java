package HelpSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Testing {
	protected static Connection connection;
	
	static {
		try{
			connection = HelpSystem.userDatabaseHelper.getConnection();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	public static boolean Message_Test(String message, String username) throws SQLException {
			HelpSystem.userDatabaseHelper.sendMessage(message, username, HelpSystem.userDatabaseHelper.getUserId(username));
			String query = "SELECT * FROM messages WHERE username = ? AND messagebody = ?";
			try(PreparedStatement pstmt = connection.prepareStatement(query)){
				pstmt.setString(1, username);
				pstmt.setString(2, message);
				try(ResultSet rs = pstmt.executeQuery()){
					if(rs.next()) {
						System.out.println("***Success***");
						return true;
					}
					else {
						System.out.println("***Failure***");
						return false;
					}
				}
			}	
		
	}
	
	public static boolean All_Level_Test() throws SQLException {
			int numOfReturnedArticles = HelpSystem.userDatabaseHelper.getArticles().size();
			int numOfArticles;
			String query = "SELECT COUNT(ID) FROM articles";
			try(PreparedStatement pstmt = connection.prepareStatement(query)){
				try(ResultSet rs = pstmt.executeQuery()){
					if(rs.next()) {
						numOfArticles = rs.getInt(1);
						if(numOfArticles == numOfReturnedArticles) {
							System.out.println("***Success***");
							return true;
						}
						else {
							System.out.println("***Failure***");
							return false;
						}
					}
					else {
						return false;
					}
				}
			}	
	}
}
