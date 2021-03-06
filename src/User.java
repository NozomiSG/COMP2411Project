import oracle.jdbc.driver.OracleConnection;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;

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

    public void checkStatus() throws SQLException {
        int[] userOrder = new int[100];
        ArrayList<Integer> list1 = new ArrayList<>();
        ArrayList<Integer> list2 = new ArrayList<>();
        int count = 0;
        DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
        OracleConnection conn = (OracleConnection) DriverManager.getConnection("jdbc:oracle:thin:@studora.comp.polyu.edu.hk:1521:dbms", "20074794D", "Peter0817..");
        Statement stmt = conn.createStatement();
        ResultSet rset = stmt.executeQuery("select order_id from orderinf where d_phone = '" + getID() + "'");
        while (rset.next()) {
            userOrder[count++] = rset.getInt(1);
        }

        if (count == 0) {
            System.out.println("No order");
        }
        while (count>0) {
            stmt = conn.createStatement();
            rset = stmt.executeQuery("select state, order_id from order_state where order_id = '" + userOrder[--count] + "'");
            rset.next();
            if (rset.getInt(1) == 0) {
                list1.add(rset.getInt(2));
            } else {
                list2.add(rset.getInt(2));
            }
        }
        conn.close();
        System.out.println("======Your current order status=====");
        System.out.printf("In the delivery: %d\n", list1.size());
        System.out.println("OrderId: "+list1);
        System.out.println("------------------------------------");
        System.out.printf("Delivered: %d\n", list2.size());
        System.out.println("OrderId: "+list2);
        System.out.println("====================================\n");

        System.out.println("Do you want to search more order? (Enter 'N' to exit and other to search)");
        Scanner scanner = new Scanner(System.in);
        String cmd;
        while (true) {
            System.out.print("Please enter your command: ");
            try {
                cmd = scanner.next();
                if (cmd.equals("N")) {
                    return;
                } else {
                    Application.checkDelivery();
                }
            } catch (Exception e) {
                System.out.println("Your enter is wrong, please try again!");
                scanner.nextLine();
            }
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