package kr.test.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class OracleTest {

	public static void main(String[] args) throws Exception {
		String url = "jdbc:oracle:thin:@10.1.9.32:33100:dmis";
		String uname = "scgs_parkyk";
		String upass = "scgs_parkyk";

		Class.forName("oracle.jdbc.OracleDriver");
		Connection c = DriverManager.getConnection(url, uname, upass);
		Statement s = c.createStatement();
		ResultSet r = s.executeQuery("select sysdate d from dual");
		r.next();
		System.out.println(r.getString("d"));
		r.close();
		s.close();
		c.close();
	}
}
