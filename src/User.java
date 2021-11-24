import oracle.jdbc.driver.OracleConnection;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class User extends Account {

    private String userInfo;
    private String userName;


    public User(String ID, String Password) {
        super(ID, Password);
    }

    @Override
    public boolean checkLogin() throws SQLException {
        DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
        OracleConnection conn = (OracleConnection) DriverManager.getConnection("jdbc:oracle:thin:@studora.comp.polyu.edu.hk:1521:dbms", "20078998D", "Xyf20020429");
        Statement stmt = conn.createStatement();
        ResultSet rset = stmt.executeQuery("select * from userinf");
        while (rset.next()) {
            System.out.println("local name: "+rset.getString(4));
            System.out.println("local pass: "+rset.getString(3));
            if (rset.getString(4).equals(getInfo()) || rset.getString(2).equals(getInfo())) {
                if (rset.getString(3).equals(getPassword())) {
                    this.getID() = rset.getString(1);
                    this.userName = rset.getString(4);
                    conn.close();
                    return true;
                }
                else {
                    conn.close();
                    return false;
                }
            }
        }
        conn.close();
        return false;
    }

    public String getName() {
        return userName;
    }
    public String getInfo() {
        return userInfo;
    }
    public void setInfo(String useInfo) {
        this.userInfo = userInfo;
    }

}