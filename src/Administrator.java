import oracle.jdbc.driver.OracleConnection;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;

public class Administrator extends Account{
    public Administrator(String ID, String Password) {
        super(ID, Password);
    }

    @Override
    public boolean checkLogin() throws SQLException {
        DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
        OracleConnection conn = (OracleConnection) DriverManager.getConnection("jdbc:oracle:thin:@studora.comp.polyu.edu.hk:1521:dbms", "20074794D", "Peter0817..");
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

    public void addPlace(String placeName) throws SQLException {
        int[] distance = new int[100];
        String[] allPlace = new String[100];
        int[] allID = new int[100];
        int count = 0, total = 0;
        Scanner scanner = new Scanner(System.in);
        DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
        OracleConnection conn = (OracleConnection) DriverManager.getConnection("jdbc:oracle:thin:@studora.comp.polyu.edu.hk:1521:dbms", "20074794D", "Peter0817..");
        Statement stmt = conn.createStatement();
        ResultSet rset = stmt.executeQuery("select MAX(place_id) from place");
        rset.next();
        int placeID=rset.getInt(1)+1;

        rset = stmt.executeQuery("select * from place");
        while (rset.next()) {
            allID[count] = rset.getInt(1);
            allPlace[count++] = rset.getString(2);
        }
        conn.close();
        while (total < count) {
            while (true) {
                try {
                    System.out.printf("Please enter the distance between %S and %S: ", placeName, allPlace[total]);
                    distance[total] = scanner.nextInt();
                    total++;
                    break;
                } catch (Exception e) {
                    System.out.println("Your enter is wrong, please try again!");
                    scanner.next();
                }
            }
        }
        conn = (OracleConnection) DriverManager.getConnection("jdbc:oracle:thin:@studora.comp.polyu.edu.hk:1521:dbms", "20074794D", "Peter0817..");
        while(count>0) {
            count--;
            stmt = conn.createStatement();
            stmt.executeQuery("insert into placeDistance values(" + placeID + ", " + allID[count] + "," + distance[count] + ")");

        }
        stmt = conn.createStatement();
        stmt.executeQuery("INSERT INTO place values(" + placeID + ",'" + placeName +"')");
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
        OracleConnection conn = (OracleConnection) DriverManager.getConnection("jdbc:oracle:thin:@studora.comp.polyu.edu.hk:1521:dbms", "20074794D", "Peter0817..");
        Statement stmt = conn.createStatement();
        ResultSet rset=stmt.executeQuery("select order_id from order_state");
        while(rset.next()){
            if(rset.getInt(1)==orderID) break;
        }
        try{
            if(checkState(String.valueOf(orderID))==state) {
                System.out.println("Update failed. REASON: The state of the order is already "+state+"!");
                conn.close();
                return;
            }
        }catch (Exception e){
            System.out.println("Update failed. REASON: The orderID dose not exist!");
            return;
        }
        stmt.executeQuery("update order_state set state="+st+" where order_id="+orderID);
        System.out.println("The OrderID dose not exist!");
        stmt.executeQuery("COMMIT");
        conn.close();
        System.out.println("Update successfully!");
    }

    public void deletePlace(int placeID) throws SQLException {
        DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
        OracleConnection conn = (OracleConnection) DriverManager.getConnection("jdbc:oracle:thin:@studora.comp.polyu.edu.hk:1521:dbms", "20074794D", "Peter0817..");
        Statement stmt = conn.createStatement();
        ResultSet rset=stmt.executeQuery("select place_send_id,place_receive_id from orderPlace ");
        ArrayList<Integer> list=new ArrayList<>();
        while (rset.next()){
            list.add(rset.getInt(1));
            list.add(rset.getInt(2));
        }
        if(list.contains(placeID)){
            System.out.println("Delete failed. REASON: Some order contains this place! ");
            return;
        }
        try{
            stmt.executeQuery("delete from place where place_id="+placeID);
        }catch (SQLException e){
            System.out.println("Delete failed. REASON: The place id dose not exist!");
            return;
        }
        stmt = conn.createStatement();
        try{
            stmt.executeQuery("delete from placedistance where place_A="+placeID + " OR " + "place_B="+placeID);
        }catch (SQLException e){
            System.out.println("Delete failed. REASON: The place id dose not exist!");
            return;
        }
        System.out.println("Delete successfully");
    }

    public boolean changePassword(String oldPas,String newPas) throws SQLException {
        DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
        OracleConnection conn = (OracleConnection) DriverManager.getConnection("jdbc:oracle:thin:@studora.comp.polyu.edu.hk:1521:dbms", "20074794D", "Peter0817..");
        Statement stmt = conn.createStatement();
        ResultSet rest=stmt.executeQuery("select password from administrator where account= '"+this.getID()+"'");
        rest.next();
        if(oldPas.equals(rest.getString(1))){
            stmt.executeQuery("update administrator set password="+"'"+newPas+"'"+" where ACCOUNT="+"'"+this.getID()+"'");
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
}