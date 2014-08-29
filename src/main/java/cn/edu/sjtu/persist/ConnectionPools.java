package cn.edu.sjtu.persist;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionPools {
	static {
		try {
			Class.forName("org.h2.Driver");
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}
	public static Connection getConn() throws SQLException {
		Connection conn = DriverManager.
			       getConnection("jdbc:h2:tcp://localhost/~/embed_crawler", "embed_crawler", "");
		 return conn;
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
