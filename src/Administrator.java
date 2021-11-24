import oracle.jdbc.driver.OracleConnection;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Administrator {
    private String account;
    private String password;

    public Administrator(String account, String password) {
        this.account = account;
        this.password = password;
    }
    public boolean checklogin() throws SQLException {
        DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
        OracleConnection conn = (OracleConnection) DriverManager.getConnection("jdbc:oracle:thin:@studora.comp.polyu.edu.hk:1521:dbms", "20078998D", "Xyf20020429");
        Statement stmt = conn.createStatement();
        ResultSet rset = stmt.executeQuery("select * from administrator");
        while (rset.next()) {
            if (rset.getString(1).equals(username)) {
                if (rset.getString(2).equals(password)) {
                    System.out.println("Login successfully!\n\n\n");
                    user_id = rset.getString(3);
                    conn.close();
                    user.userHomePage(user_id);
                } else {
                    System.out.println("The user name or password is incorrect. Please try again");
                    break;
                }
            }
        }
        conn.close();
        System.out.println("The username or password is incorrect. Please try again");
        return false;
    }

}
