import oracle.jdbc.driver.OracleConnection;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class Account {
    private String ID;
    private String Password;
    Account(String ID,String Password){
        this.setID(ID);
        this.setPassword(Password);
    }
    public abstract boolean checkLogin() throws SQLException;
    public abstract boolean changePassword(String oldPas,String newPas) throws SQLException;
    public String getID(){return ID;}
    public String getPassword(){return Password;}
    public void setID(String ID){this.ID=ID;}
    public void setPassword(String Password){this.Password = Password;}
    public boolean checkState(String OrderID) throws SQLException {
        String flag;
        DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
        OracleConnection conn = (OracleConnection) DriverManager.getConnection("jdbc:oracle:thin:@studora.comp.polyu.edu.hk:1521:dbms", "20074794D", "Peter0817..");
        Statement stmt = conn.createStatement();
        ResultSet rset=stmt.executeQuery("select state from order_state where order_id ="+OrderID);
        rset.next();
        flag=rset.getString(1);
        conn.close();
        if(flag.equals("1")) return true;
        return false;
    }
}
