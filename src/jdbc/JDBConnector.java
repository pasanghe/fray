package jdbc;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.swing.JOptionPane;

public class JDBConnector implements Runnable {
	/**
	 * @author Gagandeep Ghotra, Sang Heon Park, Zain Razvi, Lee Fyyfe
	 * This static class is made static for easy of access through project,
	 * but in future revisions, it will be made into a non static object class.
	 * This class connects to the Fray Card Game Database using the setCon() method.
	 * When the method is connected successfully, than any classes could easily 
	 * just do JDBConnector.getCon().
	 */
	private static String databaseSite = "sql3.freesqldatabase.com"; // Database site
	private static String databaseName = "sql3282320"; // Database name
	private static String databaseUsername = "sql3282320"; // Database username
	private static String databasePassword = "vAuzhDvkue"; // Database password
	private static Connection con;

	public static Connection getCon() {
		return con;
	}

	public static void setCon() {// Connects to database and sets a connection variable to the connection and any class could do getCon and use the connection for sql statements.
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
	
	public static void testMethod() { // A test mehtod that is used only when this class is run, all other classes accessing
		// JBConnector will use JBConnector.getCon().
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