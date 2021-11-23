import oracle.jdbc.driver.OracleConnection;

import java.io.IOException;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


class addEmployees {

    public static void main(String[] args) throws SQLException, IOException
    {

        // Connection
        DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
        OracleConnection conn =
                (OracleConnection)DriverManager.getConnection(
                        "jdbc:oracle:thin:@studora.comp.polyu.edu.hk:1521:dbms","20074794D","Peter0817..");

        Statement stmt = conn.createStatement();
        ResultSet rset = stmt.executeQuery("DELETE FROM EMPLOYEES WHERE ENO = 1028");
//        ResultSet rest = stmt.executeQuery("ALTER SESSION SET NLS_LANGUAGE='AMERICAN';");
        System.out.println("Record DELETED.");

        rset = stmt.executeQuery(
                "insert into userinf values("+")");
        while (rset.next())
        {
            System.out.println(
                    rset.getInt(1)
                            + " " + rset.getString(2)
                            + " " + rset.getInt(3)
                            + " " + rset.getDate(4)
            );
        }
        System.out.println();

        rset = stmt.executeQuery("INSERT INTO EMPLOYEES VALUES (1028, 'Mary',67200,'26-MAY-19')");
        System.out.println("Record ADDED.");

        conn.close();
    }
}

