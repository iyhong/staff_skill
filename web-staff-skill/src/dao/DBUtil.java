package dao;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBUtil {
	private static final String driver;
	private static final String url;
	private static final String user;
	private static final String pw;
	static {
		//mysql
		driver = "com.mysql.jdbc.Driver"; 
		url = "jdbc:mysql://127.0.0.1:3306/jjdev";
		
		//oracle
		//driver = "oracle.jdbc.OracleDriver"; 
		//url = "jdbc:oracle:thin:@localhost:1521:xe";
		
		user = "root";
		pw = "java0000";
	}
	
	public static Connection getConnection() {
		Connection connection = null;
		try{
			Class.forName(driver);
			connection = DriverManager.getConnection(url, user, pw);
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{if(connection != null)connection.close();}catch(Exception e){}
		}
		
		return connection;
	}
}
