package dbas2;

import java.sql.Connection;
import java.sql.DriverManager;

public class Main {
	
	public static void main(String[] args) {
		Connection c = null;
	      try {
	         Class.forName("org.postgresql.Driver");
	         c = DriverManager
	            .getConnection("jdbc:postgresql://nestor2.csc.kth.se:5432/frebern",
	            "frebern", "E4VkF794");
	      } catch (Exception e) {
	         e.printStackTrace();
	         System.err.println(e.getClass().getName()+": "+e.getMessage());
	         System.exit(0);
	      }
	      System.out.println("Opened database successfully");
	      
	}

}
