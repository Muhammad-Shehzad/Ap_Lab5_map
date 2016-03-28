import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.Test;

public class JdbcClassTest {
	  static Connection conn = null;
	  static Statement stmt = null;
	  
	@Test
	public void testShowSearchedCity() throws SQLException {
		 conn = DriverManager.getConnection("jdbc:mysql://localhost/", "root", "123qweasd");
		 stmt = conn.createStatement();
		/* 
		JdbcClass jdbcClass=new JdbcClass();
		String result=jdbcClass.showSearchedCity(stmt, "Cape Town");
		assertEquals("", result);
*/
	}

	@Test
	public void testFindCities() throws Exception {
		conn = DriverManager.getConnection("jdbc:mysql://localhost/", "root", "123qweasd");
		 stmt = conn.createStatement();
		/*
		JdbcClass jdbcClass=new JdbcClass();
		String result=jdbcClass.findCities(stmt, "-33.9258", "18.4232");
		assertEquals("", result);
		*/
	}

}
