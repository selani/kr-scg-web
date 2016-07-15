package kr.test.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class MysqlTest {

	public static void main(String[] args) throws Exception {
		String url = "jdbc:mysql://127.0.0.1:3306/mysql";
		String uname = "root";
		String upass = "root";

		Class.forName("com.mysql.jdbc.Driver");
		Connection c = DriverManager.getConnection(url, uname, upass);
		Statement s = c.createStatement();
		ResultSet r = s.executeQuery("select 10 as d from dual");
		r.next();
		System.out.println(r.getString("d"));
		r.close();
		s.close();
		c.close();
	}
}
