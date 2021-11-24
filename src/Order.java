import oracle.jdbc.driver.OracleConnection;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Order {
    private int orderID;
    private int userID;

    Order(int userID) throws SQLException {
        DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
        OracleConnection conn = (OracleConnection) DriverManager.getConnection("jdbc:oracle:thin:@studora.comp.polyu.edu.hk:1521:dbms", "20074794D", "Peter0817..");
        Statement stmt = conn.createStatement();
        ResultSet rset = stmt.executeQuery("select count(*) from orderinf");
        rset.next();
        int id = rset.getInt(3) + 1;
        conn.close();
        this.orderID = id;
        this.userID = userID;
    }

    public void newOrder() throws SQLException {

        System.out.println("\n\n\nInsert objects");
        System.out.println("\n=====================================");
        System.out.println("Insert an object         >>> Enter(1)");
        System.out.println("Next step                >>> Enter(2)");
        System.out.println("Exit the program         >>> Enter(0)");
        System.out.println("=====================================\n\n");
        int scan;
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("Please enter your command: ");
            try {
                scan = scanner.nextInt();
                if (scan >= 0 && scan <= 2)
                    break;
                else
                    System.out.println("Your enter is wrong, please try again!");
            } catch (Exception e) {
                System.out.println("Your enter is wrong, please try again!");
                scanner.next();
            }
        }
        switch (scan) {
//            case 1 ->
//            case 2 -> deliverOrder();
//            case 0 -> return;
        }
        newOrder();
    }

    public int getOrderID() {
        return orderID;
    }
    public void creatObject() throws SQLException {
        String name, we;
        int weight;
        Scanner scanner = new Scanner(System.in);
        DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());

        while (true) {
            System.out.print("Please enter your object name(Enter ~ to quit): ");
            try {
                name = scanner.nextLine();
                if (name.equals("~"))
                    return;
                if (name.length() > 30)
                    System.out.println("Your username should be less than 30 characters. Please try again!");
                else
                    break;
            } catch (Exception e) {
                System.out.println("Your enter is wrong, please try again!");
                scanner.next();
            }
        }

        while (true) {
            System.out.print("Please enter your object weight(Enter ~ to quit): ");
            try {
                we = scanner.nextLine();
                if (we.equals("~"))
                    return;
                weight = Integer.parseInt(we);
                break;
            } catch (Exception e) {
                System.out.println("Your enter is wrong, please try again!");
                scanner.next();
            }
        }
        OracleConnection conn = (OracleConnection) DriverManager.getConnection("jdbc:oracle:thin:@studora.comp.polyu.edu.hk:1521:dbms", "20074794D", "Peter0817..");
        Statement stmt = conn.createStatement();
        stmt = conn.createStatement();
        ResultSet rset = stmt.executeQuery("insert into object values((" + this.orderID + ", '" + name + "', '" + weight + "')");
        stmt.executeQuery("COMMIT");
        conn.close();
    }
    public void listObject() {

    }


}
