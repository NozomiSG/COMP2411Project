import oracle.jdbc.driver.OracleConnection;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Administrator extends Account{
    private String ID;
    private String Password;

    public Administrator(String ID, String Password) {
        super(ID, Password);
    }

    @Override
    public boolean checkLogin() throws SQLException {
        DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
        OracleConnection conn = (OracleConnection) DriverManager.getConnection("jdbc:oracle:thin:@studora.comp.polyu.edu.hk:1521:dbms", "20078998D", "Xyf20020429");
        Statement stmt = conn.createStatement();
        ResultSet rset = stmt.executeQuery("select * from administrator");
        while (rset.next()) {
            if (rset.getString(1).equals(ID)) {
                if (rset.getString(2).equals(Password)) {
                    System.out.println("Login successfully!\n\n\n");
                    conn.close();
                } else {
                    System.out.println("The user name or password is incorrect. Please try again");
                    break;
                }
            }
        }
        conn.close();
        System.out.println("The account or password is incorrect. Please try again");
        return false;
    }
}
