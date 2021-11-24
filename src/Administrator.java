import oracle.jdbc.driver.OracleConnection;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Administrator extends Account{
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
            if (rset.getString(1).equals(this.getID())) {
                if (rset.getString(2).equals(this.getPassword())) {
                    conn.close();
                    return true;
                } else {
                    break;
                }
            }
        }
        conn.close();
        return false;
    }
    public void addPlace(String placeName,double x,double y) throws SQLException {
        DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
        OracleConnection conn = (OracleConnection) DriverManager.getConnection("jdbc:oracle:thin:@studora.comp.polyu.edu.hk:1521:dbms", "20078998D", "Xyf20020429");
        Statement stmt = conn.createStatement();
        ResultSet rset = stmt.executeQuery("select MAX(place_id) from place");
        rset.next();
        int placeid=rset.getInt(1);
        stmt.executeQuery("INSERT INTO place values("+(++placeid)+",'"+placeName+"',"+String.valueOf(x)+","+String.valueOf(y)+")");
        stmt.executeQuery("COMMIT");
        System.out.println("Add place successfully!");
        conn.close();
    }

    public void setOrderState(int orderID,boolean state) throws SQLException {
        int st;
        if(state){
            st=1;
            }
        else st=0;
        DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
        OracleConnection conn = (OracleConnection) DriverManager.getConnection("jdbc:oracle:thin:@studora.comp.polyu.edu.hk:1521:dbms", "20078998D", "Xyf20020429");
        Statement stmt = conn.createStatement();
        stmt.executeQuery("update order_state set state="+st+" where order_id="+orderID);
        stmt.executeQuery("COMMIT");
        conn.close();
        System.out.println("Update successfully!");
    }

    public void changePassword(String oldPas,String newPas) throws SQLException {
        DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
        OracleConnection conn = (OracleConnection) DriverManager.getConnection("jdbc:oracle:thin:@studora.comp.polyu.edu.hk:1521:dbms", "20078998D", "Xyf20020429");
        Statement stmt = conn.createStatement();
        ResultSet rest=stmt.executeQuery("select password from administrator where account="+this.getID());
        if(oldPas==rest.getString(1)){
            stmt.executeQuery("update administrator set password="+"'"+newPas+"'"+" where ACCOUNT="+"'"+this.getID()+"'");
            stmt.executeQuery("COMMIT");
            System.out.println("Password has been changed!");
        }
        else {
            System.out.println("The old password is wrong!");
        }
        conn.close();
    }

    public static void main(String[] args) throws SQLException {
        Administrator ad=new Administrator("20078998D","13510719357");
        if(ad.checkLogin()){
            ad.setOrderState(1,true);
        }

    }

}
