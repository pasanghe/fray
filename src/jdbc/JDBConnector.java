package jdbc;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.swing.JOptionPane;

public class JDBConnector implements Runnable {
	
	private static String databaseSite = "sql3.freesqldatabase.com";
	private static String databaseName = "sql3282320";
	private static String databaseUsername = "sql3282320";
	private static String databasePassword = "vAuzhDvkue";
	private static Connection con;
	
	//private static String databaseSite = "ghotraga.dev.fast.sheridanc.on.ca";
	//private static String databaseName = "AccountInformation";
	//private static String databaseUsername = "ghotraga_ghotrag";
	//private static String databasePassword = "?yNV#6?bfXE";
	//private static Connection con;

	public static Connection getCon() {
		return con;
	}

	public static void setCon() {
		try {
			JDBConnector.con = DriverManager.getConnection("jdbc:mysql://" + databaseSite + "/" + databaseName, databaseUsername, databasePassword);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static boolean connectionEstablished = false;
	
	public static void main(String[] args) throws Exception {
		testMethod();
	}
	
	public void run(){
		try {
			setCon();
			if(con.isValid(0)) {
				//JOptionPane.showMessageDialog(null, "Connection Established", "Connection Established", JOptionPane.PLAIN_MESSAGE);
				System.out.println("Connection Established");
				setConnectionEstablished(true);
			}
		} catch(SQLException e1) {
			//JOptionPane.showMessageDialog(null, "Connection Error! Please check your Internet connection!", "Connection Error", 0);
			System.out.println("Connection Error! Please check your Internet Connection!");
			setConnectionEstablished(false);
			e1.printStackTrace();
		}
	}
	
	public static void testMethod() {
		try {
			setCon();
			if(con.isValid(0)) {
				JOptionPane.showMessageDialog(null, "Connection Established", "Connection Established", JOptionPane.PLAIN_MESSAGE);
				setConnectionEstablished(true);
			}
		} catch(SQLException e1) {
			JOptionPane.showMessageDialog(null, "Connection Error! Please check your internet connection!", "Connection Error", 0);
			setConnectionEstablished(false);
			e1.printStackTrace();
		}
	}

	public static boolean isConnectionEstablished() {
		return connectionEstablished;
	}

	public static void setConnectionEstablished(boolean connectionEstablished) {
		JDBConnector.connectionEstablished = connectionEstablished;
	}

}