/************************************/
/* Employee JDBC					*/
/************************************/

import oracle.jdbc.driver.OracleConnection;
import oracle.jdbc.driver.OracleDriver;

import java.io.Console;
import java.io.IOException;
import java.sql.*;

public class employee
{
	public static void main(String args[]) throws SQLException, IOException
	{
		String user, pass, snum, namer;
		int enumber;

		Console console = System.console();
		System.out.print("Enter your username: ");    // Your Oracle ID with double quote
		user = console.readLine();        			  // e.g. "98765432D"
		System.out.print("Enter your password: ");    // Password of Oracle Account
		char[] password = console.readPassword();
		pass = String.valueOf(password);

		// Connection
		DriverManager.registerDriver(new OracleDriver());
		OracleConnection conn = 
			(OracleConnection)DriverManager.getConnection(
			 "jdbc:oracle:thin:@studora.comp.polyu.edu.hk:1521:dbms",user,pass);
		
		System.out.println("Program starts.");
		Statement stmt = conn.createStatement();
		ResultSet rset = stmt.executeQuery(
			"SELECT ENO, ENAME, ZIP, HDATE FROM EMPLOYEES");
		while (rset.next())
		{
			System.out.println(rset.getInt(1)
			+ " " + rset.getString(2) 
			+ " " + rset.getInt(3) 
			+ " " + rset.getDate(4));
		}
		System.out.println();

		PreparedStatement prepareQuery = conn.prepareStatement(
			"SELECT ENO, ENAME, ZIP, HDATE FROM EMPLOYEES WHERE ENO = ?");
		System.out.println(
			"Enter -1 to quit program when prompted for employee number");
		System.out.println();

		snum = readEntry("employee number: ");
		enumber = Integer.valueOf(snum).intValue();
		while (enumber != -1)
		{
			System.out.println("Processing .....");
			prepareQuery.setInt(1, enumber);
			rset = prepareQuery.executeQuery();
			while (rset.next())
			{
				System.out.println(rset.getInt(1) + " " +
					rset.getString(2) + " " +
					rset.getInt(3) + " " +
					rset.getDate(4));
			}
			snum = readEntry("employee number: ");
			enumber = Integer.valueOf(snum).intValue();
		}
		conn.close();
	}

	// readEntry function -- Read input string
	static String readEntry(String prompt)
	{
		try
		{
			StringBuffer buffer = new StringBuffer();
			System.out.print(prompt);
			System.out.flush();
			int c = System.in.read();
			while (c != '\n' && c != -1)
			{
				buffer.append((char)c);
				c = System.in.read();
			}
			return buffer.toString().trim();
		}
		catch (IOException e)
		{
			return "Opps!";
		}
	}
}
