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
		url = "jdbc:mysql://127.0.0.1:3306/loverman85?useUnicode=true&characterEncoding=euckr";
		//url = "jdbc:mysql://127.0.0.1:3306/jjdev2?useUnicode=true&characterEncoding=euckr";
		
		//oracle
		//driver = "oracle.jdbc.OracleDriver"; 
		//url = "jdbc:oracle:thin:@localhost:1521:xe";
		
		user = "loverman85";
		pw = "java5963";
		//user = "root";
		//pw = "java0000";
	}
	
	public static Connection getConnection() {
		System.out.println("getConnection() 진입");
		Connection connection = null;
		try{
			Class.forName(driver);
			connection = DriverManager.getConnection(url, user, pw);
			System.out.println("드라이버로딩, connection 완료");
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return connection;
	}
}
