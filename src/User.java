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
        OracleConnection conn = (OracleConnection) DriverManager.getConnection("jdbc:oracle:thin:@studora.comp.polyu.edu.hk:1521:dbms", "20074794D", "Peter0817..");
        Statement stmt = conn.createStatement();
        ResultSet rset = stmt.executeQuery("select * from userinf");
        while (rset.next()) {
            if (rset.getString(1).equals(getInfo()) || rset.getString(3).equals(getInfo())) {
                if (rset.getString(2).equals(getPassword())) {
                    setID(rset.getString(1));
                    setUserName(rset.getString(3));
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

    @Override

    public boolean changePassword(String oldPas,String newPas) throws SQLException {
        DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
        OracleConnection conn = (OracleConnection) DriverManager.getConnection("jdbc:oracle:thin:@studora.comp.polyu.edu.hk:1521:dbms", "20074794D", "Peter0817..");
        Statement stmt = conn.createStatement();
        ResultSet rest=stmt.executeQuery("select password from userinf where phone_number = " + this.getID());
        rest.next();
        if(oldPas.equals(rest.getString(1))){
            stmt.executeQuery("update userinf set password="+"'"+newPas+"'"+" where phone_number ="+this.getID());
            stmt.executeQuery("COMMIT");
            System.out.println("Password has been changed!");
            conn.close();
            return false;
        }
        else {
            System.out.println("The old password is wrong!");
            conn.close();
            return true;
        }
    }

    public String getName() {
        return userName;
    }
    public String getInfo() {
        return userInfo;
    }
    public void setInfo(String userInfo) {
        this.userInfo = userInfo;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }

}